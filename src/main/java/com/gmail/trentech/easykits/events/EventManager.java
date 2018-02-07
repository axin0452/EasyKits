package com.gmail.trentech.easykits.events;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.block.tileentity.ChangeSignEvent;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.entity.living.humanoid.player.RespawnPlayerEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractItemEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.Inventory.Builder;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.entity.PlayerInventory;
import org.spongepowered.api.item.inventory.property.InventoryDimension;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.item.inventory.transaction.InventoryTransactionResult.Type;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.storage.WorldProperties;

import com.gmail.trentech.easykits.Main;
import com.gmail.trentech.easykits.data.ImmutableKitInfoData;
import com.gmail.trentech.easykits.data.KitInfo;
import com.gmail.trentech.easykits.data.KitInfoData;
import com.gmail.trentech.easykits.data.KitUsage;
import com.gmail.trentech.easykits.data.PlayerData;
import com.gmail.trentech.easykits.kit.Kit;
import com.gmail.trentech.easykits.kit.KitService;
import com.gmail.trentech.easykits.utils.Resource;
import com.gmail.trentech.pjc.core.ConfigManager;

public class EventManager {
	
	@Listener(order = Order.POST)
	public void ClientConnectionEventJoin(ClientConnectionEvent.Join event, @Getter("getTargetEntity") Player player) {
		if(player.getJoinData().lastPlayed().get().getEpochSecond() - player.getJoinData().firstPlayed().get().getEpochSecond() <= 3) {
			String kitName = ConfigManager.get(Main.getPlugin()).getConfig().getNode("options", "new-player-kit").getString();
			
			KitService kitService = Sponge.getServiceManager().provideUnchecked(KitService.class);
			
			Optional<Kit> optionalKit = kitService.getKit(kitName);
			
			if(!optionalKit.isPresent()) {
				Sponge.getServer().getConsole().sendMessage(Text.of(TextColors.RED, "Could not give new player kit because ", kitName, " does not exist."));
				return;
			}
			
			kitService.setKit(player, optionalKit.get(), false);
		}
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
		
		if(!player.hasPermission("easykits.kit." + kit.getName())) {
			player.sendMessage(Text.of(TextColors.RED, "You do not have permission to get ", kit.getName()));
			event.setCancelled(true);
			return;
		}
		
		if(!player.hasPermission("easykits.override.price")) {
			if(kit.getPrice() > 0) {
				Optional<EconomyService> optionalEconomy = Sponge.getServiceManager().provide(EconomyService.class);

				if (optionalEconomy.isPresent()) {
					EconomyService economyService = optionalEconomy.get();

					UniqueAccount account = economyService.getOrCreateAccount(player.getUniqueId()).get();

					BigDecimal balance = account.getBalance(economyService.getDefaultCurrency());
					
					if(balance.compareTo(BigDecimal.valueOf(kit.getPrice())) > 0) {
						player.sendMessage(Text.of(TextColors.RED, "You do not have enough money. Require $", kit.getPrice()));
						event.setCancelled(true);
						return;
					}
				}
			}
		}


		Optional<PlayerData> optionalList = player.get(PlayerData.class);

		Map<String, KitUsage> list = new HashMap<>();
		
		if (optionalList.isPresent()) {
			list = optionalList.get().asMap();
		}

		if (list.containsKey(kit.getName())) {
			KitUsage kitUsage = list.get(kit.getName());
			
			if(!player.hasPermission("easykits.override.cooldown")) {
				Date date = kitUsage.getDate();
				
				long timeSince = TimeUnit.MILLISECONDS.toSeconds(new Date().getTime() - date.getTime());
				long waitTime = kit.getCooldown();
				
				if(waitTime - timeSince > 0) {	
					player.sendMessage(Text.of(TextColors.RED, "You must wait ", Resource.getReadableTime(waitTime - timeSince)));
					event.setCancelled(true);
					return;
				}
			}

			if(!player.hasPermission("easykits.override.limit")) {
				if(kit.getLimit() > 0) {
					if(kitUsage.getTimesUsed() >= kit.getLimit()) {
						player.sendMessage(Text.of(TextColors.RED, "You've reached the max number of this kit you can get."));
						event.setCancelled(true);
						return;
					}
				}
			}
		}
	}
	
