package com.example.a76568.weathertest;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import cn.edu.pku.hanqin.bean.TodayWeather;
import cn.edu.pku.hanqin.util.NetUtil;


/**
 * Created by 76568 on 2016/11/15 0015.
 */
public class WeatherService extends Service {

    int counter = 0;
    static final int UPDATE_INTERVAL = 100000;
    private Timer timer = new Timer();

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d("good", "create");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {        // We want this service to continue running until it is explicitly        // stopped, so return sticky.
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        updatetheWeather();
        return START_STICKY;
    }

    public void updatetheWeather() {
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                Log.d("MyService", String.valueOf(++counter));
                SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
                String cityCode = sharedPreferences.getString("main_city_code", "101010100");
                final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
                Log.d("myWeather", address);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpURLConnection con = null;
                        TodayWeather todayWeather = null;
                        try {
                            URL url = new URL(address);
                            con = (HttpURLConnection) url.openConnection();
                            con.setRequestMethod("GET");
                            con.setConnectTimeout(8000);
                            con.setReadTimeout(8000);
                            InputStream in = con.getInputStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                            StringBuilder response = new StringBuilder();
                            String str;
                            while ((str = reader.readLine()) != null) {
                                response.append(str);
                                Log.d("myWeather", str);
                            }
                            String responsStr = response.toString();
                            Log.d("myWeather", responsStr);
                            if (responsStr != null) {
                                SharedPreferences mySharedPreferences = getSharedPreferences("config",
                                        Activity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = mySharedPreferences.edit();
                                editor.putString("DefaultWeather3",responsStr);
                                editor.commit();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (con != null) {
                                con.disconnect();
                            }
                        }
                    }
                }).start();
            }
        }, 0, UPDATE_INTERVAL);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        Log.d("good", "end");
    }
}
