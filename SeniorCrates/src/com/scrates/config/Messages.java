package com.scrates.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import com.scrates.utils.ComponentBuilder;
import com.scrates.utils.MessageUtils;

public class Messages extends Config{

	public String prefix = "&c[&6SeniorCrates&c]";
	public String error = "%prefix% sorry an error has accured!";;
	public String invalid_syntax = "%prefix% &cInvalid syntax";
	public String invalid_permission = "%prefix% &cYou cant do that!";
	public String invalid_entitiy = "%prefix% &cplayers only!";
	public String invalid_help_command = "%prefix% &c%command% is not a command!";
	public String invalid_configure_command = "%prefix% &c%config% is not a valid config!";
	public List<String> help_format = ComponentBuilder.createLore("%prefix% &b%command%&7: &c%description%", "%prefix% &bpermissions&7: &c%permission%");

	//public String insufficient_balance = "%prefix% &6Sorry you dont have enough money!";
	//public String purchase_successful = "%prefix% &6Your pet has been added to your battle inventory!";
	
	public List<HashMap<String, String>> messages = new ArrayList<HashMap<String, String>>();
	
	public Map<Integer, String> key = new HashMap<Integer, String>();
	public List<String> data = new ArrayList<String>();

	public Messages(String name, double version) {
		super(name, version);
		
		this.items.add(new ConfigItem("Messages.prefix", prefix));
		this.items.add(new ConfigItem("Messages.error", error));
		this.items.add(new ConfigItem("Messages.invalid_syntax", invalid_syntax));
		this.items.add(new ConfigItem("Messages.invalid_permission", invalid_permission));
		this.items.add(new ConfigItem("Messages.invalid_entitiy", invalid_entitiy));
		this.items.add(new ConfigItem("Messages.help.invalid_command", invalid_help_command));
		this.items.add(new ConfigItem("Messages.configure.invalid_command", invalid_configure_command));
		this.items.add(new ConfigItem("Messages.help.command_help_format", help_format));
		
		
		//plugin_reload
		this.updateMessages("plugin_reload", "%prefix% &creloaded!");
		
		this.updateMessages("creation_rejected", "%prefix% &cYou have canceled the creation request.");
		this.updateMessages("creation_completed", "%prefix% &aTask completed!");
		this.updateMessages("creation_error_hand", "%prefix% &cYou don't have anything in your hand.");
		this.updateMessages("creation_error_exists", "%prefix% &cCrate already exists!");
		
		this.updateMessages("give_error_user", "%prefix% &cThis player is not online!");
		this.updateMessages("give_error_crate", "%prefix% &cThis crate does not exist!");
		this.updateMessages("give_error_amount", "%prefix% &cInvalid amount!");
		this.updateMessages("give_error_inventoryfull", "%prefix% &cYour inventory is full!");
		this.updateMessages("give_complete", "%prefix% &aYou have recieved &7%amount%x &a%crate%");

		this.updateMessages("deletion_rejected", "%prefix% &cYou have canceled the deletion request.");
		this.updateMessages("deletion_completed", "%prefix% &aTask completed!");
		this.updateMessages("deletion_error_notexists", "%prefix% &cCrate does not exist!");
		
		this.updateMessages("edit_error_notexists", "%prefix% &cCrate does not exist!");
		this.updateMessages("edit_error_setchance_notNumber", "%prefix% &cThats not a valid chance, please provide a decimal number.");
		this.updateMessages("edit_error_setchance_cratefull", "%prefix% &cThe crate is full!");
		this.updateMessages("edit_setchance", "%prefix% &aPlease type in the chance.");
		this.updateMessages("edit_setchance_sucess", "%prefix% &aChance has been set!");
		
		this.updateMessages("open_rejected", "%prefix% &cYou have canceled the open creation request.");
		this.updateMessages("open_completed", "%prefix% &aOpening crate! You have %crates% left for today.");
		this.updateMessages("open_error_doesntExists", "%prefix% &cCrate doesnt exists!");
		this.updateMessages("open_error_timer", "%prefix% &cPlease wait %cooldown%");
		this.updateMessages("open_error_crates_max_usage", "%prefix% &cYou have used your daily limit of crates. Please wait %cooldown% to use more!");
		
		
		this.updateMessages("open_item_won", "%prefix% &cYou won &7%item_name%&c with a &7%item_chance%% &cwin chance!");
		this.updateMessages("open_item_won_nothing", "%prefix% &cYou won nothing.");
		
		
		//		this.updateMessages("edit_setchance_error_notNumber", "%prefix% &cThats not a valid chance, please provide a decimal number.");
		
		this.items.add(new ConfigItem("Messages.contents", messages));
		
	}
	
