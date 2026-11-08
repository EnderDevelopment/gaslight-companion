package com.enderdevelopment.plugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public final class FartGirlfriendCommand implements CommandExecutor {
    private final NamespacedKey spawnEggKey;

    public FartGirlfriendCommand(Main plugin, NamespacedKey spawnEggKey) {
        this.spawnEggKey = spawnEggKey;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can receive a spawn egg.");
            return true;
        }
        if (!player.hasPermission("fartgirlfriend.use")) {
            player.sendMessage(Component.text("You do not have permission to use this egg.").color(NamedTextColor.RED));
            return true;
        }

        ItemStack egg = new ItemStack(Material.CHICKEN_SPAWN_EGG);
        ItemMeta meta = egg.getItemMeta();
        if (meta == null) {
            player.sendMessage(Component.text("The spawn egg could not be created.").color(NamedTextColor.RED));
            return true;
        }
        meta.displayName(Component.text("Fart Girlfriend Spawn Egg").color(NamedTextColor.LIGHT_PURPLE));
        meta.lore(java.util.List.of(
                Component.text("Right-click the ground to summon her.").color(NamedTextColor.GRAY),
                Component.text("She is affectionate and harmless.").color(NamedTextColor.GRAY)));
        meta.getPersistentDataContainer().set(spawnEggKey, PersistentDataType.BYTE, (byte) 1);
        egg.setItemMeta(meta);
        player.getInventory().addItem(egg);
        player.sendMessage(Component.text("You received a Fart Girlfriend spawn egg!").color(NamedTextColor.LIGHT_PURPLE));
        return true;
    }
}
