
package com.scrates.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import com.scrates.config.ConfigManager;
import com.scrates.data.Crate;
import com.scrates.main.SeniorCrates;
import com.scrates.tags.TagFactory;
import com.scrates.utils.MessageUtils;
import com.scrates.utils.Numbers;

public class Give extends SubCommand {

    @Override
    public void onCommand(Player player, String[] args) {
    	
    	ConfigManager configManager = SeniorCrates.getInstance().configManager;
    	
    	if(args.length != 4) {
    		MessageUtils.sendMessage(player, "&c/crate give {player} {crate} {amount}");
    		return;
    	}
    	
    	String user = args[1];
    	String crate = args[2];
    	String amount = args[3];
    	
    	Player target = null;
    	
    	for(Player users : Bukkit.getOnlinePlayers()) {
    		if(users.getDisplayName().equals(user)) {
    			target = users;
    		}
    	}
    	
    	if(target == null) {
    		configManager.messages.send(player, "give_error_user");
    		return;
    	}
    	
    	if(!configManager.crates.exists(crate)) {
    		configManager.messages.send(player, "give_error_crate");
    		return;
    	}
    	
    	if(!Numbers.isRealNumber(amount)) {
    		configManager.messages.send(player, "give_error_amount");
    		return;
    	}
    	
    	Crate targetCrate = configManager.crates.getCrate(crate);
    	int targetAmount = Integer.parseInt(amount);
    	
    	if(player.getInventory().firstEmpty() == -1) {
    		configManager.messages.send(player, "give_error_inventoryfull");
    		return;
    	}
    	
    	ItemStack toAdd = targetCrate.getObject();
    	toAdd.setAmount(targetAmount);
    	
    	toAdd.getItemMeta().addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
    	toAdd.getItemMeta().addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    	
    	player.getInventory().addItem(toAdd);
    	
    	
    	String giveMessage = configManager.messages.getMessage("give_complete");
    	
    	TagFactory tagFactory = TagFactory.instance(player);
    	tagFactory.addMapping("%amount%", targetAmount+"");
    	tagFactory.addMapping("%crate%", targetCrate.getName());
    	
    	MessageUtils.sendRawMessage(player, tagFactory.playerParse(giveMessage));
    	
    	
    	
    	
    }

    @Override

    public String name() {
        return "give";
    }

    @Override
    public String info() {
        return "gives a player a crate.";
    }

    @Override
    public String[] aliases() {
        return new String[0];
    }

	@Override
	public String permission() {
		return SeniorCrates.getInstance().configManager.permissions.give;	
	}
	
	@Override
	public AutoComplete autoComplete(CommandSender sender) {
		AutoComplete tabCompleter = new AutoComplete();
		return tabCompleter;
	}
	

}