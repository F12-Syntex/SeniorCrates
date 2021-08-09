package com.scrates.gui;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.scrates.data.Crate;
import com.scrates.data.CrateItem;
import com.scrates.itembuilder.ItemGenerator;
import com.scrates.main.SeniorCrates;
import com.scrates.utils.Communication;
import com.scrates.utils.Input;
import com.scrates.utils.MessageUtils;
import com.scrates.utils.Numbers;

public class Edit extends GUI{

	private Crate crate;
	
	private Map<Integer, CrateItem> originalItems;
	
	public Edit(Player player, Crate crate) {
		super(player, (((crate.getItems().size()/9)+1) > 5 ? 5*9 : ((crate.getItems().size()/9)+1)*9));
		this.crate = crate;
		this.originalItems = new HashMap<Integer, CrateItem>();
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return MessageUtils.translateAlternateColorCodes("&6Edit");
	}

	@Override
	public String permission() {
		// TODO Auto-generated method stub
		return SeniorCrates.getInstance().configManager.permissions.basic;
	}

	
	@Override
	public int size() {
		return 54;
	}

	@Override
	public Sound sound() {
		// TODO Auto-generated method stub
		return Sound.values()[0];
	}

	@Override
	public float soundLevel() {
		// TODO Auto-generated method stub
		return 0f;
	}

	@Override
	public boolean canTakeItems() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onClickInventory(InventoryClickEvent e) {
		
		ItemStack selected = e.getCurrentItem().clone();
		if(selected.getType() == Material.AIR) return;

		boolean userInventory = e.getRawSlot() >= this.inv.getSize();
		double chance = 0;
		
		if(!userInventory) {
			CrateItem item = this.originalItems.get(e.getSlot());
			selected = item.getItem().clone();
			chance = this.originalItems.get(e.getSlot()).getChance();
		}
		
		if(e.getClick() == ClickType.RIGHT) {
			crate.remove(selected);
			SeniorCrates.getInstance().configManager.crates.update();
			this.Contents(inv);
		}
		
		if(e.getClick() == ClickType.LEFT) {
			
			if(crate.getItems().size() >= SeniorCrates.getInstance().configManager.settings.maxItemPerCrate) {
				SeniorCrates.getInstance().configManager.messages.send(player, "edit_error_setchance_cratefull");
				return;
			}
			
			crate.addItem(new CrateItem(crate.getItems().size()+"", selected, chance));
			SeniorCrates.getInstance().configManager.crates.update();
			this.Contents(inv);
		}
		
		if((e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT) && this.originalItems.containsKey(e.getRawSlot())) {
			
			final CrateItem item = this.originalItems.get(e.getRawSlot());
			
			player.closeInventory();
			
			SeniorCrates.getInstance().configManager.messages.send(player, "edit_setchance");
	
			Communication.applyInput(player, new Input() {
				
				@Override
				public void onRecieve(String i) {
					
					if(!Numbers.isDouble(i)) {
						SeniorCrates.getInstance().configManager.messages.send(player, "edit_error_setchance_notNumber");
						return;
					}
					
					double chance = Double.valueOf(i);
					
					if(chance > 1) chance = 1;
					if(chance < 0) chance = 0;
					chance = (Math.round(chance*1000.0)/1000.0);
					
					item.setChance(chance);
					
					player.sendMessage(i);
					player.closeInventory();
					GUI edit = new Edit(player, crate);
					edit.open();
					
					SeniorCrates.getInstance().configManager.messages.send(player, "edit_setchance_sucess");
					SeniorCrates.getInstance().configManager.crates.update();
					return;
				
				}
				
			}, 60000);
		}
		
	}

	@Override
	public void onOpenInventory(InventoryOpenEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCloseInventory(InventoryCloseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Contents(Inventory inv) {
		
		inv.clear();
		originalItems.clear();
		
		int slot = 0;
		
		int size = (((crate.getItems().size()/9)+1) > 5 ? 5*9 : ((crate.getItems().size()/9)+1)*9);
		
		if(size != this.inv.getSize()) {
			player.closeInventory();
			GUI edit = new Edit(player, crate);
			edit.open();
			return;
		}
		
		for(CrateItem i : this.crate.getItems()) {
			
			final ItemStack item = ItemGenerator.getInstance(i.getItem().clone())
			.addLore("&6Chance: &b" + (i.getChance()*100) + "%",
			"", "&eRIGHT CLICK &7to &eREMOVE", "&eLEFT CLICK &7to &eADD", "&eSHIFT+CLICK &7to &eSET CHANCE").get();
			
			this.inv.setItem(slot, item);
			
			originalItems.put(slot, i);
			
			slot++;
		}
	}


}
