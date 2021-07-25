package com.example.schedulenew.thread;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.schedulenew.Utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;

public class WEBQueryTask extends AsyncTask<URL, Void, String> {

    private Consumer<String> lambda;

    public WEBQueryTask(Consumer<String> lambda){

        this.lambda = lambda;
    }

    @Override
    protected String doInBackground(URL... urls) {
        String response = null;
        try {
            response = NetworkUtils.getResponseFromURL(urls[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  response;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onPostExecute(String s) {
        lambda.accept(s);
    }
}
