package me.unfear.BiomePointer.menus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;

import de.tr7zw.changeme.nbtapi.NBTItem;
import me.unfear.BiomePointer.BiomePointer;
import me.unfear.BiomePointer.Utils;

public class BiomePointerSelectionMenu {

	private static final String NBT_BIOME = "BiomePointer.biome_to_select";

	private static ArrayList<ItemStack> getBiomes() {
		final ArrayList<ItemStack> biomes = new ArrayList<>();
		for (Biome biome : BiomePointer.inst.getConfigLoader().getBiomes()) {
			final ItemStack item = new ItemStack(Material.PAPER);
			final ItemMeta meta = Objects.requireNonNull(item.getItemMeta());
			meta.setDisplayName(ChatColor.BLUE + Utils.prettifyEnum(biome));
			item.setItemMeta(meta);

			final NBTItem nbt = new NBTItem(item);
			nbt.setString(NBT_BIOME, biome.toString());
			nbt.applyNBT(item);

			biomes.add(item);
		}
		return biomes;
	}

	public static ChestGui create(int page) {

		final ChestGui gui = new ChestGui(6, ChatColor.GRAY + "Select Biome");

		final ItemStack background = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		final ItemMeta backgroundMeta = Objects.requireNonNull(background.getItemMeta());
		backgroundMeta.setDisplayName(ChatColor.RED + " ");
		background.setItemMeta(backgroundMeta);

		final ItemStack prevArrow = new ItemStack(Material.ARROW);
		final ItemMeta prevArrowMeta = Objects.requireNonNull(prevArrow.getItemMeta());
		prevArrowMeta.setDisplayName(ChatColor.GRAY + "Previous Page");
		prevArrow.setItemMeta(prevArrowMeta);

		final ItemStack nextArrow = new ItemStack(Material.ARROW);
		final ItemMeta nextArrowMeta = Objects.requireNonNull(nextArrow.getItemMeta());
		nextArrowMeta.setDisplayName(ChatColor.GRAY + "Next Page");
		nextArrow.setItemMeta(nextArrowMeta);

		gui.setOnGlobalClick(event -> event.setCancelled(true));

		PaginatedPane pages = new PaginatedPane(0, 0, 9, 5);
		pages.populateWithItemStacks(getBiomes());
		pages.setOnClick(event -> {
			if (event.getCurrentItem() == null) return;
			event.getWhoClicked().closeInventory();
			
			// make sure player still has the pointer
			final ItemStack held = event.getWhoClicked().getInventory().getItemInMainHand();
			if (!BiomePointer.inst.isBiomePointer(held)) {
				event.getWhoClicked().sendMessage(ChatColor.GRAY + "Something went wrong: couldn't find your Biome Pointer!");
				return;
			}
			
			// set biome
			final NBTItem nbt = new NBTItem(event.getCurrentItem());
			if (!nbt.hasTag(NBT_BIOME))
				return;
			final String biomeName = nbt.getString(NBT_BIOME);

			event.getWhoClicked().sendMessage(
					ChatColor.GRAY + "Searching for " + ChatColor.AQUA + Utils.prettifyEnum(biomeName));

			final NBTItem heldItemNBT = new NBTItem(held);
			heldItemNBT.setString(BiomePointer.NBT_BIOME, biomeName);
			heldItemNBT.applyNBT(held);
			
			final ItemMeta meta = Objects.requireNonNull(held.getItemMeta());
			meta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', "&7Searching for: &b" + Utils.prettifyEnum(biomeName)), ChatColor.GRAY + "Right click to set biome"));
			held.setItemMeta(meta);
		});

		gui.addPane(pages);

		OutlinePane backgroundPane = new OutlinePane(0, 5, 9, 1);
		backgroundPane.addItem(new GuiItem(background));
		backgroundPane.setRepeat(true);
		backgroundPane.setPriority(Pane.Priority.LOWEST);

		gui.addPane(backgroundPane);

		pages.setPage(page);
		gui.update();

		StaticPane navigation = new StaticPane(0, 5, 9, 1);
		if (page > 0) {
			navigation.addItem(new GuiItem(prevArrow, event -> {
				if (pages.getPage() > 0) {
					create(page - 1).show(event.getWhoClicked());
				}
			}), 0, 0);
		}

		if (pages.getPage() < pages.getPages() - 1) {
			navigation.addItem(new GuiItem(new ItemStack(nextArrow), event -> {
				if (pages.getPage() < pages.getPages() - 1) {
					create(page + 1).show(event.getWhoClicked());
				}
			}), 8, 0);
		}

		gui.addPane(navigation);

		return gui;
	}
}
