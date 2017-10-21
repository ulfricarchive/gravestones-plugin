package com.ulfric.plugin.gravestones;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerGravestoneCreateEvent extends PlayerEvent implements Cancellable {

	private static final HandlerList HANDLERS = new HandlerList();

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	private boolean cancelled;

	public PlayerGravestoneCreateEvent(Player who) {
		super(who);
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}

}
