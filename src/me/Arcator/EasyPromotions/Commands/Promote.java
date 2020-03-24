package me.Arcator.EasyPromotions.Commands;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.Arcator.EasyPromotions.Main;
import me.Arcator.EasyPromotions.Files.FilesManager;
import me.Arcator.EasyPromotions.GUI.PromotionsGUI;
import me.Arcator.EasyPromotions.Utils.RankTimer;
import me.Arcator.EasyPromotions.Utils.Utils;

public class Promote implements CommandExecutor {

	private Main plugin;

	public Promote(Main plugin) {
		this.plugin = plugin;

		plugin.getCommand("promote").setExecutor(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		FileConfiguration config = plugin.getConfig();
		if (!(sender instanceof Player)) {
			sender.sendMessage(Utils.chat(config.getString("messages.OnlyPlayers")));
			return true;
		}
		Player p = (Player) sender;
		String placePerm = "easypromotions.promote";
		if (!p.hasPermission(placePerm)) {
			p.sendMessage(Utils.chat(config.getString("messages.NoPermission").replace("%permission%", placePerm)));
			return true;
		}
		if (args.length == 0) {
			p.sendMessage(Utils.chat("&6" + plugin.getDescription().getName() + " v"
					+ plugin.getDescription().getVersion() + " by Darrionat/Arcator"));
			p.sendMessage(Utils.chat("&6/" + label + " help for information"));
			return true;
		}
		if (args[0].equalsIgnoreCase("help")) {
			p.sendMessage(Utils.chat("&6/" + label + " {user}"));
			p.sendMessage(Utils.chat("&6/" + label + " info {user}"));
			p.sendMessage(Utils.chat("&6/" + label + " reset {user} - Resets their promotion"));
			return true;
		}
		// /promote info user
		if (args[0].equalsIgnoreCase("info")) {
			if (args.length == 1) {
				p.sendMessage(Utils.chat("&6/" + label + " info {user}"));
				return true;
			}
			if (Bukkit.getOfflinePlayer(args[1]) == null) {
				p.sendMessage(Utils.chat(config.getString("messages.InvalidPlayer")));
				return true;
			}
			OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
			String uuid = target.getUniqueId().toString();
			FilesManager filesmanager = new FilesManager((Main) plugin);
			FileConfiguration playerdata = filesmanager.getDataConfig("playerdata");
			ConfigurationSection uuidSection = playerdata.getConfigurationSection(uuid);
			if (uuidSection == null) {
				p.sendMessage(Utils.chat(config.getString("messages.NoEasyPromotion")));
				return true;
			}
			long timeEntered = uuidSection.getLong("Time");
			long secondsLeft = (timeEntered / 1000) - (System.currentTimeMillis() / 1000);
			long hours = secondsLeft / 3600;
			long minutes = (secondsLeft % 3600) / 60;
			long seconds = secondsLeft % 60;

			String timeString = String.format("%02dh %02dm %02ds", hours, minutes, seconds);

			p.sendMessage(Utils.chat("&6UUID:&e " + uuid));
			p.sendMessage(Utils.chat("&6Rank:&e " + uuidSection.getString("Role")));
			if (secondsLeft < 0) {
				p.sendMessage(Utils.chat("&6Time Remaining:&e None"));
			} else {
				p.sendMessage(Utils.chat("&6Time Remaining:&e " + timeString));
			}
			return true;
		}
		// /promote reset user
		if (args[0].equalsIgnoreCase("reset")) {
			if (Bukkit.getOfflinePlayer(args[1]) == null) {
				p.sendMessage(Utils.chat(config.getString("messages.InvalidPlayer")));
				return true;
			}
			OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
			String uuid = target.getUniqueId().toString();
			FilesManager filesmanager = new FilesManager((Main) plugin);
			FileConfiguration playerdata = filesmanager.getDataConfig("playerdata");
			ConfigurationSection uuidSection = playerdata.getConfigurationSection(uuid);
			if (uuidSection == null) {
				p.sendMessage(Utils.chat(config.getString("messages.NoEasyPromotion")));
				return true;
			}
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getConfig().getString("RemoveRoleCmd")
					.replace("%user%", p.getName()).replace("%rank%", playerdata.getString(uuid + ".Role")));
			playerdata.set(uuid, null);

			File playerDataFile = filesmanager.getFile("playerdata");
			try {
				playerdata.save(playerDataFile);
				p.sendMessage(Utils.chat(
						plugin.getConfig().getString("messages.ResetMessage").replace("%target%", target.getName())));
				Bukkit.getScheduler().cancelTasks(plugin);
				RankTimer timer = new RankTimer(plugin);
				timer.startTimer();
				
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return true;
			}
		}
		// /promote user rank
		if (Bukkit.getOfflinePlayer(args[0]) == null) {
			p.sendMessage(Utils.chat(config.getString("messages.InvalidPlayer")));
			return true;
		}
		OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
		String uuid = target.getUniqueId().toString();
		FilesManager filesmanager = new FilesManager((Main) plugin);
		FileConfiguration playerdata = filesmanager.getDataConfig("playerdata");
		ConfigurationSection uuidSection = playerdata.getConfigurationSection(uuid);
		if (uuidSection != null) {
			p.sendMessage(Utils.chat(plugin.getConfig().getString("messages.AlreadyPromoted")));
			return true;
		}
		new PromotionsGUI(target);
		p.openInventory(PromotionsGUI.GUI(p, plugin));
		return true;
	}

}