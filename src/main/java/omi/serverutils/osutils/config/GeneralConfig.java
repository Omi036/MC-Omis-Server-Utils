package omi.serverutils.osutils.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import omi.serverutils.osutils.OSUtils;

public class GeneralConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();


    public static final ForgeConfigSpec.ConfigValue<Boolean> TABLIST_ENABLED = BUILDER
            .comment("Enable custom tablist (configurable on tablist.toml")
            .define("tablist_enabled", true);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean tablistEnabled;

    public static void load() {
        tablistEnabled = TABLIST_ENABLED.get();
    }
}
