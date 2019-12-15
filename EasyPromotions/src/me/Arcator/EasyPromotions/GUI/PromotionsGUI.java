package me.Arcator.EasyPromotions.GUI;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.Arcator.EasyPromotions.Main;
import me.Arcator.EasyPromotions.Files.FilesManager;
import me.Arcator.EasyPromotions.Utils.RankTimer;
import me.Arcator.EasyPromotions.Utils.Utils;

public class PromotionsGUI {

	public static OfflinePlayer target;

	public PromotionsGUI(OfflinePlayer target) {
		PromotionsGUI.target = target;
	}

	public static Inventory inv;
	public static String inventory_name;
	public static int inv_boxes = 6;
	public static int rows = inv_boxes * 9;

	public static void initialize(JavaPlugin plugin) {
		inventory_name = Utils.chat(plugin.getConfig().getString("Promotion GUI Name"));

		inv = Bukkit.createInventory(null, rows);
	}

	public static Inventory GUI(Player p, JavaPlugin plugin) {
		Inventory toReturn = Bukkit.createInventory(null, rows, inventory_name);

		for (int i = 1; i <= 54; i++) {
			// Barrier skip
			if (i == 50) {
				continue;
			}
			// Row of glass skip
			if (i > 18 && i < 28) {
				continue;
			}
			// Black stained glass
			Utils.createItemByte(inv, Material.STAINED_GLASS_PANE, 15, 1, i, " ");

		}
		/*
		 * ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short) 3); SkullMeta
		 * meta = (SkullMeta) skull.getItemMeta(); meta.setOwner(target.getName());
		 * skull.setItemMeta(meta);
		 * 
		 * Utils.createSkull(inv, skull, 1, 5, "&ePromote " + target.getName());
		 */

		// 5 min, 1 hour, 1 day, 1 week, 1 month, 3 months, 6 months, 1 year, Perm
		// Byte Id's 14, 1, 4, 5, 13, 3, 11, 10, 6
		// Utils.createItemByte(inv, material, byteId, amount, invSlot,
		// displayName, loreString)
		// Red 5 mins
		Utils.createItemByte(inv, Material.STAINED_GLASS_PANE, 14, 1, 19, "&45 Minutes");
		// Orange 1 hour
		Utils.createItemByte(inv, Material.STAINED_GLASS_PANE, 1, 1, 20, "&c1 Hour");
		// Yellow 1 Day
		Utils.createItemByte(inv, Material.STAINED_GLASS_PANE, 4, 1, 21, "&e1 Day");
		// Lime Green 1 Week
		Utils.createItemByte(inv, Material.STAINED_GLASS_PANE, 5, 1, 22, "&a1 Week");
		// Green 1 Month
		Utils.createItemByte(inv, Material.STAINED_GLASS_PANE, 13, 1, 23, "&21 Month");
		// Light Blue 3 months
		Utils.createItemByte(inv, Material.STAINED_GLASS_PANE, 3, 1, 24, "&b3 Months");
		// Blue 6 months
		Utils.createItemByte(inv, Material.STAINED_GLASS_PANE, 11, 1, 25, "&16 Months");
		// Purple 1 Year
		Utils.createItemByte(inv, Material.STAINED_GLASS_PANE, 10, 1, 26, "&51 Year");
		// Pink Perm
		Utils.createItemByte(inv, Material.STAINED_GLASS_PANE, 6, 1, 27, "&dPermanent");
		int rankSlot = 28;
		for (String rank : plugin.getConfig().getStringList("ranks")) {
			Utils.createItemByte(inv, Material.STAINED_GLASS_PANE, 8, 1, rankSlot, "&e" + rank);
			rankSlot++;
			continue;
		}

		Utils.createItem(inv, Material.BARRIER, 1, 50, "&cClose Menu");
		toReturn.setContents(inv.getContents());
		p.updateInventory();
		return toReturn;
	}

	public static HashMap<UUID, Long> timeMap = new HashMap<UUID, Long>();
	public static HashMap<UUID, String> rankMap = new HashMap<UUID, String>();

