package com.zhuandanbao.app.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.appdelegate.GdmapD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.mvp.BaseHttpActivity;
import com.zhuandanbao.app.utils.AMapUtil;
import com.zhuandanbao.app.utils.MLog;
import com.zhuandanbao.app.utils.StrUtils;
import com.zhuandanbao.app.utils.ToastUtil;

/**
 * Created by BFTECH on 2017/4/12.
 */

public class GDMapActivity extends BaseHttpActivity<GdmapD> implements AMap.OnMapClickListener,GeocodeSearch.OnGeocodeSearchListener,AMap.OnMarkerClickListener {

    private SkipInfoEntity infoEntity;
    private MapView mMapView = null;
    private AMap aMap;

    private ProgressDialog progDialog = null;
    private GeocodeSearch geocoderSearch;
    private String addressName=null;
    private Marker regeoMarker;
    private LatLonPoint latLonPoint=null;

    private TextView adderss;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        if (null!=getIntent().getSerializableExtra(Constants.SKIP_INFO_ID)){
            infoEntity= (SkipInfoEntity) getIntent().getSerializableExtra(Constants.SKIP_INFO_ID);
        }
        viewDelegate.setBack("返回",R.mipmap.img_back_icon,null);
        viewDelegate.setTitle("选择店铺地址");
        viewDelegate.setRight("保存", 0, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putExtra("address",addressName);
                setResult(200,intent);
                finish();
            }
        });
        //获取地图控件引用
        mMapView = viewDelegate.get(R.id.map);
        adderss=viewDelegate.get(R.id.map_shop_address);
        adderss.setOnClickListener(this);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        if (aMap == null) {
            aMap = mMapView.getMap();
            regeoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            aMap.setOnMarkerClickListener(this);
            aMap.setOnMapClickListener(this);
        }
        progDialog = new ProgressDialog(this);
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        getLatlon(infoEntity.msg);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId()==R.id.map_shop_address){
            showInputDialog(addressName);
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        MLog.e("latitude=="+latLng.latitude+";longitude="+latLng.longitude);
        getAddress(new LatLonPoint(latLng.latitude,latLng.longitude));
    }
    /**
     * 响应地理编码
     */
    public void getLatlon(final String name) {
        MLog.e("name===="+name);
        addressName= (String) infoEntity.data;
        showDialog();
        adderss.setText(name.replace("|",""));
        GeocodeQuery query = new GeocodeQuery(name.replace("|",""), infoEntity.pushId);// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
        geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
    }
    /**
     * 响应逆地理编码
     */
    public void getAddress( LatLonPoint latLonPoint) {
        showDialog();
        this.latLonPoint=latLonPoint;
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    protected Class<GdmapD> getDelegateClass() {
        return GdmapD.class;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }
    /**
     * 逆地理编码回调
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        dismissDialog();
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null && result.getRegeocodeAddress().getFormatAddress() != null) {
                infoEntity.pushId=result.getRegeocodeAddress().getProvince();
                addressName = result.getRegeocodeAddress().getFormatAddress()
                        .replace(result.getRegeocodeAddress().getProvince(),"")
                        .replace(result.getRegeocodeAddress().getCity(),"")
                        .replace(result.getRegeocodeAddress().getDistrict(),"");
                MLog.e("====getDistrict======"+result.getRegeocodeAddress().getDistrict());
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(AMapUtil.convertToLatLng(latLonPoint), 15));
                regeoMarker.setPosition(AMapUtil.convertToLatLng(latLonPoint));
                adderss.setText(result.getRegeocodeAddress().getFormatAddress());
                ToastUtil.show(GDMapActivity.this, result.getRegeocodeAddress().getFormatAddress());
            } else {
                ToastUtil.show(GDMapActivity.this, R.string.no_result);
            }
        } else {
            ToastUtil.showerror(this, rCode);
        }
    }
    /**
     * 地理编码查询回调
     */
    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {
        dismissDialog();
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getGeocodeAddressList() != null
                    && result.getGeocodeAddressList().size() > 0) {
                GeocodeAddress geAddress = result.getGeocodeAddressList().get(0);
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(AMapUtil.convertToLatLng(geAddress.getLatLonPoint()), 15));
                regeoMarker.setPosition(AMapUtil.convertToLatLng(geAddress.getLatLonPoint()));
//                addressName = "经纬度值:" + address.getLatLonPoint() + "\n位置描述:" + address.getFormatAddress();
                MLog.e("getFormatAddress()==="+geAddress.getFormatAddress());
                ToastUtil.show(GDMapActivity.this, geAddress.getFormatAddress());
                adderss.setText(geAddress.getFormatAddress());
                addressName=geAddress.getFormatAddress().replace(geAddress.getProvince(),"")
                        .replace(geAddress.getCity(),"")
                        .replace(geAddress.getDistrict(),"");
            } else {
                if (StrUtils.isNotNull(infoEntity.payCode)) {
                    GeocodeQuery query = new GeocodeQuery(infoEntity.payCode.replace("|", ""), infoEntity.pushId);// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
                    geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
                }else {
                    ToastUtil.show(GDMapActivity.this, R.string.no_result);
                }
            }
        } else {
            ToastUtil.showerror(this, rCode);
        }
    }


    /**
     * 显示进度条对话框
     */
    public void showDialog() {
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在获取地址");
        progDialog.show();
    }

    /**
     * 隐藏进度条对话框
     */
    public void dismissDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return false;
    }


    private void showInputDialog(String contentStr) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_input, null);
        final Dialog dialog = new Dialog(this, R.style.fullDialog);
        dialog.getWindow().setContentView(view);
        dialog.show();
        final EditText content = (EditText) view.findViewById(R.id.dialog_input_content);
        content.setText(contentStr);
        TextView ok = (TextView) view.findViewById(R.id.ok);
        TextView cancel = (TextView) view.findViewById(R.id.cancel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StrUtils.isNull(content.getText().toString().trim())){
                    viewDelegate.toast("请输入地址");
                    return;
                }
                dialog.dismiss();
                addressName=content.getText().toString().trim();
                GeocodeQuery query = new GeocodeQuery(content.getText().toString().trim(), infoEntity.pushId);// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
                geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
}
