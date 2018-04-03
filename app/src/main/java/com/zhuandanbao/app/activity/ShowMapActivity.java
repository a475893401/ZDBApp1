package com.zhuandanbao.app.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapException;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.NaviPara;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.appdelegate.ShowMapD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.AddressEntity;
import com.zhuandanbao.app.mvp.BaseHttpActivity;
import com.zhuandanbao.app.utils.MLog;
import com.zhuandanbao.app.utils.StrUtils;

/**
 * 地图展示
 * Created by BFTECH on 2017/3/6.
 */

public class ShowMapActivity extends BaseHttpActivity<ShowMapD> implements LocationSource, AMapLocationListener {

    public static final int REQUEST_PERMISSION_STORAGE = 0x01;
    private TextView locationInfo;
    private TextView distance;
    private AddressEntity modle = null;
    /**
     * 导航对象(单例)
     */
    private AMap mAmap;
    /**
     * 地图对象
     */
    private MapView mapView;
    private Marker mStartMarker;
    private Marker mEndMarker;
    private Button startBut;
    private UiSettings mUiSettings;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    @Override
    protected Class<ShowMapD> getDelegateClass() {
        return ShowMapD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        viewDelegate.setBack("返回", R.mipmap.img_back_icon,null);
        viewDelegate.setTitle("位置信息");
        if (null != getIntent().getSerializableExtra(Constants.GD_MAP_ADDRESS_INFO)) {
            modle = (AddressEntity) getIntent().getSerializableExtra(Constants.GD_MAP_ADDRESS_INFO);
        }
        mapView = viewDelegate.get(R.id.show_navi_view);
        mapView.onCreate(savedInstanceState);
        //地圖縮放級別  min3  max 19
        // 设置定位的类型为 跟随模式
//        mAmap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        locationInfo= viewDelegate.get(R.id.location_info);
        distance= viewDelegate.get(R.id.title);
        startBut= viewDelegate.get(R.id.start_gdmap);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            if (checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                initView(true);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION_STORAGE);
            }
        }
    }
    private void initView(boolean isLocation) {
        if (null==mAmap){
            mAmap=mapView.getMap();
        }
        mAmap.clear();
        mAmap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(modle.endLat,modle.endLon),13));
        mAmap.moveCamera(CameraUpdateFactory.zoomTo(5));
        mUiSettings=mAmap.getUiSettings();
        mUiSettings.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);//顯示右中
        if (isLocation){//有定位功能
//            mAmap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
            mAmap.setLocationSource(this);// 设置定位监听
            mUiSettings.setMyLocationButtonEnabled(true); // 是否显示默认的定位按钮
            mAmap.setMyLocationEnabled(true);// 是否可触发定位并显示定位层
        }else {//无定位功能
//            startBut.setVisibility(View.GONE);
//            final ShopLatEntity shopInfo = (ShopLatEntity) BaseApplication.cache.getAsObject(Constants.SHOP_LAT_INFO);
        }
        mStartMarker = mAmap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.start))));
        mEndMarker = mAmap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.end))));
        startBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStartBut();

            }
        });
        // 初始化Marker添加到地图
        if (null != getIntent().getSerializableExtra(Constants.GD_MAP_ADDRESS_INFO)) {
            modle = (AddressEntity) getIntent().getSerializableExtra(Constants.GD_MAP_ADDRESS_INFO);
            MLog.e(modle.startLat+"======"+modle.startLon+"======"+modle.endLat+"======"+modle.endLon);
            //中心点
//            mAmap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(modle.startLat, modle.startLon), 18, 30, 30)));
//            mAmap.addMarker(new MarkerOptions().position(new LatLng(modle.startLat, modle.startLon)).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.start))));
            //起点
//            mStartMarker = mAmap.addMarker(new MarkerOptions().position(new LatLng(modle.startLat,modle.startLon)).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.start))));
            mStartMarker.setPosition(new LatLng(modle.startLat, modle.startLon));
            mStartMarker.setTitle("店铺");
            //终点
//            mEndMarker = mAmap.addMarker(new MarkerOptions().position(new LatLng(modle.endLat, modle.endLon)).icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.end))));
            mEndMarker.setPosition(new LatLng(modle.endLat, modle.endLon));
            mEndMarker.setTitle("送货地点");
//            LatLngBounds latLngBounds = new LatLngBounds(new LatLng(modle.startLat, modle.startLon), new LatLng(modle.endLat, modle.endLon));
//            mAmap.setMapStatusLimits(latLngBounds);
            if (StrUtils.isNotNull(modle.distance)){
                distance.setText("距配送位置约： "+modle.distance+" 公里");
            }
            locationInfo.setText(modle.endCity);
        }
    }
    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation aLocation) {
        if (mListener != null) {
            mListener.onLocationChanged(aLocation);// 显示系统小蓝点
//            aLocation.getProvince();//省信息
//            aLocation.getCity();//城市信息
//            aLocation.getDistrict();//城区信息
//            aLocation.getStreet();//街道信息
//            aLocation.getStreetNum();//街道门牌号信息
//             locationInfo.setText(aLocation.getProvince()+aLocation.getCity()+aLocation.getDistrict()+aLocation.getStreet()+ aLocation.getStreetNum());
        }
    }
    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }
    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    public boolean checkPermission(@NonNull String permission) {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }
    public void showToast(String toastText) {
        Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_LONG).show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initView(true);
            } else {
                showToast("权限被禁止，无法开启定位功能");
//                locationInfo.setText("定位权限被禁止，无法获取定位功能");
                initView(false);
            }
        }
    }

    private void setStartBut() {
        final AlertDialog alertDialog = new AlertDialog.Builder(ShowMapActivity.this).create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
        Window window = alertDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setContentView(R.layout.pop_select_image);
        View views = window.getDecorView();
        Button pcPopPibtn = (Button) views.findViewById(R.id.pop_photo_image);//相机
        Button pcPopCibtn = (Button) views.findViewById(R.id.pop_camera_image);//选择
        Button pcPopBackbtn = (Button) views.findViewById(R.id.pop_back_callimage);//返回
        pcPopPibtn.setText("百度地图");
        pcPopCibtn.setText("高德地图");
        pcPopPibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
//                OpenClientUtil.getLatestBaiduMapApp(ShowMapActivity.this);
                openBaiDu();
            }
        });
        pcPopCibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                openGD();
            }
        });
        pcPopBackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }


    private void openGD(){
        // 构造导航参数
        NaviPara naviPara = new NaviPara();
        // 设置终点位置
        naviPara.setTargetPoint(new LatLng(modle.endLat,modle.endLon));
        // 设置导航策略，这里是避免拥堵
        naviPara.setNaviStyle(AMapUtils.DRIVING_AVOID_CONGESTION);
        try {
            // 调起高德地图导航
            AMapUtils.openAMapNavi(naviPara, this.getApplicationContext());
        } catch (AMapException e) {
            // 如果没安装会进入异常，调起下载页面
            // com.amap.api.maps.AMapUtils.getLatestAMapApp(activity.getApplicationContext());
            // Toast.makeText(this, "未安装高德地图", Toast.LENGTH_SHORT).show();
            showGDDialog();
        }
    }

    private void openBaiDu(){
        // 天安门坐标
        double mLat1 = modle.startLat;
        double mLon1 = modle.startLon;
        // 百度大厦坐标
        double mLat2 = modle.endLat;
        double mLon2 = modle.endLon;

//        double mLat1 = 39.915291;
//        double mLon1 = 116.403857;
//// 百度大厦坐标
//        double mLat2 = 40.056858;
//        double mLon2 = 116.308194;
        com.baidu.mapapi.model.LatLng pt1 = new com.baidu.mapapi.model.LatLng(mLat1, mLon1);
        com.baidu.mapapi.model.LatLng pt2 = new com.baidu.mapapi.model.LatLng(mLat2, mLon2);
        // 将google地图、soso地图、aliyun地图、mapabc地图和amap地图// 所用坐标转换成百度坐标
        CoordinateConverter converter  = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.COMMON);
        // sourceLatLng待转换坐标
        converter.coord(pt1);
        com.baidu.mapapi.model.LatLng ptS = converter.convert();
        converter.coord(pt2);
        com.baidu.mapapi.model.LatLng ptE = converter.convert();
        // 构建 导航参数
        NaviParaOption para = new NaviParaOption().startPoint(ptS).endPoint(ptE);
        try {
            // 调起百度地图骑行导航
            boolean open= BaiduMapNavigation.openBaiduMapBikeNavi(para, this);
            if (!open){
                showBDDialog();
            }
        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
            showBDDialog();
        }
    }

    /**
     * 提示未安装百度地图app或app版本过低
     */
    public void showBDDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
        builder.setTitle("溫馨提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                OpenClientUtil.getLatestBaiduMapApp(ShowMapActivity.this);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 提示未安装高德地图app或app版本过低
     */
    public void showGDDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("您尚未安装高德地图app或app版本过低，点击确认安装？");
        builder.setTitle("温馨提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                AMapUtils.getLatestAMapApp(ShowMapActivity.this.getApplicationContext());
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
