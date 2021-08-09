package com.scrates.userprofiles;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;

import com.scrates.config.Database;
import com.scrates.config.Settings;
import com.scrates.main.SeniorCrates;

public class UserProfiles {

	private Database credentials;
	
	private Connection connection;
	
	private final String ACCOUNTS = "users";
	private final String UPDATE = "lastUpdate";
	
	private List<Profile> profiles;
	private long last_update;
	
	public UserProfiles() {
		this.last_update = 0;
		this.credentials = SeniorCrates.getInstance().configManager.database;
		this.profiles = new ArrayList<Profile>();
	}
	
	public boolean isConnected() {
		return connection != null;
	}
	
	public void connect() throws SQLException {
		if(!this.isConnected()) {
			this.connection = DriverManager.getConnection("jdbc:mysql://" + credentials.getHost() + ":"
																		  + credentials.getPort() + "/"
																		  + credentials.getDatabase() + "?useSSL=false", 
																		  credentials.getUsername(), credentials.getPassword());
			this.initialize();
		}
	}
	
	public void initialize() {
		
		this.profiles.clear();
		
		try {

			this.createDatabase();
			
			PreparedStatement keys = this.getConnection().prepareStatement("SELECT UUID FROM " + this.ACCOUNTS);
			ResultSet results = keys.executeQuery();
			
			while(results.next()) {
				
				UUID result = UUID.fromString(results.getString(results.getRow()));
			
				PreparedStatement cratesQuery = this.getConnection().prepareStatement("SELECT CRATES FROM " + this.ACCOUNTS + " WHERE UUID=?");
				cratesQuery.setString(1, result.toString());
				
				ResultSet cratesResult = cratesQuery.executeQuery();
			
				if(!cratesResult.next()) {
					Bukkit.getServer().shutdown();
				}
				
				int crates = cratesResult.getInt(cratesResult.getRow());
			
				PreparedStatement delayQuery = this.getConnection().prepareStatement("SELECT DELAY FROM " + this.ACCOUNTS + " WHERE UUID=?");
				delayQuery.setString(1, result.toString());
				
				ResultSet delayResult = delayQuery.executeQuery();
				
				if(!delayResult.next()) {
					Bukkit.getServer().shutdown();
				}
				
				int delay = delayResult.getInt(delayResult.getRow());
				
				Profile profile = new Profile(result, crates, delay);
				this.profiles.add(profile);
			}
			
			PreparedStatement updateQuery = this.getConnection().prepareStatement("SELECT LASTUPDATE FROM " + this.UPDATE);
			ResultSet updateResult = updateQuery.executeQuery();
		
			if(!updateResult.next()) {
				Bukkit.getServer().shutdown();
			}
			
			long update = updateResult.getLong(updateResult.getRow());
			this.setLast_update(update);
			

		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void createDatabase() {
		
		PreparedStatement preparedStatement;
		
		try {
		
			preparedStatement = this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS " + this.ACCOUNTS + " (UUID VARCHAR(37), CRATES INT(10), DELAY INT(10), PRIMARY KEY(UUID))");
			preparedStatement.executeUpdate();
			
			preparedStatement = this.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS " + this.UPDATE + " (LASTUPDATE BIGINT(20))");
			preparedStatement.executeUpdate();
		
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void update() {
		new Thread(() -> {
			
			PreparedStatement preparedStatement;
			
			try {
				
				this.createDatabase();
				
				for(Profile account : this.profiles) {

					preparedStatement = this.getConnection().prepareStatement("SELECT * FROM " + this.ACCOUNTS + " WHERE UUID=?");
					preparedStatement.setString(1, account.getIdentity().toString());
					ResultSet results = preparedStatement.executeQuery();
					
					if(!results.next()) {
						PreparedStatement insert = this.getConnection().prepareStatement("INSERT INTO " + this.ACCOUNTS + " (UUID,CRATES,DELAY) VALUES (?,?,?)");
						
						insert.setString(1, account.getIdentity().toString());
						insert.setDouble(2, account.getCrates());
						insert.setDouble(3, account.getDelay());
						insert.executeUpdate();
						
					}else {
						PreparedStatement insertCrates = this.getConnection().prepareStatement("UPDATE " + this.ACCOUNTS + " SET CRATES=? WHERE UUID=?");
						
						insertCrates.setDouble(1, account.getCrates());
						insertCrates.setString(2, account.getIdentity().toString());
						
						insertCrates.executeUpdate();
						
						PreparedStatement insertDelay = this.getConnection().prepareStatement("UPDATE " + this.ACCOUNTS + " SET DELAY=? WHERE UUID=?");
						
						insertDelay.setInt(1, account.getDelay());
						insertDelay.setString(2, account.getIdentity().toString());
						
						insertDelay.executeUpdate();
					}
					
				
					updateLastUpdate();
					
				}
				
			}catch (Exception e) {
				e.printStackTrace();
			}
			
		}).run();
	}
	
	public void updateLastUpdate() {
		
		PreparedStatement preparedStatement;
		
		try {
		
			preparedStatement = this.getConnection().prepareStatement("SELECT LASTUPDATE FROM " + this.UPDATE);
			ResultSet results = preparedStatement.executeQuery();
			
			if(!results.next()) {
				PreparedStatement insert = this.getConnection().prepareStatement("INSERT INTO " + this.UPDATE + " (LASTUPDATE) VALUES (?)");
				insert.setLong(1, this.last_update);
				insert.executeUpdate();
			}else {
				PreparedStatement insertCrates = this.getConnection().prepareStatement("UPDATE " + this.UPDATE + " SET LASTUPDATE=?");
				insertCrates.setLong(1, this.last_update);
				insertCrates.executeUpdate();
			}
		
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public boolean AccountsUuidExists(UUID uuid) {
		for(Profile profile : this.profiles) {
			if(profile.getIdentity().compareTo(uuid) == 0) return true;
		}
		return false;
	}
	
	public Profile getAccount(UUID uuid) {
		
		this.resetIfNewDay();
		
		for(Profile profile : this.profiles) {
			if(profile.getIdentity().compareTo(uuid) == 0) return profile;
		}
		
		Settings settings = SeniorCrates.getInstance().configManager.settings;
		Profile profile = new Profile(uuid, settings.crates, 0);
		this.profiles.add(profile);
		return profile;
	}
	
	public void resetIfNewDay() {

	    if(timeTillNewDay() <= 0) {
	    	SeniorCrates.getInstance().userProfiles.setLast_update(System.currentTimeMillis());
			for(Profile profile : this.profiles) {
				profile.setCrates(SeniorCrates.getInstance().configManager.settings.crates);
			}
			this.updateLastUpdate();
	    }
	    	
	}
	
	public long timeTillNewDay() {
	    long diffInMillies = Math.abs(System.currentTimeMillis() - SeniorCrates.getInstance().userProfiles.getLast_update());
	    return (SeniorCrates.getInstance().configManager.settings.reset)*1000 - diffInMillies;
	}
	
	public void disconnect() {
		this.update();
		this.updateLastUpdate();
		if(this.isConnected()) {
			try {
				this.connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setup() {
		
	}
	
	public Connection getConnection() {
		return this.connection;
	}

	public Database getCredentials() {
		return credentials;
	}

	public void setCredentials(Database credentials) {
		this.credentials = credentials;
	}

	public List<Profile> getProfiles() {
		return profiles;
	}

	public void setProfiles(List<Profile> profiles) {
		this.profiles = profiles;
	}

	public long getLast_update() {
		return last_update;
	}

	public void setLast_update(long last_update) {
		this.last_update = last_update;
		this.updateLastUpdate();
	}

	public String getACCOUNTS() {
		return ACCOUNTS;
	}

	public String getUPDATE() {
		return UPDATE;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	
	
}
