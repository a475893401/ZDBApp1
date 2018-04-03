package app.zhuandanbao.com.reslib.widget.area;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.zhuandanbao.com.reslib.R;


/**
 * Created by BFTECH on 2016/3/7.
 */
public class PopArea {

    private Context context;
    private PopupWindow popW;
    private SelectAreaWheelPopWOnClick onClick;
    /**
     * 所有省
     */
    private String[] mProvinceDatas;
    private List<ProvinceModel> provinceList;
    /**
     * key - 省 value - 市
     */
    private Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区
     */
    private Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * key - 区 values - id
     */
    private Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();
    /**
     * 当前省的名称
     */
    private String mCurrentProviceName;
    private String mCurrentProviceId = "0";
    /**
     * 当前市的名称
     */
    private String mCurrentCityName;
    private String mCurrentCityId = "0";
    /**
     * 当前区的名称
     */
    private String mCurrentDistrictName = "";
    /**
     * 当前区的id
     */
    private String mCurrentDistrictId = "0";


    private WheelView provinceWheel;
    private WheelView cityWheel;
    private WheelView countyWheel;
    private String[] defatValue;//0表示城市 1:表示市  2 表示区
    private int isDefatProvince = 0;
    private int isDefatCity = 0;
    private int isDefatCounty = 0;

    /**
     * 显示窗口
     *
     * @param v
     * @param onClick
     * @return
     */
    public PopupWindow showPopw(View v, boolean isTwo, String[] defatValue, SelectAreaWheelPopWOnClick onClick) {
        this.onClick = onClick;
        this.defatValue = defatValue;
        if (popW == null) {
            initView(isTwo);
        }
        popW.showAtLocation(v, Gravity.BOTTOM, 0, 0);
        return popW;
    }

    public static PopArea getInstance(String objStr, Context context) {
        PopArea pop = new PopArea(CityJson.getProvince(objStr), context);
        return pop;
    }

    private PopArea(List<ProvinceModel> provinceList, Context context) {
        this.provinceList = provinceList;
        this.context = context;
        initData();
    }

    private void initData() {
        mProvinceDatas = new String[provinceList.size()];
        for (int i = 0; i < provinceList.size(); i++) {
//            MLog.e("provinceList.get(i).getName()="+provinceList.get(i).getName());
            mProvinceDatas[i] = provinceList.get(i).getName();
            List<CityModel> cityList = provinceList.get(i).getCityModelList();
            String[] cityName = new String[cityList.size()];
            for (int j = 0; j < cityList.size(); j++) {
//                MLog.e("cityList.get(j).getName()="+cityList.get(j).getName());
                cityName[j] = cityList.get(j).getName();
                List<CountyModel> countyList = cityList.get(j).getCountyModelList();
                String[] coutyName = new String[countyList.size()];
                CountyModel[] countyModels = new CountyModel[countyList.size()];
                for (int k = 0; k < countyList.size(); k++) {
//                    MLog.e("countyList.get(k).getName()="+countyList.get(k).getName());
                    CountyModel countyModel = new CountyModel();
                    countyModel.setName(countyList.get(k).getName());
                    countyModel.setCodeId(countyList.get(k).getCodeId());
                    countyModels[k] = countyModel;
                    coutyName[k] = countyList.get(k).getName();
                    mZipcodeDatasMap.put(countyList.get(k).getName(), countyList.get(k).getCodeId());
                }
                mDistrictDatasMap.put(cityList.get(j).getName(), coutyName);
            }
            mCitisDatasMap.put(provinceList.get(i).getName(), cityName);
        }
    }

