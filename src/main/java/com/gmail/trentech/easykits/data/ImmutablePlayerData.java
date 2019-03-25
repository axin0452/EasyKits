package com.gmail.trentech.easykits.data;

import static com.gmail.trentech.easykits.data.Keys.KIT_USAGES;

import java.util.Map;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableMappedData;
import org.spongepowered.api.data.value.immutable.ImmutableMapValue;

public class ImmutablePlayerData extends AbstractImmutableMappedData<String, KitUsage, ImmutablePlayerData, PlayerData> {

	public ImmutablePlayerData(Map<String, KitUsage> value) {
		super(KIT_USAGES, value);
	}

	public ImmutableMapValue<String, KitUsage> homes() {
		return Sponge.getRegistry().getValueFactory().createMapValue(KIT_USAGES, getValue()).asImmutable();
	}

	@Override
	public int getContentVersion() {
		return 0;
	}

	@Override
	public PlayerData asMutable() {
		return new PlayerData(this.getValue());
	}

	@Override
	public DataContainer toContainer() {
		return super.toContainer().set(KIT_USAGES, getValue());
	}
}
