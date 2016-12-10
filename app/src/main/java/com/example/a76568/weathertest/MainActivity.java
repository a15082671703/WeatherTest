package com.example.a76568.weathertest;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;


import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
//import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import cn.edu.pku.hanqin.bean.TodayWeather;
import cn.edu.pku.hanqin.util.NetUtil;
//import cn.edu.pku.hanqin.bean.TodayWeather;

/**
 * Created by 76568 on 2016/9/20 0020.
 */
public class MainActivity extends Activity implements View.OnClickListener {
    private ImageView mUpdateBtn , mExample , mUpdatePro;
    //
    ExampleService mService;
    //
    private TextView cityTv, timeTv, humidityTv, pmDataTv, pmQualityTv,
            temperatureTv, climateTv, windTv, city_name_Tv, weekTv, temperature_numTv;

    private TextView[] Otherdatatv , Otherwendutv ,Otherfengtv , Othertypetv;
    private ImageView weatherImg, pmImg, mCitySelect;
    //viewpager
    private ViewPagerAdapter vpa;
    private ViewPager vp;
    private List<View> Views;


    //位置更新
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    //


    //    /*更新数据*/
    private static final int UPDATE_TODAY_WEATHER = 1;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case UPDATE_TODAY_WEATHER:
                    updateTodayWeather((TodayWeather) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    /*测试网络*/
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
        mCitySelect = (ImageView) findViewById(R.id.title_city_manager);
        mCitySelect.setOnClickListener(this);
        mUpdatePro = (ImageView)findViewById(R.id.title_update_progress) ;
        mExample = (ImageView) findViewById(R.id.title_share);
        mExample.setOnClickListener(this);
       // startService(new Intent(getBaseContext(),WeatherService.class));
      //  startService(new Intent(getBaseContext(),ExampleService.class));
/*        Intent intent = new Intent(getBaseContext(),ExampleService.class);
        bindService(intent,mConnection, BIND_AUTO_CREATE);
        stopService(intent);*/
        initViewpager();

        //位置更新
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener( myListener );    //注册监听函数
        //

    }

