package omi.serverutils.osutils;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import omi.serverutils.osutils.config.GeneralConfig;
import omi.serverutils.osutils.config.TablistConfig;

@Mod.EventBusSubscriber(modid = OSUtils.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    public static void register() {
        OSUtils.LOGGER.info("Registering config");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, GeneralConfig.SPEC, "osutils/general.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, TablistConfig.SPEC, "osutils/tablist.toml");
    }
}
