package me.unfear.BiomePointer;

import org.bukkit.Location;
import org.bukkit.block.Biome;

public class BiomeSearcher {

    private final Location startingLocation;
    private final Biome searchingFor;

    public BiomeSearcher(Location startingLocation, Biome searchingFor) {
        super();
        this.startingLocation = startingLocation;
        this.searchingFor = searchingFor;
    }

    /**
     * @return Location of biome, null if none was found within distance blocks.
     */
    public Location search() {
        final Location possibleLocation = BiomePointer.inst.biomeCache.getClosest(startingLocation, searchingFor);
        if (possibleLocation != null && possibleLocation.getWorld() != null && startingLocation.getWorld() != null
                && possibleLocation.getWorld().getName().equals(startingLocation.getWorld().getName())
                && possibleLocation.distance(startingLocation) < BiomePointer.inst.getConfigLoader()
                .getCacheDistance()) {
            possibleLocation.getChunk().load();
            possibleLocation.setY(possibleLocation.getWorld().getHighestBlockYAt(possibleLocation) + 1);
            return possibleLocation;
        }

        int x = 0, y = 0, d = 1, m = 1;

        int count = 0;
        while (count < BiomePointer.inst.getConfigLoader().getSearchDistance()) {
            while (2 * x * d < m) {
                count++;
                if (tryPos(x, y) != null)
                    return tryPos(x, y);
                x += d;
            }

            while (2 * y * d < m) {
                count++;
                if (tryPos(x, y) != null)
                    return tryPos(x, y);
                y += d;
            }

            d = -d;
            m += 1;
        }
        return null; // biome not found
    }

    private Location tryPos(int x, int y) {
        final Location location = new Location(startingLocation.getWorld(), startingLocation.getX() + (x * 64), 60,
                startingLocation.getY() + (y * 64));
        final Biome biome = location.getWorld() == null ? null : location.getWorld().getBiome(location);

        if (biome != null && BiomePointer.inst.getConfigLoader().getBiomes().contains(biome))
            BiomePointer.inst.biomeCache.cache(location, biome);
        if (biome == searchingFor) {
            location.getChunk().load();
            location.setY(location.getWorld().getHighestBlockYAt(location) + 1);
            return location;
        }
        return null;
    }
}
