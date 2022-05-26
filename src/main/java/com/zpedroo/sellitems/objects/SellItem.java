package com.zpedroo.sellitems.objects;

import com.google.common.collect.Table;
import com.zpedroo.multieconomy.objects.general.Currency;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.Set;

public class SellItem {

    private final Material material;
    private final byte data;
    private final Table<Currency, BigInteger, String> currenciesValueAndPermission;

    public SellItem(Material material, byte data, Table<Currency, BigInteger, String> currenciesValueAndPermission) {
        this.material = material;
        this.data = data;
        this.currenciesValueAndPermission = currenciesValueAndPermission;
    }

    public Material getMaterial() {
        return material;
    }

    public byte getData() {
        return data;
    }

    public Set<Currency> getCurrencies() {
        return currenciesValueAndPermission.rowKeySet();
    }

    public BigInteger getCurrencyValue(Currency currency) {
        return currenciesValueAndPermission.row(currency).keySet().stream().findFirst().orElse(null);
    }

    public String getCurrencyPermission(Currency currency) {
        return currenciesValueAndPermission.get(currency, getCurrencyValue(currency));
    }

    public boolean hasCurrencyPermission(Currency currency) {
        return !StringUtils.equals(getCurrencyPermission(currency), "NULL");
    }

    public boolean isSimilar(ItemStack item) {
        return item.getType().equals(material) && item.getData().getData() == data;
    }
}
