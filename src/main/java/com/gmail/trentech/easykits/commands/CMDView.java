package com.gmail.trentech.easykits.commands;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.InventoryDimension;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.easykits.Main;
import com.gmail.trentech.easykits.events.KitEvent;
import com.gmail.trentech.easykits.events.KitViewHandler;
import com.gmail.trentech.easykits.kit.Kit;
import com.gmail.trentech.easykits.utils.Resource;
import com.gmail.trentech.pjc.help.Help;

public class CMDView implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Help help = Help.get("kit view").get();
		
		if (args.hasAny("help")) {			
			help.execute(src);
			return CommandResult.empty();
		}
		
		if (!(src instanceof Player)) {
			throw new CommandException(Text.of(TextColors.RED, "Must be a player"), false);
		}
		Player player = (Player) src;

		if (!args.hasAny("kit")) {
			throw new CommandException(Text.builder().onClick(TextActions.executeCallback(help.execute())).append(help.getUsageText()).build(), false);
		}
		Kit kit = args.<Kit>getOne("kit").get();

		KitEvent.View event = new KitEvent.View(kit, Cause.of(EventContext.builder().add(EventContextKeys.PLAYER, player).build(), player));

		if (!Sponge.getEventManager().post(event)) {
			open(player, event.getKit());
		}

		return CommandResult.success();
	}
	
	public static Consumer<CommandSource> viewKit(Kit kit) {
		return (CommandSource src) -> {
			open(((Player) src), kit);
		};
	}
	
	public static void open(Player player, Kit kit) {
		Inventory inventory = Inventory.builder().of(InventoryArchetypes.DOUBLE_CHEST)
				.property(InventoryDimension.PROPERTY_NAME, new InventoryDimension(9, 5))
				.property(InventoryTitle.PROPERTY_NAME, InventoryTitle.of(Text.of("Kit: " + kit.getName())))
				.listener(ClickInventoryEvent.class, new KitViewHandler(kit))
				.build(Main.getPlugin());

		Map<Integer, ItemStack> grid = kit.getGrid();
		Map<Integer, ItemStack> hotbar = kit.getHotbar();

		int i = 0;
		for (Inventory slot : inventory.slots()) {
			if (i < 27) {
				if (grid.containsKey(i)) {
					slot.set(grid.get(i));
				}
			} else if (i < 36) {
				if (hotbar.containsKey(i - 27)) {
					slot.set(hotbar.get(i - 27));
				}
			} else {
				if (i - 36 == 0) {
					Optional<ItemStack> helmet = kit.getHelmet();

					if (helmet.isPresent()) {
						slot.set(helmet.get());
					}
				} else if(i - 36 == 1) {
					Optional<ItemStack> chestPlate = kit.getChestPlate();

					if (chestPlate.isPresent()) {
						slot.set(chestPlate.get());
					}
				} else if(i - 36 == 2) {
					Optional<ItemStack> leggings = kit.getLeggings();

					if (leggings.isPresent()) {
						slot.set(leggings.get());
					}
				} else if(i - 36 == 3) {
					Optional<ItemStack> boots = kit.getBoots();

					if (boots.isPresent()) {
						slot.set(boots.get());
					}
				} else if(i - 36 == 4) {
					Optional<ItemStack> offHand = kit.getOffHand();
					
					if(offHand.isPresent()) {
						slot.set(offHand.get());
					}
				} else {
					if(i - 36 == 5) {
						slot.set(ItemStack.builder().itemType(ItemTypes.BARRIER).add(Keys.DISPLAY_NAME, Text.of(TextColors.GREEN, "Price: ", TextColors.WHITE, kit.getPrice())).build());
					}
					if(i - 36 == 6) {
						slot.set(ItemStack.builder().itemType(ItemTypes.BARRIER).add(Keys.DISPLAY_NAME, Text.of(TextColors.GREEN, "Limit: ", TextColors.WHITE, kit.getLimit())).build());
					}
					if(i - 36 == 7) {
						slot.set(ItemStack.builder().itemType(ItemTypes.BARRIER).add(Keys.DISPLAY_NAME, Text.of(TextColors.GREEN, "Cooldown: ", TextColors.WHITE, Resource.getReadableTime(kit.getCooldown()))).build());
					}
					if(i - 36 == 8) {
						slot.set(ItemStack.builder().itemType(ItemTypes.NETHER_STAR).add(Keys.DISPLAY_NAME, Text.of(TextColors.LIGHT_PURPLE, "Get Kit")).build());
					}
				}
			}

			i++;
		}
		player.openInventory(inventory);
	}
}
