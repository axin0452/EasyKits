package com.gmail.trentech.easykits.events;

import java.util.Optional;
import java.util.function.Consumer;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.transaction.SlotTransaction;

import com.gmail.trentech.easykits.commands.CMDView;
import com.gmail.trentech.easykits.kit.Kit;
import com.gmail.trentech.easykits.kit.KitService;

public class KitBookHandler implements Consumer<ClickInventoryEvent> {

	private KitService kitService;
	
	public KitBookHandler() {
		this.kitService = Sponge.getServiceManager().provideUnchecked(KitService.class);
	}
	
	@Override
	public void accept(ClickInventoryEvent event) {
		Player player = event.getCause().first(Player.class).get();

		for(SlotTransaction transaction : event.getTransactions()) {
			ItemStackSnapshot itemStack = transaction.getOriginal();
			
			if(itemStack.getType().equals(ItemTypes.BOOK)) {
				String name = itemStack.get(Keys.DISPLAY_NAME).get().toPlain().replace("Kit: ", "");
				
				Optional<Kit> optionalKit = kitService.getKit(name);
				
				if(optionalKit.isPresent()) {
					CMDView.open(player, optionalKit.get());
					return;
				}
			}
		}
		
		event.setCancelled(true);
		return;
	}

}
