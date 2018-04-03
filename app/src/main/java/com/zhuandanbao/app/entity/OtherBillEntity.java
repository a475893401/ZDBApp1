package com.zhuandanbao.app.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by BFTECH on 2016/7/13.
 */
public class OtherBillEntity implements Serializable {

    public String ReceiveStoreId;
    public int order_type;
    public int delivery_type;
    public String delivery_date;
    public String delivery_time;
    public String areainfo;
    public String address;
    public String consignee;
    public String mobile;
    public String order_amount;
    public String order_remarks;
    public String card_con;
    public String deadline;
    public String shop_credit;
    public List<OrderItemListEntity> order_wares;

    public OtherBillEntity() {
    }
}
