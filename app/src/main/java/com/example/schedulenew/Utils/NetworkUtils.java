package com.example.schedulenew.Utils;

import com.example.schedulenew.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class NetworkUtils {
    public static String generateCurrentDayMonthYear() {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        Date qw = new Date();
        String formatData = format.format(qw);
        return formatData;
    } // Получение сегодняшнего дня

    public static URL generatedUrl() {
        String generateUrl = "https://daily-routine-api.herokuapp.com/day/" + generateCurrentDayMonthYear();
        URL url = null;
        try {
            url = new URL(generateUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    } // Генерация URL

    public static String getResponseFromURL(URL url) throws IOException {
        String response = new String();
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            // scanner.useDelimiter("\\A");
            while (scanner.hasNext()) response += scanner.next();
        }
        finally{
                urlConnection.disconnect();
            }

        return response;
    }
    // Получение ответа от URL
    public static String getTime (JSONObject activitiesTime) {
        String activeTime = null;
        String[] timeSplit;
        String time2 = null;
        try {
            activeTime = activitiesTime.getString("time"); //получаем время из json
            timeSplit  = activeTime.split("-"); // разбиваем его чтобы получить два значения
            time2 = timeSplit [1]; // второе время например 17:00
            timeSplit = time2.split(":");
            return timeSplit [0];
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return time2;
    }

}
