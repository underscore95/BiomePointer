package me.unfear.BiomePointer.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.unfear.BiomePointer.BiomePointer;

public class BiomecacheCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (args.length < 1 || (!args[0].equalsIgnoreCase("clear") && !args[0].equalsIgnoreCase("count"))) {
			sender.sendMessage(ChatColor.GRAY + "/biomecache clear" + ChatColor.WHITE + " Clear biome cache.");
			sender.sendMessage(ChatColor.GRAY + "/biomecache count" + ChatColor.WHITE + " Count locations of biomes cached.");
			return false;
		}
		
		if (args[0].equalsIgnoreCase("clear")) {
			BiomePointer.inst.biomeCache.clear();
			sender.sendMessage(ChatColor.GREEN + "Biome cache cleared.");
			return true;
		} else if (args[0].equalsIgnoreCase("count")) {
			sender.sendMessage(ChatColor.GRAY + "Locations cached: " + ChatColor.WHITE + BiomePointer.inst.biomeCache.count());
			return true;
		}
		return true;
	}
}
