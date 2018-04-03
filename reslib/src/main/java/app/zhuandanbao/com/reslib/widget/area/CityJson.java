package app.zhuandanbao.com.reslib.widget.area;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by BFTECH on 2016/3/7.
 */
public class CityJson {
    public static List<ProvinceModel> getProvince(String objProvince) {
        final List<ProvinceModel> provinceList = new ArrayList<ProvinceModel>();
        ProvinceModel provice;
        final JSONObject cityJson;
        try {
            cityJson = new JSONObject(objProvince);
            JSONArray dataJson=null;
            if (cityJson.has("data")){
                 dataJson = cityJson.optJSONObject("data").optJSONArray("area_data");
            }else {
                dataJson=cityJson.optJSONArray("area_data");
            }
            for (int i = 0; i < dataJson.length(); i++) {
                provice = new ProvinceModel();
                JSONObject priocObj = dataJson.getJSONObject(i);
                int sn = priocObj.optInt("sn");
                String p = priocObj.optString("p");
                provice.setCodeId(sn);
                provice.setName(p);
                provice.setCityModelList(getCity(priocObj.optString("c")));
                provinceList.add(provice);
            }
            Collections.sort(provinceList, new Comparator<ProvinceModel>() {
                @Override
                public int compare(ProvinceModel provinceModel, ProvinceModel t1) {
                    return provinceModel.getCodeId()-t1.getCodeId();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return provinceList;
    }

    public static List<CityModel> getCity(String objCity) {
        List<CityModel> listCity = new ArrayList<CityModel>();
        TreeMap<String,Object> cityMapJson=new TreeMap<>(RebuildMap.perJson(objCity));
        Iterator city = cityMapJson.entrySet().iterator();
        while (city.hasNext()) {
            HashMap.Entry cityEnt = (HashMap.Entry) city.next();
            String cityID = cityEnt.getKey().toString();
            CityModel cityBean = new CityModel();
            cityBean.setCodeId(cityID);
            JSONObject regionJson;
            try {
                regionJson = new JSONObject(cityEnt.getValue().toString());
                String cityName = regionJson.optString("n");
                cityBean.setName(cityName);
                cityBean.setCountyModelList(getCounty(regionJson.optString("a")));
                listCity.add(cityBean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return listCity;
    }

    public static List<CountyModel> getCounty(String objCity) {
        final List<CountyModel> listRegion = new ArrayList<CountyModel>();
        try {
            if (null == objCity || "".equals(objCity) || "[]".equals(objCity)) {
                CountyModel regionBean = new CountyModel();
                regionBean.setCodeId("000000");
                regionBean.setName("");
                listRegion.add(regionBean);
            } else {
                TreeMap<String,Object> regMapJson=new TreeMap<>(RebuildMap.perJson(objCity));
                Iterator reg = regMapJson.entrySet().iterator();
                while (reg.hasNext()) {
                    HashMap.Entry regEnt = (HashMap.Entry) reg.next();
                    CountyModel regionBean = new CountyModel();
                    regionBean.setCodeId(regEnt.getKey().toString());
                    regionBean.setName(regEnt.getValue().toString());
                    listRegion.add(regionBean);
                }
            }
        } catch (Exception e) {
        }
        return listRegion;
    }
}