	private void updateMessages(String key, String data) {
		HashMap<String, String> message = new HashMap<String, String>();
		message.put(key, data);
		this.messages.add(message);
	}
	
	@Override
	public void initialize() {
		this.prefix = MessageUtils.translateAlternateColorCodes(this.getConfiguration().getString("Messages.prefix"));
		this.error = MessageUtils.translateAlternateColorCodes(this.getConfiguration().getString("Messages.error").replace("%prefix%", prefix));
		this.invalid_syntax = MessageUtils.translateAlternateColorCodes(this.getConfiguration().getString("Messages.invalid_syntax").replace("%prefix%", prefix));
		this.invalid_permission = MessageUtils.translateAlternateColorCodes(this.getConfiguration().getString("Messages.invalid_permission").replace("%prefix%", prefix));
		this.invalid_entitiy = MessageUtils.translateAlternateColorCodes(this.getConfiguration().getString("Messages.invalid_entitiy").replace("%prefix%", prefix));
		this.invalid_help_command = MessageUtils.translateAlternateColorCodes(this.getConfiguration().getString("Messages.help.invalid_command").replace("%prefix%", prefix));
		this.help_format = ComponentBuilder.createLore(this.getConfiguration().getStringList("Messages.help.command_help_format"));
		this.invalid_configure_command = MessageUtils.translateAlternateColorCodes(this.getConfiguration().getString("Messages.configure.invalid_command").replace("%prefix%", prefix));
	
		List<Map<?, ?>> mapping = this.getConfiguration().getMapList("Messages.contents");
		
		for(Map<?, ?> i : mapping) {
			String key = String.valueOf(i.keySet().stream().findFirst().get());
			String data = i.get(key).toString();
			this.data.add(MessageUtils.translateAlternateColorCodes(data).replace("%prefix%", prefix));
			int index = (this.data.size() - 1);
			this.key.put(index, key);
		}
		
	
	}
	
	
	public void send(Player player, String key) {
		MessageUtils.sendRawMessage(player, this.getMessage(key));
	}
	
	public void send(Player player, String key, Map<String, String> placeholders) {
		
		String messageToSend = this.getMessage(key);
		
		for(String replace : placeholders.keySet()) {
			messageToSend = messageToSend.replace(replace, placeholders.get(replace));
		}
		
		MessageUtils.sendRawMessage(player, messageToSend);
	}
	
	public String getMessage(String key) {
		
		for(Integer i : this.key.keySet()) {
			if(this.key.get(i).equalsIgnoreCase(key)) {
				return this.data.get(i);
			}
		}

		String data = this.prefix + " &cPlease update your config for the setting &7[&c" + key + "&7]";
		
		HashMap<String, String> message = new HashMap<String, String>();
		message.put(key, data);
		List<Map<?, ?>> mapping = this.getConfiguration().getMapList("Messages.contents");
		mapping.add(message);
		this.getConfiguration().set("Messages.contents", mapping);
		this.save();
		
		this.data.add(data);
		this.key.put(this.key.size(), key);
		
		return MessageUtils.translateAlternateColorCodes(data);
	}

	
}
