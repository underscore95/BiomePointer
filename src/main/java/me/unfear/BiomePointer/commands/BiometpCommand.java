package me.unfear.BiomePointer.commands;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.unfear.BiomePointer.BiomePointer;
import me.unfear.BiomePointer.BiomeSearcher;
import me.unfear.BiomePointer.Utils;

public class BiometpCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		Player target = null;

		if (sender instanceof Player)
			target = (Player) sender;
		if (args.length > 1)
			target = Bukkit.getPlayer(args[1]);

		if (target == null) {
			sender.sendMessage(ChatColor.RED + "No target specified!");
			return false;
		}

		if (args.length < 1 || args[0].equalsIgnoreCase("help")) {
			sender.sendMessage(ChatColor.RED + "/biometp <biome> [<player>] [-s]");
			return false;
		}

		try {
			Biome.valueOf(args[0].toUpperCase());
		} catch (IllegalArgumentException e) {
			sender.sendMessage(ChatColor.RED + args[0] + " isn't a biome! /biomelist");
			return false;
		}
		
		final Biome biome = Biome.valueOf(args[0].toUpperCase());
		if (!BiomePointer.inst.getConfigLoader().getBiomes().contains(biome)) {
			sender.sendMessage(ChatColor.RED + args[0] + " isn't a biome! /biomelist");
			return false;
		}
		
		Location biomeLocation = new BiomeSearcher(target.getLocation(), biome).search();
		if (biomeLocation == null && !sender.getName().equals(target.getName()) && sender instanceof Player) biomeLocation = new BiomeSearcher(((Player) sender).getLocation(), biome).search();
		if (biomeLocation == null) {
		final HashMap<Biome, HashSet<Location>> allBiomes = BiomePointer.inst.biomeCache.getCache().get(target.getWorld());
			for (Location loc : allBiomes.get(biome)) {
				biomeLocation = loc;
				break;
			}
		}
		
		if (biomeLocation == null) {
			sender.sendMessage(ChatColor.RED + "No " + ChatColor.AQUA + Utils.prettifyEnum(biome) + ChatColor.RED + " was cached, unable to teleport!");
			return true;
		}
		
		if (args.length <= 2 || !args[3].equalsIgnoreCase("-s")) {
			sender.sendMessage(ChatColor.GREEN + "You have teleported " + target.getName() + " to " + ChatColor.AQUA + Utils.prettifyEnum(biome));
		}
		
		target.teleport(biomeLocation);

		return true;
	}

}
