package me.Arcator.EasyPromotions.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.Arcator.EasyPromotions.Main;
import me.Arcator.EasyPromotions.GUI.PromotionsGUI;

public class InventoryClick implements Listener {
	private Main plugin;

	public InventoryClick(Main plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		String title = e.getView().getTitle();
		String promotionsGUI = PromotionsGUI.inventory_name;

		Player p = (Player) e.getWhoClicked();
		int slot = e.getSlot();
		ItemStack item = e.getCurrentItem();
		Inventory inv = e.getInventory();

		if (title.equals(promotionsGUI)) {
			if (item == null) {
				return;
			}
			e.setCancelled(true);
			PromotionsGUI.clicked(p, slot, item, inv, plugin);

		}
	}

}
