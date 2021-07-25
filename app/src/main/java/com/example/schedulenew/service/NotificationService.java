package com.example.schedulenew.service;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;

import com.example.schedulenew.R;
import com.example.schedulenew.Utils.NetworkUtils;
import com.example.schedulenew.Utils.TimeHelper;
import com.example.schedulenew.thread.WEBQueryTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.Iterator;

public class NotificationService extends IntentService {

    public static final int NOTIFY_ID = 101;     // Идентификатор уведомления
    public static final String CHANNEL_ID = "time_to_do";     // Идентификатор канала
    private WEBQueryTask task;
    private URL apiUrl;
    private Context context;
    private NotificationManager notificationManager;


    public NotificationService() {
        super("name");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private void buildNotify() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.lunchtime)
                .setContentTitle("Уведомление")
                .setContentText("Откройте программу")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        CreateChannelIfNeeded(notificationManager);
        notificationManager.notify(NOTIFY_ID, builder.build());
    }

    private void CreateChannelIfNeeded(NotificationManager manager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel =
                    new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(notificationChannel);
        }
    }

    @SuppressLint("WrongThread")
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        while (!isRestricted()) {
            try {
                WEBQueryTask task = new WEBQueryTask((resp) -> {
                    try {
                        JSONObject mainObject = new JSONObject(resp);
                        JSONObject uniObject = mainObject.getJSONObject("activities");
                        for (Iterator<String> it = uniObject.keys(); it.hasNext(); ) {
                            String key = it.next();
                            Object object = uniObject.opt(key);
                            if (object == JSONObject.NULL) {
                                continue;
                            }
                            JSONObject data = new JSONObject(object.toString());
                            if (TimeHelper.getTimeBreak(data)) {
                                buildNotify();
                                break;
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
                task.execute(NetworkUtils.generatedUrl());

                Thread.sleep(50 * 60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
