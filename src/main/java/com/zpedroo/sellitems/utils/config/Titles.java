package com.zpedroo.sellitems.utils.config;

import com.zpedroo.sellitems.utils.FileUtils;
import com.zpedroo.sellitems.utils.color.Colorize;

public class Titles {

    public static final String[] SOLD_TITLES = new String[]{
            Colorize.getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Titles.sold.title")),
            Colorize.getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Titles.sold.subtitle"))
    };

    public static final String[] NOTHING_TITLES = new String[]{
            Colorize.getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Titles.nothing.title")),
            Colorize.getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Titles.nothing.subtitle"))
    };
}