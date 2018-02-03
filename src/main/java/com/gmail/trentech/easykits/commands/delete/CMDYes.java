package com.gmail.trentech.easykits.commands.delete;

import java.util.HashMap;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.easykits.kit.KitService;

public class CMDYes implements CommandExecutor {

	protected static HashMap<CommandSource, String> confirm = new HashMap<>();

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (confirm.containsKey(src)) {
			String kit = confirm.get(src);

			KitService kitService = Sponge.getServiceManager().provideUnchecked(KitService.class);

			kitService.delete(kit);

			src.sendMessage(Text.of(TextColors.DARK_GREEN, "Deleted kit ", kit));

			confirm.remove(src);
		}

		return CommandResult.success();
	}
}
