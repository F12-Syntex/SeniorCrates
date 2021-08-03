package com.scrates.cooldown;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.scheduler.BukkitScheduler;

import com.scrates.main.SeniorCrates;

public class CooldownTick {
	
	private BukkitScheduler scheduler;

	public CooldownTick() {
		this.scheduler = SeniorCrates.getInstance().getServer().getScheduler();
	}

	@SuppressWarnings("deprecation")
	public void schedule() {

		new Thread(() -> {

	        scheduler.scheduleSyncRepeatingTask(SeniorCrates.getInstance(), new Runnable() {
	            @Override
	            public void run() {
	            	List<CooldownRunnable> remove = new ArrayList<CooldownRunnable>();
	            	
	            	
	            	for(CooldownRunnable i : SeniorCrates.getInstance().cooldownManager.getRunnables()) {
	            		
	            		i.setTimer((i.getTimer()-1));
	            		
	            		if(i.getTimer() == 0) {
	            			i.onComplete();
	            			remove.add(i);
	            		}else {
	            			i.onTick();
	            		}
	            		
	            	}
	            	
	            	for(CooldownRunnable i : remove) {
	            		SeniorCrates.getInstance().cooldownManager.getRunnables().remove(i);
	            	}
	            }  	
	            	
	        }, 0L, 20L);
	        
	        scheduler.scheduleAsyncRepeatingTask(SeniorCrates.getInstance(), new Runnable() {
	            @Override
	            public void run() {
	            	for(CooldownUser i : SeniorCrates.getInstance().cooldownManager.getUsers()) {
	            		i.tick();
	            	}
	            }  	
	            	
	        }, 0L, 20L);
			
		}, "Schedular").start();

	}
	
	public void stop() {
		this.scheduler.cancelTasks(SeniorCrates.getInstance());
	}
	
}
