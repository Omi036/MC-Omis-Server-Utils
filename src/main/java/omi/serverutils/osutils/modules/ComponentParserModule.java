package omi.serverutils.osutils.modules;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import omi.serverutils.osutils.OSUtils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ComponentParserModule {
    public static String parseUserString(String message, ServerPlayer player){
        String ping = String.valueOf(player.latency);
        String name = player.getName().getString();
        String ip = player.getIpAddress();
        String playerCount = String.valueOf(OSUtils.Server.getPlayerCount());
        String hour = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        String health = String.valueOf(Math.round(player.getHealth()));

        return message
            .replaceAll("\\{player_ping}", ping)
            .replaceAll("\\{player_name}", name)
            .replaceAll("\\{player_ip}", ip)
            .replaceAll("\\{player_count}", playerCount)
            .replaceAll("\\{player_health}", health)
            .replaceAll("\\{hour}", hour);
    }

    public static String parseField(String initialString, String field, String value){
        return initialString.replaceAll(String.format("\\{%s}", field), value);
    }

    public static Component parseFromJSONString(String jsonString){
        return Component.Serializer.fromJson(jsonString);
    }

    public static Component parseJsonUserString(String message, ServerPlayer player){
        message = parseUserString(message, player);
        return parseFromJSONString(message);
    }

    public static String escapeSpecialCharacters(String message) {
        message = message.replace("\\", "\\\\");
        message = message.replace("\"", "\\\"");

        return message;
    }
}
