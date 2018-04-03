package com.zhuandanbao.app.entity;

import java.io.Serializable;

/**
 * Created by BFTECH on 2017/3/6.
 */

public class OrderInfoEntity implements Serializable {

    private String currShopInfo;
    private String orderData;
    private String refundInfo;
    private String signInfo;
    private String test_grab_condition;

    public OrderInfoEntity(){

    }

    public String getCurrShopInfo() {
        return currShopInfo;
    }

    public void setCurrShopInfo(String currShopInfo) {
        this.currShopInfo = currShopInfo;
    }

    public String getOrderData() {
        return orderData;
    }

    public void setOrderData(String orderData) {
        this.orderData = orderData;
    }

    public String getRefundInfo() {
        return refundInfo;
    }

    public void setRefundInfo(String refundInfo) {
        this.refundInfo = refundInfo;
    }

    public String getSignInfo() {
        return signInfo;
    }

    public void setSignInfo(String signInfo) {
        this.signInfo = signInfo;
    }

    public String getTest_grab_condition() {
        return test_grab_condition;
    }

    public void setTest_grab_condition(String test_grab_condition) {
        this.test_grab_condition = test_grab_condition;
    }

}
