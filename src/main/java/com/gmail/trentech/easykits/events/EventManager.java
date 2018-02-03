package com.gmail.trentech.easykits.events;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.entity.living.humanoid.player.RespawnPlayerEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.storage.WorldProperties;

import com.gmail.trentech.easykits.data.Keys;
import com.gmail.trentech.easykits.data.KitUsage;
import com.gmail.trentech.easykits.kit.Kit;
import com.gmail.trentech.easykits.utils.Resource;

public class EventManager {
	
	@Listener(order = Order.POST)
	public void ClientConnectionEventJoin(ClientConnectionEvent.Join event, @Getter("getTargetEntity") Player player) {
		//FIRST JOIN KITS
	}


	@Listener
	public void onRespawnPlayerEvent(RespawnPlayerEvent event, @Getter("getTargetEntity") Player player) {
		// RESPAWN KITS..MAYBE
	}

	@Listener(order = Order.POST)
	public void onMoveEntityEventTeleport(MoveEntityEvent.Teleport event, @Getter("getTargetEntity") Player player) {
		WorldProperties from = event.getFromTransform().getExtent().getProperties();
		WorldProperties to = event.getToTransform().getExtent().getProperties();

		if (from.equals(to)) {
			return;
		}
		
		// FIRST JOIN WORLD KITS?
	}
	
	@Listener
	public void onKitEventEventGet(KitEvent.Get event, @Root Player player) {
		Kit kit = event.getKit();
		
		if(kit.getPrice() > 0) {
			Optional<EconomyService> optionalEconomy = Sponge.getServiceManager().provide(EconomyService.class);

			if (optionalEconomy.isPresent()) {
				EconomyService economyService = optionalEconomy.get();

				UniqueAccount account = economyService.getOrCreateAccount(player.getUniqueId()).get();

				BigDecimal balance = account.getBalance(economyService.getDefaultCurrency());
				
				if(balance.compareTo(BigDecimal.valueOf(kit.getPrice())) > 0) {
					player.sendMessage(Text.of(TextColors.RED, "You do not have enough money. Require $", kit.getPrice()));
					event.setCancelled(true);
				}
			}
		}

		Optional<Map<String, KitUsage>> optionalList = player.get(Keys.KIT_USAGES);

		Map<String, KitUsage> list = new HashMap<>();
		
		if (optionalList.isPresent()) {
			list = optionalList.get();
		}

		KitUsage kitUsage;
		
		if (list.containsKey(kit.getName())) {
			kitUsage = list.get(kit.getName());
			
			Date date = kitUsage.getDate();
			
			long timeSince = TimeUnit.MILLISECONDS.toSeconds(new Date().getTime() - date.getTime());
			long waitTime = kit.getCooldown();
			
			if(waitTime - timeSince > 0) {	
				player.sendMessage(Text.of(TextColors.RED, Resource.getReadableTime(waitTime - timeSince)));
				event.setCancelled(true);
			}
			
			if(kit.getLimit() > 0) {
				if(kitUsage.getTimesUsed() >= kit.getLimit()) {
					player.sendMessage(Text.of(TextColors.RED, "You've reached the max number of this kit you can get."));
					event.setCancelled(true);
				}
			}
			
			kitUsage.setDate(new Date());
			kitUsage.setTimesUsed(kitUsage.getTimesUsed() + 1);
		}
	}
}
