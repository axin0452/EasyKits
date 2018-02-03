package com.gmail.trentech.easykits.kit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Optional;

import com.gmail.trentech.easykits.Main;
import com.gmail.trentech.pjc.core.SQLManager;

public class KitDB {

	public static HashMap<String, Kit> all() {
		HashMap<String, Kit> map = new HashMap<>();

		try {
			SQLManager sqlManager = SQLManager.get(Main.getPlugin());
			Connection connection = sqlManager.getDataSource().getConnection();

			PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + sqlManager.getPrefix("KITS"));

			ResultSet result = statement.executeQuery();

			while (result.next()) {
				String name = result.getString("NAME");
				Kit kit = Kit.deserialize(result.getBytes("DATA"));

				map.put(name, kit);
			}

			connection.close();
			statement.close();
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return map;
	}

	public static boolean exists(String name) {
		try {
			SQLManager sqlManager = SQLManager.get(Main.getPlugin());
			Connection connection = sqlManager.getDataSource().getConnection();

			PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + sqlManager.getPrefix("KITS"));

			ResultSet result = statement.executeQuery();

			while (result.next()) {
				if (result.getString("NAME").equalsIgnoreCase(name)) {
					connection.close();
					statement.close();
					result.close();
					
					return true;
				}
			}

			connection.close();
			statement.close();
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;
	}
	
	public static Optional<Kit> get(String name) {
		try {
			SQLManager sqlManager = SQLManager.get(Main.getPlugin());
			Connection connection = sqlManager.getDataSource().getConnection();

			PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + sqlManager.getPrefix("KITS"));

			ResultSet result = statement.executeQuery();

			while (result.next()) {
				if (result.getString("NAME").equalsIgnoreCase(name)) {
					Kit kit = Kit.deserialize(result.getBytes("DATA"));
					
					connection.close();
					statement.close();
					result.close();
					
					return Optional.of(kit);
				}
			}

			connection.close();
			statement.close();
			result.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return Optional.empty();
	}

	public static void save(Kit kit) {
		if (exists(kit.getName())) {
			update(kit);
		} else {
			create(kit);
		}
	}

	public static void remove(String name) {
		try {
			SQLManager sqlManager = SQLManager.get(Main.getPlugin());
			Connection connection = sqlManager.getDataSource().getConnection();

			PreparedStatement statement = connection.prepareStatement("DELETE FROM " + sqlManager.getPrefix("KITS") + " WHERE NAME = ?");

			statement.setString(1, name);
			statement.executeUpdate();

			connection.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void create(Kit kit) {
		try {
			SQLManager sqlManager = SQLManager.get(Main.getPlugin());
			Connection connection = sqlManager.getDataSource().getConnection();

			PreparedStatement statement = connection.prepareStatement("INSERT INTO " + sqlManager.getPrefix("KITS") + " (NAME, DATA) VALUES (?, ?)");

			statement.setString(1, kit.getName());
			statement.setBytes(2, Kit.serialize(kit));

			statement.executeUpdate();

			connection.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void update(Kit kit) {
		try {
			SQLManager sqlManager = SQLManager.get(Main.getPlugin());
			Connection connection = sqlManager.getDataSource().getConnection();

			PreparedStatement statement = connection.prepareStatement("UPDATE " + sqlManager.getPrefix("KITS") + " SET DATA = ? WHERE NAME = ?");

			statement.setString(2, kit.getName());
			statement.setBytes(1, Kit.serialize(kit));

			statement.executeUpdate();

			connection.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