    private void initView(boolean isTwo) {
        LayoutInflater lf = LayoutInflater.from(context);
        View v = lf.inflate(R.layout.popw_select_area, null);
        popW = new PopupWindow(v, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        popW.setFocusable(true);
        popW.setOutsideTouchable(true);
        popW.setBackgroundDrawable(new BitmapDrawable());
        provinceWheel = (WheelView) v.findViewById(R.id.wheel1);
        cityWheel = (WheelView) v.findViewById(R.id.wheel2);
        countyWheel = (WheelView) v.findViewById(R.id.wheel3);
        if (isTwo) {
            countyWheel.setVisibility(View.GONE);
        }
        provinceWheel.addChangingListener(new wheelListener());
        cityWheel.addChangingListener(new wheelListener());
        countyWheel.addChangingListener(new wheelListener());
        LinearLayout no = (LinearLayout) v.findViewById(R.id.no);
        LinearLayout yes = (LinearLayout) v.findViewById(R.id.yes);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popW.dismiss();
                onClick.cancleOnClick();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popW.dismiss();
                onClick.sureOnClick(Integer.parseInt(mCurrentProviceId), Integer.parseInt(mCurrentCityId), Integer.parseInt(mCurrentDistrictId), mCurrentProviceName, mCurrentCityName, mCurrentDistrictName);
            }
        });
        if (null != defatValue) {
            if (defatValue.length > 0) {
                for (int i = 0; i < provinceList.size(); i++) {
                    if (provinceList.get(i).getName().equals(defatValue[0])) {
                        isDefatProvince = i;
                        List<CityModel> cityModelList = provinceList.get(i).getCityModelList();
                        if (defatValue.length > 1) {
                            for (int j = 0; j < cityModelList.size(); j++) {
                                if (cityModelList.get(j).getName().equals(defatValue[1])) {
                                    isDefatCity = j;
                                    List<CountyModel> countyModelList = cityModelList.get(j).getCountyModelList();
                                    if (defatValue.length > 2) {
                                        for (int k = 0; k < countyModelList.size(); k++) {
                                            if (countyModelList.get(k).getName().equals(defatValue[2])) {
                                                isDefatCounty = k;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        provinceWheel.setViewAdapter(new ArrayWheelAdapter<>(context, mProvinceDatas));
        // 设置可见条目数量
        provinceWheel.setVisibleItems(7);
        provinceWheel.setCurrentItem(isDefatProvince);
        cityWheel.setVisibleItems(7);
        countyWheel.setVisibleItems(7);
        updateCities(false, 0);
    }

    /**
     * 根据当前的市，更新区WheelView的信息
     * isUpdata  是不是刷新  newIndex 刷新的地址
     */
    private void updateAreas(boolean isUpdata, int newIndex) {
        if (isUpdata) {
            mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[newIndex];
        } else {
            int pCurrent = cityWheel.getCurrentItem();
            mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        }
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);
        if (areas == null) {
            areas = new String[]{""};
        }
        countyWheel.setViewAdapter(new ArrayWheelAdapter<String>(context, areas));
        if (isUpdata) {
            countyWheel.setCurrentItem(0);
        } else {
            countyWheel.setCurrentItem(isDefatCounty);
        }
        mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
        if (!TextUtils.isEmpty(mCurrentDistrictName)) {
            mCurrentDistrictId = mZipcodeDatasMap.get(mCurrentDistrictName);
        }
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     * isUpdata  是不是刷新  newIndex 刷新的地址
     */
    private void updateCities(boolean isUpdata, int newIndex) {
        if (isUpdata) {
            mCurrentProviceName = mProvinceDatas[newIndex];
        } else {
            int pCurrent = provinceWheel.getCurrentItem();
            mCurrentProviceName = mProvinceDatas[pCurrent];
        }
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        cityWheel.setViewAdapter(new ArrayWheelAdapter<String>(context, cities));
        if (isUpdata) {
            cityWheel.setCurrentItem(0);
            updateAreas(true, 0);
        } else {
            cityWheel.setCurrentItem(isDefatCity);
            updateAreas(false, 0);
        }
    }


    class wheelListener implements OnWheelChangedListener {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            if (wheel == provinceWheel) {
                updateCities(true, newValue);
            } else if (wheel == cityWheel) {
                updateAreas(true, newValue);
            }
            if (wheel == countyWheel) {
                mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
                if (!TextUtils.isEmpty(mCurrentDistrictName)) {
                    mCurrentDistrictId = mZipcodeDatasMap.get(mCurrentDistrictName);
                }
            }
        }
    }
}
