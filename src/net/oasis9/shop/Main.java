package net.oasis9.shop;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

public class Main extends JavaPlugin implements Listener {

	public static Map<UUID, Integer> coins = new HashMap<UUID, Integer>();
	private static FileConfiguration config;
	private static Main instance;
	
	public void onEnable() {
		instance = this;
		config = getConfig();
		if (!config.isConfigurationSection("items")) {
			config.set("items.&bDiamond.cost", 300);
			config.set("items.&bDiamond.material", "DIAMOND");
			config.set("items.&bDiamond.amount", 10);
			config.set("items.&bDiamond.data", 0);
			saveConfig();
		}
		ShopItem.loadItems(config);
		getServer().getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler
	public void inventoryClick(InventoryClickEvent e) {
		InventoryManager.inventoryClick(e);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("You cannot use this command!");
			return true;
		}
		Player pl = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("shop"))
			InventoryManager.openShop(pl, 0);
		else if (cmd.getName().equalsIgnoreCase("spawn"))
			FakePlayer.spawnShop(pl.getLocation());
		return true;
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
		else Main.coins.put(uuid, 0);
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
		else Main.coins.put(uuid, coins);
		instance.save();
	}
	
	public void save() {
		for (Entry<UUID, Integer> coinData : coins.entrySet())
			config.set("coins." + coinData.getKey(), coinData.getValue());
		saveConfig();
	}
	
	public static void actionBar(Player pl, String message) {
		PacketPlayOutChat msg = new PacketPlayOutChat(ChatSerializer.a("{text:\"" + ChatColor.translateAlternateColorCodes('&', message) + "\"}"), (byte) 2);
		((CraftPlayer) pl).getHandle().playerConnection.sendPacket(msg);
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