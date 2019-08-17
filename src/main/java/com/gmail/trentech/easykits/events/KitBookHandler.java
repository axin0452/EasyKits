package com.gmail.trentech.easykits.events;

import java.util.Optional;
import java.util.function.Consumer;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.transaction.SlotTransaction;

import com.gmail.trentech.easykits.data.ImmutableKitInfoData;
import com.gmail.trentech.easykits.data.KitInfo;
import com.gmail.trentech.easykits.kit.Kit;
import com.gmail.trentech.easykits.kit.KitService;

public class KitBookHandler implements Consumer<ClickInventoryEvent> {

	private KitService kitService;
	
	public KitBookHandler() {
		this.kitService = Sponge.getServiceManager().provideUnchecked(KitService.class);
	}
	
	@Override
	public void accept(ClickInventoryEvent event) {
		event.setCancelled(true);
		Player player = event.getCause().first(Player.class).get();

		for(SlotTransaction transaction : event.getTransactions()) {
			ItemStackSnapshot itemStack = transaction.getOriginal();

			Optional<ImmutableKitInfoData> optionalKitInfo = itemStack.get(ImmutableKitInfoData.class);
			
			if(optionalKitInfo.isPresent()) {
				KitInfo kitInfo = optionalKitInfo.get().kitInfo().get();
				
				Optional<Kit> optionalKit = kitService.getKit(kitInfo.getKitName());
				
				if(optionalKit.isPresent()) {
					optionalKit.get().open(player, kitInfo.doChecks());
					return;
				}
			}
		}
		
		return;
	}

}
