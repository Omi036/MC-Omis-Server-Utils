package omi.serverutils.osutils.modules;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import omi.serverutils.osutils.config.GeneralConfig;
import omi.serverutils.osutils.config.TablistConfig;

import java.util.List;

public class TablistModule {

    public static void updateTabList(MinecraftServer server){
        if(!GeneralConfig.tablistEnabled) return;

        List<ServerPlayer> players = server.getPlayerList().getPlayers();

        for (ServerPlayer player : players) {
            String ping = String.valueOf(player.latency);
            String name = player.getName().getString();
            String ip = player.getIpAddress();
            String health = String.valueOf(Math.round(player.getHealth()));
            String playerCount = String.valueOf(server.getPlayerList().getPlayerCount());

            String headerContent = TablistConfig.tablistHeader
                .replaceAll("(?<!\\\\)&","§")
                .replaceAll("\\{player_ping}", ping)
                .replaceAll("\\{player_name}", name)
                .replaceAll("\\{player_ip}", ip)
                .replaceAll("\\{player_count}", playerCount)
                .replaceAll("\\{player_health}", health);

            String footerContent = TablistConfig.tablistFooter
                .replaceAll("(?<!\\\\)&","§")
                .replaceAll("\\{player_ping}", ping)
                .replaceAll("\\{player_name}", name)
                .replaceAll("\\{player_ip}", ip)
                .replaceAll("\\{player_count}", playerCount)
                .replaceAll("\\{player_health}", health);

            Component header = Component.literal(headerContent);
            Component footer = Component.literal(footerContent);

            player.connection.send(new ClientboundTabListPacket(header, footer));
        }
    }

}
