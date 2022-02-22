package me.playajames.go2sleep.listeners;

import me.playajames.go2sleep.Go2Sleep;
import me.playajames.go2sleep.SleepHandler;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.persistence.PersistentDataType;

import static me.playajames.go2sleep.Go2Sleep.BEDTIME_ENCHANTMENT;
import static me.playajames.go2sleep.SleepHandler.SLEEPING;

public class ArrowShootListener implements Listener {

    private final NamespacedKey BEDTIME_KEY = new NamespacedKey(Go2Sleep.getPlugin(Go2Sleep.class), "go2sleep_bedtime");

    @EventHandler
    public void onArrowShoot(EntityShootBowEvent event) {

        if (event.getBow().getEnchantments().isEmpty())
            return;

        if (event.getBow().getItemMeta().hasEnchant(BEDTIME_ENCHANTMENT))
            event.getProjectile().getPersistentDataContainer().set(BEDTIME_KEY, PersistentDataType.BYTE, (byte) 1);
    }

    @EventHandler
    public void onArrowHit(ProjectileHitEvent event) {

        if (!(event.getEntity() instanceof Arrow))
            return;

        if (event.getHitEntity() == null || !(event.getHitEntity() instanceof Player))
            return;

        Arrow arrow = (Arrow) event.getEntity();
        Player hitPlayer = (Player) event.getHitEntity();

        if (SLEEPING.containsKey(hitPlayer.getUniqueId())) {
            if (!Go2Sleep.getPlugin(Go2Sleep.class).getConfig().getBoolean("take-damage-by-entity-while-sleeping"))
                arrow.remove();
            return;
        }

        if (arrow.getPersistentDataContainer().has(BEDTIME_KEY, PersistentDataType.BYTE)) {
            hitPlayer.damage(arrow.getDamage(), arrow);
            new SleepHandler().sleep(hitPlayer);
        }

    }

}
