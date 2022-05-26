package com.zpedroo.sellitems.utils.menu;

import com.zpedroo.multieconomy.objects.general.Currency;
import com.zpedroo.sellitems.manager.DataManager;
import com.zpedroo.sellitems.manager.SellManager;
import com.zpedroo.sellitems.objects.PlayerData;
import com.zpedroo.sellitems.utils.FileUtils;
import com.zpedroo.sellitems.utils.builder.InventoryBuilder;
import com.zpedroo.sellitems.utils.builder.InventoryUtils;
import com.zpedroo.sellitems.utils.builder.ItemBuilder;
import com.zpedroo.sellitems.utils.color.Colorize;
import com.zpedroo.sellitems.utils.config.Settings;
import com.zpedroo.sellitems.utils.config.Status;
import com.zpedroo.sellitems.utils.formatter.NumberFormatter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.Map;

public class Menus extends InventoryUtils {

    private static Menus instance;
    public static Menus getInstance() { return instance; }

    public Menus() {
        instance = this;
    }

    public void openMainMenu(Player player) {
        FileUtils.Files file = FileUtils.Files.MAIN;

        String title = Colorize.getColored(FileUtils.get().getString(file, "Inventory.title"));
        int size = FileUtils.get().getInt(file, "Inventory.size");

        InventoryBuilder inventory = new InventoryBuilder(title, size);
        PlayerData data = DataManager.getInstance().getPlayerData(player);

        Object[] values = SellManager.getInstance().getItemsPriceAndAmount(player);

        Map<Currency, BigInteger> itemsPrice = (Map<Currency, BigInteger>) values[0];
        int itemsAmount = (int) values[1];
        boolean shiftSelling = data.isShiftSell();
        boolean autoSelling = DataManager.getInstance().isAutoSelling(player);

        boolean canAutoSellItems = player.hasPermission(Settings.AUTOMATIC_SELL_PERMISSION);

        String lockedLore = Colorize.getColored(FileUtils.get().getString(file, "Lore-Filter.locked"));
        String unlockedLore = Colorize.getColored(FileUtils.get().getString(file, "Lore-Filter.unlocked"));

        for (String items : FileUtils.get().getSection(file, "Inventory.items")) {
            ItemStack item = ItemBuilder.build(FileUtils.get().getFile(file).getFileConfiguration(), "Inventory.items." + items, new String[]{
                    "{value}",
                    "{items_amount}",
                    "{shift_sell_status}",
                    "{auto_sell_status}",
                    "{auto_sell_lore}"
            }, new String[]{
                    SellManager.getInstance().getDisplayValueString(itemsPrice),
                    NumberFormatter.getInstance().formatDecimal(itemsAmount),
                    shiftSelling ? Status.ENABLED : Status.DISABLED,
                    autoSelling ? Status.ENABLED : Status.DISABLED,
                    canAutoSellItems ? unlockedLore : lockedLore
            }).build();
            int slot = FileUtils.get().getInt(file, "Inventory.items." + items + ".slot");
            String action = FileUtils.get().getString(file, "Inventory.items." + items + ".action");

            inventory.addItem(item, slot, () -> {
                switch (action.toUpperCase()) {
                    case "SELL":
                        SellManager.getInstance().sellPlayerItems(player, true);
                        inventory.close(player);
                        break;
                    case "SHIFT_SELL":
                        data.setShiftSell(!shiftSelling);
                        openMainMenu(player);
                        break;
                    case "AUTO_SELL":
                        if (!canAutoSellItems) return;

                        DataManager.getInstance().setAutoSelling(player, !autoSelling);
                        openMainMenu(player);
                        break;
                }
            }, InventoryUtils.ActionType.ALL_CLICKS);
        }

        inventory.open(player);
    }
}