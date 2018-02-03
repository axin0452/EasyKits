package com.gmail.trentech.easykits.data;

import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.MapValue;
import org.spongepowered.api.data.value.mutable.Value;

import com.google.common.reflect.TypeToken;

public class Keys {

	private static final TypeToken<MapValue<String, KitUsage>> KIT_USAGES_MAP__TOKEN = new TypeToken<MapValue<String, KitUsage>>() {
		private static final long serialVersionUID = -1;
	};
	private static final TypeToken<Value<KitUsage>> KIT_USAGE_TOKEN = new TypeToken<Value<KitUsage>>() {
		private static final long serialVersionUID = -1;
	};

	public static final Key<Value<KitUsage>> KIT_USAGE = Key.builder().type(KIT_USAGE_TOKEN).id("kit_usage").name("kit_usage").query(DataQuery.of("kit_usage")).build();
	public static final Key<MapValue<String, KitUsage>> KIT_USAGES = Key.builder().type(KIT_USAGES_MAP__TOKEN).id("portals").name("portals").query(DataQuery.of("portals")).build();
}
