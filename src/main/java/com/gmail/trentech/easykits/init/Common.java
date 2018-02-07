package com.gmail.trentech.easykits.init;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.gmail.trentech.easykits.Main;
import com.gmail.trentech.pjc.core.ConfigManager;
import com.gmail.trentech.pjc.core.SQLManager;
import com.gmail.trentech.pjc.help.Argument;
import com.gmail.trentech.pjc.help.Help;
import com.gmail.trentech.pjc.help.Usage;

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
		Usage usageCreate = new Usage(Argument.of("<kit>", "Specifies the name of the kit"));
		
		Help kitCreate = new Help("kit create", "create", "Create a new kit")
				.setPermission("pji.cmd.kit.create")
				.addExample("/kit create starter")
				.setUsage(usageCreate);
		
		Usage usageDelete = new Usage(Argument.of("<kit>", "Specifies the name of the kit"));
		
		Help kitDelete = new Help("kit delete", "delete", "Deletes an existing kit")
				.setPermission("pji.cmd.kit.delete")
				.addExample("/kit create starter")
				.setUsage(usageDelete);

		Usage usageReset = new Usage(Argument.of("<kit>", "Specifies the name of the kit"))
				.addArgument(Argument.of("<player>", "Specifies the name of the player"));
		
		Help kitResetCooldown = new Help("kit reset cooldown", "cooldown", "Reset the cooldown of kit for a player")
				.setPermission("pji.cmd.kit.reset")
				.addExample("/kit reset cooldown kit Notch")
				.setUsage(usageReset);
		
		Help kitResetLimit = new Help("kit reset limit", "limit", "Reset the limit of kit for a player")
				.setPermission("pji.cmd.kit.reset")
				.addExample("/kit reset limit kit Notch")
				.setUsage(usageReset);
		
		Help kitResetAll = new Help("kit reset all", "all", "Reset cooldown and limit of kit for a player")
				.setPermission("pji.cmd.kit.reset")
				.addExample("/kit reset all kit Notch")
				.setUsage(usageReset);
		
		Help kitReset = new Help("kit reset", "reset", "")
				.setPermission("pji.cmd.kit.reset")
				.addChild(kitResetCooldown)
				.addChild(kitResetAll)
				.addChild(kitResetLimit);
		
		Usage usageCooldown = new Usage(Argument.of("<kit>", "Specifies the name of the kit"))
				.addArgument(Argument.of("<cooldown>", "Specifies the cooldown value. Comma speperated list of value time values. ex. 1w,5d,2h,30m,10s etc.."));
				
		
		Help kitCooldown = new Help("kit cooldown", "cooldown", "Sets to cooldown value of a kit")
				.setPermission("pji.cmd.kit.cooldown")
				.setUsage(usageCooldown)
				.addExample("/kit cooldown kit 30m")
				.addExample("/kit cooldown kit 1h,30m")
				.addExample("/kit cooldown kit 1d,12h,30m,10s");
		
		Usage usageGive = new Usage(Argument.of("<kit>", "Specifies the name of the kit"))
				.addArgument(Argument.of("<player>", "Specifies the name of the player"));
		
		Help kitGive = new Help("kit give", "give", "Give player the specified kit. This bypasses all checks.")
				.setPermission("pji.cmd.kit.give")
				.addExample("/kit give kit Notch")
				.setUsage(usageGive);

		Usage usageLimit = new Usage(Argument.of("<kit>", "Specifies the name of the kit"))
				.addArgument(Argument.of("<limit>", "Specifies an integer value for the max limit"));
		
		Help kitLimit = new Help("kit limit", "limit", "Sets the max limit value of a kit")
				.setPermission("pji.cmd.kit.limit")
				.addExample("/kit limit kit 5")
				.setUsage(usageLimit);
		
		Help kitList = new Help("kit list", "list", "List all avaiable kits player has permission to")
				.setPermission("pji.cmd.kit.list");
		
		Usage usageBook = new Usage(Argument.of("[kit]", "Specifies the name of the kit"));
				
		Help kitBook = new Help("kit book", "book", "Gives player a book of kits player has permission to or get a specified kit book")
				.setPermission("pji.cmd.kit.book")
				.addExample("/kit book kit")
				.setUsage(usageBook);
		
		Usage usagePrice = new Usage(Argument.of("<kit>", "Specifies the name of the kit"))
				.addArgument(Argument.of("<price>", "Specifies the price of a kit"));
		
		Help kitPrice = new Help("kit price", "price", "Set to price of a kit")
				.setPermission("pji.cmd.kit.price")
				.addExample("/kit price kit 25.50")
				.setUsage(usagePrice);
		
		Usage usageView = new Usage(Argument.of("<kit>", "Specifies the name of the kit"));
		
		Help kitView = new Help("kit view", "view", "View the contents of a kit")
				.setPermission("pji.cmd.kit.view")
				.addExample("/kit view kit")
				.setUsage(usageView);

		Help kit = new Help("kit", "kit", "")
				.setPermission("pji.cmd.kit")
				.addChild(kitView)
				.addChild(kitPrice)
				.addChild(kitDelete)
				.addChild(kitCreate)
				.addChild(kitList)
				.addChild(kitBook)
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
		if (config.getNode("options", "currency-symbol").isVirtual()) {
			config.getNode("options", "currency-symbol").setValue("$").setComment("The leading symbol when displaying prices");
		}
		if (config.getNode("options", "new-player-kit").isVirtual()) {
			config.getNode("options", "new-player-kit").setValue("starter").setComment("Kit to give new players.");
		}			
		if (config.getNode("options", "sign-action").isVirtual()) {
			config.getNode("options", "sign-action").setValue("view").setComment("Sign click action. 'view' 'get' or 'book'");
		}
		configManager.save();
	}
}
