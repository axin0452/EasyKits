package com.gmail.trentech.easykits.commands;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.enchantment.EnchantmentTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.entity.PlayerInventory;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.item.inventory.transaction.InventoryTransactionResult.Type;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.easykits.kit.Kit;

public class CMDBook implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(!(src instanceof Player)) {
			throw new CommandException(Text.of(TextColors.RED, "Must be a player."), false);
		}
		Player player = (Player) src;
		
		List<Text> lore = new ArrayList<>();
		
		lore.add(Text.of(TextColors.YELLOW, "EasyKits"));
		lore.add(Text.of(TextColors.GREEN, "By: ", TextColors.WHITE, "TrenTech"));
		
		List<Enchantment> enchantment = new ArrayList<>();
		enchantment.add(Enchantment.builder().type(EnchantmentTypes.INFINITY).level(EnchantmentTypes.INFINITY.getMinimumLevel()).build());

		ItemStack itemStack = ItemStack.builder().itemType(ItemTypes.BOOK)
			.add(Keys.ITEM_LORE, lore)
			.add(Keys.ITEM_ENCHANTMENTS, enchantment)
			.add(Keys.HIDE_ENCHANTMENTS, true)
			.add(Keys.DISPLAY_NAME, Text.of(TextColors.WHITE, "Book of Kits")).build();
		
		if (args.hasAny("kit")) {
			Kit kit = args.<Kit>getOne("kit").get();
			
			if(!player.hasPermission("easykits.kit." + kit.getName())) {
				throw new CommandException(Text.of(TextColors.RED, "You do not have permission to get ", kit.getName()), false);
			}
			
			itemStack = kit.getBook(true);
		}

		PlayerInventory inv = player.getInventory().query(QueryOperationTypes.INVENTORY_TYPE.of(PlayerInventory.class));
		
		if(!inv.getHotbar().offer(itemStack).getType().equals(Type.SUCCESS)) {
			if(!inv.getMainGrid().offer(itemStack).getType().equals(Type.SUCCESS)) {
				src.sendMessage(Text.of(TextColors.RED, "Your inventory does not have enough space"));
			}
		}
		
		return CommandResult.success();
	}

	
}
