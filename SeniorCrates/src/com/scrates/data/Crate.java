package com.scrates.data;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.scrates.main.SeniorCrates;
import com.scrates.utils.Luck;

public class Crate {
	
	private String name;
	private ItemStack object;
	private List<CrateItem> items;

	public Crate(String name, List<CrateItem> items, ItemStack object) {
		this.name = name;
		this.items = items;
		this.object = object;
	}
	
	public Crate(String name, CrateItem... items) {
		this.name = name;
		this.items = Arrays.asList(items);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CrateItem> getItems() {
		return items;
	}

	public void setItems(List<CrateItem> items) {
		this.items = items;
	}

	public ItemStack getObject() {
		return object;
	}

	public void setObject(ItemStack object) {
		this.object = object;
	}
	
	public CrateItem getWinningItem() {
		
		CrateItem nothing = new CrateItem("", new ItemStack(Material.AIR), 0);
		
		for(CrateItem item : this.items) {
			if(Luck.chance(item.getChance())) {
				return item;
			}
		}
		
		return nothing;
	}
	
	public void remove(ItemStack remove) {
		List<CrateItem> toRemove = items.stream().filter(i -> i.getItem().serialize().equals(remove.serialize())).collect(Collectors.toList());
		
		if(!toRemove.isEmpty()) {
			this.items.remove(toRemove.get(0));
			SeniorCrates.getInstance().configManager.crates.update();	
		}

	}
	
	public CrateItem get(String id) {
		return this.items.stream().filter(i -> i.getIdentification().equals(id)).findFirst().get();
	}
	
	public void addItem(CrateItem item) {
		this.items.add(item);
	}
	
	
}
