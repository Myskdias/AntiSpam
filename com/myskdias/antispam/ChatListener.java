package com.myskdias.antispam;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

	public HashMap<String, Long> cooldown = new HashMap<>();
	public ArrayList<Long> chatTime;
	public Sky sky;
	public long timeEnd;
	public boolean b;
	
	public ChatListener() {
		sky = Sky.getInstance();
		timeEnd = 0;
		b = false;
		chatTime = new ArrayList<>();
		for(int i = 0; i < sky.messageLimite; i++) {
			chatTime.add(-1L);
		}
	}
	
	public void rl() {
		chatTime = new ArrayList<>();
		for(int i = 0; i < sky.messageLimite; i++) {
			chatTime.add(-1L);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChat(AsyncPlayerChatEvent e) {
		if(!sky.on) return;
		Player p = e.getPlayer();
		if(p.hasPermission(sky.permBypass) || p.hasPermission("antispam.*") || p.isOp()) {
			return;
		}
		long timeMillis = System.currentTimeMillis();
		//chat Limité
		if(timeMillis < timeEnd) {
			Long l = cooldown.get(p.getName());
			if(l == null) {
				cooldown.put(p.getName(), timeMillis + sky.messageCooldown);
				updateChatTime(timeMillis);
				return;
			}
			if(timeMillis < l) {
				e.setCancelled(true);
				if(!sky.messageError.isEmpty()) {
					p.sendMessage(sky.messageError.replace("%i", "" + (int) (((l - timeMillis)/1000) + 1)));
				}
				return;
			} else {
				updateChatTime(timeMillis);
				cooldown.put(p.getName(), timeMillis + sky.messageCooldown);
				return;
			}
		}
		
		if(b) {
			b = false;
			if(!sky.messageDesactivation.isEmpty())
				Bukkit.broadcastMessage(sky.messageDesactivation);
		}
		updateChatTime(timeMillis);
	}
	
	public void updateChatTime(long timeMillis) {
		Long base = chatTime.remove(0);
		chatTime.add(sky.messageLimite - 1, timeMillis);
		
		if(base != null) {
			if(base != -1) {
				if(timeMillis - base < 60_000) {
					timeEnd = timeMillis + sky.spamTime;
					if(!b) {
						b = true;
						if(!sky.messageActivation.isEmpty())
							Bukkit.broadcastMessage(sky.messageActivation.replace("%i", ""+(int)sky.messageCooldown / 1000));
					}
				}
			}
		}
	}
	
}
