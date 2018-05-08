package com.myskdias.antispam;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AntispamCommand implements CommandExecutor {

	public Sky sky;
	
	public AntispamCommand() {
		sky = Sky.getInstance();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		if(args.length == 0) {
			if(sender.hasPermission(sky.permTurnOff) || sender.hasPermission("antispam.*") || sender.isOp()) {
				if(sky.on) {
					sky.on = false;
					sender.sendMessage("§6L'antispam est désactivé");
				} else {
					sky.on = true;
					sender.sendMessage("§6L'antispam est activé");
				}
				return false;
			}
		} else if(args.length == 3) {
			if(sender.hasPermission(sky.permModify) || sender.hasPermission("antispam.*") || sender.isOp()) {
				if(args[0].equalsIgnoreCase("set")) {
					if(args[1].equalsIgnoreCase("spamTime")) {
						try {
							int a = Integer.parseInt(args[2]);
							sky.getConfig().set("plugin.spamTime", a);
							sky.saveConfig();
							sender.sendMessage("§6Fait");
							sky.loadConfig();
							sky.listener.rl();
							return false;
						} catch(NumberFormatException e) {
							sender.sendMessage("§6Please enter a valid number");
							return false;
						}
					} else if(args[1].equalsIgnoreCase("messageCooldown")) {
						try {
							int a = Integer.parseInt(args[2]);
							sky.getConfig().set("plugin.messageCooldown", a);
							sky.saveConfig();
							sender.sendMessage("§6Fait");
							sky.loadConfig();
							sky.listener.rl();
							return false;
						} catch(NumberFormatException e) {
							sender.sendMessage("§6Please enter a valid number");
							return false;
						}
					} else if(args[1].equalsIgnoreCase("messageLimite")) {
						try {
							int a = Integer.parseInt(args[2]);
							sky.getConfig().set("plugin.messageLimite", a);
							sky.saveConfig();
							sender.sendMessage("§6Fait");
							sky.loadConfig();
							sky.listener.rl();
							return false;
						} catch(NumberFormatException e) {
							sender.sendMessage("§6Please enter a valid number");
							return false;
						}
					}
				}
				sender.sendMessage("§6/antispam set + [args] + time");
				sender.sendMessage("§6args = messageLimite");
				sender.sendMessage("§6args = messageCooldown");
				sender.sendMessage("§6args = spamTime");
			}
		}
		return true;
	}

}
