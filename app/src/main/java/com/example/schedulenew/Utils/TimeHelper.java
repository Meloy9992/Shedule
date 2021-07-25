package com.example.schedulenew.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeHelper {

    public static boolean getTimeBreak (JSONObject activeTime){
        String currentTime = new SimpleDateFormat("HH" + ":00").format(Calendar.getInstance().getTime()); // текущий час
        String time = null;
        String[] timeSplit;

        try {
            time = activeTime.getString("time");
            timeSplit  = time.split("-"); // разбиваем его чтобы получить два значения
            if (timeSplit [0].equals(currentTime) == true){
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

}
