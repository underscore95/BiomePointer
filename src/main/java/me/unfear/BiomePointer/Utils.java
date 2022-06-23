package me.unfear.BiomePointer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Utils {

	public static boolean hasSpace(Player player) {
		for (ItemStack item : player.getInventory().getContents()) {
			if (item == null || item.getType() == Material.AIR) return true;
		}
		return false;
	}
	
	public static <E extends Enum<E>> String prettifyEnum(E e) {
		return prettifyEnum(e.toString());
	}
	
	public static <E extends Enum<E>> String prettifyEnum(String string) {
		String pretty = "";
		for (String word : string.toLowerCase().split("_")) {
			pretty += word.replaceFirst(word.charAt(0) + "", ("" + word.charAt(0)).toUpperCase()) + " ";
		}
		
		return pretty.substring(0, pretty.length() - 1);
	}
}
