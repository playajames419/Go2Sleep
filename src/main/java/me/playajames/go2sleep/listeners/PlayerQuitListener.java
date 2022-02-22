package me.playajames.go2sleep.listeners;

import me.playajames.go2sleep.SleepHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        new SleepHandler().forceWake(event.getPlayer());

    }

}
