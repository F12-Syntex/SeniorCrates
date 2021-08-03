package com.scrates.itembuilder;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemGenerator extends ItemContructor{

	public ItemGenerator(ItemStack item) {
		super(item);
	}

	public static ItemGenerator getInstance(Material material) {
		ItemStack itemStack = new ItemStack(material);
		ItemGenerator item = new ItemGenerator(itemStack);
		return item;
	}
	
	@Override
	public ItemStack get() {
		return this.itemStack;
	}

	
    
}
