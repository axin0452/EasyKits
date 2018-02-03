package com.gmail.trentech.easykits.commands.elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.easykits.kit.Kit;
import com.gmail.trentech.easykits.kit.KitService;

public class KitElement extends CommandElement {

	public KitElement(Text key) {
		super(key);
	}

	@Override
	protected Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
		final String next = args.next().toUpperCase();

		KitService kitService = Sponge.getServiceManager().provideUnchecked(KitService.class);

		Optional<Kit> optionalData = kitService.getKit(next);
		
		if (optionalData.isPresent()) {
			return optionalData.get();
		}

		throw args.createError(Text.of(TextColors.RED, "Kit not found"));
	}

	@Override
	public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
		List<String> list = new ArrayList<>();

		Optional<String> next = args.nextIfPresent();

		KitService kitService = Sponge.getServiceManager().provideUnchecked(KitService.class);

		if (next.isPresent()) {
			for (Entry<String, Kit> entry : kitService.getKits().entrySet()) {
				String inv = entry.getKey();
				
				if (inv.startsWith(next.get().toUpperCase())) {
					list.add(inv);
				}
			}
		}

		return list;
	}

	@Override
	public Text getUsage(CommandSource src) {
		return Text.of(getKey());
	}
}
