package net.oasis9.shop;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	public static Map<UUID, Integer> coins = new HashMap<UUID, Integer>();
	private static FileConfiguration config;
	private static Main instance;
	
	public void onEnable() {
		instance = this;
		config = getConfig();
		if (!config.isConfigurationSection("items")) {
			config.set("items.Diamond.cost", 300);
			config.set("items.Diamond.material", "DIAMOND");
		}
		ShopItem.loadItems(config);
		getServer().getPluginManager().registerEvents(new InventoryManager(), this);
	}
	
	public static String getShopName() {
		if (!config.isString("inventoryName"))
			config.set("inventoryName", "Shop");
		return config.getString("inventoryName");
	}
	
	public static Integer getCoins(Player pl) {
		UUID uuid = pl.getUniqueId();
		if (coins.containsKey(uuid))
			return coins.get(uuid);
		else setCoins(pl, 0);
		return 0;
	}
	
	public static void setCoins(Player pl, int coins) {
		Main.coins.put(pl.getUniqueId(), coins);
		instance.save();
	}
	
	public static void modifyCoins(Player pl, int coins) {
		UUID uuid = pl.getUniqueId();
		if (Main.coins.containsKey(uuid))
			Main.coins.put(uuid, Main.coins.get(uuid) + coins);
		else setCoins(pl, coins);
		instance.save();
	}
	
	public void save() {
		for (Entry<UUID, Integer> coinData : coins.entrySet())
			config.set("coins." + coinData.getKey(), coinData.getValue());
		saveConfig();
	}
	
	public static boolean tryParseInt(String value) {  
		try {
			Integer.parseInt(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}