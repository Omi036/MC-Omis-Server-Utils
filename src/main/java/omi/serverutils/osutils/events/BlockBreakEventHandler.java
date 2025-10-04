package omi.serverutils.osutils.events;

import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import omi.serverutils.osutils.OSUtils;

@Mod.EventBusSubscriber(modid = OSUtils.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BlockBreakEventHandler {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {

    }

}
