package com.scrates.utils;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;

public class DyeUtils {
	
	public static DyeColor getRandomDyeColour() {
		try {
			DyeColor random = DyeColor.valueOf("SILVER");
			while(random == DyeColor.valueOf("SILVER")) {
				random = DyeColor.values()[ThreadLocalRandom.current().nextInt((DyeColor.values().length - 1))];
			}			
			return random;
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		return DyeColor.values()[ThreadLocalRandom.current().nextInt((DyeColor.values().length - 1))];
	}
	
	public static ChatColor getColourFromDye(DyeColor colour) {
		switch (colour) {
		case BLACK: return ChatColor.BLACK;
		case BROWN: return ChatColor.YELLOW;
		case BLUE: return ChatColor.DARK_BLUE;
		case CYAN: return ChatColor.BLUE;
		case GRAY: return ChatColor.GRAY;
		case GREEN: return ChatColor.DARK_GREEN;
		case LIGHT_BLUE: return ChatColor.BLUE;
		case LIME: return ChatColor.GREEN;
		case MAGENTA: return ChatColor.DARK_PURPLE;
		case ORANGE: return ChatColor.YELLOW;
		case PINK: return ChatColor.LIGHT_PURPLE;
		case PURPLE: return ChatColor.LIGHT_PURPLE;
		case RED: return ChatColor.RED;
		case WHITE: return ChatColor.WHITE;
		case YELLOW: return ChatColor.YELLOW;
		default:
			return ChatColor.GRAY;
		}
	}


}
