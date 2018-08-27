package com.gmail.trentech.easykits.data;

import static com.gmail.trentech.easykits.data.Keys.KIT_INFO;

import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableSingleData;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

public class ImmutableKitInfoData extends AbstractImmutableSingleData<KitInfo, ImmutableKitInfoData, KitInfoData> {

	protected ImmutableKitInfoData(KitInfo value) {
		super(value, KIT_INFO);
	}

	public ImmutableValue<KitInfo> kitInfo() {
		return Sponge.getRegistry().getValueFactory().createValue(KIT_INFO, getValue(), getValue()).asImmutable();
	}

	@Override
	public <E> Optional<ImmutableKitInfoData> with(Key<? extends BaseValue<E>> key, E value) {
		if (this.supports(key)) {
			return Optional.of(asMutable().set(key, value).asImmutable());
		} else {
			return Optional.empty();
		}
	}

	@Override
	public int getContentVersion() {
		return 1;
	}

	@Override
	protected ImmutableValue<?> getValueGetter() {
		return Sponge.getRegistry().getValueFactory().createValue(KIT_INFO, getValue()).asImmutable();
	}

	@Override
	public KitInfoData asMutable() {
		return new KitInfoData(this.getValue());
	}

	@Override
	protected DataContainer fillContainer(DataContainer dataContainer) {
		return super.toContainer().set(KIT_INFO, getValue());
	}
}
