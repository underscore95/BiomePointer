package me.unfear.BiomePointer.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

import me.unfear.BiomePointer.BiomePointer;

public class ItemCraftListener implements Listener {

	@EventHandler
	void onCraft(PrepareItemCraftEvent e) {
		for (ItemStack item : e.getInventory().getMatrix()) {
			if (BiomePointer.inst.isBiomePointer(item)) {
				e.getInventory().setResult(null);
			}
		}
	}
}
