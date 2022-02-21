package me.playajames.go2sleep.npc;

import com.mojang.authlib.GameProfile;
import me.playajames.go2sleep.Go2Sleep;
import me.playajames.go2sleep.protocol.ProtocolUtils;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.entity.Player;

import java.util.*;

public class NPC {

    private static HashMap<Integer, NPC> npcs = new HashMap<>();

    Set<Player> observers = new HashSet<>();
    EntityPlayer entityPlayer;
    GameProfile profile;

    public static NamespacedKey NPC_KEY = new NamespacedKey(Go2Sleep.getPlugin(Go2Sleep.class), "go2sleep_npc");

    public NPC(String name, Location location) {
        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer nmsWorld = ((CraftWorld)location.getWorld()).getHandle();
        this.profile = new GameProfile(UUID.randomUUID(), name);
        this.entityPlayer =  new EntityPlayer(nmsServer, nmsWorld, this.profile, new PlayerInteractManager(nmsWorld));
        this.entityPlayer.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        npcs.put(entityPlayer.getId(), this);
    }

    public static NPC find(int id) {
        if (npcs.containsKey(id))
            return npcs.get(id);
        return null;
    }

    public void show(Player player) {
        observers.add(player);
        ProtocolUtils.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer), player); // "Adds the player data for the client to use when spawning a player" - https://wiki.vg/Protocol#Spawn_Player
        ProtocolUtils.sendPacket(new PacketPlayOutNamedEntitySpawn(entityPlayer), player); // Spawns the NPC for the player client.
        ProtocolUtils.sendPacket(new PacketPlayOutEntityHeadRotation(entityPlayer, (byte) (entityPlayer.yaw * 256 / 360)), player); // Correct head rotation when spawned in player look direction.
    }

    public void showAll() {
        for (Player player : Bukkit.getOnlinePlayers())
            show(player);
    }

    public void showAll(Set<Player> excluded) {
        for (Player player : Bukkit.getOnlinePlayers())
            if (!excluded.contains(player))
                show(player);
    }

    public void hide(Player player) {
        if (observers.contains(player))
            observers.remove(player);
        ProtocolUtils.sendPacket(new PacketPlayOutEntityDestroy(entityPlayer.getId()), player);
    }

    public void hideAll() {
        for (Player player : Bukkit.getOnlinePlayers())
            hide(player);
    }

    public void setPose(EntityPose pose) {
        DataWatcher watcher = entityPlayer.getDataWatcher();
        watcher.set(DataWatcherRegistry.s.a(6), pose);
        PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(entityPlayer.getId(), watcher, false);
        ProtocolUtils.sendPacket(metadata, observers);
    }

    public void destroy() {
        hideAll();
        ProtocolUtils.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer), observers);
        if (npcs.containsKey(entityPlayer.getId()))
            npcs.remove(entityPlayer.getId());
    }

    public void removeFromTabList() {
        ProtocolUtils.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer), observers);
    }
}