    private ServiceConnection mConnection = new ServiceConnection()
    {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service)
        {
            // We've bound to LocalService, cast the IBinder and get
            // LocalService instance
            ExampleService.LocalBinder binder = (ExampleService.LocalBinder) service;
            mService = binder.getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0)
        {

        }
    };
    @Override
    public void onStart(){
        super.onStart();
        TodayWeather todayWeather = null;
        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        String Default = sharedPreferences.getString("DefaultWeather","NO_info");
//
        initView();
        Log.d("good",Default);
        if(Default!="NO_info" && Default.length()>300) {
            todayWeather = parsXML(Default);
            if (todayWeather != null) {
                Log.d("myWeather", todayWeather.toString());
                Message msg = new Message();
                msg.what = UPDATE_TODAY_WEATHER;
                msg.obj = todayWeather;
                mHandler.sendMessage(msg);
            }
        }
//
    }

    /*初始化viewpager*/
    private void initViewpager(){
        LayoutInflater inflater = LayoutInflater.from(this);
        Views = new ArrayList<View>();
        //
        Views.add(inflater.inflate(R.layout.page1,null));
        Views.add(inflater.inflate(R.layout.page2,null));
        //
        vpa = new ViewPagerAdapter(Views,this);
        vp = (ViewPager) findViewById(R.id.viewpager);
        vp.setAdapter(vpa);
      //  vp.setOnPageChangeListener(this);
    }
    /*等待接受数据*/
    public void queryWeatherCode(String cityCode) {
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
                    //
                    if(responsStr!=null){
                        SharedPreferences mySharedPreferences = getSharedPreferences("config",
                                Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = mySharedPreferences.edit();
                        editor.putString("DefaultWeather",responsStr);
                        editor.commit();
                    }
                    //
                    Log.d("myWeather", responsStr);
                    todayWeather = parsXML(responsStr);
                    if (todayWeather != null ) {
                        Log.d("myWeather", todayWeather.toString());
                        Message msg = new Message();
                        msg.what = UPDATE_TODAY_WEATHER;
                        msg.obj = todayWeather;
                        mHandler.sendMessage(msg);

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

    /*点击按钮*/
    @Override
    public void onClick(View view) {
        Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.title_update_anim);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        if (view.getId() == R.id.title_city_manager) {
            Intent inten = new Intent(this, SelectCity.class);
            startActivityForResult(inten, 1);
        }

        if (view.getId() == R.id.title_update_btn) {
            SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
            String cityCode = sharedPreferences.getString("main_city_code", "101010100");
            Log.d("myWeather", cityCode);

            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                Log.d("myWeather", "网络可以~");
                if (operatingAnim != null) {
                    mUpdatePro.setVisibility(View.VISIBLE);
                    mUpdateBtn.setVisibility(View.GONE);
                    mUpdatePro.startAnimation(operatingAnim);
                }
                queryWeatherCode(cityCode);
               // mUpdateBtn.clearAnimation();
            } else {
                Log.d("myWeather", "网络爆炸");
                Toast.makeText(MainActivity.this, "网络炸了！", Toast.LENGTH_LONG).show();
            }
        }
        if (view.getId() == R.id.title_share) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},0);
            }
            Log.d("位置","开始");
            mLocationClient.start();
            Log.d("位置","结束");
            stopService(new Intent(getBaseContext(),ExampleService.class));
        }
    }

    /*解析得到的数据*/
    private TodayWeather parsXML(String xmldata) {
        TodayWeather todayWeather = null;
        int fengxiangCount = 0;
        int fengliCount = 0;
        int dateCount = 0;
        int highCount = 0;
        int lowCount = 0;
        int typeCount = 0;
        int detailCount = 0;
        try {
            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = fac.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            int eventType = xmlPullParser.getEventType();
            Log.d("myWeather", "parseXML");
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        Log.d("myWeather", "1start11");
                        break;
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("resp")) {
                            todayWeather = new TodayWeather();
                        }
                        if (todayWeather != null) {
                            if (xmlPullParser.getName().equals("city")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setCity(xmlPullParser.getText());
                                Log.d("myWeather", "city:    " + xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("updatetime")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setUpdatetime(xmlPullParser.getText());
                                Log.d("myWeather", "updatetime:    " + xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("shidu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setShidu(xmlPullParser.getText());
                                Log.d("myWeather", "shidu:    " + xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("wendu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWendu(xmlPullParser.getText());
                                Log.d("myWeather", "wendu:    " + xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("pm25")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setPm25(xmlPullParser.getText());
                               Log.d("myWeather", "pm25:    " + xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("quality")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setQuality(xmlPullParser.getText());
                                Log.d("myWeather", "quality:    " + xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fengxiang")) {
                                eventType = xmlPullParser.next();
                                Log.d("myWeather", "fengxiang:    " + xmlPullParser.getText());
                                fengxiangCount++;
                                switch(fengxiangCount-1){
                                    case 0:
                                        todayWeather.setDate(xmlPullParser.getText());
                                        break;
                                    case 1:
                                        todayWeather.other1.setFengxiang(xmlPullParser.getText());
                                        break;
                                    case 2:
                                        todayWeather.other2.setFengxiang(xmlPullParser.getText());
                                        break;
                                    case 3:
                                        todayWeather.other3.setFengxiang(xmlPullParser.getText());
                                        break;
                                    case 4:
                                        todayWeather.other4.setFengxiang(xmlPullParser.getText());
                                        break;
                                    case 5:
                                        todayWeather.other5.setFengxiang(xmlPullParser.getText());
                                        break;
                                    case 6:
                                        todayWeather.other6.setFengxiang(xmlPullParser.getText());
                                        break;
                                }
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli(xmlPullParser.getText());
                                Log.d("myWeather", "fengli:    " + xmlPullParser.getText());
                                fengliCount++;
                            } else if (xmlPullParser.getName().equals("date")) {
                                eventType = xmlPullParser.next();
                                Log.d("myWeather", "date:    " + xmlPullParser.getText());
                                dateCount++;
                                switch(dateCount-1){
                                    case 0:
                                        todayWeather.setDate(xmlPullParser.getText());
                                        break;
                                    case 1:
                                        todayWeather.other1.setDate(xmlPullParser.getText());
                                        break;
                                    case 2:
                                        todayWeather.other2.setDate(xmlPullParser.getText());
                                        break;
                                    case 3:
                                        todayWeather.other3.setDate(xmlPullParser.getText());
                                        break;
                                    case 4:
                                        todayWeather.other4.setDate(xmlPullParser.getText());
                                        break;
                                    case 5:
                                        todayWeather.other5.setDate(xmlPullParser.getText());
                                        break;
                                    case 6:
                                        todayWeather.other6.setDate(xmlPullParser.getText());
                                        break;
                                }
                            } else if (xmlPullParser.getName().equals("high")) {
                                eventType = xmlPullParser.next();
                                Log.d("myWeather", "high:    " + xmlPullParser.getText().substring(2).trim());
                                highCount++;
                                switch(highCount-1){
                                    case 0:
                                        todayWeather.setHigh(xmlPullParser.getText().substring(2).trim());
                                        break;
                                    case 1:
                                        todayWeather.other1.setHigh(xmlPullParser.getText().substring(2).trim());
                                        break;
                                    case 2:
                                        todayWeather.other2.setHigh(xmlPullParser.getText().substring(2).trim());
                                        break;
                                    case 3:
                                        todayWeather.other3.setHigh(xmlPullParser.getText().substring(2).trim());
                                        break;
                                    case 4:
                                        todayWeather.other4.setHigh(xmlPullParser.getText().substring(2).trim());
                                        break;
                                    case 5:
                                        todayWeather.other5.setHigh(xmlPullParser.getText().substring(2).trim());
                                        break;
                                    case 6:
                                        todayWeather.other6.setHigh(xmlPullParser.getText().substring(2).trim());
                                        break;
                                }
                            } else if (xmlPullParser.getName().equals("low")) {
                                eventType = xmlPullParser.next();
                                Log.d("myWeather", "low:    " + xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                                switch(lowCount-1){
                                    case 0:
                                        todayWeather.setLow(xmlPullParser.getText().substring(2).trim());
                                        break;
                                    case 1:
                                        todayWeather.other1.setLow(xmlPullParser.getText().substring(2).trim());
                                        break;
                                    case 2:
                                        todayWeather.other2.setLow(xmlPullParser.getText().substring(2).trim());
                                        break;
                                    case 3:
                                        todayWeather.other3.setLow(xmlPullParser.getText().substring(2).trim());
                                        break;
                                    case 4:
                                        todayWeather.other4.setLow(xmlPullParser.getText().substring(2).trim());
                                        break;
                                    case 5:
                                        todayWeather.other5.setLow(xmlPullParser.getText().substring(2).trim());
                                        break;
                                    case 6:
                                        todayWeather.other6.setLow(xmlPullParser.getText().substring(2).trim());
                                        break;
                                }
                            } else if (xmlPullParser.getName().equals("type")) {
                                eventType = xmlPullParser.next();
                                Log.d("myWeather", "type:    " + xmlPullParser.getText());
                                typeCount++;
                                switch(typeCount-1){
                                    case 0:
                                        todayWeather.setType(xmlPullParser.getText());
                                        break;
                                    case 1:
                                        todayWeather.other1.setType(xmlPullParser.getText());
                                        break;
                                    case 2:
                                        todayWeather.other2.setType(xmlPullParser.getText());
                                        break;
                                    case 3:
                                        todayWeather.other3.setType(xmlPullParser.getText());
                                        break;
                                    case 4:
                                        todayWeather.other4.setType(xmlPullParser.getText());
                                        break;
                                    case 5:
                                        todayWeather.other5.setType(xmlPullParser.getText());
                                        break;
                                    case 6:
                                        todayWeather.other6.setType(xmlPullParser.getText());
                                        break;
                                }
                            }
                            break;
                        }
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return todayWeather;
    }

    /*初始化控件的内容*/
    void initView() {
        city_name_Tv = (TextView) findViewById(R.id.title_city_name);
        cityTv = (TextView) findViewById(R.id.city);
        timeTv = (TextView) findViewById(R.id.time);
        humidityTv = (TextView) findViewById(R.id.humidity);
        weekTv = (TextView) findViewById(R.id.week_today);
        pmDataTv = (TextView) findViewById(R.id.pm_data);
        pmQualityTv = (TextView) findViewById(R.id.pm2_5_quality);
        pmImg = (ImageView) findViewById(R.id.pm2_5_img);
        temperatureTv = (TextView) findViewById(R.id.temperature);
        climateTv = (TextView) findViewById(R.id.climate);
        windTv = (TextView) findViewById(R.id.wind);
        weatherImg = (ImageView) findViewById(R.id.weather_img);
        pmImg = (ImageView) findViewById(R.id.pm2_5_img);
        temperature_numTv = (TextView) findViewById(R.id.temperature_num);

        Otherdatatv = new TextView[6];
        Otherdatatv[0] = (TextView)Views.get(0).findViewById(R.id.data1);
        Otherdatatv[1] = (TextView)Views.get(0).findViewById(R.id.data2);
        Otherdatatv[2] = (TextView)Views.get(0).findViewById(R.id.data3);
        Otherdatatv[3] = (TextView)Views.get(1).findViewById(R.id.data4);
        Otherdatatv[4] = (TextView)Views.get(1).findViewById(R.id.data5);
        Otherdatatv[5] = (TextView)Views.get(1).findViewById(R.id.data6);


        Othertypetv = new TextView[6];
        Othertypetv[0] = (TextView)Views.get(0).findViewById(R.id.week1);
        Othertypetv[1] = (TextView)Views.get(0).findViewById(R.id.week2);
        Othertypetv[2] = (TextView)Views.get(0).findViewById(R.id.week3);
        Othertypetv[3] = (TextView)Views.get(1).findViewById(R.id.week4);
        Othertypetv[4] = (TextView)Views.get(1).findViewById(R.id.week5);
        Othertypetv[5] = (TextView)Views.get(1).findViewById(R.id.week6);

        Otherfengtv = new TextView[6];
        Otherfengtv[0] = (TextView)Views.get(0).findViewById(R.id.feng1);
        Otherfengtv[1] = (TextView)Views.get(0).findViewById(R.id.feng2);
        Otherfengtv[2] = (TextView)Views.get(0).findViewById(R.id.feng3);
        Otherfengtv[3] = (TextView)Views.get(1).findViewById(R.id.feng4);
        Otherfengtv[4] = (TextView)Views.get(1).findViewById(R.id.feng5);
        Otherfengtv[5] = (TextView)Views.get(1).findViewById(R.id.feng6);

        Otherwendutv = new TextView[6];
        Otherwendutv[0] = (TextView)Views.get(0).findViewById(R.id.wendu1);
        Otherwendutv[1] = (TextView)Views.get(0).findViewById(R.id.wendu2);
        Otherwendutv[2] = (TextView)Views.get(0).findViewById(R.id.wendu3);
        Otherwendutv[3] = (TextView)Views.get(1).findViewById(R.id.wendu4);
        Otherwendutv[4] = (TextView)Views.get(1).findViewById(R.id.wendu5);
        Otherwendutv[5] = (TextView)Views.get(1).findViewById(R.id.wendu6);

        city_name_Tv.setText("N/A");
        cityTv.setText("N/A");
        timeTv.setText("N/A");
        humidityTv.setText("N/A");
        pmDataTv.setText("N/A");
        pmQualityTv.setText("N/A");
        weekTv.setText("N/A");
        temperatureTv.setText("N/A");
        climateTv.setText("N/A");
        windTv.setText("N/A");
        temperature_numTv.setText("N/A");
        pmImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_0_50));
        weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_qing));
    }

    /*修改控件内容*/
    void updateTodayWeather(TodayWeather todayWeather) {
        city_name_Tv.setText(todayWeather.getCity() + "天气");
        cityTv.setText(todayWeather.getCity());
        timeTv.setText(todayWeather.getUpdatetime() + "发布");
        humidityTv.setText("湿度：" + todayWeather.getShidu());
        pmDataTv.setText(todayWeather.getPm25());
        try {
            int a = Integer.parseInt(todayWeather.getPm25());
            if (a < 50) {
                pmImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_0_50));
            } else if (a < 100) {
                pmImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_51_100));
            } else if (a < 150) {
                pmImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_101_150));
            } else if (a < 200) {
                pmImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_151_200));
            } else if (a < 300) {
                pmImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_201_300));
            } else {
                pmImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_greater_300));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        pmQualityTv.setText(todayWeather.getQuality());
        weekTv.setText(todayWeather.getDate());
        temperatureTv.setText(todayWeather.getLow() + "~" + todayWeather.getHigh());
        climateTv.setText(todayWeather.getType());
        try {
            String tianqi = todayWeather.getType();
            Log.d("myWeather", tianqi);
            if (tianqi == "晴") {
                weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_zhongyu));
            } else if (tianqi.equals("暴雪")) {
                weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_baoxue));
            } else if (tianqi.equals("暴雨")) {
                weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_baoyu));
            } else if (tianqi.equals("大暴雨")) {
                weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_dabaoyu));
            } else if (tianqi.equals("大雪")) {
                weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_daxue));
            } else if (tianqi.equals("大雨")) {
                weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_dayu));
            } else if (tianqi.equals("多云")) {
                weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_duoyun));
            } else if (tianqi.equals("雷阵雨")) {
                weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_leizhenyu));
            } else if (tianqi.equals("雷阵雨冰雹")) {
                weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_leizhenyubingbao));
            } else if (tianqi.equals("沙尘暴")) {
                weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_shachenbao));
            } else if (tianqi.equals("特大暴雨")) {
                weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_tedabaoyu));
            } else if (tianqi.equals("雾")) {
                weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_wu));
            } else if (tianqi.equals("小雪")) {
                weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_xiaoxue));
            } else if (tianqi.equals("小雨")) {
                weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_xiaoyu));
            } else if (tianqi.equals("阴")) {
                weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_yin));
            } else if (tianqi.equals("雨夹雪")) {
                weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_yujiaxue));
            } else if (tianqi.equals("阵雪")) {
                weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_zhenxue));
            } else if (tianqi.equals("阵雨")) {
                weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_zhenyu));
            } else if (tianqi.equals("中雪")) {
                weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_zhongxue));
            } else {
                weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_qing));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        windTv.setText("风力:" + todayWeather.getFengli());
        temperature_numTv.setText(todayWeather.getWendu() + "℃");

        Otherdatatv[0].setText(todayWeather.other1.getDate());
        Otherdatatv[1].setText(todayWeather.other2.getDate());
        Otherdatatv[2].setText(todayWeather.other3.getDate());
        Otherdatatv[3].setText(todayWeather.other4.getDate());
        Otherdatatv[4].setText(todayWeather.other5.getDate());
        Otherdatatv[5].setText(todayWeather.other6.getDate());

        Otherwendutv[0].setText(todayWeather.other1.getLow() + "~"+todayWeather.other1.getHigh() );
        Otherwendutv[1].setText(todayWeather.other2.getLow() + "~"+todayWeather.other2.getHigh());
        Otherwendutv[2].setText(todayWeather.other3.getLow() + "~"+todayWeather.other3.getHigh());
        Otherwendutv[3].setText(todayWeather.other4.getLow() + "~"+todayWeather.other4.getHigh());
        Otherwendutv[4].setText(todayWeather.other5.getLow() + "~"+todayWeather.other5.getHigh());
        Otherwendutv[5].setText(todayWeather.other6.getLow() + "~"+todayWeather.other6.getHigh());

        Otherfengtv[0].setText(todayWeather.other1.getFengxiang());
        Otherfengtv[1].setText(todayWeather.other2.getFengxiang());
        Otherfengtv[2].setText(todayWeather.other3.getFengxiang());
        Otherfengtv[3].setText(todayWeather.other4.getFengxiang());
        Otherfengtv[4].setText(todayWeather.other5.getFengxiang());
        Otherfengtv[5].setText(todayWeather.other6.getFengxiang());

        Othertypetv[0].setText(todayWeather.other1.getType());
        Othertypetv[1].setText(todayWeather.other2.getType());
        Othertypetv[2].setText(todayWeather.other3.getType());
        Othertypetv[3].setText(todayWeather.other4.getType());
        Othertypetv[4].setText(todayWeather.other5.getType());
        Othertypetv[5].setText(todayWeather.other6.getType());
        Toast.makeText(MainActivity.this, "更新成功！", Toast.LENGTH_SHORT).show();
        mUpdatePro.setVisibility(View.INVISIBLE);
        mUpdateBtn.setVisibility(View.VISIBLE);
        mUpdatePro.clearAnimation();
    }

    /*将得到的返回数据再修改一下城市*/
    protected void onActivityResult(int RequestCode, int ResultCode, Intent data) {
        if (RequestCode == 1 && ResultCode == RESULT_OK) {
            String newCityCode = data.getStringExtra("cityCode");
            Log.d("myWeather", "选择出来的城市代码" + newCityCode);
            if(newCityCode == null){
                SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
                String cityCode = sharedPreferences.getString("main_city_code", "101010100");
                newCityCode = cityCode;
            }
            Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.title_update_anim);
            LinearInterpolator lin = new LinearInterpolator();
            operatingAnim.setInterpolator(lin);
            if (operatingAnim != null) {
                mUpdatePro.setVisibility(View.VISIBLE);
                mUpdateBtn.setVisibility(View.GONE);
                mUpdatePro.startAnimation(operatingAnim);
            }
            int n =1000 , m =1000;
            while(n--!=0) {
                m = 1000;
                while (m--!=0) ;
            }
            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                Log.d("myWeather", "网络可以~");

                queryWeatherCode(newCityCode);
            } else {
                Log.d("myWeather", "网络爆炸");
                Toast.makeText(MainActivity.this, "网络炸了！", Toast.LENGTH_LONG).show();
            }
        }
    }
}