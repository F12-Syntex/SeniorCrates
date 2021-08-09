package com.scrates.config;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import com.scrates.data.Crate;
import com.scrates.data.CrateItem;
import com.scrates.itembuilder.ItemGenerator;

public class Crates extends Config{

	public List<Crate> crates = new ArrayList<Crate>();

	public Crates(String name, double version) {
		super(name, version);	

		this.items.add(new ConfigItem("Crates.Test.Object",  ItemGenerator.getInstance(Material.GOLD_INGOT)
				.addEnchant(Enchantment.DURABILITY, 1)
				.setName("&6One peice")
				.setLore("Gol D. Roger's secret stash the \"One piece\".")
				.addFlag(ItemFlag.HIDE_ENCHANTS).get()));
				
				
		
		String item1 = "1";
		String item2 = "2";
		
		this.items.add(new ConfigItem("Crates.Test.Items." + item1 + ".ItemStack", ItemGenerator.getInstance(Material.DIAMOND_SWORD)
			.addEnchant(Enchantment.DURABILITY, 10)
			.addEnchant(Enchantment.DAMAGE_ALL, 10)
			.addEnchant(Enchantment.LOOT_BONUS_MOBS, 10)
			.setName("&cBetsy")
			.setLore("An ancient sword lost by the old qing dynasty.")
			.addFlag(ItemFlag.HIDE_ENCHANTS).get()));
		
		this.items.add(new ConfigItem("Crates.Test.Items." + item1 +".Chance", 0.5));
		
		this.items.add(new ConfigItem("Crates.Test.Items." + item2 + ".ItemStack", ItemGenerator.getInstance(Material.APPLE)
				.setName("&cDevil fruit.")
				.setLore("A wild devil fruit that does nothing!")
				.addFlag(ItemFlag.HIDE_ENCHANTS).get()));
			
		this.items.add(new ConfigItem("Crates.Test.Items." + item2 + ".Chance", 0.5));
		
		
	}

	@Override
	public void initialize() {

		if(!this.getConfiguration().isConfigurationSection("Crates")) {
			return;
		}
		
		ConfigurationSection section = this.getConfiguration().getConfigurationSection("Crates");
		
		for(String crateName : section.getKeys(false)) {
			
			ConfigurationSection crateSection = section.getConfigurationSection(crateName);
			
			List<CrateItem> items = new ArrayList<CrateItem>();
			
			if(crateSection.isConfigurationSection("Items")) {
			
				ConfigurationSection crate = crateSection.getConfigurationSection("Items");
				
				for(String index : crate.getKeys(false)) {
					
					ConfigurationSection itemData = crate.getConfigurationSection(index);
					
					ItemStack itemstack = itemData.getItemStack("ItemStack");
					double chance = itemData.getDouble("Chance");
					
					CrateItem crateItem = new CrateItem(index, itemstack, chance);
					items.add(crateItem);
				}
				
			}
			
			ItemStack object = crateSection.getItemStack("Object");
			
			Crate crateInstance = new Crate(crateName, items, object);
			this.crates.add(crateInstance);
		}
		
		Bukkit.getLogger().info("Loaded " + this.crates.size() + " crates.");
	}

	public boolean registerCrate(String name, ItemStack hand) {
		if(!this.exists(name)) {
			Crate crate = new Crate(name, new ArrayList<CrateItem>(), hand);
			this.crates.add(crate);	
			this.update();
			return true;
		}
		return false;
	}
	
	public Crate getCrate(String name) {
		for(Crate crate : this.crates) {
			if(crate.getName().equals(name)) return crate;
		}
		return null;
	}
	
	public boolean exists(String name) {
		for(Crate crate : this.crates) {
			if(crate.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean delete(String name) {
		if(this.exists(name)) {
			Crate toRemove = this.getCrate(name);
			this.update();
			return this.crates.remove(toRemove);
		}else {
			return false;
		}
	}
	
	
	public void update() {
		
		ConfigurationSection section = this.getConfiguration().getConfigurationSection("Crates");
		
		for(Crate crate : this.crates) {
			
			int index = 1;

			section.set(crate.getName(), null);
			section.set(crate.getName() + ".Object", crate.getObject());
			
			for(CrateItem item : crate.getItems()) {

				section.set(crate.getName() + ".Items." + index + ".ItemStack", item.getItem());
				section.set(crate.getName() + ".Items." + index + ".Chance", item.getChance());
				
				index++;
			}
		}

		Bukkit.getLogger().info("Saved " + this.crates.size() + " crates.");
		this.save();
	}

	public List<Crate> getCrates() {
		return crates;
	}

	public void setCrates(List<Crate> crates) {
		this.crates = crates;
	}
	
}
