package com.zpedroo.sellitems.utils.config;

import com.zpedroo.sellitems.utils.FileUtils;
import com.zpedroo.sellitems.utils.color.Colorize;

public class Status {

    public static String ENABLED = Colorize.getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Status.enabled"));

    public static String DISABLED = Colorize.getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Status.disabled"));
}