	@Listener
	public void onChangeSignEvent(ChangeSignEvent event, @Root Player player) {
		Optional<Text> line = event.getText().get(0);
		
		if(line.isPresent() && line.get().toPlain().equalsIgnoreCase("[kit]")) {
			if(!player.hasPermission("easykits.sign.create")) {
				player.sendMessage(Text.of(TextColors.RED, "You do not have permission to create kit sign"));
				event.setCancelled(true);
				return;
			}
			
			KitService kitService = Sponge.getServiceManager().provideUnchecked(KitService.class);
			
			Optional<Text> line2 = event.getText().get(1);
			
			if(!line2.isPresent()) {
				return;
			}
			
			Optional<Kit> optionalKit = kitService.getKit(line2.get().toPlain());
			
			if(!optionalKit.isPresent()) {
				player.sendMessage(Text.of(TextColors.RED, line2.get().toPlain(), " does not exist."));
				return;
			}
			Kit kit = optionalKit.get();
			
			boolean checks = true;
			
			Optional<Text> line3 = event.getText().get(2);
			
			if(line3.isPresent()) {
				if(line3.get().toPlain().equalsIgnoreCase("true") || line3.get().toPlain().equalsIgnoreCase("false")) {
					checks = Boolean.parseBoolean(line3.get().toPlain());
				}
			}
			
			List<Text> lines = new ArrayList<>();

			lines.add(Text.of(TextColors.BLUE, "[Kit]"));
			lines.add(Text.of(TextColors.BLACK, kit.getName()));
				
			if(kit.getPrice() > 0) {
				lines.add(Text.of(TextColors.GREEN, kit.getPrice()));
			}

			event.getText().setElements(lines);
			
			event.getTargetTile().offer(new KitInfoData(new KitInfo(kit.getName(), checks)));
		}
	}
	
	@Listener
	public void onInteractEventSecondaryBook(InteractBlockEvent.Secondary event, @Root Player player) {
		Optional<ItemStack > optionalItemStack = player.getItemInHand(HandTypes.MAIN_HAND);
		
		if(!optionalItemStack.isPresent()) {
			return;
		}
		Optional<KitInfoData> optionalKitInfo = optionalItemStack.get().get(KitInfoData.class);
		
		if(!optionalKitInfo.isPresent()) {
			return;
		}
		KitInfo kitInfo = optionalKitInfo.get().kitInfo().get();
		
		KitService kitService = Sponge.getServiceManager().provideUnchecked(KitService.class);

		Optional<Kit> optionalKit = kitService.getKit(kitInfo.getKitName());
		
		if(!optionalKit.isPresent()) {
			player.sendMessage(Text.of(TextColors.RED, kitInfo.getKitName(), " does not exist"));
			return;
		}
		Kit kit = optionalKit.get();
		
		if(kitInfo.doChecks()) {
			if(!player.hasPermission("easykits.kit." + kit.getName())) {
				player.sendMessage(Text.of(TextColors.RED, "You do not have permission to get ", kit.getName()));
				return;
			}
		}
		
		kit.open(player, kitInfo.doChecks());
	}
	
