package omi.serverutils.osutils.events;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import omi.serverutils.osutils.OSUtils;
import omi.serverutils.osutils.config.GeneralConfig;
import omi.serverutils.osutils.config.TablistConfig;
import omi.serverutils.osutils.config.WelcomeMsgConfig;

@Mod.EventBusSubscriber(modid = OSUtils.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigEventHandler {
    @SubscribeEvent
    public static void onLoad(final ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == GeneralConfig.SPEC) {
            OSUtils.LOGGER.info("Loaded general config");
            GeneralConfig.load();
        }

        if (event.getConfig().getSpec() == TablistConfig.SPEC) {
            OSUtils.LOGGER.info("Loaded tablist config");
            TablistConfig.load();
        }

        if(event.getConfig().getSpec() == WelcomeMsgConfig.SPEC) {
            OSUtils.LOGGER.info("Loaded welcoming config");
            WelcomeMsgConfig.load();
        }
    }

    @SubscribeEvent
    public static void onReloading(final ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == GeneralConfig.SPEC) {
            OSUtils.LOGGER.info("Reloaded general config");
            GeneralConfig.load();
        }

        if (event.getConfig().getSpec() == TablistConfig.SPEC) {
            OSUtils.LOGGER.info("Reloaded tablist config");
            TablistConfig.load();
        }

        if(event.getConfig().getSpec() == WelcomeMsgConfig.SPEC) {
            OSUtils.LOGGER.info("Reloaded welcoming config");
            WelcomeMsgConfig.load();
        }
    }
}
