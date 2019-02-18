package com.gmail.trentech.easykits.data;

import static com.gmail.trentech.easykits.data.Keys.KIT_USAGES;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractMappedData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.data.value.mutable.MapValue;

import com.google.common.base.Preconditions;

public class PlayerData extends AbstractMappedData<String, KitUsage, PlayerData, ImmutablePlayerData> {

	public PlayerData(Map<String, KitUsage> value) {
		super(KIT_USAGES, value);
	}

	public PlayerData() {
		super(KIT_USAGES, new HashMap<>());
	}

	public MapValue<String, KitUsage> portals() {
		return Sponge.getRegistry().getValueFactory().createMapValue(KIT_USAGES, getValue());
	}

	@Override
	public Optional<KitUsage> get(String key) {
		if (getValue().containsKey(key)) {
			return Optional.of(getValue().get(key));
		}
		return Optional.empty();
	}

	@Override
	public Set<String> getMapKeys() {
		return getValue().keySet();
	}

	@Override
	public PlayerData put(String key, KitUsage value) {
		getValue().put(key, value);
		return this;
	}

	@Override
	public PlayerData putAll(Map<? extends String, ? extends KitUsage> map) {
		getValue().putAll(map);
		return this;
	}

	@Override
	public PlayerData remove(String key) {
		getValue().remove(key);
		return this;
	}

	@Override
	public Optional<PlayerData> fill(DataHolder dataHolder, MergeFunction mergeFn) {
		PlayerData homeData = Preconditions.checkNotNull(mergeFn).merge(copy(), dataHolder.get(PlayerData.class).orElse(copy()));
		return Optional.of(set(KIT_USAGES, homeData.get(KIT_USAGES).get()));
	}

	@Override
	public Optional<PlayerData> from(DataContainer container) {
		if (container.contains(KIT_USAGES.getQuery())) {
			HashMap<String, KitUsage> kitUsageList = new HashMap<>();

			DataView kitUsages = container.getView(KIT_USAGES.getQuery()).get();

			for (DataQuery kitUsageQ : kitUsages.getKeys(false)) {
				Optional<KitUsage> optionalKitUsage = kitUsages.getSerializable(kitUsageQ, KitUsage.class);
				
				if(optionalKitUsage.isPresent()) {
					KitUsage kitUsage = optionalKitUsage.get();
					
					kitUsageList.put(kitUsage.getKitName(), kitUsage);
				}
			}
			
			return Optional.of(new PlayerData(kitUsageList));
		}
		return Optional.empty();
	}

	@Override
	public PlayerData copy() {
		return new PlayerData(getValue());
	}

	@Override
	public int getContentVersion() {
		return 0;
	}

	@Override
	public ImmutablePlayerData asImmutable() {
		return new ImmutablePlayerData(getValue());
	}

	@Override
	protected DataContainer fillContainer(DataContainer dataContainer) {
		return super.toContainer().set(KIT_USAGES, getValue());
	}

	public static class Builder extends AbstractDataBuilder<PlayerData> implements DataManipulatorBuilder<PlayerData, ImmutablePlayerData> {

		public Builder() {
			super(PlayerData.class, 0);
		}

		@Override
		public Optional<PlayerData> buildContent(DataView container) throws InvalidDataException {
			if (container.contains(KIT_USAGES.getQuery())) {
				HashMap<String, KitUsage> kitUsageList = new HashMap<>();

				DataView kitUsages = container.getView(KIT_USAGES.getQuery()).get();

				for (DataQuery kitUsageQ : kitUsages.getKeys(false)) {
					Optional<KitUsage> optionalKitUsage = kitUsages.getSerializable(kitUsageQ, KitUsage.class);
					
					if(optionalKitUsage.isPresent()) {
						KitUsage kitUsage = optionalKitUsage.get();
						
						kitUsageList.put(kitUsage.getKitName(), kitUsage);
					}
				}
				return Optional.of(new PlayerData(kitUsageList));
			}
			return Optional.of(new PlayerData());
		}

		@Override
		public PlayerData create() {
			return new PlayerData(new HashMap<String, KitUsage>());
		}

		@Override
		public Optional<PlayerData> createFrom(DataHolder dataHolder) {
			return create().fill(dataHolder);
		}

	}
}