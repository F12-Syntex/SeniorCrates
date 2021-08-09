package com.scrates.gui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.scrates.cooldown.CooldownUser;
import com.scrates.itembuilder.GlassPaneItem;
import com.scrates.main.SeniorCrates;
import com.scrates.tags.TagFactory;
import com.scrates.utils.MessageUtils;

public abstract class PagedGUI implements Listener{

	protected Player player;
	protected Inventory inv;
	
	public int page;
	
	private Map<Integer, Runnable> execution = new HashMap<Integer, Runnable>();
	
	public PagedGUI(Player player) {
		this.page = 1;
		this.player = player;
		SeniorCrates.getInstance().getServer().getPluginManager().registerEvents(this, SeniorCrates.getInstance());
		this.inv = Bukkit.createInventory(player, size(), name());		
	}
	
	@EventHandler()
	public void onOpen(InventoryOpenEvent e) {
		if(e.getPlayer().getUniqueId() != this.player.getUniqueId()) return;
		if(!e.getPlayer().hasPermission(permission())) {
			MessageUtils.sendRawMessage(player, SeniorCrates.getInstance().configManager.messages.invalid_permission);
			e.setCancelled(true);
			return;
		}
		onOpenInventory(e);
	}
	
	@EventHandler()
	public void onClose(InventoryCloseEvent e) {
		if(e.getPlayer().getUniqueId() != this.player.getUniqueId()) return;
		this.onCloseInventory(e);
		HandlerList.unregisterAll(this);
	}
	
	@EventHandler()
	public void onClick(InventoryClickEvent e) {
		
		if(e.getWhoClicked().getUniqueId() != this.player.getUniqueId()) return;
		
		e.setCancelled(!canTakeItems());
		if(e.getCurrentItem() == null) {
			return;
		}
		this.onClickInventory(e);
		
		if(e.getSlot() == 45) {
			this.front();
			return;
		}
		
		if(e.getSlot() == 53) {
			this.back();
			return;
		}
		
		int index = e.getSlot() + ((this.page-1) * 36);
		
		if(this.execution.containsKey(index) && e.getSlot() < 36) {
			this.execution.get(index).run();
		}
		
		for(SpecialItem i : this.SpecialContents()) {
			if(e.getSlot() == i.getSlot()) {
				i.getExecution().run();
			}
		}
		
	}
	
	public abstract String name();
	public abstract String permission();
	
	public abstract int size();
	
	public abstract Sound sound();
	public abstract float soundLevel();
	public abstract boolean canTakeItems();
	
	public abstract void onClickInventory(InventoryClickEvent e);
	public abstract void onOpenInventory(InventoryOpenEvent e);
	public abstract void onCloseInventory(InventoryCloseEvent e);
	public abstract List<PagedItem> Contents();
	public abstract List<SpecialItem> SpecialContents();
	
	
	
	public void back() {
		

		int maxPage = this.Contents().size()/35;
		if(this.Contents().size() % 35 > 0) {
			maxPage++;
		}
		if(this.page < maxPage) {
		
		CooldownUser user = SeniorCrates.getInstance().cooldownManager.getUser(player.getUniqueId());
		
		int newClicksPerSecond = user.getClicksParSecond() + 1;
		
		user.setClicksParSecond(newClicksPerSecond);
		
		int timer = user.getTime("next");
    	
		if(user.getClicksParSecond() > 5) {
			this.player.closeInventory();
			user.reset("open", 60);
			
        	TagFactory tagHelper = TagFactory.instance("%prefix% &cYou have been banned from using that command for %cooldown% due to spam!");
            
        	tagHelper.setCooldown(60);
        	
        	MessageUtils.sendRawMessage(player, tagHelper.parse());
			
        	return;
        	
		}
		
    	if(timer <= 0) {
    	
			this.page++;
			this.refresh();
			user.reset("next");
			
			
    	}else {
        	
        	TagFactory tagHelper = TagFactory.instance("%prefix% &cPlease wait %cooldown%&7 before going to the next page!");
        
        	tagHelper.setCooldown(timer);
        	
        	MessageUtils.sendRawMessage(player, tagHelper.parse());
        }
	}
		
	}
	
