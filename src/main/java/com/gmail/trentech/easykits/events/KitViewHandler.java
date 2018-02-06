package com.gmail.trentech.easykits.events;

import java.util.Optional;
import java.util.function.Consumer;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.transaction.SlotTransaction;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.easykits.kit.Kit;
import com.gmail.trentech.easykits.kit.KitService;

public class KitViewHandler implements Consumer<ClickInventoryEvent> {

	private Kit kit;
	private KitService kitService;
	
	public KitViewHandler(Kit kit) {
		this.kit = kit;
		this.kitService = Sponge.getServiceManager().provideUnchecked(KitService.class);
	}
	
	@Override
	public void accept(ClickInventoryEvent event) {
		Player player = event.getCause().first(Player.class).get();

		for(SlotTransaction transaction : event.getTransactions()) {
			ItemStackSnapshot itemStack = transaction.getOriginal();
			
			if(itemStack.getType().equals(ItemTypes.BARRIER)) {
				event.setCancelled(true);
				return;
			}

			if(itemStack.getType().equals(ItemTypes.NETHER_STAR) && itemStack.get(Keys.DISPLAY_NAME).isPresent()) {
				if(itemStack.get(Keys.DISPLAY_NAME).get().toPlain().equalsIgnoreCase("Get Kit")) {
					KitEvent.Get kitEvent = new KitEvent.Get(kit, Cause.of(EventContext.builder().add(EventContextKeys.PLAYER, player).build(), player));

					if (!Sponge.getEventManager().post(kitEvent)) {
						if(!kitEvent.getKitService().setKit(player, kit, true)) {
							player.sendMessage(Text.of(TextColors.RED, "Could not give kit. Possibly need more inventory space."));
						}
					}				
					// NOT WORKING YET
					//player.getOpenInventory().get().close(player);
					event.setCancelled(true);
					return;
				}
			}
			
			if(!player.hasPermission("easykits.modify")) {
				event.setCancelled(true);
				return;
			}
		}
		
		int i = 0;
		for (Inventory slot : event.getTargetInventory().slots()) {
			if (i < 27) {
				Optional<ItemStack> optionalItem = slot.peek();

				if (optionalItem.isPresent()) {
					kit.addGrid(i, optionalItem.get());
				} else {
					kit.removeGrid(i);
				}
			} else if (i < 36) {
				Optional<ItemStack> optionalItem = slot.peek();

				if (optionalItem.isPresent()) {
					kit.addHotbar(i - 27, optionalItem.get());
				} else {
					kit.removeHotbar(i - 27);
				}
			} else if (i < 40) {
				Optional<ItemStack> optionalItem = slot.peek();

				if (optionalItem.isPresent()) {
					kit.addEquipment(i - 36, optionalItem.get());
				} else {
					kit.removeEquipment(i - 36);
				}
			} else if(i == 40) {
				Optional<ItemStack> optionalItem = slot.peek();

				kit.setOffHand(optionalItem);
			}

			i++;
		}
		kitService.save(kit);
	}

}
