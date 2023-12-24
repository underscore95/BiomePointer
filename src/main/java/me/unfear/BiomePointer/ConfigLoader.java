package me.unfear.BiomePointer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ShapedRecipe;

public class ConfigLoader {

	private int searchDistance, cacheDistance;
	private long updateTime;
	private HashSet<Biome> biomes;
	private final FileConfiguration config;

	public ConfigLoader() {
		BiomePointer.inst.saveDefaultConfig();

		config = BiomePointer.inst.getConfig();
		this.biomes = new HashSet<>();

		this.searchDistance = config.getInt("search-distance", 2000);
		this.cacheDistance = config.getInt("cache-distance", 5000);
		this.updateTime = config.getLong("update-time", 20);

		if (this.cacheDistance <= this.searchDistance) {
			BiomePointer.inst.getLogger().severe("cache-distance must be greater than search-distance");
			BiomePointer.inst.getServer().getPluginManager().disablePlugin(BiomePointer.inst);
			return;
		}

		if (this.searchDistance <= 100) { // no need to check cache-distance, as it's greater than search-distance atm
			BiomePointer.inst.getLogger().severe("cache-distance and search-distance must be >100");
			BiomePointer.inst.getServer().getPluginManager().disablePlugin(BiomePointer.inst);
			return;
		}

		if (updateTime <= 0) {
			BiomePointer.inst.getLogger().severe("update-time must be greater than 0");
			BiomePointer.inst.getServer().getPluginManager().disablePlugin(BiomePointer.inst);
			return;
		}

		List<String> biomeNames = config.getStringList("biomes");
		if (biomeNames == null)
			biomeNames = new ArrayList<String>();

		for (String biomeName : biomeNames) {
			try {
				Biome biome = Biome.valueOf(biomeName);
				biomes.add(biome);
			} catch (IllegalArgumentException e) {
				BiomePointer.inst.getLogger()
						.severe(biomeName + " isn't a biome, it won't be added to the biome list!");
			}
		}

		if (biomes.size() < 1) {
			BiomePointer.inst.getLogger().warning("No biomes defined in config.yml, Biome Pointer cannot work");
			BiomePointer.inst.getServer().getPluginManager().disablePlugin(BiomePointer.inst);
			return;
		}

		registerRecipe();
	}

	private void registerRecipe() {
		if (!config.getBoolean("recipe.enabled", false)) return; // Recipe disabled

		NamespacedKey key = new NamespacedKey(BiomePointer.inst, "biomepointer");
		ShapedRecipe recipe = new ShapedRecipe(key, BiomePointer.inst.createBiomePointer());
		List<String> shapeList = config.getStringList("recipe.shape");
		if (shapeList.size() != 3) {
			BiomePointer.inst.getLogger().severe("Failed to register recipe, shape didn't have 3 rows");
			return;
		}
		for (String row : shapeList) {
			if (row.length() != 3) {
				BiomePointer.inst.getLogger().severe("Failed to register recipe, shape didn't have 3 columns");
				return;
			}
		}

		String[] shape = new String[3];
		shapeList.toArray(shape);
		recipe.shape(shape);

		ConfigurationSection ingredientsSection = config.getConfigurationSection("recipe.ingredients");
		if (ingredientsSection == null) {
			BiomePointer.inst.getLogger().severe("Failed to register recipe, ingredients config section doesn't exist");
			return;
		}

		for (String iKey : ingredientsSection.getKeys(false)) {
			if (iKey.length() != 1) {
				BiomePointer.inst.getLogger().severe("Failed to register recipe, ingredient letter '" + iKey + "' wasn't 1 letter long");
				return;
			}

			Material m = null;
			try {
				m = Material.valueOf(ingredientsSection.getString(iKey));
			} catch (IllegalArgumentException ignored) {}
			if (m == null) {
				BiomePointer.inst.getLogger().severe("Failed to register recipe, " + m + " isn't a valid material");
				return;
			}

			recipe.setIngredient(iKey.charAt(0), m);
		}

		Bukkit.addRecipe(recipe);
	}

	public int getSearchDistance() {
		return searchDistance;
	}

	public int getCacheDistance() {
		return cacheDistance;
	}

	public HashSet<Biome> getBiomes() {
		return biomes;
	}

	public long getUpdateTime() {
		return updateTime;
	}
}
