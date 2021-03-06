package com.zhuandanbao.app.entity;

import java.io.Serializable;

/**
 * Created by BFTECH on 2017/3/16.
 */

public class SystemSettingEntity  implements Serializable {


    /**
     * ZD_ALLOW_CASH : YES
     * ZD_ALLOW_CASH_FEE : 0.01
     * ZD_ALLOW_CROSS_GRAB_ORDERS : NO
     * ZD_ALLOW_CROSS_ORDERS : NO
     * ZD_ALLOW_WITHDRAW : YES
     * ZD_CASH_WITHDRAWAL_FEE : NO
     * ZD_DAY_PLATFORM_GRAB_ORDERS_MAX_NUM : 0
     * ZD_DAY_STORE_GRAB_ORDERS_MAX_NUM : 0
     * ZD_DAY_WITHDRAW_LIMIT : 3
     * ZD_DAY_WITHDRAW_MAX_AMOUNT : 60000
     * ZD_MAX_RADIUS_RANGE_GRAB_ORDER_ : 10
     * ZD_MONTH_PROHIBIT_GRAB_ORDER_AFTERSALE_MAX_NUM : 3
     * ZD_ORDER_MUST_REAL_PHOTO : NO
     * ZD_ORDER_MUST_REAL_PHOTO_EXPLAIN : 您还未上传实物照，上传后可以增加发单方对您的信任度！以便接更多的订单。
     * ZD_ORDER_MUST_SIGN_PHOTO : YES
     * ZD_ORDER_MUST_SIGN_PHOTO_EXPLAIN : 请上传签收凭证！
     * ZD_PROHIBIT_WITHDRAW_DATE_LIMIT :
     * ZD_PROHIBIT_WITHDRAW_REASON :
     * ZD_SINGLE_WITHDRAW_FEE : 3.00
     * ZD_SINGLE_WITHDRAW_MAX_AMOUNT : 20000
     * ZD_SINGLE_WITHDRAW_MIN_AMOUNT : 100
     * ZD_WITHDRAW_MONTH_FREE_NUM : 1
     */

    private String ZD_ALLOW_CASH;
    private String ZD_ALLOW_CASH_FEE;
    private String ZD_ALLOW_CROSS_GRAB_ORDERS;
    private String ZD_ALLOW_CROSS_ORDERS;
    private String ZD_ALLOW_WITHDRAW;
    private String ZD_CASH_WITHDRAWAL_FEE;
    private String ZD_DAY_PLATFORM_GRAB_ORDERS_MAX_NUM;
    private String ZD_DAY_STORE_GRAB_ORDERS_MAX_NUM;
    private int ZD_DAY_WITHDRAW_LIMIT;
    private int ZD_DAY_WITHDRAW_MAX_AMOUNT;
    private String ZD_MAX_RADIUS_RANGE_GRAB_ORDER_;
    private String ZD_MONTH_PROHIBIT_GRAB_ORDER_AFTERSALE_MAX_NUM;
    private String ZD_ORDER_MUST_REAL_PHOTO;
    private String ZD_ORDER_MUST_REAL_PHOTO_EXPLAIN;
    private String ZD_ORDER_MUST_SIGN_PHOTO;
    private String ZD_ORDER_MUST_SIGN_PHOTO_EXPLAIN;
    private String ZD_PROHIBIT_WITHDRAW_DATE_LIMIT;
    private String ZD_PROHIBIT_WITHDRAW_REASON;
    private String ZD_SINGLE_WITHDRAW_FEE;
    private int ZD_SINGLE_WITHDRAW_MAX_AMOUNT;
    private int ZD_SINGLE_WITHDRAW_MIN_AMOUNT;
    private String ZD_WITHDRAW_MONTH_FREE_NUM;
    private String ZD_CUSTOMER_SERVICE_HOTLINE;
    private String ZD_CUSTOMER_SERVICE_QQ;

    public String getZD_CUSTOMER_SERVICE_HOTLINE() {
        return ZD_CUSTOMER_SERVICE_HOTLINE;
    }

    public void setZD_CUSTOMER_SERVICE_HOTLINE(String ZD_CUSTOMER_SERVICE_HOTLINE) {
        this.ZD_CUSTOMER_SERVICE_HOTLINE = ZD_CUSTOMER_SERVICE_HOTLINE;
    }

    public String getZD_CUSTOMER_SERVICE_QQ() {
        return ZD_CUSTOMER_SERVICE_QQ;
    }

    public void setZD_CUSTOMER_SERVICE_QQ(String ZD_CUSTOMER_SERVICE_QQ) {
        this.ZD_CUSTOMER_SERVICE_QQ = ZD_CUSTOMER_SERVICE_QQ;
    }

    public SystemSettingEntity(){

    }


    public String getZD_ALLOW_CASH() {
        return ZD_ALLOW_CASH;
    }