	@Listener
	public void onInteractEventSecondarySign(InteractBlockEvent.Secondary event, @Root Player player) {
		BlockSnapshot snapshot = event.getTargetBlock();

		Optional<ImmutableKitInfoData> optionalKitInfo = snapshot.get(ImmutableKitInfoData.class);
		
		if(!optionalKitInfo.isPresent()) {
			return;
		}
		KitInfo kitInfo = optionalKitInfo.get().kitInfo().get();

		KitService kitService = Sponge.getServiceManager().provideUnchecked(KitService.class);

		Optional<Kit> optionalKit = kitService.getKit(kitInfo.getKitName());
		
		if(!optionalKit.isPresent()) {
			player.sendMessage(Text.of(TextColors.RED, kitInfo.getKitName(), " does not exist"));
			return;
		}
		Kit kit = optionalKit.get();
		
		String action = ConfigManager.get(Main.getPlugin()).getConfig().getNode("options", "sign-action").getString();
		
		if(action.equalsIgnoreCase("view")) {
			kit.open(player, kitInfo.doChecks());
		}else if (action.equalsIgnoreCase("get")) {
			KitEvent.Get kitEvent = new KitEvent.Get(kit, Cause.of(EventContext.builder().add(EventContextKeys.PLAYER, player).build(), player));

			if (!Sponge.getEventManager().post(kitEvent)) {
				if(!kitEvent.getKitService().setKit(player, kitEvent.getKit(), kitInfo.doChecks())) {
					player.sendMessage(Text.of(TextColors.RED, "Could not give kit. Possibly need more inventory space."));
				}
			}
		}else if (action.equalsIgnoreCase("book")) {
			ItemStack itemStack = kit.getBook(kitInfo.doChecks());
	
			PlayerInventory inv = player.getInventory().query(QueryOperationTypes.INVENTORY_TYPE.of(PlayerInventory.class));
			
			if(!inv.getHotbar().offer(itemStack).getType().equals(Type.SUCCESS)) {
				if(!inv.getMainGrid().offer(itemStack).getType().equals(Type.SUCCESS)) {
					player.sendMessage(Text.of(TextColors.RED, "Your inventory does not have enough space"));
				}
			}
		} else {
			player.sendMessage(Text.of(TextColors.RED, "'sign-action' node in config is incorrect. Should be 'view' 'get' or 'book'"));
		}
	}
	
	@Listener
	public void onChangeBlockEventBreak(ChangeBlockEvent.Break event, @Root Player player) {
		for(Transaction<BlockSnapshot> transaction : event.getTransactions()) {
			BlockSnapshot snapshot = transaction.getOriginal();

			Optional<ImmutableKitInfoData> optionalKitInfo = snapshot.get(ImmutableKitInfoData.class);
			
			if(!optionalKitInfo.isPresent()) {
				return;
			}

			if(!player.hasPermission("easykits.sign.break")) {
				player.sendMessage(Text.of(TextColors.RED, "You do not have permission to break this kit sign"));
				transaction.setValid(false);
			}
		}
	}
	
	@Listener
	public void onInteractItemEventSecondaryMainHand(InteractItemEvent.Secondary.MainHand event, @Root Player player) {
		ItemStackSnapshot snapshot = event.getItemStack();
		Optional<Text> optionalName = snapshot.get(org.spongepowered.api.data.key.Keys.DISPLAY_NAME);
		
		if(!optionalName.isPresent()) {
			return;
		}		

		if(!optionalName.get().toPlain().equalsIgnoreCase("Book of Kits")) {
			return;
		}
		
		Builder builder = Inventory.builder().of(InventoryArchetypes.DOUBLE_CHEST).property(InventoryTitle.PROPERTY_NAME, InventoryTitle.of(Text.of("Kits:")));
		
		HashMap<String, Kit> list = Sponge.getServiceManager().provideUnchecked(KitService.class).getKits();
		
		if(list.size() <= 9) {
			builder.property(InventoryDimension.PROPERTY_NAME, new InventoryDimension(9, 1));
		} else if(list.size() <= 18) {
			builder.property(InventoryDimension.PROPERTY_NAME, new InventoryDimension(9, 2));
		} else if(list.size() <= 27) {
			builder.property(InventoryDimension.PROPERTY_NAME, new InventoryDimension(9, 3));
		} else if(list.size() <= 36) {
			builder.property(InventoryDimension.PROPERTY_NAME, new InventoryDimension(9, 4));
		} else if(list.size() <= 45) {
			builder.property(InventoryDimension.PROPERTY_NAME, new InventoryDimension(9, 5));
		} else if(list.size() <= 54) {
			builder.property(InventoryDimension.PROPERTY_NAME, new InventoryDimension(9, 6));
		} else {
			player.sendMessage(Text.of(TextColors.YELLOW, "Could not fit all kits in book"));
			builder.property(InventoryDimension.PROPERTY_NAME, new InventoryDimension(9, 6));
		}
		
		Inventory inventory = builder.listener(ClickInventoryEvent.class, new KitBookHandler()).build(Main.getPlugin());
		
		for(Entry<String, Kit> entry : list.entrySet()) {
			if(player.hasPermission("easykits.kit." + entry.getValue().getName())) {
				inventory.offer(entry.getValue().getBook(true));
			}
		}
		
		player.openInventory(inventory);
	}
}
