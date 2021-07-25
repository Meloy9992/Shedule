package com.example.schedulenew;

import android.app.NotificationManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.schedulenew.Utils.NetworkUtils;
import com.example.schedulenew.Utils.TimeHelper;
import com.example.schedulenew.service.NotificationService;
import com.example.schedulenew.thread.WEBQueryTask;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.function.Consumer;

public class MainActivity extends AppCompatActivity {
    private Runnable runnable;
    private Handler handler;
    private String actualTime;
    private String currentTime;

    TextView text;
    ImageView pictogram;
    TextView mainText;
    ImageView imageClock2;
    ImageView imageClock1;
    CheckBox checkBox;

    public synchronized void image(String URL) {
        Picasso.with(this).load(URL).into(pictogram);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textUserSystemTime = findViewById(R.id.textClockUser);
        activeTime(textUserSystemTime);
        ImageView mainImage = findViewById(R.id.MainUserClock);
        mainImage.setImageResource(pictogramTime());
        imageClock1 = findViewById(R.id.clockTimeAction);
        imageClock2 = findViewById(R.id.clock2TimeAction);
        pictogram = findViewById(R.id.imageTime);
        text = findViewById(R.id.commonTimeAction);
        mainText = findViewById(R.id.nameTime);
        checkBox = findViewById(R.id.checkBox);

        URL generatedURL = NetworkUtils.generatedUrl();
        WEBQueryTask task = new WEBQueryTask((resp) -> {
            try {
                byte[] bytes = resp.getBytes(StandardCharsets.UTF_8);

                String response = new String(bytes, StandardCharsets.UTF_8);
                JSONObject mainObject = new JSONObject(response);
                JSONObject uniObject = mainObject.getJSONObject("activities");
                for (Iterator<String> it = uniObject.keys(); it.hasNext(); ) {
                    String key = it.next();
                    Object object = uniObject.opt(key);
                    if (object == JSONObject.NULL) {
                        continue;
                    }
                    JSONObject data = new JSONObject(object.toString());
                    if (TimeHelper.getTimeBreak(data)) {
                        setData(data);
                        break;
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        task.execute(generatedURL);
        TimerNotification();

    }

    private void setData(JSONObject data) {
        try {
            text.setText(data.getString("time"));
            mainText.setText(data.getString("name"));
            image(data.getString("pictogram"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        imageClock2.setImageResource(pictogramTime(NetworkUtils.getTime(data)));
        imageClock1.setImageResource(pictogramTime());
    }

    public void TimerNotification() {
        URL generatedURL = NetworkUtils.generatedUrl();
        Intent intent = new Intent(this, NotificationService.class);
        startService(intent);
    }

    public void activeTime(TextView textUserSystemTime) {
        handler = new Handler();
        runnable = () -> {
            actualTime = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
            textUserSystemTime.setText(actualTime);
            handler.postDelayed(runnable, 1000);
        };
        handler.postDelayed(runnable, 1000);
    } // Постоянная проверка времени

    public int pictogramTime() {
        currentTime = new SimpleDateFormat("HH").format(Calendar.getInstance().getTime());
        return pictogramTime(currentTime);
    } // Перерисовка изображений взависимости от времени устройства (по часам)

    public void onCheckboxClicked(View view) {
        if (checkBox.isChecked()) {
            checkBox.setClickable(false);
        }
    } // обработка нажатия на checkBox

    public int pictogramTime(String activeTime) {
        switch (activeTime) {
            case "00": {
                return R.drawable.time12;
            }
            case "01": {
                return R.drawable.time01;
            }
            case "02": {
                return R.drawable.time02;
            }
            case "03": {
                return R.drawable.time03;
            }
            case "04": {
                return R.drawable.time04;
            }
            case "05": {
                return R.drawable.time05;
            }
            case "06": {
                return R.drawable.time06;
            }
            case "07": {
                return R.drawable.time07;
            }
            case "08": {
                return R.drawable.time08;
            }
            case "09": {
                return R.drawable.time09;
            }
            case "10": {
                return R.drawable.time10;
            }
            case "11": {
                return R.drawable.time11;
            }
            case "12": {
                return R.drawable.time12;
            }
            case "13": {
                return R.drawable.time01;
            }
            case "14": {
                return R.drawable.time02;
            }
            case "15": {
                return R.drawable.time03;
            }
            case "16": {
                return R.drawable.time04;
            }
            case "17": {
                return R.drawable.time05;
            }
            case "18": {
                return R.drawable.time06;
            }
            case "19": {
                return R.drawable.time07;
            }
            case "20": {
                return R.drawable.time08;
            }
            case "21": {
                return R.drawable.time09;
            }
            case "22": {
                return R.drawable.time10;
            }
            case "23": {
                return R.drawable.time11;
            }
            default:
                return R.drawable.error;
        }
    } // Перерисовка изображений взависимости от переданного времени (Берется из JSONObject)



}