package com.zhuandanbao.app.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by BFTECH on 2017/3/10.
 */

public class OrderItemListEntity implements Serializable {

    public String item_name;
    public String item_remarks;
    public String item_total;
    public List<String> item_img;

    public OrderItemListEntity(){

    }
}
