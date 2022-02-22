package me.playajames.go2sleep.listeners;

import me.playajames.go2sleep.SleepHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        new SleepHandler().forceWake(event.getEntity());

    }

}