	public void front() {
		
		if(this.page > 1) {
		
		CooldownUser user = SeniorCrates.getInstance().cooldownManager.getUser(player.getUniqueId());
		
		int newClicksPerSecond = user.getClicksParSecond() + 1;
		
		user.setClicksParSecond(newClicksPerSecond);
		
		if(user.getClicksParSecond() > 5) {
			this.player.closeInventory();
			user.reset("open", 60);
			
        	TagFactory tagHelper = TagFactory.instance("%prefix% &cYou have been banned from using that command for %cooldown% due to spam!");
            
        	tagHelper.setCooldown(60);
        	
        	MessageUtils.sendRawMessage(player, tagHelper.parse());
			
        	return;
        	
		}
		
		int timer = user.getTime("back");
    	
    	if(timer <= 0) {
    	
			this.page--;
			this.refresh();
			user.reset("back");
			
    	}else {
        	
        	TagFactory tagHelper = TagFactory.instance("%prefix% &cPlease wait %cooldown%&7 before going to the previous page!");
        
        	tagHelper.setCooldown(timer);
        	
        	MessageUtils.sendRawMessage(player, tagHelper.parse());
        }
		}
	}
	
	public void open() {
		player.getWorld().playSound(player.getLocation(), sound(), soundLevel(), soundLevel());
		this.refresh();
		player.openInventory(inv);
	}

	public void refresh() {
		
		Bukkit.getServer().getScheduler().runTask(SeniorCrates.getInstance(), new Runnable() {
		    public void run() {

				inv.clear();
				ItemStack blackPane = GlassPaneItem.getInstance(DyeColor.BLACK).setName("").get();
				fillEmpty(blackPane);		
			
				final List<PagedItem> contents = Contents();
				final List<SpecialItem> special = SpecialContents();
				
				int maxPage = contents.size()/35;
				
				if(contents.size() % 35 > 0) {
					maxPage++;
				}
				
				for(int i = 0; i < 36; i++) {
					int index = i + ((page-1) * 36);
					if(index < contents.size()) {
						PagedItem item = contents.get(index);
						inv.setItem(i, contents.get(index).getItem());
						execution.put(index, item.getExecution());
					}
				}
			
				for(int i = 36; i < 45; i++) {
					ItemStack emptyPane = GlassPaneItem.getInstance(DyeColor.WHITE).setName("").get();
					inv.setItem(i, emptyPane);
				}
				for(int i = 45; i < 54; i++) {

					boolean flag = false;
					for(SpecialItem o : special) {
						if(o.getSlot() == i) {
							inv.setItem(i, o.getItem());
							flag = true;
						}
					}

					if(!flag) {
						ItemStack emptyPane = new ItemStack(Material.AIR, 1);
						inv.setItem(i, emptyPane);	
					}
					
				}
		
				
				if(page > 1) {
					ItemStack back =  GlassPaneItem.getInstance(DyeColor.GREEN).setName("&3Takes you to page " + (page-1)).get();
					inv.setItem(45, back);
				}else {
					ItemStack back = GlassPaneItem.getInstance(DyeColor.RED).setName("&cThis is the first page!").get();
					inv.setItem(45, back);	
				}
				
				if(page < maxPage) {
					ItemStack next = GlassPaneItem.getInstance(DyeColor.GREEN).setName("&3Takes you to page " + (page+1)).get();
					inv.setItem(53, next);		
				}else {
					ItemStack next = GlassPaneItem.getInstance(DyeColor.RED).setName("&cThis is the last page!").get();
					inv.setItem(53, next);	
				}
		    }
		});
			
	}
	
	public void addItem(int index, ItemStack item) {
		inv.setItem(index, item);
	}
	
	public void fillEmpty(ItemStack stack) {
		for(int i = 0; i < this.size(); i++) {
			if(this.inv.getItem(i) == null) {
				this.inv.setItem(i, stack);
			}
		}
	}
	
	
}
