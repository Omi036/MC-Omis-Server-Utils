package omi.serverutils.osutils.modules;

import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import omi.serverutils.osutils.OSUtils;
import omi.serverutils.osutils.config.GeneralConfig;
import omi.serverutils.osutils.config.TablistConfig;
import omi.serverutils.osutils.config.WelcomeMsgConfig;

public class ConfigModule {

    public static void registerConfigFiles(){
        OSUtils.LOGGER.info("Registering config files");

        ModLoadingContext.get().registerConfig(
            ModConfig.Type.COMMON,
            GeneralConfig.SPEC,
            "osutils/general.toml"
        );

        ModLoadingContext.get().registerConfig(
            ModConfig.Type.COMMON,
            TablistConfig.SPEC,
            "osutils/tablist.toml"
        );

        ModLoadingContext.get().registerConfig(
            ModConfig.Type.COMMON,
            WelcomeMsgConfig.SPEC,
            "osutils/welcoming.toml"
        );
    }
}
