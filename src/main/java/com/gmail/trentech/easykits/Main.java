package com.gmail.trentech.easykits;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import com.gmail.trentech.easykits.data.ImmutableKitInfoData;
import com.gmail.trentech.easykits.data.ImmutablePlayerData;
import com.gmail.trentech.easykits.data.Keys;
import com.gmail.trentech.easykits.data.KitInfo;
import com.gmail.trentech.easykits.data.KitInfoData;
import com.gmail.trentech.easykits.data.PlayerData;
import com.gmail.trentech.easykits.events.EventManager;
import com.gmail.trentech.easykits.init.Commands;
import com.gmail.trentech.easykits.init.Common;
import com.gmail.trentech.easykits.kit.Kit;
import com.gmail.trentech.easykits.kit.KitService;
import com.gmail.trentech.easykits.utils.Resource;
import com.google.inject.Inject;

@Plugin(id = Resource.ID, name = Resource.NAME, version = Resource.VERSION, description = Resource.DESCRIPTION, authors = Resource.AUTHOR, url = Resource.URL, dependencies = { @Dependency(id = "pjc", optional = false) })
public class Main {

	@Inject
	@ConfigDir(sharedRoot = false)
	private Path path;

	@Inject
	private Logger log;

	private static PluginContainer plugin;
	private static Main instance;

	@Listener
	public void onPreInitializationEvent(GamePreInitializationEvent event) {
		plugin = Sponge.getPluginManager().getPlugin(Resource.ID).get();
		instance = this;

		try {
			Files.createDirectories(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		@SuppressWarnings("unused")
		Object obj = Keys.KIT_USAGE;
	}

	@Listener
	public void onInitializationEvent(GameInitializationEvent event) {
		Common.initConfig();

		Sponge.getEventManager().registerListeners(this, new EventManager());
		
		Sponge.getCommandManager().register(this, new Commands().cmdKit, "kit", "k");

		Sponge.getDataManager().registerBuilder(Kit.class, new Kit.Builder());
		Sponge.getDataManager().registerBuilder(KitInfo.class, new KitInfo.Builder());
		
		DataRegistration.builder().dataClass(PlayerData.class).immutableClass(ImmutablePlayerData.class).builder(new PlayerData.Builder()).name("player_data")
			.id("player_data").build();
		DataRegistration.builder().dataClass(KitInfoData.class).immutableClass(ImmutableKitInfoData.class).builder(new KitInfoData.Builder()).name("kit_info_data")
			.id("kit_info_data").build();
	
		Common.initData();
		Common.initHelp();

		Sponge.getServiceManager().setProvider(getPlugin(), KitService.class, new KitService());
	}

	@Listener
	public void onReloadEvent(GameReloadEvent event) {
		Common.initConfig();
	}

	public Logger getLog() {
		return log;
	}

	public Path getPath() {
		return path;
	}

	public static PluginContainer getPlugin() {
		return plugin;
	}

	public static Main instance() {
		return instance;
	}
}