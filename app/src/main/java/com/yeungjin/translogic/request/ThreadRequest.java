package com.yeungjin.translogic.request;

import com.yeungjin.translogic.utility.Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ThreadRequest extends Thread {
    protected final Map<String, Object> parameters = new HashMap<>();

    private final String URI;
    private String response;

    public ThreadRequest(String URI) {
        this.URI = URI;
    }

    @Override
    public void run() {
        StringBuilder response = new StringBuilder();

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(Server.URL + URI + ".db").openConnection();

            if (connection != null) {
                connection.setConnectTimeout(10000);
                connection.setRequestMethod("POST");
                connection.setDoInput(true);

                if (!parameters.isEmpty()) {
                    StringBuilder value = new StringBuilder();
                    for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
                        if (value.length() != 0) {
                            value.append("&");
                        }
                        value.append(URLEncoder.encode(parameter.getKey(), "UTF-8")).append("=");
                        value.append(URLEncoder.encode(String.valueOf(parameter.getValue()), "UTF-8"));
                    }
                    byte[] bytes = value.toString().getBytes(StandardCharsets.UTF_8);

                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setRequestProperty("Content-Length", String.valueOf(bytes.length));
                    connection.setDoOutput(true);

                    connection.getOutputStream().write(bytes);
                }

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader http = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    String line;
                    while ((line = http.readLine()) != null) {
                        if (line.length() != 0) {
                            response.append(line);
                        }
                    }

                    http.close();
                    connection.disconnect();
                }
            }
        }
        catch (Exception error) {
            error.printStackTrace();
        }

        this.response = response.toString();
    }

    public String getResponse() {
        return response;
    }
}
