package com.gmail.trentech.easykits.commands;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.easykits.kit.Kit;
import com.gmail.trentech.easykits.kit.KitService;
import com.gmail.trentech.pjc.help.Help;

public class CMDLimit implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Help help = Help.get("kit limit").get();

		if (!args.hasAny("kit")) {
			throw new CommandException(Text.builder().onClick(TextActions.executeCallback(help.execute())).append(help.getUsageText()).build(), false);
		}
		Kit kit = args.<Kit>getOne("kit").get();
		
		if (!args.hasAny("limit")) {
			throw new CommandException(Text.builder().onClick(TextActions.executeCallback(help.execute())).append(help.getUsageText()).build(), false);
		}
		int limit = args.<Integer>getOne("limit").get();
		
		kit.setLimit(limit);
		
		KitService kitService = Sponge.getServiceManager().provideUnchecked(KitService.class);
		
		kitService.save(kit);
		
		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Set limit of kit ", kit.getName(), " to ", limit));
		
		return CommandResult.success();
	}
}