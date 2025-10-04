package omi.serverutils.osutils.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class TablistConfig {
    static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.ConfigValue<String> TABLIST_HEADER = BUILDER
            .comment("Message on the top of the TabList, you can use minecraft format")
            .define("tablist_header", "Default Header");

    public static final ForgeConfigSpec.ConfigValue<String> TABLIST_FOOTER = BUILDER
            .comment("Message on the bottom of the TabList")
            .define("tablist_footer", "Default Footer");

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static String tablist_header;
    public static String tablist_footer;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        tablist_header = TABLIST_HEADER.get();
        tablist_footer = TABLIST_FOOTER.get();
    }
}
