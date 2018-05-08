package com.myskdias.antispam;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Sky extends JavaPlugin {

	public static Sky instance;
	public int messageLimite;
	//cooldown en ms;
	public long messageCooldown;
	public long spamTime;
	
	public String messageError;
	public String messageActivation;
	public String messageDesactivation;
	public String permBypass;
	public String permTurnOff;
	public String permModify;
	
	public boolean on;
	
	public ChatListener listener;
	
	@Override
	public void onEnable() {
		instance = this;
		on = true;
		getCommand("antispam").setExecutor(new AntispamCommand());
		loadConfig();
		Bukkit.getPluginManager().registerEvents(listener = new ChatListener(), this);
	}
	
	
	public static Sky getInstance() {
		return instance;
	}
	
	public void loadConfig() {
		saveDefaultConfig();
		this.messageLimite = getConfig().getInt("plugin.messageLimite");
		this.messageCooldown = getConfig().getInt("plugin.messageCooldown") * 1000;
		this.spamTime = getConfig().getInt("plugin.spamTime") * 1000;
		
		this.messageError = toColoredMessage(getConfig().getString("chat.messageError"));
		this.messageActivation = toColoredMessage(getConfig().getString("chat.messageActivation"));
		this.messageDesactivation = toColoredMessage(getConfig().getString("chat.messageDesactivation"));
		this.permBypass = getConfig().getString("plugin.permBypass");
		this.permTurnOff = getConfig().getString("plugin.permTurnOff");
		this.permModify = getConfig().getString("plugin.permModify");
	}
	
	public String toColoredMessage(String s) {
		return s.replace('&', '§');
	}
	
}
