package net.oasis9.shop;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ShopItem {
	
	private String name;
	private ItemStack item;
	private int cost;
	
	public ShopItem(String name, Material material, int cost) {
		this.name = ChatColor.translateAlternateColorCodes('&', name);
		this.cost = cost;
		item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		InventoryManager.addItem(this);
	}
	
	public String getName() {
		return name;
	}
	
	public ItemStack getItemStack() {
		return item;
	}
	
	public int getCost() {
		return cost;
	}
	
	public static void loadItems(FileConfiguration config) {
		try {
			ConfigurationSection items = config.getConfigurationSection("items");
			for (String key : items.getKeys(false))
				try {
					new ShopItem(key, Material.valueOf(items.getString(key + ".material")), items.getInt(key + ".cost"));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
}