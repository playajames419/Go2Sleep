package me.playajames.go2sleep.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.playajames.go2sleep.protocol.ProtocolUtils;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class NPC {

    private static HashMap<Integer, NPC> npcs = new HashMap<>();

    Set<Player> observers = new HashSet<>();
    EntityPlayer entityPlayer;
    GameProfile profile;

    public NPC(String name, Location location, Player player) {
        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer nmsWorld = ((CraftWorld)location.getWorld()).getHandle();
        this.profile = new GameProfile(UUID.randomUUID(), name);
        this.entityPlayer =  new EntityPlayer(nmsServer, nmsWorld, this.profile, new PlayerInteractManager(nmsWorld));
        this.entityPlayer.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        //todo Sets skin, not working?!>W:!?>>!<#&!^#@
        DataWatcher dataWatcher = entityPlayer.getDataWatcher();
        dataWatcher.set(new DataWatcherObject<>(16, DataWatcherRegistry.a), (byte) 127);
        String[] skin = getSkin(player);
        this.profile.getProperties().put("textures", new Property("textures", skin[0], skin[1]));

        npcs.put(entityPlayer.getId(), this);
    }

    private static String[] getSkin(Player player) {//todo Not working!>:QL!(Q!L
        try {
            EntityPlayer ep = ((CraftPlayer) player).getHandle();
            GameProfile profile = ep.getProfile();
            Property property = profile.getProperties().get("textures").iterator().next();
            String texture = property.getValue();
            String signature = property.getSignature();
            return new String[] {texture, signature};
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static NPC find(int id) {
        if (npcs.containsKey(id))
            return npcs.get(id);
        return null;
    }

    public void show(Player player) {
        if (!observers.contains(player.getUniqueId()))
            observers.add(player);
        ProtocolUtils.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer), player); // "Adds the player data for the client to use when spawning a player" - https://wiki.vg/Protocol#Spawn_Player
        ProtocolUtils.sendPacket(new PacketPlayOutNamedEntitySpawn(entityPlayer), player); // Spawns the NPC for the player client.
        ProtocolUtils.sendPacket(new PacketPlayOutEntityHeadRotation(entityPlayer, (byte) (entityPlayer.yaw * 256 / 360)), player); // Correct head rotation when spawned in player look direction.
        ProtocolUtils.sendPacket(new PacketPlayOutEntityMetadata(entityPlayer.getId(), entityPlayer.getDataWatcher(), true), player);
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
        BlockPosition b = new BlockPosition(entityPlayer.locX(), entityPlayer.locY(), entityPlayer.locZ());
        watcher.set(DataWatcherRegistry.s.a(6), pose);
        PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(entityPlayer.getId(), watcher, true);
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
