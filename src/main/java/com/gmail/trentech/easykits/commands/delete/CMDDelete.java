package com.gmail.trentech.easykits.commands.delete;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import com.gmail.trentech.easykits.kit.Kit;
import com.gmail.trentech.pjc.help.Help;

public class CMDDelete implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Help help = Help.get("kit delete").get();

		if (!args.hasAny("kit")) {
			throw new CommandException(Text.builder().onClick(TextActions.executeCallback(help.execute())).append(help.getUsageText()).build(), false);
		}
		Kit kit = args.<Kit>getOne("kit").get();

		src.sendMessage(Text.builder().color(TextColors.RED).append(Text.of(TextColors.RED, "[WARNING] ", TextColors.YELLOW, "Deleting this kit is permanent and cannot be undone. Confirm? ")).onClick(TextActions.runCommand("/easykits:kit delete yes")).append(Text.of(TextColors.DARK_PURPLE, TextStyles.UNDERLINE, "/kit delete yes")).build());

		CMDYes.confirm.put(src, kit);

		return CommandResult.success();
	}
}
