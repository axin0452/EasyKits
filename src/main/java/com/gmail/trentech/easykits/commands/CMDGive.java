package com.gmail.trentech.easykits.commands;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.easykits.events.KitEvent;
import com.gmail.trentech.easykits.kit.Kit;
import com.gmail.trentech.pjc.help.Help;

public class CMDGive implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Help help = Help.get("kit give").get();
		
		if (!args.hasAny("kit")) {
			throw new CommandException(Text.builder().onClick(TextActions.executeCallback(help.execute())).append(help.getUsageText()).build(), false);
		}
		Kit kit = args.<Kit>getOne("kit").get();
		
		if (!args.hasAny("player")) {
			throw new CommandException(Text.builder().onClick(TextActions.executeCallback(help.execute())).append(help.getUsageText()).build(), false);
		}
		Player player = args.<Player>getOne("player").get();		

		KitEvent.Give event = new KitEvent.Give(kit, Cause.of(EventContext.builder().add(EventContextKeys.PLAYER, player).build(), player));

		if (!Sponge.getEventManager().post(event)) {
			if(!event.getKitService().setKit(player, event.getKit(), false)) {
				throw new CommandException(Text.of(TextColors.RED, "Could not give kit. Possibly need more inventory space."), false);
			}
			
			player.sendMessage(Text.of(TextColors.DARK_GREEN, src.getName(),  " gave you kit ", event.getKit().getName()));
			src.sendMessage(Text.of(TextColors.DARK_GREEN, "Gave kit ", event.getKit().getName(), " to ", player.getName()));
		}

		return CommandResult.success();
	}
}