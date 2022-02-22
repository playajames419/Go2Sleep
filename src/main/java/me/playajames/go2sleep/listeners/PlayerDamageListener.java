package me.playajames.go2sleep.listeners;

import me.playajames.go2sleep.Go2Sleep;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerDamageListener implements Listener {

    @EventHandler
    public void onPlayerDamaged(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof Player))
            return;

        if (!Go2Sleep.getPlugin(Go2Sleep.class).getConfig().getBoolean("take-damage-by-entity-while-sleeping"))
            event.setCancelled(true);

    }

}
