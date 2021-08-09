
package com.scrates.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.scrates.config.ConfigManager;
import com.scrates.gui.Action;
import com.scrates.gui.Confirmation;
import com.scrates.gui.GUI;
import com.scrates.main.SeniorCrates;
import com.scrates.utils.MessageUtils;

public class Open extends SubCommand {

    @Override
    public void onCommand(Player player, String[] args) {
    	
    	if(args.length != 2) {
    		MessageUtils.sendMessage(player, "&c/crate open {name}");
    		return;
    	}
    	
    	String name = args[1].trim();
    	
    	GUI confirmation = new Confirmation(player, new Action() {
			
			@Override
			public void reject() {
				SeniorCrates.getInstance().configManager.messages.send(player, "open_rejected");
			}
			
			@Override
			public void accept() {
				ConfigManager configManager = SeniorCrates.getInstance().configManager;
				
				if(configManager.crates.exists(name)) {
					configManager.messages.send(player, "open_completed");
					
					GUI open = new com.scrates.gui.Open(player, configManager.crates.getCrate(name));
					open.open();
					
					
					
				}else {
					configManager.messages.send(player, "open_error_doesntExists");
				}
				
			}
			
		});
    	
    	confirmation.open();
    	
    	
    	
    }

    @Override

    public String name() {
        return "open";
    }

    @Override
    public String info() {
        return "Opens a certain.";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }

	@Override
	public String permission() {
		return  SeniorCrates.getInstance().configManager.permissions.basic;
	}
	
	@Override
	public AutoComplete autoComplete(CommandSender sender) {
		AutoComplete tabCompleter = new AutoComplete();
		return tabCompleter;
	}
	

}