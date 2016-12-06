package com.example.a76568.weathertest;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by 76568 on 2016/11/20 0020.
 */
public class ExampleService extends Service{
    int mStartMode; // indicates how to behave if the service is killed
    IBinder mBinder; // interface for clients that bind
    boolean mAllowRebind; // indicates whether onRebind should be used

    @Override
    public void onCreate()
    {
        // The service is being created
        Log.d("Service","Create");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        // The service is starting, due to a call to startService()
        Log.d("Service","StartCommand");
        return mStartMode;
    }

    public class LocalBinder extends Binder
    {
        ExampleService getService()
        {
            // Return this instance of LocalService so clients can call public
            // methods
            return ExampleService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent)
    {
        // A client is binding to the service with bindService()
        Log.d("Service","Bind");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent)
    {
        // All clients have unbound with unbindService()
        Log.d("Service","Unbind");
        return mAllowRebind;
    }

    @Override
    public void onRebind(Intent intent)
    {
        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called
        Log.d("Service","Rebind");
    }

    @Override
    public void onDestroy()
    {
        // The service is no longer used and is being destroyed
        Log.d("Service","Destroy");
    }
}
