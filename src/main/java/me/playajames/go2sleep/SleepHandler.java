package me.playajames.go2sleep;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Bed;
import org.bukkit.entity.Player;

public class SleepHandler {

    private final NamespacedKey SLEEP_KEY = new NamespacedKey(Go2Sleep.getPlugin(Go2Sleep.class), "go2sleep_sleep");

    public void sleep(Player player) {
        System.out.println("Trying to sleep hitPlayer."); //todo remove

        if (player.isDead())
            return;

    }

    public void wake(Player player) {

    }

    private void setBed(Block start, BlockFace facing, Material material) {
        for (Bed.Part part : Bed.Part.values()) {
            start.setBlockData(Bukkit.createBlockData(material, (data) -> {
                ((Bed) data).setPart(part);
                ((Bed) data).setFacing(facing);
            }));
            start = start.getRelative(facing.getOppositeFace());
        }
    }
}
