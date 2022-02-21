package me.playajames.go2sleep.protocol;

import net.minecraft.server.v1_15_R1.Packet;
import net.minecraft.server.v1_15_R1.PlayerConnection;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Set;

public class ProtocolUtils {

    public static void sendPacket(Packet packet, Player player) {
        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
        connection.sendPacket(packet);
    }

    public static void sendPacket(Packet packet, Set<Player> players) {
        for (Player player : players) {
            sendPacket(packet, player);
        }
    }

}
