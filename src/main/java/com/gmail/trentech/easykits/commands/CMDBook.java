package com.gmail.trentech.easykits.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.transaction.InventoryTransactionResult.Type;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.pjc.help.Help;

public class CMDBook implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Help help = Help.get("kit book").get();
		
		if (args.hasAny("help")) {			
			help.execute(src);
			return CommandResult.empty();
		}
		
		if(!(src instanceof Player)) {
			throw new CommandException(Text.of(TextColors.RED, "Must be a player."), false);
		}
		Player player = (Player) src;
		
		ItemStack itemStack = ItemStack.builder().itemType(ItemTypes.BOOK).add(Keys.DISPLAY_NAME, Text.of(TextColors.GREEN, "Book of Kits")).build();
		
		if(!player.getInventory().offer(itemStack).getType().equals(Type.SUCCESS)) {
			src.sendMessage(Text.of(TextColors.RED, "Your inventory does not have enough space"));
		}

		return CommandResult.success();
	}

	
}