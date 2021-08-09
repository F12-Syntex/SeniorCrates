package com.scrates.config;

public class Settings extends Config{

	private final int MAX_ITEMS_HARD_CAP = 45;
	
	public int maxItemPerCrate = 45;
	public long crateOpenTimer = 5L;
	
	public int crates = 5;
	public int delay = 10;
	public int reset = 86400;
	
	public Settings(String name, double version) {
		super(name, version);
		this.items.add(new ConfigItem("Settings.Crate.maxItems", maxItemPerCrate));
		this.items.add(new ConfigItem("Settings.Crate.openRate", crateOpenTimer));
		
		this.items.add(new ConfigItem("Settings.Crate.open.maxPerDay", crates));
		this.items.add(new ConfigItem("Settings.Crate.open.delay", delay));
		this.items.add(new ConfigItem("Settings.Crate.open.reset", reset));
		
	}

	@Override
	public void initialize() {
		this.maxItemPerCrate = this.getConfiguration().getInt("Settings.Crate.maxItems");
		this.crateOpenTimer = this.getConfiguration().getLong("Settings.Crate.openRate");
		
		this.crates = this.getConfiguration().getInt("Settings.Crate.open.maxPerDay");
		this.delay = this.getConfiguration().getInt("Settings.Crate.open.delay");
		this.reset = this.getConfiguration().getInt("Settings.Crate.open.reset");
		
		
		if(this.maxItemPerCrate > this.MAX_ITEMS_HARD_CAP) this.maxItemPerCrate = this.MAX_ITEMS_HARD_CAP; 
	}


	
}
