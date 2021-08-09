package com.scrates.config;

public class Permissions extends Config{

	public String shop = "bukkit.command.help";
	public String basic = "bukkit.command.help";
	public String admin = "seniorcrates.admin";
	public String commandTimerBypass = "seniorcrates.timer.bypass";
	public String reload = "seniorcrates.admin.reload";
	public String set = "seniorcrates.admin.set";
	
	public String create = "seniorcrates.admin.create";
	public String delete = "seniorcrates.admin.delete";
	public String edit = "seniorcrates.admin.edit";
	public String give = "seniorcrates.admin.give";
	public String bypass = "seniorcrates.admin.bypass";
	
	public Permissions(String name, double version) {
		super(name, version);

		this.items.add(new ConfigItem("Permissions.everyone.basic", basic));
		this.items.add(new ConfigItem("Permissions.everyone.shop", shop));
		
		this.items.add(new ConfigItem("Permissions.administration.admin", admin));
		this.items.add(new ConfigItem("Permissions.administration.commad.timer_bypass", commandTimerBypass));
		this.items.add(new ConfigItem("Permissions.administration.reload", reload));
		this.items.add(new ConfigItem("Permissions.administration.set", set));
		
		this.items.add(new ConfigItem("Permissions.administration.crate.create", create));
		this.items.add(new ConfigItem("Permissions.administration.crate.delete", delete));
		this.items.add(new ConfigItem("Permissions.administration.crate.edit", edit));
		this.items.add(new ConfigItem("Permissions.administration.crate.give", give));
		this.items.add(new ConfigItem("Permissions.administration.crate.bypass", bypass));
		
		
	}
	
	@Override
	public void initialize() {
		this.shop = this.getConfiguration().getString("Permissions.everyone.shop");
		this.basic = this.getConfiguration().getString("Permissions.everyone.basic");
		this.admin = this.getConfiguration().getString("Permissions.administration.admin");
		this.commandTimerBypass = this.getConfiguration().getString("Permissions.administration.commad.timer_bypass");
		this.reload = this.getConfiguration().getString("Permissions.administration.reload");
		this.create = this.getConfiguration().getString("Permissions.administration.configure");
		this.set = this.getConfiguration().getString("Permissions.administration.set");
		
		this.create = this.getConfiguration().getString("Permissions.administration.crate.create");
		this.delete = this.getConfiguration().getString("Permissions.administration.crate.delete");
		this.edit = this.getConfiguration().getString("Permissions.administration.crate.edit");
		this.give = this.getConfiguration().getString("Permissions.administration.crate.give");
		this.bypass = this.getConfiguration().getString("Permissions.administration.crate.bypass");
		
		
	}


	
}
