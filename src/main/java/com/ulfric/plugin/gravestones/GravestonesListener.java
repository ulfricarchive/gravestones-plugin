package com.ulfric.plugin.gravestones;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockCanChangeEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.ulfric.commons.bukkit.event.EventHelper;

public class GravestonesListener implements Listener {

	@EventHandler
	public void on(PlayerDeathEvent event) {
		Player player = event.getEntity();
		Block block = player.getLocation().getBlock();
		Player killer = player.getKiller();

		if (!canPlaceGravestone(player, killer, block)) {
			return;
		}

		placeGravestone(player, killer, block);
	}

	private boolean canPlaceGravestone(Player player, Player killer, Block block) {
		if (block.getType() != Material.AIR) {
			return false;
		}

		if (!isChangeable(block, player)) {
			if (killer == null) {
				return false;
			}

			return isChangeable(block, killer);
		}

		if (!EventHelper.call(new PlayerGravestoneCreateEvent(player))) {
			return false;
		}

		return true;
	}

	private boolean isChangeable(Block block, Player changer) {
		return EventHelper.called(new BlockCanChangeEvent(block, changer, true)).isChangeable();
	}

	private void placeGravestone(Player player, Player killer, Block block) { // TODO use Details?
		block.setType(Material.SIGN_POST); // TODO we should actually try to make this a wall sign if needed

		Sign sign = (Sign) block.getState();
		sign.setLine(0, ChatColor.BOLD + "GRAVESTONE");
		sign.setLine(1, player.getName());
		if (killer != null) {
			sign.setLine(2, "killed by");
			sign.setLine(3, killer.getName());
		}
		sign.update(false, false);
	}

	@EventHandler(ignoreCancelled = true)
	public void on(SignChangeEvent event) {
		String line = event.getLine(0);

		if (StringUtils.isEmpty(line)) {
			return;
		}

		if (!isGravestone(line)) {
			return;
		}

		if (event.getPlayer().hasPermission("gravestones-manual-create")) {
			return;
		}

		event.setLine(0, "FAKE NEWS"); // TODO this might be too edgy
	}

	private boolean isGravestone(String text) {
		text = text.trim().toLowerCase();
		text = ChatColor.translateAlternateColorCodes('&', text);
		text = ChatColor.stripColor(text);
		if (text.startsWith("[")) {
			text = text.substring(1);

			if (text.endsWith("]")) {
				text = text.substring(0, text.length() - 1);
			}

			text = text.trim();
		}
		return text.equals("gravestone");
	}

}
