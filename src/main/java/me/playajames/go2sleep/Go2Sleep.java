package me.playajames.go2sleep;

import me.playajames.go2sleep.enchants.BedtimeEnchantment;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class Go2Sleep extends JavaPlugin {

    public static Logger LOGGER;

    @Override
    public void onEnable() {
        LOGGER = getLogger();
        saveDefaultConfig();
        registerCustomEnchants();
        LOGGER.info("Plugin has been enabled.");
    }

    @Override
    public void onDisable() {
        LOGGER.info("Plugin has been disabled.");
    }

    private void registerCustomEnchants() {
        ConfigurationSection config = getConfig();
        new BedtimeEnchantment(
                config.getString("name"),
                config.getString("name"),
                1,
                1,
                config.getBoolean("treasure"),
                config.getBoolean("cursed")).register();
    }
}
