package com.enderdevelopment.plugin;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.plugin.java.JavaPlugin;

public final class CompanionListener implements Listener {
    private static final long FART_COOLDOWN_TICKS = 120L;
    private final JavaPlugin plugin;
    private final NamespacedKey spawnEggKey;
    private final NamespacedKey companionKey;
    private final java.util.Map<UUID, Long> cooldowns = new java.util.HashMap<>();

    public CompanionListener(JavaPlugin plugin, NamespacedKey spawnEggKey, NamespacedKey companionKey) {
        this.plugin = plugin;
        this.spawnEggKey = spawnEggKey;
        this.companionKey = companionKey;
        startCompanionTask();
    }

    @EventHandler
    public void onEggUse(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND || event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        ItemStack item = event.getItem();
        if (!isSpawnEgg(item)) {
            return;
        }
        event.setCancelled(true);
        Player player = event.getPlayer();
        Location spawnLocation = event.getClickedBlock().getRelative(event.getBlockFace()).getLocation().add(0.5, 0, 0.5);
        Villager companion = (Villager) player.getWorld().spawnEntity(spawnLocation, EntityType.VILLAGER);
        configureCompanion(companion, player);
        if (player.getGameMode().isSurvival() || player.getGameMode().isAdventure()) {
            item.setAmount(item.getAmount() - 1);
        }
        player.sendMessage(Component.text("Your Fart Girlfriend has arrived!").color(NamedTextColor.LIGHT_PURPLE));
    }

    @EventHandler
    public void onCompanionInteract(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Villager companion) || !isCompanion(companion)) {
            return;
        }
        event.setCancelled(true);
        UUID ownerId = ownerOf(companion);
        if (ownerId != null && ownerId.equals(event.getPlayer().getUniqueId())) {
            makeFart(companion, event.getPlayer());
        } else {
            event.getPlayer().sendMessage(Component.text("She is already someone else's girlfriend.").color(NamedTextColor.GRAY));
        }
    }

    @EventHandler
    public void onCompanionDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Villager villager && isCompanion(villager)) {
            event.setCancelled(true);
        }
    }

    private void configureCompanion(Villager companion, Player owner) {
        companion.customName(Component.text("Fart Girlfriend").color(NamedTextColor.LIGHT_PURPLE));
        companion.setCustomNameVisible(true);
        companion.setInvulnerable(true);
        companion.setCollidable(false);
        companion.setAdult();
        companion.setProfession(Villager.Profession.NONE);
        companion.setAI(true);
        companion.getPersistentDataContainer().set(companionKey, PersistentDataType.STRING, owner.getUniqueId().toString());
        ItemStack crown = new ItemStack(Material.PINK_WOOL);
        companion.getEquipment().setHelmet(crown);
        companion.getEquipment().setHelmetDropChance(0.0f);
    }

    private void startCompanionTask() {
        BukkitTask ignored = plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            for (org.bukkit.World world : plugin.getServer().getWorlds()) {
                for (Entity entity : world.getEntitiesByClass(Villager.class)) {
                    if (!isCompanion(entity)) {
                        continue;
                    }
                    UUID ownerId = ownerOf(entity);
                    Player owner = ownerId == null ? null : plugin.getServer().getPlayer(ownerId);
                    if (owner != null && owner.getWorld().equals(entity.getWorld()) && owner.distanceSquared(entity) <= 36.0) {
                        if (ThreadLocalRandom.current().nextInt(4) == 0) {
                            makeFart((Villager) entity, owner);
                        }
                    }
                }
            }
        }, 100L, 120L);
    }

    private void makeFart(Villager companion, Player owner) {
        long now = System.currentTimeMillis();
        long last = cooldowns.getOrDefault(owner.getUniqueId(), 0L);
        if (now - last < 5000L) {
            return;
        }
        cooldowns.put(owner.getUniqueId(), now);
        Location location = owner.getLocation().add(0, 0.8, 0);
        owner.getWorld().spawnParticle(Particle.CLOUD, location, 18, 0.55, 0.35, 0.55, 0.02);
        owner.getWorld().spawnParticle(Particle.COMPOSTER, location, 10, 0.45, 0.3, 0.45, 0.02);
        owner.getWorld().playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 0.35f, 0.65f);
        owner.sendMessage(Component.text("Fart Girlfriend gives you an affectionate toot!").color(NamedTextColor.LIGHT_PURPLE));
    }

    private boolean isSpawnEgg(ItemStack item) {
        if (item == null || item.getType() != Material.CHICKEN_SPAWN_EGG) {
            return false;
        }
        ItemMeta meta = item.getItemMeta();
        return meta != null && meta.getPersistentDataContainer().has(spawnEggKey, PersistentDataType.BYTE);
    }

    private boolean isCompanion(Entity entity) {
        return entity.getPersistentDataContainer().has(companionKey, PersistentDataType.STRING);
    }

    private UUID ownerOf(Entity entity) {
        String value = entity.getPersistentDataContainer().get(companionKey, PersistentDataType.STRING);
        if (value == null) {
            return null;
        }
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }
}
