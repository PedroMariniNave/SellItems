package com.zpedroo.sellitems.utils.config;

import com.zpedroo.sellitems.utils.FileUtils;
import com.zpedroo.sellitems.utils.color.Colorize;

import java.util.List;

public class Settings {

    public static final String COMMAND = FileUtils.get().getString(FileUtils.Files.CONFIG, "Settings.command");

    public static final List<String> ALIASES = FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Settings.aliases");

    public static final String NOTHING_DISPLAY = Colorize.getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Settings.nothing-display"));

    public static final String CURRENCY_SEPARATOR = Colorize.getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Settings.currency-separator"));

    public static final String AUTOMATIC_SELL_PERMISSION = FileUtils.get().getString(FileUtils.Files.CONFIG, "Settings.automatic-sell.permission");

    public static final long AUTOMATIC_SELL_INTERVAL = FileUtils.get().getLong(FileUtils.Files.CONFIG, "Settings.automatic-sell.interval");
}