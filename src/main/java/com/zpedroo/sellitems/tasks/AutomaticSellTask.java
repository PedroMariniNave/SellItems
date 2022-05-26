package com.zpedroo.sellitems.tasks;

import com.zpedroo.sellitems.manager.DataManager;
import com.zpedroo.sellitems.manager.SellManager;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

import static com.zpedroo.sellitems.utils.config.Settings.AUTOMATIC_SELL_INTERVAL;
import static com.zpedroo.sellitems.utils.config.Settings.AUTOMATIC_SELL_PERMISSION;

public class AutomaticSellTask extends BukkitRunnable {

    public AutomaticSellTask(Plugin plugin) {
        this.runTaskTimerAsynchronously(plugin, AUTOMATIC_SELL_INTERVAL, AUTOMATIC_SELL_INTERVAL);
    }

    @Override
    public void run() {
        new ArrayList<>(DataManager.getInstance().getAutoSellingPlayers()).forEach(player -> {
            if (player == null || !player.isOnline() || !player.hasPermission(AUTOMATIC_SELL_PERMISSION)) {
                DataManager.getInstance().setAutoSelling(player, false);
                return;
            }

            SellManager.getInstance().sellPlayerItems(player, false);
        });
    }
}
