
package com.scrates.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.scrates.main.SeniorCrates;

public class Developer extends SubCommand {

    @Override
    public void onCommand(Player player, String[] args) {
    	
    }

    @Override

    public String name() {
        return "developer";
    }

    @Override
    public String info() {
        return "testing command";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }

	@Override
	public String permission() {
		return SeniorCrates.getInstance().configManager.permissions.reload;	
	}
	
	@Override
	public AutoComplete autoComplete(CommandSender sender) {
		AutoComplete tabCompleter = new AutoComplete();
		return tabCompleter;
	}
	

}