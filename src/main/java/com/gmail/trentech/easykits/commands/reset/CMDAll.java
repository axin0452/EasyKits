package com.gmail.trentech.easykits.commands.reset;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.easykits.data.Keys;
import com.gmail.trentech.easykits.data.KitUsage;
import com.gmail.trentech.easykits.kit.Kit;
import com.gmail.trentech.pjc.help.Help;

public class CMDAll implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Help help = Help.get("kit reset all").get();
		
		if (args.hasAny("help")) {			
			help.execute(src);
			return CommandResult.empty();
		}

		if (!args.hasAny("kit")) {
			throw new CommandException(Text.builder().onClick(TextActions.executeCallback(help.execute())).append(help.getUsageText()).build(), false);
		}
		Kit kit = args.<Kit>getOne("kit").get();
		
		if (!args.hasAny("player")) {
			throw new CommandException(Text.builder().onClick(TextActions.executeCallback(help.execute())).append(help.getUsageText()).build(), false);
		}
		Player player = args.<Player>getOne("player").get();
		
		Optional<Map<String, KitUsage>> optionalList = player.get(Keys.KIT_USAGES);

		Map<String, KitUsage> list = new HashMap<>();
		
		if (optionalList.isPresent()) {
			list = optionalList.get();
		}

		if (!list.containsKey(kit.getName())) {
			throw new CommandException(Text.of(TextColors.GOLD, "Nothing to reset", false));
		}
		list.remove(kit.getName());

		player.offer(Keys.KIT_USAGES, list);

		player.sendMessage(Text.of(TextColors.DARK_GREEN, "Reset limit of kit ", kit.getName(), " for ", player.getName()));
		src.sendMessage(Text.of(TextColors.DARK_GREEN, "Reset limit of kit ", kit.getName(), " for ", player.getName()));
		
		return CommandResult.success();
	}
}