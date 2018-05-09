package com.myskdias.antispam;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
	
	public File configFile;
	public YamlConfiguration config;
	
	@SuppressWarnings("deprecation")
	@Override
	public void onEnable() {
		instance = this;
		on = true;
		saveDefaultConfig();
		getCommand("antispam").setExecutor(new AntispamCommand());
		config = new YamlConfiguration();
		boolean b = YamlConfiguration.UTF8_OVERRIDE;
		Field f = null;
		//Load the config in UTF-8
		try {
			f = YamlConfiguration.class.getField("UTF8_OVERRIDE");
			setFieldValue(f, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		configFile = new File(getDataFolder(), "config.yml");
		try {
			config.load(new FileInputStream(configFile));
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		if(f != null)
			try {
				setFieldValue(f, b);
			} catch (Exception e) {
				e.printStackTrace();
			}
		loadConfig();
		Bukkit.getPluginManager().registerEvents(listener = new ChatListener(), this);
	}
	
	
	public static void setFieldValue(Field field, Object newValue) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
	      field.setAccessible(true);

	      Field modifiersField = Field.class.getDeclaredField("modifiers");
	      modifiersField.setAccessible(true);
	      modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

	      field.set(null, newValue);
	   }
	
	public static Sky getInstance() {
		return instance;
	}
	
	public void loadConfig() {
		this.messageLimite = getConfig().getInt("plugin.messageLimite");
		this.messageCooldown = getConfig().getInt("plugin.messageCooldown") * 1000;
		this.spamTime = getConfig().getInt("plugin.spamTime") * 1000;
		
		this.messageError = toColoredMessage(getConfig().getString("chat.messageError"));
		this.messageActivation = toColoredMessage(getConfig().getString("chat.messageActivation"));
		this.messageDesactivation = toColoredMessage(getConfig().getString("chat.messageDesactivation"));
		this.permBypass = getConfig().getString("plugin.permBypass");
		this.permTurnOff = getConfig().getString("plugin.permTurnOff");
		this.permModify = getConfig().getString("plugin.permModify");
		this.on = getConfig().getBoolean("plugin.activate");
	}
	
	@Override
	public void saveConfig() {
		try {
			config.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public FileConfiguration getConfig() {

		return config;
	}
	
	public String toColoredMessage(String s) {
		return s.replace('&', '§');
	}
	
}
