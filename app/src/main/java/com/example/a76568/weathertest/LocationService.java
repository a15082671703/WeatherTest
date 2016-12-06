package com.example.a76568.weathertest;


import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;

/**
 *
 * @author baidu
 *
 */
public class LocationService {
    private LocationClient client = null;
    private LocationClientOption mOption,DIYoption;
    private Object  objLock = new Object();

    /***
     *
     * @param locationContext
     */
    public LocationService(Context locationContext){
        synchronized (objLock) {
            if(client == null){
                client = new LocationClient(locationContext);
                client.setLocOption(getDefaultLocationClientOption());
            }
        }
    }

    /***
     *
     * @param listener
     * @return
     */

    public boolean registerListener(BDLocationListener listener){
        boolean isSuccess = false;
        if(listener != null){
            client.registerLocationListener(listener);
            isSuccess = true;
        }
        return  isSuccess;
    }

    public void unregisterListener(BDLocationListener listener){
        if(listener != null){
            client.unRegisterLocationListener(listener);
        }
    }

    /***
     *
     * @param option
     * @return isSuccessSetOption
     */
    public boolean setLocationOption(LocationClientOption option){
        boolean isSuccess = false;
        if(option != null){
            if(client.isStarted())
                client.stop();
            DIYoption = option;
            client.setLocOption(option);
            isSuccess = true;
        }
        return isSuccess;
    }

    public LocationClientOption getOption(){
        return DIYoption;
    }
    /***
     *
     * @return DefaultLocationClientOption
     */
    public LocationClientOption getDefaultLocationClientOption(){
        if(mOption == null){
            mOption = new LocationClientOption();
            mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//鍙�夛紝榛樿楂樼簿搴︼紝璁剧疆瀹氫綅妯″紡锛岄珮绮惧害锛屼綆鍔熻�楋紝浠呰澶�
            mOption.setCoorType("bd09ll");//鍙�夛紝榛樿gcj02锛岃缃繑鍥炵殑瀹氫綅缁撴灉鍧愭爣绯伙紝濡傛灉閰嶅悎鐧惧害鍦板浘浣跨敤锛屽缓璁缃负bd09ll;
            mOption.setScanSpan(3000);//鍙�夛紝榛樿0锛屽嵆浠呭畾浣嶄竴娆★紝璁剧疆鍙戣捣瀹氫綅璇锋眰鐨勯棿闅旈渶瑕佸ぇ浜庣瓑浜�1000ms鎵嶆槸鏈夋晥鐨�
            mOption.setIsNeedAddress(true);//鍙�夛紝璁剧疆鏄惁闇�瑕佸湴鍧�淇℃伅锛岄粯璁や笉闇�瑕�
            mOption.setIsNeedLocationDescribe(true);//鍙�夛紝璁剧疆鏄惁闇�瑕佸湴鍧�鎻忚堪
            mOption.setNeedDeviceDirect(false);//鍙�夛紝璁剧疆鏄惁闇�瑕佽澶囨柟鍚戠粨鏋�
            mOption.setLocationNotify(false);//鍙�夛紝榛樿false锛岃缃槸鍚﹀綋gps鏈夋晥鏃舵寜鐓�1S1娆￠鐜囪緭鍑篏PS缁撴灉
            mOption.setIgnoreKillProcess(true);//鍙�夛紝榛樿true锛屽畾浣峉DK鍐呴儴鏄竴涓猄ERVICE锛屽苟鏀惧埌浜嗙嫭绔嬭繘绋嬶紝璁剧疆鏄惁鍦╯top鐨勬椂鍊欐潃姝昏繖涓繘绋嬶紝榛樿涓嶆潃姝�
            mOption.setIsNeedLocationDescribe(true);//鍙�夛紝榛樿false锛岃缃槸鍚﹂渶瑕佷綅缃涔夊寲缁撴灉锛屽彲浠ュ湪BDLocation.getLocationDescribe閲屽緱鍒帮紝缁撴灉绫讳技浜庘�滃湪鍖椾含澶╁畨闂ㄩ檮杩戔��
            mOption.setIsNeedLocationPoiList(true);//鍙�夛紝榛樿false锛岃缃槸鍚﹂渶瑕丳OI缁撴灉锛屽彲浠ュ湪BDLocation.getPoiList閲屽緱鍒�
            mOption.SetIgnoreCacheException(false);//鍙�夛紝榛樿false锛岃缃槸鍚︽敹闆咰RASH淇℃伅锛岄粯璁ゆ敹闆�

            mOption.setIsNeedAltitude(false);//鍙�夛紝榛樿false锛岃缃畾浣嶆椂鏄惁闇�瑕佹捣鎷斾俊鎭紝榛樿涓嶉渶瑕侊紝闄ゅ熀纭�瀹氫綅鐗堟湰閮藉彲鐢�
        }
        return mOption;
    }

    public void start(){
        synchronized (objLock) {
            if(client != null && !client.isStarted()){
                client.start();
            }
        }
    }
    public void stop(){
        synchronized (objLock) {
            if(client != null && client.isStarted()){
                client.stop();
            }
        }
    }

}