	public static void clicked(Player p, int slot, ItemStack clicked, Inventory inv, JavaPlugin plugin) {

		UUID uuid = target.getUniqueId();
		if (clicked.getItemMeta().getDisplayName() == null) {
			return;
		}
		if (clicked.getItemMeta().getDisplayName().contains(Utils.chat("&ePromote " + target.getName() + " to"))) {
			setTime(p, timeMap.get(uuid), plugin, rankMap.get(uuid));
			return;
		}

		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&cClose Menu"))) {
			p.closeInventory();
			return;
		}
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&45 Minutes"))) {
			long time = 300000L;
			long futureTime = System.currentTimeMillis() + time;
			timeMap.put(uuid, futureTime);
			return;
		}
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&c1 Hour"))) {
			long time = 3600000L;
			long futureTime = System.currentTimeMillis() + time;
			timeMap.put(uuid, futureTime);
			replaceTimeGlass(inv, slot);
			return;
		}
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&e1 Day"))) {
			long time = 86400000L;
			long futureTime = System.currentTimeMillis() + time;
			timeMap.put(uuid, futureTime);
			replaceTimeGlass(inv, slot);
			return;
		}
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&a1 Week"))) {
			long time = 604800000L;
			long futureTime = System.currentTimeMillis() + time;
			timeMap.put(uuid, futureTime);
			replaceTimeGlass(inv, slot);
			return;
		}
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&21 Month"))) {
			long time = 2592000000L;
			long futureTime = System.currentTimeMillis() + time;
			timeMap.put(uuid, futureTime);
			replaceTimeGlass(inv, slot);
			return;
		}
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&b3 Months"))) {
			long time = 7776000000L;
			long futureTime = System.currentTimeMillis() + time;
			timeMap.put(uuid, futureTime);
			replaceTimeGlass(inv, slot);
			return;
		}
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&16 Months"))) {
			long time = 15552000000L;
			long futureTime = System.currentTimeMillis() + time;
			timeMap.put(uuid, futureTime);
			replaceTimeGlass(inv, slot);
			return;
		}
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&51 Year"))) {
			long time = 31556952000L;
			long futureTime = System.currentTimeMillis() + time;
			timeMap.put(uuid, futureTime);
			replaceTimeGlass(inv, slot);
			return;
		}
		if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&dPermanent"))) {
			long time = 50 * 31556952000L;
			long futureTime = System.currentTimeMillis() + time;
			timeMap.put(uuid, futureTime);
			replaceTimeGlass(inv, slot);
			return;
		}
		for (String rank : plugin.getConfig().getStringList("ranks")) {
			if (clicked.getItemMeta().getDisplayName().equalsIgnoreCase(Utils.chat("&e" + rank))) {
				rankMap.put(uuid, rank);
				replaceRankGlass(inv, slot);
				return;
			}
			continue;
		}

	}

	public static void replaceTimeGlass(Inventory inv, int slot) {
		slot++;
		for (int i = 19; i <= 27; i++) {
			if (i != slot) {
				Utils.createItemByte(inv, Material.STAINED_GLASS_PANE, 0, 1, i, "&cTime Selected");
				continue;
			}
			placeNetherStar(inv);
		}
	}

	public static void replaceRankGlass(Inventory inv, int slot) {
		slot++;
		for (int i = 28; i <= 36; i++) {
			if (i != slot) {
				Utils.createItemByte(inv, Material.STAINED_GLASS_PANE, 0, 1, i, "&cRank Selected");
				continue;
			}
			placeNetherStar(inv);
		}
	}

	public static void placeNetherStar(Inventory inv) {
		UUID uuid = target.getUniqueId();
		if (timeMap.get(uuid) != null && rankMap.get(uuid) != null) {
			Utils.createItem(inv, Material.NETHER_STAR, 1, 5,
					"&ePromote " + target.getName() + " to " + rankMap.get(uuid));
		}
	}

	public static void setTime(Player p, long futureTime, JavaPlugin plugin, String rank) {
		String uuid = target.getUniqueId().toString();
		FilesManager filesmanager = new FilesManager((Main) plugin);
		FileConfiguration playerdata = filesmanager.getDataConfig("playerdata");
		ConfigurationSection uuidSection = playerdata.getConfigurationSection(uuid);
		if (uuidSection != null) {
			p.sendMessage(Utils.chat(plugin.getConfig().getString("messages.AlreadyPromoted")));
			p.closeInventory();
			return;
		}
		playerdata.createSection(uuid);
		ConfigurationSection section = playerdata.getConfigurationSection(uuid);
		section.set("Time", futureTime);
		section.set("Role", rank);
		p.closeInventory();

		Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
				plugin.getConfig().getString("AddRoleCmd").replace("%user%", target.getName()).replace("%rank%", rank));
		File playerDataFile = filesmanager.getFile("playerdata");
		try {
			playerdata.save(playerDataFile);
			p.sendMessage(Utils.chat(plugin.getConfig().getString("messages.PromoteMessage")
					.replace("%target%", target.getName()).replace("%rank%", rank)));
			Bukkit.getScheduler().cancelTasks(plugin);
			RankTimer timer = new RankTimer((Main) plugin);
			timer.startTimer();
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

	}

}
