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
            String headerContent = ComponentParserModule.parseUserString(TablistConfig.tablistHeader, player);
            headerContent = headerContent.replaceAll("(?<!\\\\)&","§");

            String footerContent = ComponentParserModule.parseUserString(TablistConfig.tablistFooter, player);
            footerContent = footerContent.replaceAll("(?<!\\\\)&","§");

            Component header = Component.literal(headerContent);
            Component footer = Component.literal(footerContent);

            player.connection.send(new ClientboundTabListPacket(header, footer));
        }
    }

}
