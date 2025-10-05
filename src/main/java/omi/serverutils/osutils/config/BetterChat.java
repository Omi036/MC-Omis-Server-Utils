package omi.serverutils.osutils.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class BetterChat {
    static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.ConfigValue<String> USER_MSG_TEMPLATE = BUILDER
            .comment("What to show on default user message (json format)")
            .define("user_msg_template", "[{ \"text\": \"{hour} {player_name}: {message}\" }]");

    public static final ForgeConfigSpec.ConfigValue<String> OP_MSG_TEMPLATE = BUILDER
            .comment("What to show on OP message (json format)")
            .define("op_msg_template", "[{ \"text\": \"{hour} [OP] {player_name}: {message}\" }]");


    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static String userMessageTemplate;
    public static String opMessageTemplate;

    public static void load() {
        userMessageTemplate = USER_MSG_TEMPLATE.get();
        opMessageTemplate = OP_MSG_TEMPLATE.get();
    }
}
