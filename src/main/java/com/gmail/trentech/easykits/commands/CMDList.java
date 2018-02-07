package com.gmail.trentech.easykits.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.easykits.kit.Kit;
import com.gmail.trentech.easykits.kit.KitService;

public class CMDList implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		List<Text> list = new ArrayList<>();

		KitService kitService = Sponge.getServiceManager().provideUnchecked(KitService.class);

		for (Entry<String, Kit> entry : kitService.getKits().entrySet()) {
			if(src.hasPermission("easykits.kit." + entry.getKey())) {
				list.add(Text.builder().onClick(TextActions.executeCallback(entry.getValue().viewKit(true))).append(Text.of(TextColors.YELLOW, " - ", entry.getKey())).build());	
			}
		}

		if (src instanceof Player) {
			PaginationList.Builder pages = Sponge.getServiceManager().provide(PaginationService.class).get().builder();

			pages.title(Text.builder().color(TextColors.DARK_GREEN).append(Text.of(TextColors.GREEN, "Kits")).build());

			pages.contents(list);

			pages.sendTo(src);
		} else {
			src.sendMessage(Text.of(TextColors.GREEN, "Kits:"));

			for (Text text : list) {
				src.sendMessage(text);
			}
		}

		return CommandResult.success();
	}

	
}
