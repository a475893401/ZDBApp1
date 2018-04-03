package com.zhuandanbao.app.appdelegate;

import java.io.Serializable;

/**
 * Created by BFTECH on 2017/3/14.
 */

public class TradeLogEntity implements Serializable {
    /**
     * parties : 发布抢单模式订单支付抢单款项
     * parties_id : 0
     * plus_or_minus : -
     * shopName : 上海雯葭测试店铺请勿下单
     * sid : 11405
     * status : 1
     * status_reamrk : 交易进行中
     * time : 1489460276
     * time_format : 2017-03-14 10:57:56
     * tr_type : 0
     * trade_amount : 0.01
     * trade_con : 订单：2017031424694231
     * trade_sn : 2017031424694231
     * type : 0
     */
    private String parties;
    private String parties_id;
    private String plus_or_minus;
    private String shopName;
    private String sid;
    private String status;
    private String status_reamrk;
    private String time;
    private String time_format;
    private String tr_type;
    private String trade_amount;
    private String trade_con;
    private String trade_sn;
    private String type;

    public TradeLogEntity() {

    }


    public String getParties() {
        return parties;
    }

    public void setParties(String parties) {
        this.parties = parties;
    }

    public String getParties_id() {
        return parties_id;
    }

    public void setParties_id(String parties_id) {
        this.parties_id = parties_id;
    }

    public String getPlus_or_minus() {
        return plus_or_minus;
    }

    public void setPlus_or_minus(String plus_or_minus) {
        this.plus_or_minus = plus_or_minus;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus_reamrk() {
        return status_reamrk;
    }

    public void setStatus_reamrk(String status_reamrk) {
        this.status_reamrk = status_reamrk;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime_format() {
        return time_format;
    }

    public void setTime_format(String time_format) {
        this.time_format = time_format;
    }

    public String getTr_type() {
        return tr_type;
    }

    public void setTr_type(String tr_type) {
        this.tr_type = tr_type;
    }

    public String getTrade_amount() {
        return trade_amount;
    }

    public void setTrade_amount(String trade_amount) {
        this.trade_amount = trade_amount;
    }

    public String getTrade_con() {
        return trade_con;
    }

    public void setTrade_con(String trade_con) {
        this.trade_con = trade_con;
    }

    public String getTrade_sn() {
        return trade_sn;
    }

    public void setTrade_sn(String trade_sn) {
        this.trade_sn = trade_sn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
