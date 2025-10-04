package omi.serverutils.osutils.events;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import omi.serverutils.osutils.OSUtils;
import omi.serverutils.osutils.config.GeneralConfig;

public class PlayerJoinEventHandler {

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        OSUtils.LOGGER.info("OJOO");
        OSUtils.LOGGER.info(String.valueOf(GeneralConfig.tablistEnabled));
    }
}
