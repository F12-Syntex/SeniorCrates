
package com.scrates.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.scrates.config.ConfigManager;
import com.scrates.data.Crate;
import com.scrates.gui.GUI;
import com.scrates.main.SeniorCrates;
import com.scrates.utils.MessageUtils;

public class Edit extends SubCommand {

    @Override
    public void onCommand(Player player, String[] args) {
    	
    	ConfigManager configManager = SeniorCrates.getInstance().configManager;
    	
    	if(args.length != 2) {
    		MessageUtils.sendMessage(player, "&c/crate edit {crate}");
    		return;
    	}
    	
    	String crate = args[1];
    	
    	if(!configManager.crates.exists(crate)) {
    		configManager.messages.send(player, "edit_error_notexists");
    		return;
    	}
    	
    	Crate targetCrate = configManager.crates.getCrate(crate);
    	
    	GUI edit = new com.scrates.gui.Edit(player, targetCrate);
    	edit.open();
    	
    }

    @Override

    public String name() {
        return "edit";
    }

    @Override
    public String info() {
        return "edits a specific crate.";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }

	@Override
	public String permission() {
		return SeniorCrates.getInstance().configManager.permissions.edit;	
	}
	
	@Override
	public AutoComplete autoComplete(CommandSender sender) {
		AutoComplete tabCompleter = new AutoComplete();
		return tabCompleter;
	}
	

}