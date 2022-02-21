package me.playajames.go2sleep.npc;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NPC {

    List<Player> observers = new ArrayList<>();
    EntityPlayer entityPlayer;
    GameProfile profile;

    public NPC(String name, Location location) {
        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer nmsWorld = ((CraftWorld)location.getWorld()).getHandle();
        this.profile = new GameProfile(UUID.randomUUID(), name);
        this.entityPlayer =  new EntityPlayer(nmsServer, nmsWorld, this.profile, new PlayerInteractManager(nmsWorld));
        this.entityPlayer.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    private void sendPacket(Packet packet, Player player) {
        PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
        connection.sendPacket(packet);
    }

    private void sendPacket(Packet packet) {
        for (Player player : observers) {
            PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
            connection.sendPacket(packet);
        }
    }

    public void show(Player player) {
        observers.add(player);
        sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer), player); // "Adds the player data for the client to use when spawning a player" - https://wiki.vg/Protocol#Spawn_Player
        sendPacket(new PacketPlayOutNamedEntitySpawn(entityPlayer), player); // Spawns the NPC for the player client.
        sendPacket(new PacketPlayOutEntityHeadRotation(entityPlayer, (byte) (entityPlayer.yaw * 256 / 360)), player); // Correct head rotation when spawned in player look direction.
    }

    public void showAll() {
        for (Player player : Bukkit.getOnlinePlayers())
            show(player);
    }

    public void hide(Player player) {
        if (observers.contains(player))
            observers.remove(player);
        sendPacket(new PacketPlayOutEntityDestroy(entityPlayer.getId()), player);
    }

    public void hideAll() {
        for (Player player : Bukkit.getOnlinePlayers())
            hide(player);
    }

    public void setPose(EntityPose pose) {
        DataWatcher watcher = entityPlayer.getDataWatcher();
        watcher.set(DataWatcherRegistry.s.a(6), pose);
        PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(entityPlayer.getId(), watcher, false);
        sendPacket(metadata);
    }

    public void destroy() {
        hideAll();
        sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer));
    }

    public void removeFromTabList() {
        sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer));
    }
}
