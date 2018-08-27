package com.gmail.trentech.easykits.kit;

import static org.spongepowered.api.data.DataQuery.of;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.Optional;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.DataFormats;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.data.persistence.InvalidDataFormatException;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.enchantment.EnchantmentTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.entity.PlayerInventory;
import org.spongepowered.api.item.inventory.property.InventoryDimension;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.gmail.trentech.easykits.Main;
import com.gmail.trentech.easykits.data.KitInfo;
import com.gmail.trentech.easykits.data.KitInfoData;
import com.gmail.trentech.easykits.events.KitViewHandler;
import com.gmail.trentech.easykits.utils.Resource;
import com.gmail.trentech.pjc.core.ConfigManager;

public class Kit implements DataSerializable {

	private final static DataQuery NAME = of("name");
	private final static DataQuery OFF_HAND = of("offhand");
	private final static DataQuery HELMET = of("helmet");
	private final static DataQuery CHEST_PLATE = of("chestplate");
	private final static DataQuery LEGGINGS = of("leggings");
	private final static DataQuery BOOTS = of("boots");
	private final static DataQuery HOTBAR = of("hotbar");
	private final static DataQuery GRID = of("grid");
	private final static DataQuery SLOT_POSITION = of("slot_position");
	private final static DataQuery ITEM_STACK = of("item_stack");
	private final static DataQuery COOLDOWN = of("cooldown");
	private final static DataQuery LIMIT = of("limit");
	private final static DataQuery PRICE = of("price");
	
	private String name;
	private ItemStack offHand = ItemStack.empty();
	private ItemStack helmet = ItemStack.empty();
	private ItemStack chestPlate = ItemStack.empty();
	private ItemStack leggings = ItemStack.empty();
	private ItemStack boots = ItemStack.empty();
	private Map<Integer, ItemStack> hotbar = new HashMap<>();
	private Map<Integer, ItemStack> grid = new HashMap<>();
	private long cooldown = 0;
	private int limit = 0;
	private double price = 0.0;
	
	protected Kit(String name, ItemStack offHand, ItemStack helmet, ItemStack chestPlate, ItemStack leggings, ItemStack boots, 
			Map<Integer, ItemStack> hotbar, Map<Integer, ItemStack> grid, long cooldown, int limit, double price) {
		this.name = name;
		this.offHand = offHand;
		this.hotbar = hotbar;
		this.grid = grid;
		this.helmet = helmet;
		this.chestPlate = chestPlate;
		this.leggings = leggings;
		this.boots = boots;
		this.cooldown = cooldown;
		this.limit = limit;
		this.price = price;
	}

