package omi.serverutils.osutils.events;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import omi.serverutils.osutils.OSUtils;
import omi.serverutils.osutils.config.GeneralConfig;
import omi.serverutils.osutils.config.WelcomeMsgConfig;

import java.util.Objects;

public class PlayerJoinEventHandler {

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();

        // If welcoming is enabled
        if(GeneralConfig.welcomingEnabled) {
            String ping = String.valueOf(player.latency);
            String name = player.getName().getString();
            String ip = player.getIpAddress();
            String playerCount = String.valueOf(OSUtils.Server.getPlayerCount());

            Component message;

            String welcomeMessage = WelcomeMsgConfig.welcomeMessage;
            welcomeMessage = welcomeMessage
                .replaceAll("\\{player_ping}", ping)
                .replaceAll("\\{player_name}", name)
                .replaceAll("\\{player_ip}", ip)
                .replaceAll("\\{player_count}", playerCount);


            // Try sending message
            try {
                message = Objects.requireNonNull(Component.Serializer.fromJson(welcomeMessage));
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
