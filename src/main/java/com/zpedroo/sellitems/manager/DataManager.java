package com.zpedroo.sellitems.manager;

import com.zpedroo.sellitems.manager.cache.DataCache;
import com.zpedroo.sellitems.mysql.DBConnection;
import com.zpedroo.sellitems.objects.PlayerData;
import com.zpedroo.sellitems.objects.SellItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.List;

public class DataManager {

    private static DataManager instance;
    public static DataManager getInstance() { return instance; }

    private final DataCache dataCache = new DataCache();

    public DataManager() {
        instance = this;
    }

    public PlayerData getPlayerData(Player player) {
        PlayerData data = dataCache.getPlayerData().get(player);
        if (data == null) {
            data = DBConnection.getInstance().getDBManager().getPlayerData(player);
            dataCache.getPlayerData().put(player, data);
        }

        return data;
    }

    public SellItem getSellItem(ItemStack item) {
        for (SellItem sellItem : dataCache.getSellItems()) {
            if (sellItem.isSimilar(item)) return sellItem;
        }

        return null;
    }

    public boolean isAutoSelling(Player player) {
        return dataCache.getAutomaticSellingPlayers().contains(player);
    }

    public void setAutoSelling(Player player, boolean status) {
        if (status) {
            dataCache.getAutomaticSellingPlayers().add(player);
        } else {
            dataCache.getAutomaticSellingPlayers().remove(player);
        }
    }

    public List<Player> getAutoSellingPlayers() {
        return dataCache.getAutomaticSellingPlayers();
    }

    public void savePlayerData(Player player) {
        PlayerData data = dataCache.getPlayerData().remove(player);
        if (data == null || !data.isQueueUpdate()) return;

        DBConnection.getInstance().getDBManager().savePlayerData(data);
        data.setUpdate(false);
    }

    public void saveAllPlayersData() {
        new HashSet<>(dataCache.getPlayerData().keySet()).forEach(this::savePlayerData);
    }

    public DataCache getCache() {
        return dataCache;
    }
}