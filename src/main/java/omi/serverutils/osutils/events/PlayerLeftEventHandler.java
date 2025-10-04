package omi.serverutils.osutils.events;

import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import omi.serverutils.osutils.OSUtils;

@Mod.EventBusSubscriber(modid = OSUtils.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerLeftEventHandler {

    @SubscribeEvent
    public static void onPlayerLeft(PlayerEvent.PlayerLoggedOutEvent event) {

    }
}
