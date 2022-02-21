package me.playajames.go2sleep.protocol;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import me.playajames.go2sleep.Go2Sleep;
import me.playajames.go2sleep.events.EntityAttackNPCEvent;
import me.playajames.go2sleep.npc.NPC;
import net.minecraft.server.v1_15_R1.Packet;
import net.minecraft.server.v1_15_R1.PacketPlayInUseEntity;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PacketReader {

    Channel channel;
    public static Map<UUID, Channel> channels = new HashMap<>();

    public void inject(Player player) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        channel = craftPlayer.getHandle().playerConnection.networkManager.channel;
        channels.put(player.getUniqueId(), channel);

        if (channel.pipeline().get("PacketInjector") != null)
            return;

        channel.pipeline().addAfter("decoder", "PacketInjector", new MessageToMessageDecoder<Packet<?>>() {
            @Override
            protected void decode(ChannelHandlerContext channel, Packet<?> packet, List<Object> arg) throws Exception {
                arg.add(packet);
                readPacket(player, packet);
            }
        });
    }

    public void unInject(Player player) {

    }

    public void readPacket(Player player, Packet<?> packet) {

        if (packet.getClass().getSimpleName().equalsIgnoreCase("packetplayinposition"))
            return;

        if (packet.getClass().getSimpleName().equalsIgnoreCase("packetplayinpositionlook"))
            return;

        if (packet.getClass().getSimpleName().equalsIgnoreCase("packetplayinlook"))
            return;

        if (packet.getClass().getSimpleName().equalsIgnoreCase("packetplayinflying"))
            return;

        if (packet.getClass().getSimpleName().equalsIgnoreCase("packetplayinsteervehicle"))
            return;

        System.out.println(packet);

        if (packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInUseEntity")) {
            if (getValue(packet, "action").toString().equalsIgnoreCase("ATTACK"))
                return;
            if (getValue(packet, "action").toString().equalsIgnoreCase("INTERACT_AT"))
                return;
            if (getValue(packet, "d").toString().equalsIgnoreCase("OFF_HAND"))
                return;

            int id = (int) getValue(packet, "a");
            NPC npc = NPC.find(id);

            if (npc == null)
                return;

            if (getValue(packet, "action").toString().equalsIgnoreCase("INTERACT"))
                Bukkit.getScheduler().scheduleSyncDelayedTask(Go2Sleep.getPlugin(Go2Sleep.class), new Runnable() {
                    @Override
                    public void run() {
                        Bukkit.getPluginManager().callEvent(new EntityAttackNPCEvent(player, npc));
                    }
                }, 0);
        }
    }

    public Object getValue(Object instance, String name) {
        Object result = null;

        try {
            Field field = instance.getClass().getDeclaredField(name);
            field.setAccessible(true);
            result = field.get(instance);
            field.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
