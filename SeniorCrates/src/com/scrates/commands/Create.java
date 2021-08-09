
package com.scrates.commands;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.scrates.config.ConfigManager;
import com.scrates.gui.Action;
import com.scrates.gui.Confirmation;
import com.scrates.gui.GUI;
import com.scrates.main.SeniorCrates;
import com.scrates.utils.MessageUtils;

public class Create extends SubCommand {

    @Override
    public void onCommand(Player player, String[] args) {
    	
    	if(args.length != 2) {
    		MessageUtils.sendMessage(player, "&c/crate create {name}");
    		return;
    	}
    	
    	String name = args[1].trim();
    	
    	GUI confirmation = new Confirmation(player, new Action() {
			
			@Override
			public void reject() {
				SeniorCrates.getInstance().configManager.messages.send(player, "creation_rejected");
			}
			
			@Override
			public void accept() {
				if(player.getInventory().getItemInHand().getType() == Material.AIR){
					SeniorCrates.getInstance().configManager.messages.send(player, "creation_error_hand");
					return;
				}
				
				ConfigManager configManager = SeniorCrates.getInstance().configManager;
				
				if(configManager.crates.registerCrate(name, player.getItemInHand())) {
					configManager.messages.send(player, "creation_completed");
				}else {
					configManager.messages.send(player, "creation_error_exists");
				}
				
			}
			
		});
    	
    	confirmation.open();
    	
    	
    	
    }

    @Override

    public String name() {
        return "create";
    }

    @Override
    public String info() {
        return "Creates a certain crate.";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }

	@Override
	public String permission() {
		return  SeniorCrates.getInstance().configManager.permissions.create;
	}
	
	@Override
	public AutoComplete autoComplete(CommandSender sender) {
		AutoComplete tabCompleter = new AutoComplete();
		return tabCompleter;
	}
	

}