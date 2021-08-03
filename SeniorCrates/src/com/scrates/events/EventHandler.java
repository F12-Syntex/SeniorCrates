package com.scrates.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.Plugin;

import com.scrates.main.SeniorCrates;

public class EventHandler {

    public List<SubEvent> events = new ArrayList<SubEvent>();
	
    private Plugin plugin = SeniorCrates.instance;
    
	public void setup() {
		this.events.add(new InputHandler());
		this.events.forEach(i -> plugin.getServer().getPluginManager().registerEvents(i, plugin));
	}
	
}
