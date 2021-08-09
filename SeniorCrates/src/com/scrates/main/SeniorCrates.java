package com.scrates.main;
import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.scrates.config.ConfigManager;
import com.scrates.cooldown.CooldownManager;
import com.scrates.cooldown.CooldownTick;
import com.scrates.events.EventHandler;
import com.scrates.userprofiles.UserProfiles;
import com.scrates.utils.MessageUtils;


public class SeniorCrates extends JavaPlugin implements Listener{


    private static SeniorCrates instance;
    public com.scrates.commands.CommandManager CommandManager;
    public ConfigManager configManager;
    public EventHandler eventHandler;
    public CooldownManager cooldownManager;
    public CooldownTick cooldownTick;
	public File ParentFolder;
	
	public ServerVersion version;
	public UserProfiles userProfiles;
	
	@Override
	public void onEnable(){
		
		SeniorCrates.instance = this;
		
		ParentFolder = getDataFolder();
	    
	    this.configManager = new ConfigManager();
	    this.configManager.setup(this);
	    
	    eventHandler = new EventHandler();
	    eventHandler.setup();
	    
	    this.cooldownManager = new CooldownManager();

	    this.cooldownTick = new CooldownTick();
	    this.cooldownTick.schedule();

	    this.CommandManager = new com.scrates.commands.CommandManager();
	    this.CommandManager.setup(this);
	    
	    this.version = new ServerVersion();
	    this.userProfiles = new UserProfiles();
	    
	    try {
		    this.userProfiles.connect();	
	    }catch (Exception e) {
	    	e.printStackTrace();
	    	MessageUtils.sendConsoleMessage("&c&lPLEASE UPDATE YOUR \"DATABASE.YML\" FILE TO CONTINUE USING THE PLUGIN.");
	    	Bukkit.getServer().shutdown();
	    }
	    
	}
	
	
	@Override
	public void onDisable(){
		this.configManager.crates.update();
		this.userProfiles.disconnect();
		this.eventHandler = null;
		HandlerList.getRegisteredListeners(this);
	}
	
	public static void Log(String msg){
		  Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&c" + SeniorCrates.getInstance().getName() + "&7] &c(&7LOG&c): " + msg));
	}
	

	public void reload() {
		this.configManager = new ConfigManager();
		this.configManager.setup(this);
	}
		

	public static SeniorCrates getInstance() {
		return instance;
	}
	
}
