package com.example.stxr.zzu_app.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

/*
 *  项目名：  ZZU_App
 *  包名：    com.example.stxr.zzu_app.utils
 *  文件名:   LocationUtils
 *  创建者:   Stxr
 *  创建时间:  2017/4/15 14:05
 *  描述：    获取gps信息
 */
public class LocationUtils {
    // 纬度
    public static double latitude = 0.0;
    // 经度
    public static double longitude = 0.0;
    private static LocationManager locationManager;
    public static String locationInfo;

    public static void initLocation(Context context) {
        locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        updataLocation(location);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 5, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updataLocation(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                //               updataLocation(locationManager.getLastKnownLocation(provider));
            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });
    }

    public static String updataLocation(Location location) {
        if (location != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("经度：");
            sb.append(location.getLongitude());
            sb.append("  纬度：");
            sb.append(location.getLatitude());
            sb.append("\n速度：");
            sb.append(location.getSpeed());
            sb.append(" 方向：");
            sb.append(location.getBearing());
            sb.append("\n高度：");
            sb.append(location.getAltitude());
            //TestActivity.text.setText(sb.toString());
            return locationInfo=sb.toString();
        }else {
            return null;
        }
    }


}
//
//
//    LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//        // TODO: Consider calling
//        //    ActivityCompat#requestPermissions
//        // here to request the missing permissions, and then overriding
//        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//        //                                          int[] grantResults)
//        // to handle the case where the user grants the permission. See the documentation
//        // for ActivityCompat#requestPermissions for more details.
//        return;
//        }
//        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        if (location != null) {
//        latitude = location.getLatitude();
//        longitude = location.getLongitude();
//        }
//        } else {
//        LocationListener locationListener = new LocationListener() {
//
//// Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
//@Override
//public void onStatusChanged(String provider, int status,
//        Bundle extras) {
//
//        }
//
//// Provider被enable时触发此函数，比如GPS被打开
//@Override
//public void onProviderEnabled(String provider) {
//
//        }
//
//// Provider被disable时触发此函数，比如GPS被关闭
//@Override
//public void onProviderDisabled(String provider) {
//
//        }
//
//// 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
//@Override
//public void onLocationChanged(Location location) {
//        if (location != null) {
//
//        }
//        }
//        };
//        /**
//         * LocationManager.requestLocationUpdates
//         *   (String provider, long minTime, float minDistance, LocationListener listener)
//         *   第一个参数:位置信息的provider,比如GPS
//         *   第二个参数:更新位置信息的时间间隔,单位毫秒
//         *   第三个参数:更新位置信息的距离间隔,单位米
//         *   第四个参数:位置信息变化时的回调
//         */
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000, 3, locationListener);
//        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//        if (location != null) {
//        latitude = location.getLatitude(); // 经度
//        longitude = location.getLongitude(); // 纬度
//        }
//        }