package me.playajames.go2sleep;

import me.playajames.go2sleep.npc.NPC;
import net.minecraft.server.v1_15_R1.EntityPose;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SleepHandler {

    public static HashMap<UUID, HashMap<String, Object>> SLEEPING = new HashMap<>();

    public void sleep(Player player) {
        if (player.isDead())
            return;

        NPC npc = new NPC(player.getName(), player.getLocation(), player);
        Set<Player> excluded = new HashSet<>();
        excluded.add(player);
        npc.showAll(excluded);
        npc.setPose(EntityPose.SLEEPING);
        npc.removeFromTabList();
        hidePlayer(player);

        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                wake(player);
            }
        }.runTaskLater(Go2Sleep.getPlugin(Go2Sleep.class), 20 * Go2Sleep.getPlugin(Go2Sleep.class).getConfig().getInt("sleep-time"));

        HashMap<String, Object> data = new HashMap<>();

        data.put("npc", npc);
        data.put("armorstand", sit(player));
        data.put("task", task);

        player.getBoundingBox().expand(3,3,3);
        SLEEPING.put(player.getUniqueId(), data);
    }

    public void wake(Player player) {
        ((NPC) SLEEPING.get(player.getUniqueId()).get("npc")).destroy();
        showPlayer(player);
        player.setWalkSpeed(0.2f);
        unSit(player);
        SLEEPING.remove(player.getUniqueId());
        player.teleport(player.getLocation().add(0,1,0));
    }

    public void forceWake(Player player) {
        if (SLEEPING.containsKey(player.getUniqueId())) {
            ((BukkitTask) SLEEPING.get(player.getUniqueId()).get("task")).cancel();
            new SleepHandler().wake(player);
        }
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

    private ArmorStand sit(Player player) {
        ArmorStand armorStand = player.getWorld().spawn(player.getLocation().subtract(0, 0.5, 0), ArmorStand.class, b -> {
            b.setVisible(false);
            b.setSmall(true);
            b.setGravity(false);
            b.setMarker(true);
            b.setBasePlate(false);
            b.setInvulnerable(true);
        });
        armorStand.addPassenger(player);
        return armorStand;
    }

    private void unSit(Player player) {
        ArmorStand armorStand = (ArmorStand) SLEEPING.get(player.getUniqueId()).get("armorstand");
        armorStand.removePassenger(player);
        armorStand.remove();
    }

}
