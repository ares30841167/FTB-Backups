package com.feed_the_beast.mods.ftbbackups.webhook.model;

import com.google.gson.JsonObject;

public class BackupDoneNotification {
    private String backupFileName;

    public BackupDoneNotification(String backupFileName) {
        this.backupFileName = backupFileName;
    }

    public JsonObject toJsonObject()
    {
        JsonObject o = new JsonObject();
        o.addProperty("event", "DONE");
        o.addProperty("backupFilename", backupFileName);
        return o;
    }

    public String toJsonString()
    {
        JsonObject o = this.toJsonObject();
        return o.toString();
    }
}
