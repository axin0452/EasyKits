package com.gmail.trentech.easykits.init;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.gmail.trentech.easykits.Main;
import com.gmail.trentech.pjc.core.ConfigManager;
import com.gmail.trentech.pjc.core.SQLManager;
import com.gmail.trentech.pjc.help.Help;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;

public class Common {

	public static void init() {
		initConfig();
		initHelp();
		initData();
	}
	
	public static void initData() {
		try {
			SQLManager sqlManager = SQLManager.get(Main.getPlugin());
			Connection connection = sqlManager.getDataSource().getConnection();

			PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + sqlManager.getPrefix("KITS") + " (NAME TEXT, DATA MEDIUMBLOB)");

			statement.executeUpdate();
			
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void initHelp() {
		Help kitCreate = new Help("kit create", "create", "")
				.setPermission("pji.cmd.kit.create");
		
		Help kitDelete = new Help("kit create", "create", "")
				.setPermission("pji.cmd.kit.create");

		Help kitResetCooldown = new Help("kit create", "create", "")
				.setPermission("pji.cmd.kit.create");
		
		Help kitResetLimit = new Help("kit create", "create", "")
				.setPermission("pji.cmd.kit.create");
		
		Help kitResetAll = new Help("kit create", "create", "")
				.setPermission("pji.cmd.kit.create");
		
		Help kitReset = new Help("kit create", "create", "")
				.setPermission("pji.cmd.kit.create")
				.addChild(kitResetCooldown)
				.addChild(kitResetAll)
				.addChild(kitResetLimit);
		
		Help kitCooldown = new Help("kit cooldown", "cooldown", "")
				.setPermission("pji.cmd.kit.cooldown");
		
		Help kitGive = new Help("kit give", "give", "")
				.setPermission("pji.cmd.kit.give");
		
		Help kitLimit = new Help("kit limit", "limit", "")
				.setPermission("pji.cmd.kit.limit");
		
		Help kitList = new Help("kit list", "list", "")
				.setPermission("pji.cmd.kit.list");
		
		Help kitPrice = new Help("kit price", "price", "")
				.setPermission("pji.cmd.kit.price");
		
		Help kitView = new Help("kit view", "view", "")
				.setPermission("pji.cmd.kit.view");

		Help kit = new Help("kit", "kit", "")
				.setPermission("pji.cmd.kit")
				.addChild(kitView)
				.addChild(kitPrice)
				.addChild(kitDelete)
				.addChild(kitCreate)
				.addChild(kitList)
				.addChild(kitGive)
				.addChild(kitLimit)
				.addChild(kitCooldown)
				.addChild(kitReset);
		
		Help.register(kit);
	}
	
	public static void initConfig() {
		ConfigManager configManager = ConfigManager.init(Main.getPlugin());
		CommentedConfigurationNode config = configManager.getConfig();

		if (config.getNode("options", "default-cooldown").isVirtual()) {
			config.getNode("options", "default-cooldown").setValue(0).setComment("Default cooldown when creating kit in seconds. 0 to disable");
		}
		if (config.getNode("options", "default-limit").isVirtual()) {
			config.getNode("options", "default-limit").setValue(0).setComment("Default limit when creating kit. 0 to disable");
		}
		if (config.getNode("options", "default-price").isVirtual()) {
			config.getNode("options", "default-price").setValue(0.0).setComment("Default price when creating kit. 0.0 to disable. Need economy plugin to use this");
		}
		if (config.getNode("options", "new-player-kit").isVirtual()) {
			config.getNode("options", "new-player-kit").setValue("starter").setComment("Kit to give new players.");
		}			
		if (config.getNode("options", "sign-action").isVirtual()) {
			config.getNode("options", "sign-action").setValue("view").setComment("Sign click action. 'view' or 'get'");
		}
		
		configManager.save();
	}
}
