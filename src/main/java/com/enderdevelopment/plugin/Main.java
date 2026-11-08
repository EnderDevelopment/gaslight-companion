package com.enderdevelopment.plugin;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    private NamespacedKey spawnEggKey;
    private NamespacedKey companionKey;
    private CompanionListener companionListener;

    @Override
    public void onEnable() {
        spawnEggKey = new NamespacedKey(this, "fart_girlfriend_egg");
        companionKey = new NamespacedKey(this, "fart_girlfriend");
        companionListener = new CompanionListener(this, spawnEggKey, companionKey);
        Bukkit.getPluginManager().registerEvents(companionListener, this);

        FartGirlfriendCommand command = new FartGirlfriendCommand(this, spawnEggKey);
        if (getCommand("fartgirlfriend") != null) {
            getCommand("fartgirlfriend").setExecutor(command);
        }
        getLogger().info("FartGirlfriend enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("FartGirlfriend disabled.");
    }
}
