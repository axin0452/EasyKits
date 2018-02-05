package com.gmail.trentech.easykits.commands;

import java.util.Optional;

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

import com.gmail.trentech.easykits.Main;
import com.gmail.trentech.easykits.events.KitEvent;
import com.gmail.trentech.easykits.kit.Kit;
import com.gmail.trentech.easykits.kit.KitService;
import com.gmail.trentech.pjc.core.ConfigManager;
import com.gmail.trentech.pjc.help.Help;

import ninja.leaping.configurate.ConfigurationNode;

public class CMDCreate implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(!(src instanceof Player)) {
			throw new CommandException(Text.of(TextColors.RED, "Must be a player"), false);
		}
		Help help = Help.get("kit create").get();
		
		if (args.hasAny("help")) {		
			help.execute(src);
			return CommandResult.empty();
		}
		
		if (!args.hasAny("name")) {
			throw new CommandException(Text.builder().onClick(TextActions.executeCallback(help.execute())).append(help.getUsageText()).build(), false);
		}
		String name = args.<String>getOne("name").get().toUpperCase();

		KitService kitService = Sponge.getServiceManager().provideUnchecked(KitService.class);

		Optional<Kit> optionalKit = kitService.getKit(name);
		
		if (optionalKit.isPresent()) {
			throw new CommandException(Text.of(TextColors.RED, name + ", already exists"), false);
		}
		
		ConfigurationNode config = ConfigManager.get(Main.getPlugin()).getConfig();
		
		long cooldown = config.getNode("options", "default-cooldown").getLong();
		int limit = config.getNode("options", "default-limit").getInt();
		double price = config.getNode("options", "default-price").getDouble();
		
		Kit kit = new Kit(name, ((Player) src), cooldown, limit, price);
	
		KitEvent.Create event = new KitEvent.Create(kit, Cause.of(EventContext.builder().add(EventContextKeys.PLAYER, ((Player) src)).build(), ((Player) src)));

		if (!Sponge.getEventManager().post(event)) {
			event.getKitService().save(event.getKit());
			src.sendMessage(Text.of(TextColors.DARK_GREEN, "Created new kit ", name));
		}

		return CommandResult.success();
	}

}
