package com.zpedroo.sellitems.listeners;

import com.zpedroo.sellitems.manager.DataManager;
import com.zpedroo.sellitems.manager.SellManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class PlayerGeneralListeners implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onSneak(PlayerToggleSneakEvent event) {
        if (!event.isSneaking() || !DataManager.getInstance().getPlayerData(event.getPlayer()).isShiftSell()) return;

        SellManager.getInstance().sellPlayerItems(event.getPlayer(), true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        DataManager.getInstance().savePlayerData(event.getPlayer());
    }
}