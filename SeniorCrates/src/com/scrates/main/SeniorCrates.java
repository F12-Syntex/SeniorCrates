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


public class SeniorCrates extends JavaPlugin implements Listener{


    public static SeniorCrates instance;
    public com.scrates.commands.CommandManager CommandManager;
    public ConfigManager configManager;
    public EventHandler eventHandler;
    public CooldownManager cooldownManager;
    public CooldownTick cooldownTick;
	public File ParentFolder;
	
	@Override
	public void onEnable(){
		
		ParentFolder = getDataFolder();
	    instance = this;
		
	    configManager = new ConfigManager();
	    configManager.setup(this);
	    
	    this.reload();
	    
	    eventHandler = new EventHandler();
	    eventHandler.setup();
	    
	    this.cooldownManager = new CooldownManager();

	    this.cooldownTick = new CooldownTick();
	    this.cooldownTick.schedule();
	    
	    this.CommandManager = new com.scrates.commands.CommandManager();
	    this.CommandManager.setup(this);

	}
	
	
	@Override
	public void onDisable(){
		this.eventHandler = null;
		HandlerList.getRegisteredListeners(instance);
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
