package com.zhuandanbao.app.entity;

import java.io.Serializable;

/**
 * Created by BFTECH on 2017/3/8.
 */

public class BiddingOrderInfoEntity implements Serializable {


    private String address;
    private String areainfo;
    private String delivery_geo;
    private String delivery_lat;
    private String delivery_lng;
    private String order_amount;
    private String order_id;
    private String order_sn;
    private String order_status;
    private String order_status_remark;
    private String receive_sid;
    private String source_sid;

    public BiddingOrderInfoEntity(){

    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAreainfo() {
        return areainfo;
    }

    public void setAreainfo(String areainfo) {
        this.areainfo = areainfo;
    }

    public String getDelivery_geo() {
        return delivery_geo;
    }

    public void setDelivery_geo(String delivery_geo) {
        this.delivery_geo = delivery_geo;
    }

    public String getDelivery_lat() {
        return delivery_lat;
    }

    public void setDelivery_lat(String delivery_lat) {
        this.delivery_lat = delivery_lat;
    }

    public String getDelivery_lng() {
        return delivery_lng;
    }

    public void setDelivery_lng(String delivery_lng) {
        this.delivery_lng = delivery_lng;
    }

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getOrder_status_remark() {
        return order_status_remark;
    }

    public void setOrder_status_remark(String order_status_remark) {
        this.order_status_remark = order_status_remark;
    }

    public String getReceive_sid() {
        return receive_sid;
    }

    public void setReceive_sid(String receive_sid) {
        this.receive_sid = receive_sid;
    }

    public String getSource_sid() {
        return source_sid;
    }

    public void setSource_sid(String source_sid) {
        this.source_sid = source_sid;
    }
}
