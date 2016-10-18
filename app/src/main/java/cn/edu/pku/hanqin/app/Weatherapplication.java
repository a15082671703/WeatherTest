package cn.edu.pku.hanqin.app;

import android.app.Application;
import android.util.Log;

/**
 * Created by 76568 on 2016/10/18 0018.
 */
public class Weatherapplication extends Application{
    private static final String TAG = "MyAPP";
    private static Weatherapplication mApplication;
    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG,"WeatherApplication->Oncreate");
        mApplication = this;
    }

    public static Weatherapplication getInstance(){
        return mApplication;
    }
}
