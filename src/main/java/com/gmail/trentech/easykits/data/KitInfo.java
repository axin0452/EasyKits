package com.gmail.trentech.easykits.data;

import static org.spongepowered.api.data.DataQuery.of;

import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

public class KitInfo implements DataSerializable {

	private final static DataQuery NAME = of("name");
	private final static DataQuery CHECKS = of("checks");
	private final static DataQuery ID = of("id"); // Ensures non stackable ItemStack
	
	protected String kitName;
	protected boolean checks;
	protected String id = UUID.randomUUID().toString();
	
	public KitInfo(String kitName, boolean checks) {
		this.kitName = kitName;
		this.checks = checks;
	}

	public String getKitName() {
		return kitName;
	}

	public void setKitName(String kitName) {
		this.kitName = kitName;
	}

	public boolean doChecks() {
		return checks;
	}

	public void setChecks(boolean checks) {
		this.checks = checks;
	}

	@Override
	public int getContentVersion() {
		return 1;
	}

	@Override
	public DataContainer toContainer() {
		return DataContainer.createNew().set(NAME, getKitName()).set(CHECKS, doChecks()).set(ID, id);
	}

	public static class Builder extends AbstractDataBuilder<KitInfo> {

		public Builder() {
			super(KitInfo.class, 1);
		}

		@Override
		protected Optional<KitInfo> buildContent(DataView container) throws InvalidDataException {
			if (container.contains(CHECKS, NAME)) {
				
				String name = container.getString(NAME).get();
				boolean checks = container.getBoolean(CHECKS).get();
				
				return Optional.of(new KitInfo(name, checks));
			}

			return Optional.empty();
		}
	}
}
