package com.example.stxr.zzu_app.utils;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.example.stxr.zzu_app.statics.StaticConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.utils
 *  文件名:   BaiduMapUtils
 *  创建者:   Stxr
 *  创建时间:  2017/4/16 18:38
 *  描述：    
 */
public class BaiduMapUtils {
    private StringBuffer sb;
    private Context context;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

    public  BaiduMapUtils(Context context) {
        this.context = context;
    }
    public void onCreate() {
        mLocationClient = new LocationClient(context);
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        initLocation();
        startLocation();
    }
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

        //可选，默认gcj02，设置返回的定位结果坐标系
        option.setCoorType("bd09ll");


        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        int span=1000;
        option.setScanSpan(span);

        //可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(true);

        //可选，默认false,设置是否使用gps
        option.setOpenGps(true);

        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setLocationNotify(true);

        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationDescribe(true);

        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIsNeedLocationPoiList(true);

        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIgnoreKillProcess(false);

        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.SetIgnoreCacheException(false);

        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        option.setEnableSimulateGps(false);

        mLocationClient.setLocOption(option);
    }
    //开始定位
    public void startLocation() {
        mLocationClient.start();
    }
    //结束定位
    public void stopLocation() {
        mLocationClient.stop();
    }
    //得到gps和poi信息
    public String getLocation() {
        return sb.toString();
    }

    /**
     * 根据地理位置 得到 这样的数据  北1 北2 a
     * @return List<String> 过滤后的教室信息
     */
    public List<String> getClassLocation() {
        List<String> location = new ArrayList<>();
        //模拟数据
//        String testLocation = "郑州大学新校区  郑州大学新校区-教学区北区西2号楼  郑州大学新校区-教学区北区西3号楼  郑州大学新校区-教学区北区东2号楼  郑州大学新校区-教学区北区4号楼";
        for (Map.Entry<String, String> entry: StaticConstant.CLASS_LOCATION_MAP.entrySet()) {
            if(getLocation().matches(entry.getKey())){
//            if(testLocation.matches(entry.getKey())){
                location.add(entry.getValue());
            }
        }
        L.i(location.toString());
        return location;
    }
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            updataView(location);
        }
        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }
    private String updataView(BDLocation location) {
        //获取定位结果
        sb = new StringBuffer();
        sb.append("latitude : ");
        sb.append(location.getLatitude());    //获取纬度信息

        sb.append("lontitude : ");
        sb.append(location.getLongitude());    //获取经度信息
        if (location.getLocType() == BDLocation.TypeGpsLocation){
        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
        } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
        } else if (location.getLocType() == BDLocation.TypeServerError) {
        } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
        } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
        }
        List<Poi> list = location.getPoiList();    // POI数据
        if (list != null) {
            for (Poi p : list) {
                sb.append(p.getName()).append(p.getRank());
            }
        }
        return sb.toString();
    }

}

