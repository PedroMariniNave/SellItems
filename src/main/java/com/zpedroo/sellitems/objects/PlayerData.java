package com.zpedroo.sellitems.objects;

import java.util.UUID;

public class PlayerData {

    private final UUID uuid;
    private boolean shiftSell;
    private boolean update;

    public PlayerData(UUID uuid, boolean shiftSell) {
        this.uuid = uuid;
        this.shiftSell = shiftSell;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public boolean isShiftSell() {
        return shiftSell;
    }

    public boolean isQueueUpdate() {
        return update;
    }

    public void setShiftSell(boolean shiftSell) {
        this.shiftSell = shiftSell;
        this.update = true;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }
}