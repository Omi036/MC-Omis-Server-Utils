package omi.serverutils.osutils.config;

import net.minecraftforge.common.ForgeConfigSpec;
import omi.serverutils.osutils.commands.POPCommand;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class POPConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.ConfigValue<String> POP_PASSWORD = BUILDER
        .comment("Password for pop command")
        .define("pop_password", "changeme");

    public static final ForgeConfigSpec.ConfigValue<Boolean> DEOP_ONJOIN = BUILDER
        .comment("Whether to disable op on player join")
        .define("pop_deop_onjoin", true);

    public static final ForgeConfigSpec.ConfigValue<Boolean> DEOP_ONLEAVE = BUILDER
        .comment("Whether to disable op on player leave")
        .define("pop_deop_onleave", true);

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> OP_IPS = BUILDER
        .comment("List of allowed IPS to access oop command, leave empty to disable")
        .defineListAllowEmpty("pop_allowed_ips", List.of("127.0.0.1"), POPConfig::validateIp);

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> OP_NAMES = BUILDER
        .comment("List of allowed names to access oop command, leave empty to disable")
        .defineListAllowEmpty("pop_allowed_names", List.of(), POPConfig::validateName);


    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static String popPassword;
    public static boolean popDeopOnJoin;
    public static boolean popDeopOnLeave;
    public static Set<String> popAllowedIps;
    public static Set<String> popAllowedNames;

    private static boolean validateIp(final Object obj){
        return obj instanceof final String ip && ip.matches("^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$");
    }

    private static boolean validateName(final Object obj){
        return obj instanceof String;
    }

    public static void load() {
        popPassword = POP_PASSWORD.get();
        popDeopOnJoin = DEOP_ONJOIN.get();
        popDeopOnLeave = DEOP_ONLEAVE.get();
        popAllowedIps = new HashSet<>(OP_IPS.get());
        popAllowedNames = new HashSet<>(OP_NAMES.get());
    }
}
