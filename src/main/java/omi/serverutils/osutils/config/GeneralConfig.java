package omi.serverutils.osutils.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import omi.serverutils.osutils.OSUtils;

public class GeneralConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();


    public static final ForgeConfigSpec.ConfigValue<Boolean> TABLIST_ENABLED = BUILDER
            .comment("Enable custom tablist")
            .define("tablist_enabled", true);

    public static final ForgeConfigSpec.ConfigValue<Boolean> WELCOMING_ENABLED = BUILDER
            .comment("Enable custom welcome message and sounds")
            .define("welcoming_enabled", true);

    public static final ForgeConfigSpec.ConfigValue<Boolean> BETTER_CHAT_ENBALED = BUILDER
            .comment("Enable better and customized chat")
            .define("better_chat_enabled", true);

    public static final ForgeConfigSpec.ConfigValue<Boolean> POP_ENABLED = BUILDER
            .comment("Enable passworded OP command, along with more features (see conf file)")
            .define("pop_enabled", true);

    public static final ForgeConfigSpec.ConfigValue<Boolean> SEQUENCES_ENABLED = BUILDER
            .comment("Enable command sequences")
            .define("sequences_enabled", true);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean tablistEnabled;
    public static boolean welcomingEnabled;
    public static boolean betterChatEnabled;
    public static boolean popEnabled;
    public static boolean sequencesEnabled;

    public static void load() {
        tablistEnabled = TABLIST_ENABLED.get();
        welcomingEnabled = WELCOMING_ENABLED.get();
        betterChatEnabled = BETTER_CHAT_ENBALED.get();
        popEnabled = POP_ENABLED.get();
        sequencesEnabled = SEQUENCES_ENABLED.get();
    }
}
