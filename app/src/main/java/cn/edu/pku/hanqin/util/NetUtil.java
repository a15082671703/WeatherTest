package cn.edu.pku.hanqin.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by 76568 on 2016/9/27 0027.
 */

public class NetUtil {
   public static int getNetworkState(Context context) {
       public static final int NETWORN_NONE = 0;
       public static final int NETWORN_WIFI = 1;
       public static final int NETWORN_MOBILE = 2;
       ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
       NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
       if(networkInfo == null){
           return NETWORN_NONE;
       }

       int nType = networkInfo.getType();
       if(nType == ConnectivityManager.TYPE_MOBILE){
           return NETWORN_MOBILE;
       } else if(nType == ConnectivityManager.TYPE_WIFI){
           return NETWORN_WIFI;
       }
       return NETWORN_NONE;
   }

}