    public void setZD_ALLOW_CASH(String ZD_ALLOW_CASH) {
        this.ZD_ALLOW_CASH = ZD_ALLOW_CASH;
    }

    public String getZD_ALLOW_CASH_FEE() {
        return ZD_ALLOW_CASH_FEE;
    }

    public void setZD_ALLOW_CASH_FEE(String ZD_ALLOW_CASH_FEE) {
        this.ZD_ALLOW_CASH_FEE = ZD_ALLOW_CASH_FEE;
    }

    public String getZD_ALLOW_CROSS_GRAB_ORDERS() {
        return ZD_ALLOW_CROSS_GRAB_ORDERS;
    }

    public void setZD_ALLOW_CROSS_GRAB_ORDERS(String ZD_ALLOW_CROSS_GRAB_ORDERS) {
        this.ZD_ALLOW_CROSS_GRAB_ORDERS = ZD_ALLOW_CROSS_GRAB_ORDERS;
    }

    public String getZD_ALLOW_CROSS_ORDERS() {
        return ZD_ALLOW_CROSS_ORDERS;
    }

    public void setZD_ALLOW_CROSS_ORDERS(String ZD_ALLOW_CROSS_ORDERS) {
        this.ZD_ALLOW_CROSS_ORDERS = ZD_ALLOW_CROSS_ORDERS;
    }

    public String getZD_ALLOW_WITHDRAW() {
        return ZD_ALLOW_WITHDRAW;
    }

    public void setZD_ALLOW_WITHDRAW(String ZD_ALLOW_WITHDRAW) {
        this.ZD_ALLOW_WITHDRAW = ZD_ALLOW_WITHDRAW;
    }

    public String getZD_CASH_WITHDRAWAL_FEE() {
        return ZD_CASH_WITHDRAWAL_FEE;
    }

    public void setZD_CASH_WITHDRAWAL_FEE(String ZD_CASH_WITHDRAWAL_FEE) {
        this.ZD_CASH_WITHDRAWAL_FEE = ZD_CASH_WITHDRAWAL_FEE;
    }

    public String getZD_DAY_PLATFORM_GRAB_ORDERS_MAX_NUM() {
        return ZD_DAY_PLATFORM_GRAB_ORDERS_MAX_NUM;
    }

    public void setZD_DAY_PLATFORM_GRAB_ORDERS_MAX_NUM(String ZD_DAY_PLATFORM_GRAB_ORDERS_MAX_NUM) {
        this.ZD_DAY_PLATFORM_GRAB_ORDERS_MAX_NUM = ZD_DAY_PLATFORM_GRAB_ORDERS_MAX_NUM;
    }

    public String getZD_DAY_STORE_GRAB_ORDERS_MAX_NUM() {
        return ZD_DAY_STORE_GRAB_ORDERS_MAX_NUM;
    }

    public void setZD_DAY_STORE_GRAB_ORDERS_MAX_NUM(String ZD_DAY_STORE_GRAB_ORDERS_MAX_NUM) {
        this.ZD_DAY_STORE_GRAB_ORDERS_MAX_NUM = ZD_DAY_STORE_GRAB_ORDERS_MAX_NUM;
    }

    public int getZD_DAY_WITHDRAW_LIMIT() {
        return ZD_DAY_WITHDRAW_LIMIT;
    }

    public void setZD_DAY_WITHDRAW_LIMIT(int ZD_DAY_WITHDRAW_LIMIT) {
        this.ZD_DAY_WITHDRAW_LIMIT = ZD_DAY_WITHDRAW_LIMIT;
    }

    public int getZD_DAY_WITHDRAW_MAX_AMOUNT() {
        return ZD_DAY_WITHDRAW_MAX_AMOUNT;
    }

    public void setZD_DAY_WITHDRAW_MAX_AMOUNT(int ZD_DAY_WITHDRAW_MAX_AMOUNT) {
        this.ZD_DAY_WITHDRAW_MAX_AMOUNT = ZD_DAY_WITHDRAW_MAX_AMOUNT;
    }

    public String getZD_MAX_RADIUS_RANGE_GRAB_ORDER_() {
        return ZD_MAX_RADIUS_RANGE_GRAB_ORDER_;
    }

    public void setZD_MAX_RADIUS_RANGE_GRAB_ORDER_(String ZD_MAX_RADIUS_RANGE_GRAB_ORDER_) {
        this.ZD_MAX_RADIUS_RANGE_GRAB_ORDER_ = ZD_MAX_RADIUS_RANGE_GRAB_ORDER_;
    }

    public String getZD_MONTH_PROHIBIT_GRAB_ORDER_AFTERSALE_MAX_NUM() {
        return ZD_MONTH_PROHIBIT_GRAB_ORDER_AFTERSALE_MAX_NUM;
    }

