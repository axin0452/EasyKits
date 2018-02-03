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
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.easykits.events.KitEvent;
import com.gmail.trentech.easykits.kit.Kit;
import com.gmail.trentech.pjc.help.Help;

public class CMDKit implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (args.hasAny("help")) {
			Help.executeList(src, Help.get("kit").get().getChildren());
			return CommandResult.empty();
		}
		
		if (!(src instanceof Player)) {
			throw new CommandException(Text.of(TextColors.RED, "Must be a player"), false);
		}
		Player player = (Player) src;
		
		if (!args.hasAny("kit")) {
			Help.executeList(src, Help.get("kit").get().getChildren());
			return CommandResult.success();
		}
		Kit kit = args.<Kit>getOne("kit").get();

		KitEvent.Get event = new KitEvent.Get(kit, Cause.of(EventContext.builder().add(EventContextKeys.PLAYER, player).build(), player));

		if (!Sponge.getEventManager().post(event)) {
			if(!event.getKitService().setKit(player, kit, false)) {
				throw new CommandException(Text.of(TextColors.RED, "Could not give kit. Possibly need more inventory space."), false);
			}
		}

		return CommandResult.success();
	}

}
