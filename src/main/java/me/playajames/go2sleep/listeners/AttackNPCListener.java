package me.playajames.go2sleep.listeners;

import me.playajames.go2sleep.SleepHandler;
import me.playajames.go2sleep.events.EntityAttackNPCEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitTask;

import static me.playajames.go2sleep.SleepHandler.SLEEPING;

public class AttackNPCListener implements Listener {

    @EventHandler
    public void onPlayerAttackNPC(EntityAttackNPCEvent event) {
        return;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (SLEEPING.containsKey(event.getEntity().getUniqueId())) {
            ((BukkitTask) SLEEPING.get(event.getEntity().getUniqueId()).get("task")).cancel();
            new SleepHandler().wake(event.getEntity());
        }

    }

}
