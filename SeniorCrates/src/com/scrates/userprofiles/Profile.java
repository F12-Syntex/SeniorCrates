package com.scrates.userprofiles;

import java.util.UUID;

import com.scrates.config.Settings;
import com.scrates.cooldown.CooldownUser;
import com.scrates.main.SeniorCrates;

public class Profile {

	private Settings settings;
	int cooldownID = 0;
	
	private UUID identity;
	private int crates;
	private int delay;
	
	private CooldownUser cooldown;
	
	private final String DELAY_ID = "userprofile";
	
	public Profile(UUID identity, int crates, int delay) {
		this.identity = identity;
		this.crates = crates;
		
		this.setSettings(SeniorCrates.getInstance().configManager.settings);
	
		this.cooldown = SeniorCrates.getInstance().cooldownManager.getUser(identity);
		this.cooldown.reset(DELAY_ID, delay);
	}

	public UUID getIdentity() {
		return identity;
	}

	public void setIdentity(UUID identity) {
		this.identity = identity;
	}

	public int getCrates() {
		return crates;
	}

	public void setCrates(int crates) {
		this.crates = crates;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	public ProfileResponse UseCrate() {
		
		if(this.crates <= 0) {
			return ProfileResponse.CRATES_OUT;
		}
		if(this.getTimer() > 0) {
			return ProfileResponse.TIMER;
		}
		
		this.cooldown.reset(DELAY_ID, settings.delay);
		this.crates-=1;
		return ProfileResponse.OK;
	}
	
	public int getTimer() {
		return this.cooldown.getTime(DELAY_ID);
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}
	
}
