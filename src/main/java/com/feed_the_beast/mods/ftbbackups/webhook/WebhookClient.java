package com.feed_the_beast.mods.ftbbackups.webhook;

import com.feed_the_beast.mods.ftbbackups.FTBBackupsConfig;
import com.feed_the_beast.mods.ftbbackups.webhook.model.BackupDoneNotification;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class WebhookClient
{
  private String targetURL;

  public WebhookClient() {
    this.targetURL = FTBBackupsConfig.webhookURL;
  }

  public CompletableFuture<Boolean> postBackupDoneNotification(BackupDoneNotification notification) {
    return CompletableFuture.supplyAsync(() -> {
      HttpURLConnection connection = null;

      try {
        //Create connection
        URL url = new URL(targetURL);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type",
                "application/json");

        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.setUseCaches(false);
        connection.setDoOutput(true);

        connection.setRequestProperty("Content-Length",
                Integer.toString(notification.toJsonString().getBytes().length));
        connection.setRequestProperty("Content-Language", "en-US");


        //Send request
        DataOutputStream wr = new DataOutputStream (
                connection.getOutputStream());
        wr.writeBytes(notification.toJsonString());
        wr.close();

        //Get response
        InputStream is = connection.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
        String line;
        while ((line = rd.readLine()) != null) {
          response.append(line);
          response.append('\r');
        }
        rd.close();

        if(response.indexOf("200") != -1) {
          return true;
        } else {
          return false;
        }
      } catch (Exception e) {
        return false;
      } finally {
        if (connection != null) {
          connection.disconnect();
        }
      }
    });
  }

}