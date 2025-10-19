package omi.serverutils.osutils.events;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import omi.serverutils.osutils.commands.POPCommand;
import omi.serverutils.osutils.commands.region.RegionCommand;
import omi.serverutils.osutils.commands.SequenceCommand;
import omi.serverutils.osutils.config.GeneralConfig;

public class CommandRegisterEventHandler {

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        if(GeneralConfig.popEnabled) POPCommand.register(event.getDispatcher());
        if(GeneralConfig.sequencesEnabled) SequenceCommand.register(event.getDispatcher());
        if(GeneralConfig.regionsEnabled) RegionCommand.register(event.getDispatcher());
    }

}
