package app.zhuandanbao.com.reslib.widget.area;

import java.util.List;

/**
 * Created by BFTECH on 2016/3/7.
 */
public class CityModel {
    private String codeId;
    private String name;
    private List<CountyModel> countyModelList;

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CountyModel> getCountyModelList() {
        return countyModelList;
    }

    public void setCountyModelList(List<CountyModel> countyModelList) {
        this.countyModelList = countyModelList;
    }
}
