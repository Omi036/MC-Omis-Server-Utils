package omi.serverutils.osutils.events;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import omi.serverutils.osutils.OSUtils;

public class PlayerDeathEventHandler {

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event){
        if (!(event.getEntity() instanceof Player player)) return;

    }
}
