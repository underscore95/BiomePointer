package me.unfear.BiomePointer.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.unfear.BiomePointer.BiomePointer;
import me.unfear.BiomePointer.menus.BiomePointerSelectionMenu;

public class ItemInteractListener implements Listener {
	@EventHandler
	void onClick(PlayerInteractEvent e) {
		if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		if (!BiomePointer.inst.isBiomePointer(e.getPlayer().getInventory().getItemInMainHand())) 
			return;
		
		BiomePointerSelectionMenu.create(0).show(e.getPlayer());
	}
}