    public void setZD_MONTH_PROHIBIT_GRAB_ORDER_AFTERSALE_MAX_NUM(String ZD_MONTH_PROHIBIT_GRAB_ORDER_AFTERSALE_MAX_NUM) {
        this.ZD_MONTH_PROHIBIT_GRAB_ORDER_AFTERSALE_MAX_NUM = ZD_MONTH_PROHIBIT_GRAB_ORDER_AFTERSALE_MAX_NUM;
    }

    public String getZD_ORDER_MUST_REAL_PHOTO() {
        return ZD_ORDER_MUST_REAL_PHOTO;
    }

    public void setZD_ORDER_MUST_REAL_PHOTO(String ZD_ORDER_MUST_REAL_PHOTO) {
        this.ZD_ORDER_MUST_REAL_PHOTO = ZD_ORDER_MUST_REAL_PHOTO;
    }

    public String getZD_ORDER_MUST_REAL_PHOTO_EXPLAIN() {
        return ZD_ORDER_MUST_REAL_PHOTO_EXPLAIN;
    }

    public void setZD_ORDER_MUST_REAL_PHOTO_EXPLAIN(String ZD_ORDER_MUST_REAL_PHOTO_EXPLAIN) {
        this.ZD_ORDER_MUST_REAL_PHOTO_EXPLAIN = ZD_ORDER_MUST_REAL_PHOTO_EXPLAIN;
    }

    public String getZD_ORDER_MUST_SIGN_PHOTO() {
        return ZD_ORDER_MUST_SIGN_PHOTO;
    }

    public void setZD_ORDER_MUST_SIGN_PHOTO(String ZD_ORDER_MUST_SIGN_PHOTO) {
        this.ZD_ORDER_MUST_SIGN_PHOTO = ZD_ORDER_MUST_SIGN_PHOTO;
    }

    public String getZD_ORDER_MUST_SIGN_PHOTO_EXPLAIN() {
        return ZD_ORDER_MUST_SIGN_PHOTO_EXPLAIN;
    }

    public void setZD_ORDER_MUST_SIGN_PHOTO_EXPLAIN(String ZD_ORDER_MUST_SIGN_PHOTO_EXPLAIN) {
        this.ZD_ORDER_MUST_SIGN_PHOTO_EXPLAIN = ZD_ORDER_MUST_SIGN_PHOTO_EXPLAIN;
    }

    public String getZD_PROHIBIT_WITHDRAW_DATE_LIMIT() {
        return ZD_PROHIBIT_WITHDRAW_DATE_LIMIT;
    }

    public void setZD_PROHIBIT_WITHDRAW_DATE_LIMIT(String ZD_PROHIBIT_WITHDRAW_DATE_LIMIT) {
        this.ZD_PROHIBIT_WITHDRAW_DATE_LIMIT = ZD_PROHIBIT_WITHDRAW_DATE_LIMIT;
    }

    public String getZD_PROHIBIT_WITHDRAW_REASON() {
        return ZD_PROHIBIT_WITHDRAW_REASON;
    }

    public void setZD_PROHIBIT_WITHDRAW_REASON(String ZD_PROHIBIT_WITHDRAW_REASON) {
        this.ZD_PROHIBIT_WITHDRAW_REASON = ZD_PROHIBIT_WITHDRAW_REASON;
    }

    public String getZD_SINGLE_WITHDRAW_FEE() {
        return ZD_SINGLE_WITHDRAW_FEE;
    }

    public void setZD_SINGLE_WITHDRAW_FEE(String ZD_SINGLE_WITHDRAW_FEE) {
        this.ZD_SINGLE_WITHDRAW_FEE = ZD_SINGLE_WITHDRAW_FEE;
    }

    public int getZD_SINGLE_WITHDRAW_MAX_AMOUNT() {
        return ZD_SINGLE_WITHDRAW_MAX_AMOUNT;
    }

    public void setZD_SINGLE_WITHDRAW_MAX_AMOUNT(int ZD_SINGLE_WITHDRAW_MAX_AMOUNT) {
        this.ZD_SINGLE_WITHDRAW_MAX_AMOUNT = ZD_SINGLE_WITHDRAW_MAX_AMOUNT;
    }

    public int getZD_SINGLE_WITHDRAW_MIN_AMOUNT() {
        return ZD_SINGLE_WITHDRAW_MIN_AMOUNT;
    }

    public void setZD_SINGLE_WITHDRAW_MIN_AMOUNT(int ZD_SINGLE_WITHDRAW_MIN_AMOUNT) {
        this.ZD_SINGLE_WITHDRAW_MIN_AMOUNT = ZD_SINGLE_WITHDRAW_MIN_AMOUNT;
    }

    public String getZD_WITHDRAW_MONTH_FREE_NUM() {
        return ZD_WITHDRAW_MONTH_FREE_NUM;
    }

    public void setZD_WITHDRAW_MONTH_FREE_NUM(String ZD_WITHDRAW_MONTH_FREE_NUM) {
        this.ZD_WITHDRAW_MONTH_FREE_NUM = ZD_WITHDRAW_MONTH_FREE_NUM;
    }
}
