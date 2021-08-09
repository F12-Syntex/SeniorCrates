
package com.scrates.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.scrates.GUI.Action;
import com.scrates.GUI.Confirmation;
import com.scrates.GUI.GUI;
import com.scrates.config.ConfigManager;
import com.scrates.data.Crate;
import com.scrates.main.SeniorCrates;
import com.scrates.utils.MessageUtils;

public class Delete extends SubCommand {

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
				SeniorCrates.getInstance().configManager.messages.send(player, "deletion_rejected");
			}
			
			@Override
			public void accept() {
				ConfigManager configManager = SeniorCrates.getInstance().configManager;
				
				if(configManager.crates.delete(name)) {
					configManager.messages.send(player, "deletion_completed");
				}else {
					configManager.messages.send(player, "deletion_error_notexists");
				}
			}
			
		});
    	
    	confirmation.open();
    	
    	
    	
    }

    @Override

    public String name() {
        return "delete";
    }

    @Override
    public String info() {
        return "deletes a certain crate.";
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
		for(Crate crate : SeniorCrates.getInstance().configManager.crates.crates) {
			tabCompleter.createEntry(crate.getName());
		}
		return tabCompleter;
	}
	

}