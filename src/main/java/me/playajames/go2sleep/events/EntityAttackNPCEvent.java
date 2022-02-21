package me.playajames.go2sleep.events;

import me.playajames.go2sleep.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EntityAttackNPCEvent extends Event implements Cancellable {

    private final Player player;
    private final NPC npc;
    private boolean cancelled;
    private static HandlerList HANDLERS = new HandlerList();

    public EntityAttackNPCEvent(Player player, NPC npc) {
        this.player = player;
        this.npc = npc;
        this.cancelled = false;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
