package me.unfear.BiomePointer.commands;

import org.bukkit.ChatColor;
import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import me.unfear.BiomePointer.BiomePointer;
import me.unfear.BiomePointer.Utils;

public class BiomelistCommand implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		sender.sendMessage(ChatColor.GRAY + "All biomes in config.yml:");
		for (Biome biome : BiomePointer.inst.getConfigLoader().getBiomes()) {
			sender.sendMessage(ChatColor.AQUA + Utils.prettifyEnum(biome));
		}
		return true;
	}
}
