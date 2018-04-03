package com.zhuandanbao.app.entity;

import java.io.Serializable;

/**
 * Created by BFTECH on 2017/2/20.
 */

public class ShopInfoEntity implements Serializable {

    public ShopInfoEntity(){

    }
    private String ShopCategory;
    private String ShopTypes;
    private String shopInfo;

    public String getShopCategory() {
        return ShopCategory;
    }

    public void setShopCategory(String shopCategory) {
        ShopCategory = shopCategory;
    }

    public String getShopTypes() {
        return ShopTypes;
    }

    public void setShopTypes(String shopTypes) {
        ShopTypes = shopTypes;
    }

    public String getShopInfo() {
        return shopInfo;
    }

    public void setShopInfo(String shopInfo) {
        this.shopInfo = shopInfo;
    }
}
