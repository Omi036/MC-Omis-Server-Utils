package omi.serverutils.osutils.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class WelcomeMsgConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();


    public static final ForgeConfigSpec.ConfigValue<String> WELCOME_MESSAGE = BUILDER
            .comment("Message to show when user joins (in json format)")
            .define("welcome_msg", "[{\"text\":\"Welcome {player_name}\"}]");

    public static final ForgeConfigSpec.ConfigValue<String> WELCOME_SOUND = BUILDER
            .comment("What sounds when a user joins (in resourcepack format")
            .define("welcome_sound", "minecraft:ui.toast.challenge_complete");

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static String welcomeMessage;
    public static String welcomeSound;

    public static void load() {
        welcomeMessage = WELCOME_MESSAGE.get();
        welcomeSound = WELCOME_SOUND.get();
    }
}
