package com.gmail.trentech.easykits.commands.delete;

import java.util.HashMap;

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
import com.gmail.trentech.pjc.Main;

public class CMDYes implements CommandExecutor {

	protected static HashMap<CommandSource, Kit> confirm = new HashMap<>();

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (confirm.containsKey(src)) {
			Kit kit = confirm.get(src);

			KitEvent.Delete event;
			
			if(src instanceof Player) {
				event = new KitEvent.Delete(kit, Cause.of(EventContext.builder().add(EventContextKeys.PLAYER, ((Player) src)).build(), ((Player) src)));
			} else {
				event = new KitEvent.Delete(kit, Cause.of(EventContext.builder().add(EventContextKeys.PLUGIN, Main.getPlugin()).build(), src));
			}

			if (!Sponge.getEventManager().post(event)) {
				event.getKitService().delete(event.getKit().getName());

				src.sendMessage(Text.of(TextColors.DARK_GREEN, "Deleted kit ", event.getKit().getName()));

				confirm.remove(src);
			}
		}

		return CommandResult.success();
	}
}
