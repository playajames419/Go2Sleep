package me.playajames.go2sleep;

import me.playajames.go2sleep.npc.NPC;
import net.minecraft.server.v1_15_R1.EntityPose;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SleepHandler {

    private final NamespacedKey SLEEP_KEY = new NamespacedKey(Go2Sleep.getPlugin(Go2Sleep.class), "go2sleep_sleep");

    public void sleep(Player player) {
        System.out.println("Trying to sleep hitPlayer."); //todo remove

        if (player.isDead())
            return;

        NPC npc = new NPC(player.getName(), player.getLocation());
        npc.showAll();
        npc.setPose(EntityPose.SLEEPING);
        npc.removeFromTabList();
        hidePlayer(player);

        //todo stop player from movement, maybe attach to invisible seat

        new BukkitRunnable() {
            @Override
            public void run() {
                wake(player, npc);
            }
        }.runTaskLater(Go2Sleep.getPlugin(Go2Sleep.class), 20 * Go2Sleep.getPlugin(Go2Sleep.class).getConfig().getInt("sleep-time"));
    }

    public void wake(Player player, NPC npc) {
        npc.destroy();
        showPlayer(player);
    }

    private void hidePlayer(Player player) {
        for (Player playerI : Bukkit.getOnlinePlayers()) {
            playerI.hidePlayer(Go2Sleep.getPlugin(Go2Sleep.class), player);
        }
    }

    private void showPlayer(Player player) {
        for (Player playerI : Bukkit.getOnlinePlayers()) {
            playerI.showPlayer(Go2Sleep.getPlugin(Go2Sleep.class), player);
        }
    }
}
