package com.scrates.itembuilder;

import java.util.List;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.scrates.utils.ComponentBuilder;
import com.scrates.utils.MessageUtils;

public abstract class ItemContructor {

	protected ItemStack itemStack;
	
	private ItemContructor data;
	
	public ItemContructor(ItemStack item) {
		this.itemStack = item;
	}
	
	public abstract ItemStack get();

	public ItemContructor getData() {
		return data;
	}

	public void setData(ItemContructor data) {
		this.data = data;
	}

	public ItemContructor setName(String name) {
		
		if(name.isEmpty()) {
			ItemMeta meta = this.itemStack.getItemMeta();
			meta.setDisplayName(MessageUtils.translateAlternateColorCodes("&cX"));
			this.itemStack.setItemMeta(meta);
			return this;	
		}
		
		ItemMeta meta = this.itemStack.getItemMeta();
		meta.setDisplayName(MessageUtils.translateAlternateColorCodes(name));
		this.itemStack.setItemMeta(meta);
		return this;
	}
	
	public ItemContructor setLore(List<String> lore) {
		ItemMeta meta = this.itemStack.getItemMeta();
		meta.setLore(ComponentBuilder.createLore(lore));
		this.itemStack.setItemMeta(meta);
		return this;
	}
	
	public ItemContructor addFlag(ItemFlag flag) {
		ItemMeta meta = this.itemStack.getItemMeta();
		meta.addItemFlags(flag);
		this.itemStack.setItemMeta(meta);
		return this;
	}
	
	public ItemContructor addEnchant(Enchantment enchant, int level) {
		this.itemStack.addEnchantment(enchant, level);
		return this;
	}
	
	public ItemContructor clone() {
		final ItemContructor contructor = this;
		return contructor;
	}
	
	
	
	
}
