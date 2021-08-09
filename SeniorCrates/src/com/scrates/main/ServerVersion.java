package com.scrates.main;

import org.bukkit.Bukkit;

public class ServerVersion {

	private final String VERSION;
	
	private final int MAJOR_VERSION;
	private final int MINOR_VERSION;
	private final int PATCH;
	
	public ServerVersion() {
		this.VERSION = Bukkit.getServer().getVersion().split(":")[1].replaceAll("[^\\d.]", "").trim();
		this.MAJOR_VERSION = Integer.valueOf(this.VERSION.split("\\.")[0]);
		this.MINOR_VERSION = Integer.valueOf(this.VERSION.split("\\.")[1]);
		this.PATCH = Integer.valueOf(this.VERSION.split("\\.")[2]);	
	}

	public String getVERSION() {
		return VERSION;
	}

	public int getMAJOR_VERSION() {
		return MAJOR_VERSION;
	}

	public int getMINOR_VERSION() {
		return MINOR_VERSION;
	}

	public int getPATCH() {
		return PATCH;
	}
	
	public boolean isLegacy() {
		return this.MINOR_VERSION < 13;
	}
	
}
