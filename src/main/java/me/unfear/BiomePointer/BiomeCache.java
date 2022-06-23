package me.unfear.BiomePointer;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;

public class BiomeCache {

	private HashMap<World, HashMap<Biome, HashSet<Location>>> cache;
	public BiomeCache() {
		this.cache = new HashMap<>();
	}
	
	public HashMap<World, HashMap<Biome, HashSet<Location>>> getCache() {
		return this.cache;
	}
	
	/** Get closest biome to location, returns null if none are cached */
	public Location getClosest(Location location, Biome biome) {
		if (location == null || location.getWorld() == null || biome == null) throw new IllegalArgumentException("location or biome were null " + location + " - " + biome);
		cache.putIfAbsent(location.getWorld(), new HashMap<Biome, HashSet<Location>>());
		cache.get(location.getWorld()).putIfAbsent(biome, new HashSet<Location>());
		
		final HashSet<Location> locations = cache.get(location.getWorld()).get(biome);
		
		Location closest = null;
		for (Location biomeLoc : locations) {
			if (closest == null || location.distance(closest) > location.distance(biomeLoc)) 
				closest = biomeLoc;
		}
		
		return closest;
	}
	
	/** @return true if the location was successfully added, false if the location was within 256 blocks of an already cached location */
	public boolean cache(Location location, Biome biome) {
		// getClosest checks if args are null, no need to here
		final Location closest = getClosest(location, biome);
		if (closest != null && location.distance(closest) < 256) return false;
		return cache.get(location.getWorld()).get(biome).add(location);
	}
	
	public void clear() {
		this.cache = new HashMap<>();
	}
	
	public int count() {
		int count = 0;
		for (HashMap<Biome, HashSet<Location>> world : cache.values()) {
			for (HashSet<Location> biome : world.values()) {
				count += biome.size();
			}
		}
		
		return count;
	}
}
