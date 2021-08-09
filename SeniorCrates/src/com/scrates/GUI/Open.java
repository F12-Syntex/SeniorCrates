package com.scrates.GUI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import com.scrates.data.Crate;
import com.scrates.data.CrateItem;
import com.scrates.itembuilder.GlassPaneItem;
import com.scrates.main.SeniorCrates;
import com.scrates.tags.PlaceHolders;
import com.scrates.utils.DyeUtils;
import com.scrates.utils.MessageUtils;

public class Open extends GUI{

	private boolean running;
	private BukkitTask schedular;
	

	private int firstAvailableSlot = 0;
	private int lastAvailableSlot = this.size() - 1;
	
	private Crate crate;
	
	private final CrateItem winningItem;
	
	public Open(Player player, Crate crate) {
		super(player);
		// TODO Auto-generated constructor stub
		this.running = true;
		
		this.schedular = Bukkit.getScheduler().runTaskTimer(SeniorCrates.getInstance(), () -> {
			this.Contents(inv);
		}, 0L, SeniorCrates.getInstance().configManager.settings.crateOpenTimer);
		
		this.setCrate(crate);
		
		this.winningItem = this.crate.getWinningItem();
		
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return MessageUtils.translateAlternateColorCodes("&7Drawing an item...");
	}

	@Override
	public String permission() {
		// TODO Auto-generated method stub
		return SeniorCrates.getInstance().configManager.permissions.basic;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 45;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onOpenInventory(InventoryOpenEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCloseInventory(InventoryCloseEvent e) {
		if(running) {
			this.open();
		}
	}

	@Override
	public void Contents(Inventory inv) {
	
		if(!running) {
			
			this.schedular.cancel();
			
			Bukkit.getScheduler().runTaskLater(SeniorCrates.getInstance(),() -> {
				player.closeInventory();
				
				PlaceHolders placeHolders = new PlaceHolders();
				
				ItemStack winning = this.winningItem.getItem();
				
				String name = winning.getType().name();
				double chance = winningItem.getChance();
				
				if(winning.hasItemMeta()) {
					if(winning.getItemMeta().hasDisplayName()) {
						name = winning.getItemMeta().getDisplayName();
					}
				}
				
				if(winning.getType() == Material.AIR) {
					name = "&cNothing!";
				}
				
				placeHolders.addPlaceholder("%item_name%", name);
				placeHolders.addPlaceholder("%item_chance%", (chance*100.0) + "");
				
				
				if(winning.getType() == Material.AIR) {
					SeniorCrates.getInstance().configManager.messages.send(player, "open_item_won_nothing", placeHolders.getPlaceholders());	
				}else {
					SeniorCrates.getInstance().configManager.messages.send(player, "open_item_won", placeHolders.getPlaceholders());
					if(player.getInventory().firstEmpty() != -1) {
						player.getInventory().addItem(winning);
					}else {
						player.getLocation().getWorld().dropItemNaturally(player.getLocation(), winning);
					}
				}
				
			}, 20L);
			return;
		}
		
		if(firstAvailableSlot == lastAvailableSlot) {
			inv.setItem(firstAvailableSlot, this.winningItem.getItem());
			this.running = false;
			return;
		}
		
		inv.setItem(firstAvailableSlot, this.getRandomGlassPane());
		inv.setItem(lastAvailableSlot, this.getRandomGlassPane());
	
		this.firstAvailableSlot++;
		this.lastAvailableSlot--;
		
		
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public BukkitTask getSchedular() {
		return schedular;
	}

	public void setSchedular(BukkitTask schedular) {
		this.schedular = schedular;
	}
	
	public ItemStack getRandomGlassPane() {
		
		DyeColor randomColour = DyeUtils.getRandomDyeColour();
	
		ChatColor ColourCode = DyeUtils.getColourFromDye(randomColour);
		
		ItemStack randomGlassPane = GlassPaneItem.getInstance(randomColour).setName(MessageUtils.translateAlternateColorCodes(ColourCode + "...")).get();
		
		return randomGlassPane;
	}

	public Crate getCrate() {
		return crate;
	}

	public void setCrate(Crate crate) {
		this.crate = crate;
	}

	public int getFirstAvailableSlot() {
		return firstAvailableSlot;
	}

	public void setFirstAvailableSlot(int firstAvailableSlot) {
		this.firstAvailableSlot = firstAvailableSlot;
	}

	public int getLastAvailableSlot() {
		return lastAvailableSlot;
	}

	public void setLastAvailableSlot(int lastAvailableSlot) {
		this.lastAvailableSlot = lastAvailableSlot;
	}

	public CrateItem getWinningItem() {
		return winningItem;
	}
	
	public void setState(Open open) {
		this.firstAvailableSlot = open.getFirstAvailableSlot();
		this.lastAvailableSlot = open.getLastAvailableSlot();
		
		open.Contents(this.inv);	
	}

}
