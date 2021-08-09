package com.scrates.GUI;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import com.scrates.itembuilder.GlassPaneItem;
import com.scrates.itembuilder.WoolItem;
import com.scrates.main.SeniorCrates;
import com.scrates.utils.MessageUtils;

public class Confirmation extends GUI{

	private Action action;
	
	private boolean commit = false;
	
	public Confirmation(Player player, Action action) {
		super(player);
		// TODO Auto-generated constructor stub
		this.action = action;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return MessageUtils.translateAlternateColorCodes("&6Confirmation");
	}

	@Override
	public String permission() {
		// TODO Auto-generated method stub
		return SeniorCrates.getInstance().configManager.permissions.basic;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 27;
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
		
		if(e.getSlot() == 12) {
			Bukkit.getScheduler().runTask(SeniorCrates.getInstance(), () -> this.action.accept());
			this.commit = true;
			this.player.closeInventory();
			return;
		}
		
		if(e.getSlot() == 14) {
			Bukkit.getScheduler().runTask(SeniorCrates.getInstance(), () -> this.action.reject());
			this.commit = true;
			this.player.closeInventory();
			return;
		}
		
	}

	@Override
	public void onOpenInventory(InventoryOpenEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onCloseInventory(InventoryCloseEvent e) {
		if(!this.commit) {
			Bukkit.getScheduler().runTask(SeniorCrates.getInstance(), () -> this.action.reject());
		}
	}

	@Override
	public void Contents(Inventory inv) {
		this.fillEmpty(GlassPaneItem.getInstance(DyeColor.BLACK).get());
		
		inv.setItem(12, WoolItem.getInstance(DyeColor.GREEN).setName("&aAccept").setLore("&7This will accept current task.", "&7Click to confirm.").get());
		inv.setItem(14, WoolItem.getInstance(DyeColor.RED).setName("&cCancel").setLore("&7This will reject the current task.", "&7Click to confirm.").get());
		
	}

}
