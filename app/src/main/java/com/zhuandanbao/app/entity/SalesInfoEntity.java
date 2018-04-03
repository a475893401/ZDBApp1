package com.zhuandanbao.app.entity;

import java.io.Serializable;

/**
 * Created by BFTECH on 2017/3/15.
 */

public class SalesInfoEntity  implements Serializable {

    public SalesInfoEntity(){

    }
    private String RefundData;
    private String SendShop;
    private String orderData;
    private String receiveShop;

    public String getRefundData() {
        return RefundData;
    }

    public void setRefundData(String refundData) {
        RefundData = refundData;
    }

    public String getSendShop() {
        return SendShop;
    }

    public void setSendShop(String sendShop) {
        SendShop = sendShop;
    }

    public String getOrderData() {
        return orderData;
    }

    public void setOrderData(String orderData) {
        this.orderData = orderData;
    }

    public String getReceiveShop() {
        return receiveShop;
    }

    public void setReceiveShop(String receiveShop) {
        this.receiveShop = receiveShop;
    }
}
