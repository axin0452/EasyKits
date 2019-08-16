package com.gmail.trentech.easykits.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.spongepowered.api.world.World;

import com.gmail.trentech.pjc.Main;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class WorldConfig {

	private Path path;
	private CommentedConfigurationNode config;
	private ConfigurationLoader<CommentedConfigurationNode> loader;

	private static ConcurrentHashMap<UUID, WorldConfig> configs = new ConcurrentHashMap<>();

	private WorldConfig(World world) {
		try {			
			path = world.getDirectory().resolve("easykits.conf");
			
			if (!Files.exists(path)) {
				Files.createFile(path);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		load();
	}

	public static WorldConfig init(World world) {
		WorldConfig config = new WorldConfig(world);

		config.save();

		configs.put(world.getUniqueId(), config);

		return config;
	}

	public static WorldConfig get(World world) {
		if (!configs.containsKey(world.getUniqueId())) {
			return init(world);
		}

		return configs.get(world.getUniqueId());
	}

	public ConfigurationLoader<CommentedConfigurationNode> getLoader() {
		return loader;
	}

	public CommentedConfigurationNode getConfig() {
		return config;
	}

	public void save() {
		try {
			loader.save(config);
		} catch (IOException e) {
			Main.instance().getLog().error("Failed to save config");
			e.printStackTrace();
		}
	}

	private void load() {
		loader = HoconConfigurationLoader.builder().setPath(path).build();
		try {
			config = loader.load();
		} catch (IOException e) {
			Main.instance().getLog().error("Failed to load config");
			e.printStackTrace();
		}
	}
}
