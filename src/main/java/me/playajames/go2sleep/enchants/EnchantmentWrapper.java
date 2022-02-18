package me.playajames.go2sleep.enchants;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class EnchantmentWrapper extends Enchantment {

    private final String name;
    private final int maxLvl;
    private final int startLvl;
    private final boolean treasure;
    private final boolean cursed;



    public EnchantmentWrapper(String key, String name, int maxLvl, int startLvl, boolean isTreasure, boolean isCursed) {
        super(NamespacedKey.minecraft(key));
        this.name = name;
        this.maxLvl = maxLvl;
        this.startLvl = startLvl;
        this.treasure = isTreasure;
        this.cursed = isCursed;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getMaxLevel() {
        return maxLvl;
    }

    @Override
    public int getStartLevel() {
        return startLvl;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return null;
    }

    @Override
    public boolean isTreasure() {
        return treasure;
    }

    @Override
    public boolean isCursed() {
        return cursed;
    }

    @Override
    public boolean conflictsWith(Enchantment other) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack item) {
        return item.getType().equals(Material.BOW);
    }

}
