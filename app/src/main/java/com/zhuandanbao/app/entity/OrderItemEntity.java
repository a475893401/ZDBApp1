package com.zhuandanbao.app.entity;

import java.io.Serializable;

/**
 * Created by BFTECH on 2017/3/1.
 */

public class OrderItemEntity implements Serializable {

    private String item_name;
    private String item_remarks;
    private String item_total;
    private String item_img;

    public OrderItemEntity(){

    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_remarks() {
        return item_remarks;
    }

    public void setItem_remarks(String item_remarks) {
        this.item_remarks = item_remarks;
    }

    public String getItem_total() {
        return item_total;
    }

    public void setItem_total(String item_total) {
        this.item_total = item_total;
    }

    public String getItem_img() {
        return item_img;
    }

    public void setItem_img(String item_img) {
        this.item_img = item_img;
    }
}
