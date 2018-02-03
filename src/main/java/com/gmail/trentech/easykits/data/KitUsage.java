package com.gmail.trentech.easykits.data;

import static org.spongepowered.api.data.DataQuery.of;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

public class KitUsage implements DataSerializable {

	private final static DataQuery NAME = of("name");
	private final static DataQuery TIMES_USED = of("timesUsed");
	private final static DataQuery DATE = of("date");
	
	protected String kitName;
	private int timesUsed;
	private Date date;

	public KitUsage(String kitName, int timesUsed, Date date) {
		this.kitName = kitName;
		this.timesUsed = timesUsed;
		this.date = date;
	}

	public String getKitName() {
		return kitName;
	}

	public void setKitName(String kitName) {
		this.kitName = kitName;
	}

	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}

	public int getTimesUsed() {
		return timesUsed;
	}

	public void setTimesUsed(int timesUsed) {
		this.timesUsed = timesUsed;
	}

	@Override
	public int getContentVersion() {
		return 1;
	}

	@Override
	public DataContainer toContainer() {
		return DataContainer.createNew().set(NAME, getKitName()).set(TIMES_USED, getTimesUsed()).set(DATE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date).toString());
	}

	public static class Builder extends AbstractDataBuilder<KitUsage> {

		public Builder() {
			super(KitUsage.class, 1);
		}

		@Override
		protected Optional<KitUsage> buildContent(DataView container) throws InvalidDataException {
			if (container.contains(DATE, TIMES_USED, NAME)) {
				String name = container.getString(NAME).get();
				int timesUsed = container.getInt(TIMES_USED).get();
				Date date;
				try {
					date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(container.getString(DATE).get());
				} catch (ParseException e) {
					e.printStackTrace();
					return Optional.empty();
				}
				
				return Optional.of(new KitUsage(name, timesUsed, date));
			}

			return Optional.empty();
		}
	}
}
