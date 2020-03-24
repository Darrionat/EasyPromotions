package me.Arcator.EasyPromotions;

import org.bukkit.plugin.java.JavaPlugin;

import me.Arcator.EasyPromotions.Commands.Promote;
import me.Arcator.EasyPromotions.Files.FilesManager;
import me.Arcator.EasyPromotions.GUI.PromotionsGUI;
import me.Arcator.EasyPromotions.Listeners.InventoryClick;
import me.Arcator.EasyPromotions.Utils.RankTimer;

public class Main extends JavaPlugin {

	public void onEnable() {
		FilesManager filesManager = new FilesManager(this);
		if (!filesManager.fileExists("playerdata")) {
			filesManager.setup("playerdata");
		}
		saveDefaultConfig();
		new Promote(this);
		new InventoryClick(this);
		PromotionsGUI.initialize(this);
		RankTimer timer = new RankTimer(this);
		timer.startTimer();
	}

	public void onDisable() {

	}

}
