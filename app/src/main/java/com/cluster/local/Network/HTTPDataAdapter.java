package com.cluster.local.Network;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


/**
 * Created by Jonas on 6/10/2017.
 */

public class HTTPDataAdapter {

    public static final byte UNKNOWN_ERROR = -1;
    public static final byte EMPTY_RESULT = -2;
    public static final byte ZERO_RESULTS = -3;
    public static final byte OVER_QUERY_LIMIT = -4;
    public static final byte REQUEST_DENIED = -5;
    public static final byte INVALID_REQUEST = -6;

    public HTTPDataAdapter() {
    }


   private  String response = "";

    public String getHTTPData(String requestURL) {
        try {

           URL url = new URL(requestURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = bufferedReader.readLine()) != null) {
                    response += line;
                }
            }
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getHTTPSData(String requestURL) {
        try {

            URL url = new URL(requestURL);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = bufferedReader.readLine()) != null) {
                    response += line;
                }
            }
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


}