	public Kit(String name, Player player, long cooldown, int limit, double price) {
		this.name = name;
		this.cooldown = cooldown;
		this.limit = limit;
		this.price = price;
		
		PlayerInventory inv = (PlayerInventory) player.getInventory().query(QueryOperationTypes.INVENTORY_TYPE.of(PlayerInventory.class));

		int i = 0;
		for (Inventory item : inv.getHotbar().slots()) {
			Slot slot = (Slot) item;

			addHotbar(i, slot.peek());
			i++;
		}

		i = 0;
		for (Inventory item : inv.getStorage().slots()) {
			Slot slot = (Slot) item;

			addGrid(i, slot.peek());
			i++;
		}

		setOffHand(player.getItemInHand(HandTypes.OFF_HAND));
		setHelmet(player.getHelmet());
		setChestPlate(player.getChestplate());
		setLeggings(player.getLeggings());
		setBoots(player.getBoots());	
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public long getCooldown() {
		return cooldown;
	}
	
	public void setCooldown(long cooldown) {
		this.cooldown = cooldown;
	}
	
	public int getLimit() {
		return limit;
	}
	
	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public ItemStack getOffHand() {
		return offHand;
	}

	public ItemStack getHelmet() {
		return helmet;
	}

	public ItemStack getChestPlate() {
		return chestPlate;
	}

	public ItemStack getLeggings() {
		return leggings;
	}

	public ItemStack getBoots() {
		return boots;
	}
	
	public Map<Integer, ItemStack> getHotbar() {
		return hotbar;
	}

	public Map<Integer, ItemStack> getGrid() {
		return grid;
	}
	
	public void setOffHand(ItemStack itemStack) {
		this.offHand = itemStack;
	}
	
	public void setHelmet(ItemStack itemStack) {
		this.helmet = itemStack;
	}

	public void setChestPlate(ItemStack itemStack) {
		this.chestPlate = itemStack;
	}

	public void setLeggings(ItemStack itemStack) {
		this.leggings = itemStack;
	}

	public void setBoots(ItemStack itemStack) {
		this.boots = itemStack;
	}

	public void addEquipment(int slot, ItemStack itemStack) {
		if (slot == 0) {
			this.helmet = itemStack;
		} else if(slot == 1) {
			this.chestPlate = itemStack;
		} else if(slot == 2) {
			this.leggings = itemStack;
		} else if(slot == 3) {
			this.boots = itemStack;
		}
	}
	
	public void removeEquipment(int slot) {
		if (slot == 0) {
			this.helmet = ItemStack.empty();
		} else if(slot == 1) {
			this.chestPlate = ItemStack.empty();
		} else if(slot == 2) {
			this.leggings = ItemStack.empty();
		} else if(slot == 3) {
			this.boots = ItemStack.empty();
		}
	}
	
	public void addHotbar(Integer slot, ItemStack itemStack) {
		this.hotbar.put(slot, itemStack);
	}

	public void removeHotbar(Integer slot) {
		this.hotbar.remove(slot);
	}

	public void addGrid(Integer slot, ItemStack itemStack) {
		this.grid.put(slot, itemStack);
	}

	public void removeGrid(Integer slot) {
		this.grid.remove(slot);
	}

	public ItemStack getBook(boolean check) {
		List<Text> lore = new ArrayList<>();
		
		lore.add(Text.of(TextColors.GREEN, "Name: ", TextColors.WHITE, getName()));

		if(getPrice() > 0) {
			String currency = ConfigManager.get(Main.getPlugin()).getConfig().getNode("options", "currency-symbol").getString();
			
			lore.add(Text.of(TextColors.GREEN, "Price: ", TextColors.WHITE, currency, new DecimalFormat(".##").format(getPrice())));
		}
	
		if(getLimit() > 0) {
			lore.add(Text.of(TextColors.GREEN, "Limit: ", TextColors.WHITE, getLimit()));
		}
		
		if(getCooldown() > 0) {
			lore.add(Text.of(TextColors.GREEN, "Cooldown: ", TextColors.WHITE, Resource.getReadableTime(getCooldown())));
		}
		List<Enchantment> enchantment = new ArrayList<>();
		enchantment.add(Enchantment.builder().type(EnchantmentTypes.INFINITY).level(EnchantmentTypes.INFINITY.getMinimumLevel()).build());
		
		return ItemStack.builder().itemType(ItemTypes.BOOK)
				.add(org.spongepowered.api.data.key.Keys.ITEM_LORE, lore)
				.add(Keys.ITEM_ENCHANTMENTS, enchantment)
				.add(Keys.HIDE_ENCHANTMENTS, true)
				.add(org.spongepowered.api.data.key.Keys.DISPLAY_NAME, Text.of(TextColors.WHITE, "Kit"))
				.itemData(new KitInfoData(new KitInfo(getName(), check))).build();
	}
	
	public Consumer<CommandSource> viewKit(boolean checks) {
		return (CommandSource src) -> {
			open(((Player) src), checks);
		};
	}
	
	public void open(Player player, boolean checks) {
		Inventory inventory = Inventory.builder().of(InventoryArchetypes.DOUBLE_CHEST)
				.property(InventoryDimension.of(9, 5))
				.property(InventoryTitle.of(Text.of("Kit: " + getName())))
				.listener(ClickInventoryEvent.class, new KitViewHandler(this, checks))
				.build(Main.getPlugin());

		int i = 0;
		for (Inventory slot : inventory.slots()) {
			if (i < 27) {
				if (getGrid().containsKey(i)) {
					slot.set(getGrid().get(i));
				}
			} else if (i < 36) {
				if (getHotbar().containsKey(i - 27)) {
					slot.set(getHotbar().get(i - 27));
				}
			} else {
				if (i - 36 == 0) {
					slot.set(getHelmet());
				} else if(i - 36 == 1) {
					slot.set(getChestPlate());
				} else if(i - 36 == 2) {
					slot.set(getLeggings());
				} else if(i - 36 == 3) {
					slot.set(getBoots());
				} else if(i - 36 == 4) {
					slot.set(getOffHand());
				} else {
					if(i - 36 == 5) {
						String currency = ConfigManager.get(Main.getPlugin()).getConfig().getNode("options", "currency-symbol").getString();
						
						slot.set(ItemStack.builder().itemType(ItemTypes.BARRIER).add(Keys.DISPLAY_NAME, Text.of(TextColors.GREEN, "Price: ", currency, TextColors.WHITE, new DecimalFormat(".##").format(getPrice()))).build());
					}
					if(i - 36 == 6) {
						slot.set(ItemStack.builder().itemType(ItemTypes.BARRIER).add(Keys.DISPLAY_NAME, Text.of(TextColors.GREEN, "Limit: ", TextColors.WHITE, getLimit())).build());
					}
					if(i - 36 == 7) {
						slot.set(ItemStack.builder().itemType(ItemTypes.BARRIER).add(Keys.DISPLAY_NAME, Text.of(TextColors.GREEN, "Cooldown: ", TextColors.WHITE, Resource.getReadableTime(getCooldown()))).build());
					}
					if(i - 36 == 8) {
						List<Text> lore = new ArrayList<>();
						lore.add(Text.of("Click here to get kit"));
						
						List<Enchantment> enchantment = new ArrayList<>();
						enchantment.add(Enchantment.builder().type(EnchantmentTypes.INFINITY).level(EnchantmentTypes.INFINITY.getMinimumLevel()).build());
						
						ItemStack itemStack = ItemStack.builder().itemType(ItemTypes.NETHER_STAR)
								.add(Keys.ITEM_LORE, lore)
								.add(Keys.ITEM_ENCHANTMENTS, enchantment)
								.add(Keys.HIDE_ENCHANTMENTS, true)
								.add(org.spongepowered.api.data.key.Keys.DISPLAY_NAME, Text.of(TextColors.GREEN, "Get Kit")).build();
						
						slot.set(itemStack);
					}
				}
			}

			i++;
		}
		player.openInventory(inventory);
	}
	
	@Override
	public int getContentVersion() {
		return 1;
	}

	@Override
	public DataContainer toContainer() {
		DataContainer container = DataContainer.createNew().set(NAME, getName()).set(COOLDOWN, getCooldown()).set(LIMIT, getLimit()).set(PRICE, getPrice())
				.set(OFF_HAND, this.offHand.toContainer()).set(HELMET, this.helmet.toContainer()).set(CHEST_PLATE, this.chestPlate.toContainer())
				.set(LEGGINGS, this.leggings.toContainer()).set(BOOTS, this.boots.toContainer());

		if(!this.hotbar.isEmpty()) {
			List<DataView> hotbarData = new LinkedList<>();

			for (Entry<Integer, ItemStack> entry : hotbar.entrySet()) {
				hotbarData.add(DataContainer.createNew().set(SLOT_POSITION, entry.getKey()).set(ITEM_STACK, entry.getValue().toContainer()));
			}
			
			container.set(HOTBAR, hotbarData);
		}

		if(!this.grid.isEmpty()) {
			List<DataView> gridData = new LinkedList<>();

			for (Entry<Integer, ItemStack> entry : grid.entrySet()) {
				gridData.add(DataContainer.createNew().set(SLOT_POSITION, entry.getKey()).set(ITEM_STACK, entry.getValue().toContainer()));
			}

			container.set(GRID, gridData);
		}
		
		return container;
	}

	public static class Builder extends AbstractDataBuilder<Kit> {

		public Builder() {
			super(Kit.class, 1);
		}

		@Override
		protected Optional<Kit> buildContent(DataView container) throws InvalidDataException {
			String name = container.getString(NAME).get();
			long cooldown = container.getLong(COOLDOWN).get();
			int limit = container.getInt(LIMIT).get();
			double price = container.getDouble(PRICE).get();
			
			ItemStack offHand = ItemStack.builder().fromContainer(container.getView(OFF_HAND).get()).build();
			ItemStack helmet = ItemStack.builder().fromContainer(container.getView(HELMET).get()).build();			
			ItemStack chestPlate = ItemStack.builder().fromContainer(container.getView(CHEST_PLATE).get()).build();
			ItemStack leggings = ItemStack.builder().fromContainer(container.getView(LEGGINGS).get()).build();
			ItemStack boots = ItemStack.builder().fromContainer(container.getView(BOOTS).get()).build();

			Map<Integer, ItemStack> hotbar = new HashMap<>();

			if (container.contains(HOTBAR)) {
				for (DataView data : container.getViewList(HOTBAR).get()) {
					hotbar.put(data.getInt(SLOT_POSITION).get(), ItemStack.builder().fromContainer(data.getView(ITEM_STACK).get()).build());
				}
			}

			Map<Integer, ItemStack> grid = new HashMap<>();

			if (container.contains(GRID)) {
				for (DataView data : container.getViewList(GRID).get()) {
					grid.put(data.getInt(SLOT_POSITION).get(), ItemStack.builder().fromContainer(data.getView(ITEM_STACK).get()).build());
				}
			}

			return Optional.of(new Kit(name, offHand, helmet, chestPlate, leggings, boots, hotbar, grid, cooldown, limit, price));
		}
	}
	
	public static byte[] serialize(Kit kit) {
		try {
			ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
			GZIPOutputStream gZipOutStream = new GZIPOutputStream(byteOutStream);
			DataFormats.NBT.writeTo(gZipOutStream, kit.toContainer());
			gZipOutStream.close();
			return byteOutStream.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Kit deserialize(byte[] bytes) {
		try {
			ByteArrayInputStream byteInputStream = new ByteArrayInputStream(bytes);
			GZIPInputStream gZipInputSteam = new GZIPInputStream(byteInputStream);
			DataContainer container = DataFormats.NBT.readFrom(gZipInputSteam);
			return Sponge.getDataManager().deserialize(Kit.class, container).get();
		} catch (InvalidDataFormatException | IOException e1) {
			e1.printStackTrace();
			return null;
		}
	}
}
