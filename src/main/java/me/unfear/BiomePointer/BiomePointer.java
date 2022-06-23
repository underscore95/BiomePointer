package me.unfear.BiomePointer;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.unfear.BiomePointer.commands.BiomecacheCommand;
import me.unfear.BiomePointer.commands.BiomelistCommand;
import me.unfear.BiomePointer.commands.BiomepointerCommand;
import me.unfear.BiomePointer.commands.BiometpCommand;
import me.unfear.BiomePointer.listeners.ItemCraftListener;
import me.unfear.BiomePointer.listeners.ItemInteractListener;

public class BiomePointer extends JavaPlugin {
	
	private static final String NBT_ID = "BiomePointer.pointer_item";
	public static final String NBT_BIOME = "BiomePointer.biome";

	public static BiomePointer inst;
	
	public final BiomeCache biomeCache = new BiomeCache();
	private BiomePointerManager pointerManager;
	private ConfigLoader configLoader;
	
	public void onEnable() {
		inst = this;
		
		configLoader = new ConfigLoader();
		pointerManager = new BiomePointerManager();
		
		getCommand("biomepointer").setExecutor(new BiomepointerCommand());
		getCommand("biometp").setExecutor(new BiometpCommand());
		getCommand("biomecache").setExecutor(new BiomecacheCommand());
		getCommand("biomelist").setExecutor(new BiomelistCommand());
		
		final PluginManager pm = Bukkit.getServer().getPluginManager();
		
		pm.registerEvents(new ItemInteractListener(), this);
		pm.registerEvents(new ItemCraftListener(), this);
	}
	
	public BiomePointerManager getPointerManager() {
		return this.pointerManager;
	}
	
	public ConfigLoader getConfigLoader() {
		return this.configLoader;
	}
	
	public ItemStack createBiomePointer() {
		final ItemStack item = new ItemStack(Material.COMPASS);
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Biome Pointer");
		meta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', "&7Searching for: &4Nothing"), ChatColor.GRAY + "Right click to set biome"));
		item.setItemMeta(meta);
		
		final NBTItem nbt = new NBTItem(item, true);
		nbt.setBoolean(NBT_ID, true);
		nbt.setString(NBT_BIOME, "");
		
		return item;
	}
	
	public boolean isBiomePointer(ItemStack item) {
		if (item == null || item.getType() != Material.COMPASS) return false;
		final NBTItem nbt = new NBTItem(item);
		return nbt.hasKey(NBT_ID);
	}
}
