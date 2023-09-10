package com.yeungjin.translogic.database;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Connector extends Thread {
   private static final String ADDRESS = "http://152.70.237.174/";

   private String URL;
   private String response;

   public Connector(String URI) {
      this.URL = ADDRESS + URI;
   }

   @Override
   public void run() {
      response = request();
   }

   public String getResponse() {
      return response;
   }

   private String request() {
      StringBuilder response = new StringBuilder();

      try {
         HttpURLConnection http = (HttpURLConnection) new URL(URL).openConnection();

         if (http != null) {
            http.setConnectTimeout(10000);
            http.setRequestMethod("GET");
            http.setDoInput(true);

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
               BufferedReader contents = new BufferedReader(new InputStreamReader(http.getInputStream()));

               String content;
               while ((content = contents.readLine()) != null) {
                  if (content.length() != 0) {
                     response.append(content);
                  }
               }

               contents.close();
               http.disconnect();
            }
         }
      } catch (Exception error) {
         error.printStackTrace();
      }

      return response.toString();
   }
}
