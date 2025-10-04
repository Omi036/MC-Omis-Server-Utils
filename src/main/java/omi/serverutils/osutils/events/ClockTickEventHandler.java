package omi.serverutils.osutils.events;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import omi.serverutils.osutils.OSUtils;
import omi.serverutils.osutils.config.GeneralConfig;
import omi.serverutils.osutils.config.TablistConfig;
import omi.serverutils.osutils.modules.TablistModule;

public class ClockTickEventHandler {

    public static Integer TPS = 20;

    @SubscribeEvent
    public void onTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        OSUtils.Clock++;

        // Tablist update rate
        if (OSUtils.Clock % (TPS * TablistConfig.tablistUpdateRatio) == 0) {
            TablistModule.updateTabList(event.getServer());
        };
    }
}
