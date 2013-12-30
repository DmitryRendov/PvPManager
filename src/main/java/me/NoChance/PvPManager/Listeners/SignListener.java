package me.NoChance.PvPManager.Listeners;

import me.NoChance.PvPManager.PvPManager;
import me.NoChance.PvPManager.Config.Messages;
import me.NoChance.PvPManager.Others.Utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignListener implements Listener {

	private PvPManager plugin;

	public SignListener(PvPManager plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onSignPlace(SignChangeEvent e) {
		Player p = e.getPlayer();
		if (Utils.PMAllowed(p.getWorld().getName())) {
			if (p.hasPermission("pvpmanager.sign")) {
				if (e.getLine(0).equalsIgnoreCase("[PvPManager]")) {
					e.setLine(0, "�5[PvPManager]");
					if (e.getLine(1).isEmpty() && e.getLine(2).isEmpty() && e.getLine(3).isEmpty()) {
						e.setLine(1, "Click This");
						e.setLine(2, "Sign to Change");
						e.setLine(3, "Your PvP Status");
					}
					p.sendMessage(ChatColor.GOLD + "[PvPManager]" + ChatColor.DARK_GREEN + "PvPToggle Sign Created Successfully!");
				}
			}
		}
	}

	@EventHandler
	public void onSignInteract(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		if (Utils.PMAllowed(player.getWorld().getName())) {
			Block clicked = e.getClickedBlock();
			if (clicked != null) {
				if (clicked.getType().equals(Material.SIGN_POST) || clicked.getType().equals(Material.WALL_SIGN)) {
					if (player.hasPermission("pvpmanager.pvpstatus.change")) {
						Sign sign = (Sign) clicked.getState();
						if (sign.getLine(0).equalsIgnoreCase("�5[PvPManager]")) {
							if (plugin.getCm().checkToggleCooldown(player)) {
								if (plugin.getCm().hasPvpEnabled(player.getName())) {
									plugin.getCm().disablePvp(player);
									player.sendMessage(Messages.PvP_Disabled);
								} else {
									plugin.getCm().enablePvp(player);
									player.sendMessage(Messages.PvP_Enabled);
								}
							}
							return;
						}
					} else {
						player.sendMessage(ChatColor.DARK_RED + "You don't have permission!");
					}
				}
			}
		}
	}

}