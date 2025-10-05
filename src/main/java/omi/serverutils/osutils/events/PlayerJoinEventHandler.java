package omi.serverutils.osutils.events;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import omi.serverutils.osutils.OSUtils;
import omi.serverutils.osutils.config.GeneralConfig;
import omi.serverutils.osutils.config.WelcomeMsgConfig;
import omi.serverutils.osutils.modules.ComponentParserModule;

import java.util.Objects;

public class PlayerJoinEventHandler {

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();

        // If welcoming is enabled
        if(GeneralConfig.welcomingEnabled) {


            // Try sending message
            try {
                String welcomeMessage = WelcomeMsgConfig.welcomeMessage;
                Component message = ComponentParserModule.parseJsonUserString(welcomeMessage, player);
                player.sendSystemMessage(message);
            } catch (Exception e){
                OSUtils.LOGGER.error("Error sending welcome message, does it use a valid json format?");
            }

            // Try sending sound effect
            try {
                player.playNotifySound(
                    SoundEvent.createVariableRangeEvent(new ResourceLocation(WelcomeMsgConfig.welcomeSound)),
                    SoundSource.MASTER,
                    1,
                    1);
            } catch (Exception e){
                OSUtils.LOGGER.error("Error sending welcome sound");
            }
        }
    }
}
