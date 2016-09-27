package com.example.a76568.weathertest;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorCompletionService;

import cn.edu.pku.hanqin.util.NetUtil;

/**
 * Created by 76568 on 2016/9/20 0020.
 */
public class MainActivity extends Activity implements View.OnClickListener{
    private ImageView mUpdateBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_infos);

        if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
            Log.d("myWeather", "网络不错");
            /*Toast.makeText(MainActivity.this, "网络不错!", Toast.LENGTH_LONG).show();*/
        } else {
            Log.d("myWeather", "网络不行");
            Toast.makeText(MainActivity.this, "网络不行！", Toast.LENGTH_LONG).show();
        }

        mUpdateBtn = (ImageView) findViewById(R.id.title_update_btn);
        mUpdateBtn.setOnClickListener(this);
      }
        private void queryWeatherCode(String cityCode) {
        final String address = "http://wthrcdn.etouch.cn/Weather Api?citykey="	+	cityCode;
        Log.d("myWeather", address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con = null;
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


        @Override
        public void onClick(View view){
            if(view.getId() == R.id.title_update_btn){
                SharedPreferences sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
                String cityCode = sharedPreferences.getString("main_city_code","101010100");
                Log.d("myWeather",cityCode);

                if(NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE){
                    Log.d("myWeather","网络可以~");
                    queryWeatherCode(cityCode);
                }else{
                    Log.d("myWeather","网络爆炸");
                    Toast.makeText(MainActivity.this, "网络炸了！", Toast.LENGTH_LONG).show();
                }
        }
    }
}
