package me.playajames.go2sleep.enchants;

import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Field;
import java.util.Locale;

public class BedtimeEnchantment extends EnchantmentWrapper {

    private final int sleepTime;

    public BedtimeEnchantment(String key, String name, int maxLvl, int startLvl, boolean isTreasure, boolean isCursed, int sleepTime) {
        super(key.toLowerCase(Locale.ROOT), name, maxLvl, startLvl, isTreasure, isCursed);
        this.sleepTime = sleepTime;
        register();
    }

    public void register() {
        try {
            Field f = Enchantment.class.getDeclaredField("acceptingNew");
            f.setAccessible(true);
            f.set(null, true);
            Enchantment.registerEnchantment(this);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public int getSleepTime() {
        return sleepTime;
    }

}
