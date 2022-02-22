package me.playajames.go2sleep.listeners;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.entity.EntityDismountEvent;

import static me.playajames.go2sleep.SleepHandler.SLEEPING;

public class PlayerDismountistener implements Listener {

    @EventHandler
    public void onPlayerDismount(EntityDismountEvent event) {

        if (!(event.getEntity() instanceof Player) || !(event.getDismounted() instanceof ArmorStand))
            return;

        Player player = (Player) event.getEntity();

        if (SLEEPING.containsKey(player.getUniqueId()))
            event.setCancelled(true);

    }

}
