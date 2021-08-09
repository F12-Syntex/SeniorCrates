package com.scrates.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.scrates.config.Messages;
import com.scrates.data.Crate;
import com.scrates.gui.Open;
import com.scrates.main.SeniorCrates;
import com.scrates.placeholder.time.TimeFormater;
import com.scrates.tags.PlaceHolders;
import com.scrates.userprofiles.Profile;
import com.scrates.userprofiles.ProfileResponse;

public class CrateUse extends SubEvent{

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return "input handler";
	}
	
	@Override
	public String description() {
		// TODO Auto-generated method stub
		return "handles input for components";
	}
	
	@EventHandler
	public void onCrateUse(PlayerInteractEvent e) {

		Player player = e.getPlayer();
		ItemStack hand = player.getInventory().getItemInHand().clone();
		
		for(Crate crate : SeniorCrates.getInstance().configManager.crates.getCrates()) {
			
			ItemStack crateCompare = crate.getObject().clone();
			ItemStack handCompare = hand.clone();
			
			crateCompare.setAmount(1);
			handCompare.setAmount(1);
			
			TimeFormater timeFormater = new TimeFormater();
			
			if(crateCompare.serialize().equals(handCompare.serialize())) {
				
				Profile profile = SeniorCrates.getInstance().userProfiles.getAccount(player.getUniqueId());
				Messages message = SeniorCrates.getInstance().configManager.messages;
				
				ProfileResponse crateUsageResponse = profile.UseCrate();
				
				if(crateUsageResponse == ProfileResponse.CRATES_OUT) {
					
					
					PlaceHolders placeHolders = new PlaceHolders();
					placeHolders.addPlaceholder("%cooldown%", timeFormater.parse(SeniorCrates.getInstance().userProfiles.timeTillNewDay()/1000)+"");
					
					message.send(player, "open_error_crates_max_usage", placeHolders.getPlaceholders());
					return;
				}
				
				if(crateUsageResponse == ProfileResponse.TIMER) {
					
					PlaceHolders placeHolders = new PlaceHolders();
					placeHolders.addPlaceholder("%cooldown%", timeFormater.parse(profile.getTimer()));
					
					message.send(player, "open_error_timer", placeHolders.getPlaceholders());
					return;
				}

				
				PlaceHolders placeHolders = new PlaceHolders();
				placeHolders.addPlaceholder("%crates%", profile.getCrates()+"");
				
				message.send(player, "open_completed", placeHolders.getPlaceholders());
				
				int left = hand.getAmount() - 1;
				
				if(left == 0) {
					e.getPlayer().getInventory().setItemInHand(new ItemStack(Material.AIR));
				}else {
					hand.setAmount(left);
					e.getPlayer().getInventory().setItemInHand(hand);
				}
				
				Open open = new Open(player, crate);
				open.open();
				
			}	
		}
		
		
	}

}
