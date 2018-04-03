package app.zhuandanbao.com.reslib.widget.area;

import java.util.List;

/**
 * Created by BFTECH on 2016/3/7.
 */
public class ProvinceModel {
    private int codeId;
    private String name;
    private List<CityModel> cityModelList;

    public List<CityModel> getCityModelList() {
        return cityModelList;
    }

    public void setCityModelList(List<CityModel> cityModelList) {
        this.cityModelList = cityModelList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCodeId() {
        return codeId;
    }

    public void setCodeId(int codeId) {
        this.codeId = codeId;
    }
}
