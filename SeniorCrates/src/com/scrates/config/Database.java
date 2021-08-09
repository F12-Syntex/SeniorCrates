package com.scrates.config;

public class Database extends Config{
	
	public String host = "localhost";
	public String port = "3306";
	public String database = "crates";
	public String username = "root";
	public String password = "";
	
	public Database(String name, double version) {
		super(name, version);
		this.items.add(new ConfigItem("Settings.credentials.host", this.host));
		this.items.add(new ConfigItem("Settings.credentials.port", this.port));
		this.items.add(new ConfigItem("Settings.credentials.database", this.database));
		this.items.add(new ConfigItem("Settings.credentials.username", this.username));
		this.items.add(new ConfigItem("Settings.credentials.password", this.password));
	
	}
	
	@Override
	public void initialize() {
	
		this.host = this.getConfiguration().getString("Settings.credentials.host");
		this.port = this.getConfiguration().getString("Settings.credentials.port");
		this.database = this.getConfiguration().getString("Settings.credentials.database");
		this.username = this.getConfiguration().getString("Settings.credentials.username");
		this.password = this.getConfiguration().getString("Settings.credentials.password");
		
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
}
