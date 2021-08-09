package com.scrates.data;

import org.bukkit.inventory.ItemStack;

public class CrateItem {

	private String identification;
	private ItemStack item;
	private double chance;
	
	public CrateItem(String identification, ItemStack item, double chance) {
		this.item = item;
		this.chance = chance;
		this.setIdentification(identification);
	}

	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	public double getChance() {
		return chance;
	}

	public void setChance(double chance) {
		this.chance = chance;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}
	
}
