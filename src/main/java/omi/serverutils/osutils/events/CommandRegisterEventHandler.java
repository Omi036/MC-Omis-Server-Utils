package omi.serverutils.osutils.events;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import omi.serverutils.osutils.OSUtils;

@Mod.EventBusSubscriber(modid = OSUtils.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommandRegisterEventHandler {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {

    }

}
