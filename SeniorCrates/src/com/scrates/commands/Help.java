
package com.scrates.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.scrates.main.SeniorCrates;
import com.scrates.utils.MessageUtils;

public class Help extends SubCommand {

    @Override
    public void onCommand(Player player, String[] args) {
    	
    	if(args.length <= 1) {
    		MessageUtils.sendHelp(player);
    		return;
    	}
    	
    	SubCommand command = SeniorCrates.getInstance().CommandManager.get(args[1]);
    	
    	if(command == null) {
    		MessageUtils.sendMessage(player, SeniorCrates.getInstance().configManager.messages.invalid_help_command.replace("%command%", args[1]));
    		return;
    	}
    	
    	for(String i : SeniorCrates.getInstance().configManager.messages.help_format) {
    		MessageUtils.sendRawMessage(player, i.replace("%command%", args[1]).replace("%description%", command.info()).replace("%permission%", command.permission()).replace("%prefix%", SeniorCrates.getInstance().configManager.messages.prefix));
    	}
    	
    }

    @Override

    public String name() {
        return "help";
    }

    @Override
    public String info() {
        return "displays the help command";
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
		
		List<SubCommand> commands = SeniorCrates.getInstance().CommandManager.getCommands();
		
		for(SubCommand i : commands) {
			tabCompleter.createEntry(i.name());
		}
		
		return tabCompleter;
	}
	

}