package omi.serverutils.osutils.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import omi.serverutils.osutils.OSUtils;
import omi.serverutils.osutils.config.GeneralConfig;
import omi.serverutils.osutils.config.POPConfig;

public class PlayerLeftEventHandler {

    @SubscribeEvent
    public void onPlayerLeft(PlayerEvent.PlayerLoggedOutEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();

        // POP Module
        if(GeneralConfig.popEnabled && POPConfig.popDeopOnLeave){
            OSUtils.Server.getPlayerList().deop(player.getGameProfile());
        }

    }
}
