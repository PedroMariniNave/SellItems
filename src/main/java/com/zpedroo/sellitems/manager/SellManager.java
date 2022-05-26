package com.zpedroo.sellitems.manager;

import com.zpedroo.multieconomy.api.CurrencyAPI;
import com.zpedroo.multieconomy.objects.general.Currency;
import com.zpedroo.sellitems.SellItems;
import com.zpedroo.sellitems.objects.SellItem;
import com.zpedroo.sellitems.utils.config.Settings;
import com.zpedroo.sellitems.utils.config.Titles;
import com.zpedroo.sellitems.utils.formatter.NumberFormatter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class SellManager {

    private static SellManager instance;
    public static SellManager getInstance() { return instance; }

    public SellManager() {
        instance = this;
    }

    public void sellPlayerItems(Player player, boolean sendNothingTitle) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Object[] values = getItemsPriceAndAmount(player);
                Map<Currency, BigInteger> prices = (Map<Currency, BigInteger>) values[0];
                if (prices.isEmpty()) {
                    if (sendNothingTitle) player.sendTitle(Titles.NOTHING_TITLES[0], Titles.NOTHING_TITLES[1]);
                    return;
                }

                int itemsAmount = (int) values[1];

                for (Map.Entry<Currency, BigInteger> entry : prices.entrySet()) {
                    Currency currency = entry.getKey();
                    BigInteger price = entry.getValue();

                    CurrencyAPI.addCurrencyAmount(player.getUniqueId(), currency, price);
                }

                String[] placeholders = new String[]{
                        "{value}",
                        "{amount}"
                };
                String[] replacers = new String[]{
                        getDisplayValueString(prices),
                        NumberFormatter.getInstance().formatDecimal(itemsAmount)
                };

                player.sendTitle(StringUtils.replaceEach(Titles.SOLD_TITLES[0], placeholders, replacers), StringUtils.replaceEach(Titles.SOLD_TITLES[1], placeholders, replacers));
                removeItemsFromInventory(player);
            }
        }.runTaskLaterAsynchronously(SellItems.get(), 0L);
    }

    public String getDisplayValueString(Map<Currency, BigInteger> currencyValues) {
        StringBuilder builder = new StringBuilder();

        for (Map.Entry<Currency, BigInteger> entry : currencyValues.entrySet()) {
            Currency currency = entry.getKey();
            BigInteger price = entry.getValue();

            if (builder.length() > 0) builder.append(Settings.CURRENCY_SEPARATOR);

            builder.append(currency.getAmountDisplay(price));
        }

        return builder.length() <= 0 ? Settings.NOTHING_DISPLAY : builder.toString();
    }

    private void removeItemsFromInventory(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null || item.getType().equals(Material.AIR)) continue;

            SellItem sellItem = DataManager.getInstance().getSellItem(item);
            if (sellItem != null) player.getInventory().removeItem(item);
        }
    }

    public Object[] getItemsPriceAndAmount(Player player) {
        Map<Currency, BigInteger> prices = new HashMap<>(2);
        int itemsAmount = 0;
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null || item.getType().equals(Material.AIR)) continue;

            SellItem sellItem = DataManager.getInstance().getSellItem(item);
            if (sellItem == null) continue;

            boolean canSell = false;
            int itemAmount = item.getAmount();
            for (Currency currency : sellItem.getCurrencies()) {
                if (sellItem.hasCurrencyPermission(currency) && !player.hasPermission(sellItem.getCurrencyPermission(currency))) continue;

                BigInteger actualCurrencyPrice = prices.getOrDefault(currency, BigInteger.ZERO);
                BigInteger itemPrice = sellItem.getCurrencyValue(currency).multiply(BigInteger.valueOf(itemAmount));
                BigInteger newCurrencyPrice = actualCurrencyPrice.add(itemPrice);

                prices.put(currency, newCurrencyPrice);
                canSell = true;
            }

            if (canSell) itemsAmount += itemAmount;
        }

        return new Object[] { prices, itemsAmount };
    }
}