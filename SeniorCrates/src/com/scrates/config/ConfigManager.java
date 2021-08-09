package com.scrates.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigManager {

    public ArrayList<Config> config = new ArrayList<Config>();
	
    public Messages messages;
    public Permissions permissions;
    public Cooldown cooldown;
    public Settings settings;
    public Crates crates;
    public Database database;
    
    public void setup(Plugin plugin) {
    	
    	this.messages = new Messages("messages", 1.7);
    	this.permissions = new Permissions("permissions", 1.7);
    	this.cooldown = new Cooldown("cooldown", 1.4);
    	this.settings = new Settings("settings", 1.4);
    	this.crates = new Crates("crates", 1.4);
    	this.database = new Database("database", 1.0);
    	
    	this.config.clear();

    	this.config.add(messages);
    	this.config.add(permissions);
    	this.config.add(cooldown);
    	this.config.add(settings);
    	this.config.add(crates);
    	this.config.add(database);
    	
    	this.configure(plugin, config);
    }

    public void configure(Plugin plugin, List<Config> configs) {
    	for(int i = 0; i < config.size(); i++) {
    		
    		Config specifiedConfig = configs.get(i);
    		
    		specifiedConfig.setup();
    		
    		if(specifiedConfig.getConfiguration().contains("identity.version") && (specifiedConfig.getConfiguration().getDouble("identity.version") == specifiedConfig.getVerison())) {
    			specifiedConfig.initialize();
        		continue;
    		}
    		
	    		File file = specifiedConfig.backup();
	    		File toDelete = new File(plugin.getDataFolder(), specifiedConfig.getName() + ".yml");
	    		toDelete.delete();
	    		
	    		plugin.saveResource(specifiedConfig.getName() + ".yml", false);

       			if(file != null) {
   					final FileConfiguration oldConfig = YamlConfiguration.loadConfiguration(file);
   					
   					final int index = i;
   		       		
   					oldConfig.getKeys(true).forEach(o -> {
   						if(config.get(index).getConfiguration().contains(o)) {
   							config.get(index).getConfiguration().set(o, oldConfig.get(o));
   						}
   					});
   		    
   					specifiedConfig.setDefault();
   					
   		    		config.get(index).getConfiguration().set("identity.version", specifiedConfig.getVerison());
   		    		config.get(index).save();
   		    		config.get(index).initialize();
   		    		
   				}

    	}
    }
    

}
