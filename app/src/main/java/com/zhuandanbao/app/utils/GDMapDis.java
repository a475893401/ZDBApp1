package com.zhuandanbao.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.zhuandanbao.app.MainApplication;
import com.zhuandanbao.app.activity.ShowMapActivity;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.AddressEntity;
import com.zhuandanbao.app.entity.OrderDataEntity;
import com.zhuandanbao.app.entity.ShopLatEntity;


/**
 * Created by BFTECH on 2016/11/9.
 */
public class GDMapDis {

    //    if (rCode == 1000) {
//        if (result != null && result.getGeocodeAddressList() != null && result.getGeocodeAddressList().size() > 0) {
//            GeocodeAddress address = result.getGeocodeAddressList().get(0);
//            String s = "经纬度值:" + address.getLatLonPoint() + "位置描述:" + address.getFormatAddress();
//        } else {
//            MLog.e("对不起，没有搜索到相关数据！");
//        }
//    } else {
//        MLog.e(rCode + "");
//    }

    //AMapUtils.calculateLineDistance(latLng1, latLng2)

    /**
     * 根据地址获取经纬度
     * @param context
     * @param address
     * @param citys
     * @param listener
     */
    public static void getLat(Context context, String address, String citys, GeocodeSearch.OnGeocodeSearchListener listener) {
        GeocodeSearch geocoderSearch = new GeocodeSearch(context);
        geocoderSearch.setOnGeocodeSearchListener(listener);
        GeocodeQuery query = new GeocodeQuery(address, citys);// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
        geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
    }


    public static void getShopLatInfo(Context context, final String address, final String citys) {
        MLog.e("citys="+citys+"address="+address);
        if (StrUtils.isNull(address) || StrUtils.isNull(citys)) {
            return;
        }
        GeocodeSearch geocoderSearch = new GeocodeSearch(context);
        GeocodeQuery query = new GeocodeQuery(address, citys);// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
        geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

            }

            @Override
            public void onGeocodeSearched(GeocodeResult result, int rCode) {
                if (rCode == 1000) {
                    if (result != null && result.getGeocodeAddressList() != null && result.getGeocodeAddressList().size() > 0) {
                        GeocodeAddress geocodeAddress = result.getGeocodeAddressList().get(0);
                        MLog.e("经纬度值:" + geocodeAddress.getLatLonPoint() + "位置描述:" + geocodeAddress.getFormatAddress());
                        ShopLatEntity info = new ShopLatEntity();
                        info.address = address;
                        info.city=citys;
                        info.lat=geocodeAddress.getLatLonPoint().getLatitude();
                        info.lon=geocodeAddress.getLatLonPoint().getLongitude();
                        MainApplication.cache.put(Constants.SHOP_LAT_INFO,info);
                    } else {
                        MLog.e("对不起，没有搜索到相关数据！");
                    }
                } else {
                    MLog.e(rCode + "----------------");
                }
            }
        });
    }

    /**
     * 获取地址信息
     * @return
     */
    public static String[] getDetitlsAddressInfo(String areainfo,String address){
        MLog.e("areainfo=="+areainfo+";address=="+address);
        if (StrUtils.isNull(areainfo)||StrUtils.isNull(address)){
            return null;
        }
        String[] add=new String[2];
        StringBuilder stringBuilder=new StringBuilder();
        String[] addressInfo = areainfo.trim().split("\\|");
        for (int i=0;i<addressInfo.length;i++){
            if (StrUtils.isNotNull(addressInfo[0])){
                add[0]=addressInfo[0];
            }
            stringBuilder.append(addressInfo[i]);
        }
        stringBuilder.append(address);
        add[1]=stringBuilder.toString();
        return add;
    }

    public static void showGDMap(final Activity context, OrderDataEntity info,ShopLatEntity shopInfo) {
        final Intent intent = new Intent(context, ShowMapActivity.class);
        final AddressEntity addModle = new AddressEntity();
        MLog.e("info.getDistance()=="+info.getDistance());
        addModle.startLat = shopInfo.lat;
        addModle.startLon = shopInfo.lon;
        addModle.endCity = info.getAddress();
        addModle.distance = info.getDistance();
        if (StrUtils.isNull(info.getDelivery_geo())) {
            String[] itemAdd = GDMapDis.getDetitlsAddressInfo(info.getAreainfo(), info.getAddress());
            GDMapDis.getLat(context, itemAdd[1], itemAdd[0], new GeocodeSearch.OnGeocodeSearchListener() {
                @Override
                public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

                }

                @Override
                public void onGeocodeSearched(GeocodeResult result, int rCode) {
                    if (rCode == 1000) {
                        if (result != null && result.getGeocodeAddressList() != null && result.getGeocodeAddressList().size() > 0) {
                            GeocodeAddress geocodeAddress = result.getGeocodeAddressList().get(0);
                            String s = "经纬度值:" + geocodeAddress.getLatLonPoint() + "位置描述:" + geocodeAddress.getFormatAddress();
                            LatLng itemLat = new LatLng(geocodeAddress.getLatLonPoint().getLatitude(), geocodeAddress.getLatLonPoint().getLongitude());
                            addModle.endLat = geocodeAddress.getLatLonPoint().getLatitude();
                            addModle.endLon = geocodeAddress.getLatLonPoint().getLongitude();
                            intent.putExtra(Constants.GD_MAP_ADDRESS_INFO, addModle);
                            context.startActivity(intent);
                        } else {
                            MLog.e("对不起，没有搜索到相关数据！");
                        }
                    } else {
                        MLog.e("rCode=" + rCode);
                    }
                }
            });
        } else {
            addModle.endLat = Double.parseDouble(info.getDelivery_lat());
            addModle.endLon = Double.parseDouble(info.getDelivery_lng());
            intent.putExtra(Constants.GD_MAP_ADDRESS_INFO, addModle);
            context.startActivity(intent);
        }
    }
}
