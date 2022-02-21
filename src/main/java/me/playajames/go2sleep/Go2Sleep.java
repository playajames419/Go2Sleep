package me.playajames.go2sleep;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIConfig;
import me.playajames.go2sleep.commands.Go2SleepCommand;
import me.playajames.go2sleep.enchants.BedtimeEnchantment;
import me.playajames.go2sleep.listeners.ArrowShootListener;
import me.playajames.go2sleep.listeners.AttackNPCListener;
import me.playajames.go2sleep.listeners.JoinQuitListener;
import me.playajames.go2sleep.listeners.PlayerMoveListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class Go2Sleep extends JavaPlugin {

    public static Logger LOGGER;
    public static Enchantment BEDTIME_ENCHANTMENT;
    public static String PREFIX;


    @Override
    public void onEnable() {
        LOGGER = getLogger();
        saveDefaultConfig();
        loadCommandAPI();
        PREFIX = getConfig().getString("prefix");
        registerCustomEnchants();
        registerCommands();
        registerListeners();
        LOGGER.info("Plugin has been enabled.");
    }

    @Override
    public void onDisable() {
        LOGGER.info("Plugin has been disabled.");
    }

    private void registerCustomEnchants() {
        ConfigurationSection config = getConfig();
        BEDTIME_ENCHANTMENT = new BedtimeEnchantment(
                config.getString("name"),
                config.getString("name"),
                1,
                1,
                config.getBoolean("treasure"),
                config.getBoolean("cursed"),
                config.getInt("sleep-time"));
    }

    private void registerCommands() {
        new Go2SleepCommand("go2sleep").register();
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new ArrowShootListener(), this);
        Bukkit.getPluginManager().registerEvents(new JoinQuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new AttackNPCListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(), this);
    }

    private void loadCommandAPI() {
        CommandAPI.onLoad(new CommandAPIConfig());
        CommandAPI.onEnable(this);
    }

}
