package me.unfear.BiomePointer.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.unfear.BiomePointer.BiomePointer;
import me.unfear.BiomePointer.Utils;

public class BiomepointerCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		Player target = null;
		
		if (sender instanceof Player) target = (Player) sender;
		if (args.length > 0) 
			target = Bukkit.getPlayer(args[0]);
		
		if (target == null) {
			sender.sendMessage(ChatColor.RED + "No target specified!");
			return false;
		}
		
		if (!(args.length > 1 && args[1].equalsIgnoreCase("-s")) && target.getName().equals(sender.getName())) sender.sendMessage(ChatColor.GRAY + "You have given " + target.getName() + " a Biome Pointer.");

		final ItemStack pointer = BiomePointer.inst.createBiomePointer();
		if (Utils.hasSpace(target)) 
			target.getInventory().addItem(pointer);
		else {
			target.getWorld().dropItem(target.getLocation(), pointer);
			target.sendMessage(ChatColor.RED + "You were given a Biome Pointer, but your inventory didn't have enough space, so it was dropped on the ground.");
		}
		
		return true;
	}
}
