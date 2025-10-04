package omi.serverutils.osutils.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import omi.serverutils.osutils.OSUtils;

public class TablistConfig {
    static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.ConfigValue<String> TABLIST_HEADER = BUILDER
            .comment("Message on the top of the TabList, you can use minecraft format")
            .define("tablist_header", "Default Header");

    public static final ForgeConfigSpec.ConfigValue<String> TABLIST_FOOTER = BUILDER
            .comment("Message on the bottom of the TabList")
            .define("tablist_footer", "Default Footer");

    public static final ForgeConfigSpec.ConfigValue<Integer> TABLIST_UPDATE_RATIO = BUILDER
            .comment("How many seconds it will wait before updating the tablist")
            .define("tablist_update_ratio", 5);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static String tablistHeader;
    public static String tablistFooter;
    public static Integer tablistUpdateRatio;

    public static void load() {
        tablistHeader = TABLIST_HEADER.get();
        tablistFooter = TABLIST_FOOTER.get();
        tablistUpdateRatio = TABLIST_UPDATE_RATIO.get();
    }
}
