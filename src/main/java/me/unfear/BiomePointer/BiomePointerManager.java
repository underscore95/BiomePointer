package me.unfear.BiomePointer;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import de.tr7zw.changeme.nbtapi.NBTItem;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class BiomePointerManager {
	
	private final Random random = new Random();

	public BiomePointerManager() {
		new BukkitRunnable() {
			public void run() {
				for (Player player : Bukkit.getOnlinePlayers()) {
					final ItemStack held = player.getInventory().getItemInMainHand();
					if (!BiomePointer.inst.isBiomePointer(held))
						continue;

					final NBTItem nbt = new NBTItem(held);

					if (nbt.getString(BiomePointer.NBT_BIOME).equals("")) {
						player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', "&7Searching for: &4Nothing")));
						continue;
					}

					final Biome biome = Biome.valueOf(nbt.getString(BiomePointer.NBT_BIOME));
					
					final Location biomeLocation = new BiomeSearcher(player.getLocation(), biome).search();
					
					if (biomeLocation == null) {
						player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', "&7No &b" + Utils.prettifyEnum(biome) + " &7nearby")));
						player.setCompassTarget(player.getLocation().add(random.nextInt(10) - 5, 0, random.nextInt(10) - 5));
						continue;
					}
					
					player.setCompassTarget(biomeLocation);
					final int distance = (int) biomeLocation.distance(player.getLocation());
					player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', "&7Pointing towards: &b" + Utils.prettifyEnum(biome) + " &7[&9" + distance + " &7blocks&8]")));
					
				}
			}
		}.runTaskTimer(BiomePointer.inst, 20, 20);
	}
}
