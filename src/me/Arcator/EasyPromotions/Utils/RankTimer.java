package me.Arcator.EasyPromotions.Utils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import me.Arcator.EasyPromotions.Main;
import me.Arcator.EasyPromotions.Files.FilesManager;

public class RankTimer {
	private Main plugin;

	public RankTimer(Main plugin) {
		this.plugin = plugin;
	}

	public void startTimer() {
		int time = plugin.getConfig().getInt("TimerCheck");
		FilesManager filesmanager = new FilesManager(plugin);
		FileConfiguration playerdata = filesmanager.getDataConfig("playerdata");
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					String uuid = p.getUniqueId().toString();
					if (playerdata.getConfigurationSection(uuid) == null) {
						continue;
					}
					ConfigurationSection section = playerdata.getConfigurationSection(uuid);
					long timeLeft = section.getLong("Time") - System.currentTimeMillis();
					if (timeLeft < 0) {
						String rank = section.getString("Role");
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), plugin.getConfig().getString("RemoveRoleCmd")
								.replace("%user%", p.getName()).replace("%rank%", rank));
						continue;
					}
				}
			}
		}, 0L, time * 20);
	}

}
