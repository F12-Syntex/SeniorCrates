package com.scrates.itembuilder;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class GlassPaneItem extends ItemContructor{

	public GlassPaneItem(ItemStack item) {
		super(item);
	}

	@SuppressWarnings("deprecation")
	public static GlassPaneItem getInstance(DyeColor colour) {
		ItemStack itemStack = new ItemStack(new ItemStack(Material.STAINED_GLASS_PANE, 1, colour.getData()));
		GlassPaneItem item = new GlassPaneItem(itemStack);
		return item;
	}

	@Override
	public ItemStack get() {
		return this.itemStack;
	}

    
}
