package com.gmail.trentech.easykits.kit;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.entity.PlayerInventory;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.item.inventory.transaction.InventoryTransactionResult.Type;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;

import com.gmail.trentech.easykits.Main;
import com.gmail.trentech.easykits.data.Keys;
import com.gmail.trentech.easykits.data.KitInfo;
import com.gmail.trentech.easykits.data.KitInfoData;
import com.gmail.trentech.easykits.data.KitUsage;
import com.gmail.trentech.easykits.data.PlayerData;

public class KitService {

	public Optional<Kit> getKit(String name) {
		return KitDB.get(name);
	}

	public ConcurrentHashMap<String, Kit> getKits() {
		return KitDB.all();
	}

	public void delete(String name) {
		KitDB.remove(name);
	}
	
	public void save(Kit kit) {
		if (KitDB.exists(kit.getName())) {
			KitDB.update(kit);
		} else {
			KitDB.create(kit);
		}
	}
	
	public boolean updateUsage(Kit kit, Player player) {
		if(!player.hasPermission("easykits.override.price")) {
			if(kit.getPrice() > 0) {
				Optional<EconomyService> optionalEconomy = Sponge.getServiceManager().provide(EconomyService.class);

				if (optionalEconomy.isPresent()) {
					EconomyService economyService = optionalEconomy.get();

					UniqueAccount account = economyService.getOrCreateAccount(player.getUniqueId()).get();

					BigDecimal balance = account.getBalance(economyService.getDefaultCurrency());
					
					if(balance.compareTo(BigDecimal.valueOf(kit.getPrice())) > 0) {
						return false;
					}
					
					account.withdraw(economyService.getDefaultCurrency(), BigDecimal.valueOf(kit.getPrice()), Cause.of(EventContext.builder().add(EventContextKeys.PLUGIN, Main.getPlugin()).build(), Main.getPlugin()));
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
		} else {
			kitUsage = new KitUsage(kit.getName());
		}
		
		if(!player.hasPermission("easykits.override.price")) {
			if(kit.getCooldown() > 0) {
				Date date = kitUsage.getDate();
				
				long timeSince = TimeUnit.MILLISECONDS.toSeconds(new Date().getTime() - date.getTime());
				long waitTime = kit.getCooldown();
				
				if(waitTime - timeSince > 0) {	
					return false;
				}
				
				kitUsage.setDate(new Date());
			}
		}

		if(!player.hasPermission("easykits.override.limit")) {
			if(kit.getLimit() > 0) {
				if(kitUsage.getTimesUsed() >= kit.getLimit()) {
					return false;
				}
				
				kitUsage.setTimesUsed(kitUsage.getTimesUsed() + 1);
			}
		}

		list.put(kit.getName(), kitUsage);
		player.offer(new PlayerData(list));
		
		return true;
	}
	
	public boolean setKit(Player player, Kit kit, boolean updateUsage) {
		Kit backup = new Kit("backup", player, 0, 0, 0);
		
		PlayerInventory inv = player.getInventory().query(QueryOperationTypes.INVENTORY_TYPE.of(PlayerInventory.class));

		Map<Integer, ItemStack> hotbar = kit.getHotbar();

		if (!hotbar.isEmpty()) {
			int i = 0;
			for (Inventory slot : inv.getHotbar().slots()) {
				if (hotbar.containsKey(i)) {
					if(slot.peek().isPresent()) {
						if(!likeStack(hotbar.get(i), inv)) {
							if(!firstEmpty(hotbar.get(i), inv)) {
								restoreInventory(player, backup);
								return false;
							}
						}
					} else {
						slot.set(hotbar.get(i));
					}
				}
				i++;
			}
		}

		Map<Integer, ItemStack> grid = kit.getGrid();

		if (!grid.isEmpty()) {
			int i = 0;
			for (Inventory slot : inv.getMainGrid().slots()) {
				if (grid.containsKey(i)) {
					if(slot.peek().isPresent()) {
						if(!likeStack(grid.get(i), inv)) {
							if(!firstEmpty(grid.get(i), inv)) {
								restoreInventory(player, backup);
								return false;
							}
						}
					} else {
						slot.set(grid.get(i));
					}
				}
				i++;
			}
		}

		Optional<ItemStack> helmet = kit.getHelmet();

		if (helmet.isPresent()) {
			if(player.getHelmet().isPresent()) {
				if(!likeStack(helmet.get(), inv)) {
					if(!firstEmpty(helmet.get(), inv)) {
						restoreInventory(player, backup);
						return false;
					}
				}
			} else {
				player.setHelmet(helmet.get());
			}
		}

		Optional<ItemStack> chestPlate = kit.getChestPlate();

		if (chestPlate.isPresent()) {			
			if(player.getChestplate().isPresent()) {
				if(!likeStack(chestPlate.get(), inv)) {
					if(!firstEmpty(chestPlate.get(), inv)) {
						restoreInventory(player, backup);
						return false;
					}
				}
			} else {
				player.setChestplate(chestPlate.get());
			}
		}
		
		Optional<ItemStack> leggings = kit.getLeggings();

		if (leggings.isPresent()) {
			if(player.getLeggings().isPresent()) {
				if(!likeStack(leggings.get(), inv)) {
					if(!firstEmpty(leggings.get(), inv)) {
						restoreInventory(player, backup);
						return false;
					}
				}
			} else {
				player.setLeggings(leggings.get());
			}
		}
		
		Optional<ItemStack> boots = kit.getBoots();

		if (boots.isPresent()) {
			if(player.getBoots().isPresent()) {
				if(!likeStack(boots.get(), inv)) {
					if(!firstEmpty(boots.get(), inv)) {
						restoreInventory(player, backup);
						return false;
					}
				}
			} else {
				player.setBoots(boots.get());
			}
		}
		
		Optional<ItemStack> offHand = kit.getOffHand();

		if (offHand.isPresent()) {
			if(player.getItemInHand(HandTypes.OFF_HAND).isPresent()) {
				if(!likeStack(offHand.get(), inv)) {
					if(!firstEmpty(offHand.get(), inv)) {
						restoreInventory(player, backup);
						return false;
					}
				}
			} else {
				player.setItemInHand(HandTypes.OFF_HAND, offHand.get());
			}
		}

		if(updateUsage) {
			if(!updateUsage(kit, player)) {
				restoreInventory(player, backup);
				return false;
			}
		}	
		
		Optional<ItemStack> optionalItemStack = player.getItemInHand(HandTypes.MAIN_HAND);
		
		if(optionalItemStack.isPresent()) {
			ItemStack itemStack = optionalItemStack.get();
			
			Optional<KitInfoData> optionalKitInfo = itemStack.get(KitInfoData.class);
			
			if(optionalKitInfo.isPresent()) {
				KitInfo kitInfo = optionalKitInfo.get().kitInfo().get();
				
				if(kitInfo.getKitName().equalsIgnoreCase(kit.getName())) {
					player.setItemInHand(HandTypes.MAIN_HAND, ItemStack.empty());	
				}
			}
		}
		
		return true;
	}
	
	private boolean firstEmpty(ItemStack itemStack, Inventory inventory) {
		for (Inventory slot : inventory.slots()) {

			if(!slot.peek().isPresent()) {	
				if(slot.set(itemStack).getType().equals(Type.SUCCESS)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	private boolean likeStack(ItemStack itemStack, Inventory inventory) {
		for (Inventory slot : inventory.slots()) {
			Optional<ItemStack> optionalItem = slot.peek();
			
			if(optionalItem.isPresent()) {
				ItemStack i = optionalItem.get();
				
				if(i.getType().equals(itemStack.getType())) {
					int fit = i.getMaxStackQuantity() - i.getQuantity();
					
					if(fit >= itemStack.getQuantity()) {
						i.setQuantity(i.getQuantity() + itemStack.getQuantity());
						
						if(slot.set(i).getType().equals(Type.SUCCESS)) {
							return true;
						}
					} else if(fit != 0) {
						i.setQuantity(i.getQuantity() + fit);

						if(slot.set(i).getType().equals(Type.SUCCESS)) {
							itemStack.setQuantity(itemStack.getQuantity() - fit);
						}
					}
				}
			}
		}
		
		return false;
	}
	
	private void restoreInventory(Player player, Kit backup) {
		player.getInventory().clear();

		PlayerInventory inv = player.getInventory().query(QueryOperationTypes.INVENTORY_TYPE.of(PlayerInventory.class));

		Map<Integer, ItemStack> hotbar = backup.getHotbar();

		if (!hotbar.isEmpty()) {
			int i = 0;
			for (Inventory slot : inv.getHotbar().slots()) {
				if (hotbar.containsKey(i)) {
					slot.set(hotbar.get(i));
				}
				i++;
			}
		}

		Map<Integer, ItemStack> grid = backup.getGrid();

		if (!grid.isEmpty()) {
			int i = 0;
			for (Inventory slot : inv.getMainGrid().slots()) {
				if (grid.containsKey(i)) {
					slot.set(grid.get(i));
				}
				i++;
			}
		}

		Optional<ItemStack> helmet = backup.getHelmet();

		if (helmet.isPresent()) {
			player.setHelmet(helmet.get());
		}

		Optional<ItemStack> chestPlate = backup.getChestPlate();

		if (chestPlate.isPresent()) {
			player.setChestplate(chestPlate.get());
		}
		
		Optional<ItemStack> leggings = backup.getLeggings();

		if (leggings.isPresent()) {
			player.setLeggings(leggings.get());
		}
		
		Optional<ItemStack> boots = backup.getBoots();

		if (boots.isPresent()) {
			player.setBoots(boots.get());
		}
		
		Optional<ItemStack> offHand = backup.getOffHand();

		if (offHand.isPresent()) {
			player.setItemInHand(HandTypes.OFF_HAND, offHand.get());
		}
	}
}
