package com.gmail.trentech.easykits.data;

import static com.gmail.trentech.easykits.data.Keys.KIT_INFO;

import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractSingleData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.data.value.mutable.Value;

import com.google.common.base.Preconditions;

public class KitInfoData extends AbstractSingleData<KitInfo, KitInfoData, ImmutableKitInfoData> {

	public KitInfoData() {
		super(new KitInfo("", false), KIT_INFO);
	}

	public KitInfoData(KitInfo value) {
		super(value, KIT_INFO);
	}

	public Value<KitInfo> kitInfo() {
		return Sponge.getRegistry().getValueFactory().createValue(KIT_INFO, getValue(), getValue());
	}

	@Override
	public KitInfoData copy() {
		return new KitInfoData(this.getValue());
	}

	@Override
	public Optional<KitInfoData> fill(DataHolder dataHolder, MergeFunction mergeFn) {
		KitInfoData signData = Preconditions.checkNotNull(mergeFn).merge(copy(), dataHolder.get(KitInfoData.class).orElse(copy()));
		return Optional.of(set(KIT_INFO, signData.get(KIT_INFO).get()));
	}

	@Override
	public Optional<KitInfoData> from(DataContainer container) {
		if (container.contains(KIT_INFO.getQuery())) {
			return Optional.of(set(KIT_INFO, container.getSerializable(KIT_INFO.getQuery(), KitInfo.class).orElse(getValue())));
		}
		return Optional.empty();
	}

	@Override
	public int getContentVersion() {
		return 1;
	}

	@Override
	public ImmutableKitInfoData asImmutable() {
		return new ImmutableKitInfoData(this.getValue());
	}

	@Override
	protected Value<KitInfo> getValueGetter() {
		return Sponge.getRegistry().getValueFactory().createValue(KIT_INFO, getValue(), getValue());
	}

	@Override
	public DataContainer toContainer() {
		return super.toContainer().set(KIT_INFO, getValue());
	}

	public static class Builder extends AbstractDataBuilder<KitInfoData> implements DataManipulatorBuilder<KitInfoData, ImmutableKitInfoData> {

		public Builder() {
			super(KitInfoData.class, 0);
		}

		@Override
		public Optional<KitInfoData> buildContent(DataView container) throws InvalidDataException {
			if (!container.contains(KIT_INFO.getQuery())) {
				return Optional.empty();
			}
			
			KitInfo kitInfo = container.getSerializable(KIT_INFO.getQuery(), KitInfo.class).get();
			
			return Optional.of(new KitInfoData(kitInfo));
		}

		@Override
		public KitInfoData create() {
			return new KitInfoData();
		}

		@Override
		public Optional<KitInfoData> createFrom(DataHolder dataHolder) {
			return create().fill(dataHolder);
		}

	}
}
