package omi.serverutils.osutils.events;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import omi.serverutils.osutils.OSUtils;
import omi.serverutils.osutils.config.BetterChat;
import omi.serverutils.osutils.config.GeneralConfig;
import omi.serverutils.osutils.config.WelcomeMsgConfig;
import omi.serverutils.osutils.modules.ComponentParserModule;

public class ServerChatEventHandler {

    @SubscribeEvent
    public void onServerChat(ServerChatEvent event) {

        if (GeneralConfig.betterChatEnabled) {
            ServerPlayer author = event.getPlayer();
            String messageAudio = BetterChat.messageSound;
            String message = event.getMessage().getString();
            String escapedMessage = ComponentParserModule.escapeSpecialCharacters(message);
            boolean isOp = OSUtils.Server.getPlayerList().isOp(author.getGameProfile());

            // Change template if its op
            String parsedMessage = isOp
                    ? ComponentParserModule.parseUserString(BetterChat.opMessageTemplate, author)
                    : ComponentParserModule.parseUserString(BetterChat.userMessageTemplate, author);

            parsedMessage = parsedMessage.replace("{message}", escapedMessage);
            String finalParsedMessage = parsedMessage;

            // Send message to each player
            OSUtils.Server.getPlayerList().getPlayers().forEach(player -> {
                player.sendSystemMessage(ComponentParserModule.parseFromJSONString(finalParsedMessage));

                if(!messageAudio.isEmpty()) {
                    player.playNotifySound(
                        SoundEvent.createVariableRangeEvent(new ResourceLocation(BetterChat.messageSound)),
                        SoundSource.PLAYERS,
                        1,
                        1);
                }
            });



            // Log to console and cancel the default handler
            OSUtils.LOGGER.info(String.format("<%s> %s", author.getDisplayName().getString(), message));
            event.setCanceled(true);
        }
    }
}
