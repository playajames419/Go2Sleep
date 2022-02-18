package me.playajames.go2sleep.enchants;

import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.Field;

public class BedtimeEnchantment extends EnchantmentWrapper {

    public BedtimeEnchantment(String key, String name, int maxLvl, int startLvl, boolean isTreasure, boolean isCursed) {
        super(key, name, maxLvl, startLvl, isTreasure, isCursed);
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

}
