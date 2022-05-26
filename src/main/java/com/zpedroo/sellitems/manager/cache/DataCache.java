package com.zpedroo.sellitems.manager.cache;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.zpedroo.multieconomy.api.CurrencyAPI;
import com.zpedroo.multieconomy.objects.general.Currency;
import com.zpedroo.sellitems.objects.PlayerData;
import com.zpedroo.sellitems.objects.SellItem;
import com.zpedroo.sellitems.utils.FileUtils;
import com.zpedroo.sellitems.utils.formatter.NumberFormatter;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataCache {

    private final List<Player> automaticSellingPlayers = new ArrayList<>(8);
    private final Map<Player, PlayerData> playerData = new HashMap<>(128);
    private final List<SellItem> sellItems = getSellItemsFromConfig();

    public List<Player> getAutomaticSellingPlayers() {
        return automaticSellingPlayers;
    }

    public Map<Player, PlayerData> getPlayerData() {
        return playerData;
    }

    public List<SellItem> getSellItems() {
        return sellItems;
    }

    private List<SellItem> getSellItemsFromConfig() {
        List<SellItem> ret = new ArrayList<>(8);

        FileUtils.Files file = FileUtils.Files.CONFIG;
        for (String materials : FileUtils.get().getSection(file, "Items")) {
            List<String> values = FileUtils.get().getStringList(file, "Items." + materials + ".values");

            String[] dataSplit = materials.split(":");
            String materialName = dataSplit[0].toUpperCase();
            Material material = Material.getMaterial(materialName);
            Validate.notNull(material, "Material cannot be null! Invalid material: " + materialName);

            byte data = dataSplit.length <= 1 ? 0 : Byte.valueOf(dataSplit[1]);

            Table<Currency, BigInteger, String> currenciesValueAndPermission = HashBasedTable.create();

            for (String str : values) {
                String[] valueSplit = str.split(",");
                if (valueSplit.length <= 1) continue;

                Currency currency = CurrencyAPI.getCurrency(valueSplit[0]);
                if (currency == null) continue;

                BigInteger value = NumberFormatter.getInstance().filter(valueSplit[1]);
                if (value.signum() <= 0) continue;

                String permission = valueSplit.length >= 3 ? valueSplit[2] : "NULL";

                currenciesValueAndPermission.put(currency, value, permission);
            }

            ret.add(new SellItem(material, data, currenciesValueAndPermission));
        }

        return ret;
    }
}