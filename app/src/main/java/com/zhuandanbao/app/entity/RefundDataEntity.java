package com.zhuandanbao.app.entity;

import java.io.Serializable;

/**
 * Created by BFTECH on 2017/3/15.
 */

public class RefundDataEntity implements Serializable {

    /**
     * close : 0
     * is_edit : 0
     * is_revoke : 0
     * issue_type : 17
     * issue_type_name : [撤单]发单方申请撤单：订货人取消&订单信息有误&其它
     * order_amount : 0.02
     * order_id : 246354
     * order_sn : 2017031324635467
     * return_amount : 0.02
     * return_audit_img : []
     * return_audit_text :
     * return_audit_time : 0
     * return_audit_user :
     * return_img : []
     * return_judge : 0
     * return_judge_apply_desc :
     * return_judge_apply_tel :
     * return_judge_apply_time : 0
     * return_judge_img :
     * return_judge_text :
     * return_judge_time : 0
     * return_mode : 1
     * return_mode_name : 全额退款
     * return_reason : 跛子
     * return_self_apply : Y
     * return_shop_name : 上海雯葭测试店铺请勿下单
     * return_sid : 11405
     * return_status : 0
     * return_time : 1489550145
     * return_time_format : 2017-03-15 11:55:45
     * return_user : 袁晓文
     * revoke_time :
     * revoke_user :
     * rid : 7376
     * shop_name : 天津测试店铺请勿下单
     * sid : 20739
     */

    private String close;
    private String is_edit;
    private String is_revoke;
    private String issue_type;
    private String issue_type_name;
    private String order_amount;
    private String order_id;
    private String order_sn;
    private String return_amount;
    private String return_audit_text;
    private String return_audit_time;
    private String return_audit_user;
    private String return_audit_time_format;
    private String return_judge;
    private String return_judge_apply_desc;
    private String return_judge_apply_tel;
    private String return_judge_apply_time;
    private String return_judge_apply_time_format;
    private String return_judge_img;
    private String return_judge_text;
    private String return_judge_time;
    private String return_judge_time_format;
    private String return_mode;
    private String return_mode_name;
    private String return_reason;
    private String return_self_apply;
    private String return_shop_name;
    private String return_sid;
    private String return_status;
    private String return_time;
    private String return_time_format;
    private String return_user;
    private String revoke_time;
    private String revoke_user;
    private String rid;
    private String shop_name;
    private String sid;
    private String return_audit_img;
    private String return_img;

    public RefundDataEntity() {

    }

    public String getReturn_judge_apply_time_format() {
        return return_judge_apply_time_format;
    }

    public void setReturn_judge_apply_time_format(String return_judge_apply_time_format) {
        this.return_judge_apply_time_format = return_judge_apply_time_format;
    }

    public String getReturn_judge_time_format() {
        return return_judge_time_format;
    }

    public void setReturn_judge_time_format(String return_judge_time_format) {
        this.return_judge_time_format = return_judge_time_format;
    }

    public String getReturn_audit_time_format() {
        return return_audit_time_format;
    }

    public void setReturn_audit_time_format(String return_audit_time_format) {
        this.return_audit_time_format = return_audit_time_format;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getIs_edit() {
        return is_edit;
    }

    public void setIs_edit(String is_edit) {
        this.is_edit = is_edit;
    }

    public String getIs_revoke() {
        return is_revoke;
    }

    public void setIs_revoke(String is_revoke) {
        this.is_revoke = is_revoke;
    }

    public String getIssue_type() {
        return issue_type;
    }

    public void setIssue_type(String issue_type) {
        this.issue_type = issue_type;
    }

    public String getIssue_type_name() {
        return issue_type_name;
    }

    public void setIssue_type_name(String issue_type_name) {
        this.issue_type_name = issue_type_name;
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

    public String getReturn_amount() {
        return return_amount;
    }

    public void setReturn_amount(String return_amount) {
        this.return_amount = return_amount;
    }

    public String getReturn_audit_text() {
        return return_audit_text;
    }

    public void setReturn_audit_text(String return_audit_text) {
        this.return_audit_text = return_audit_text;
    }

    public String getReturn_audit_time() {
        return return_audit_time;
    }

    public void setReturn_audit_time(String return_audit_time) {
        this.return_audit_time = return_audit_time;
    }

    public String getReturn_audit_user() {
        return return_audit_user;
    }

    public void setReturn_audit_user(String return_audit_user) {
        this.return_audit_user = return_audit_user;
    }

    public String getReturn_judge() {
        return return_judge;
    }

    public void setReturn_judge(String return_judge) {
        this.return_judge = return_judge;
    }

    public String getReturn_judge_apply_desc() {
        return return_judge_apply_desc;
    }

    public void setReturn_judge_apply_desc(String return_judge_apply_desc) {
        this.return_judge_apply_desc = return_judge_apply_desc;
    }

    public String getReturn_judge_apply_tel() {
        return return_judge_apply_tel;
    }

    public void setReturn_judge_apply_tel(String return_judge_apply_tel) {
        this.return_judge_apply_tel = return_judge_apply_tel;
    }

    public String getReturn_judge_apply_time() {
        return return_judge_apply_time;
    }

    public void setReturn_judge_apply_time(String return_judge_apply_time) {
        this.return_judge_apply_time = return_judge_apply_time;
    }

    public String getReturn_judge_img() {
        return return_judge_img;
    }

    public void setReturn_judge_img(String return_judge_img) {
        this.return_judge_img = return_judge_img;
    }

    public String getReturn_judge_text() {
        return return_judge_text;
    }

    public void setReturn_judge_text(String return_judge_text) {
        this.return_judge_text = return_judge_text;
    }

    public String getReturn_judge_time() {
        return return_judge_time;
    }

    public void setReturn_judge_time(String return_judge_time) {
        this.return_judge_time = return_judge_time;
    }

    public String getReturn_mode() {
        return return_mode;
    }

    public void setReturn_mode(String return_mode) {
        this.return_mode = return_mode;
    }

    public String getReturn_mode_name() {
        return return_mode_name;
    }

    public void setReturn_mode_name(String return_mode_name) {
        this.return_mode_name = return_mode_name;
    }

    public String getReturn_reason() {
        return return_reason;
    }

    public void setReturn_reason(String return_reason) {
        this.return_reason = return_reason;
    }

    public String getReturn_self_apply() {
        return return_self_apply;
    }

    public void setReturn_self_apply(String return_self_apply) {
        this.return_self_apply = return_self_apply;
    }

    public String getReturn_shop_name() {
        return return_shop_name;
    }

    public void setReturn_shop_name(String return_shop_name) {
        this.return_shop_name = return_shop_name;
    }

    public String getReturn_sid() {
        return return_sid;
    }

    public void setReturn_sid(String return_sid) {
        this.return_sid = return_sid;
    }

    public String getReturn_status() {
        return return_status;
    }

    public void setReturn_status(String return_status) {
        this.return_status = return_status;
    }

    public String getReturn_time() {
        return return_time;
    }

    public void setReturn_time(String return_time) {
        this.return_time = return_time;
    }

    public String getReturn_time_format() {
        return return_time_format;
    }

    public void setReturn_time_format(String return_time_format) {
        this.return_time_format = return_time_format;
    }

    public String getReturn_user() {
        return return_user;
    }

    public void setReturn_user(String return_user) {
        this.return_user = return_user;
    }

    public String getRevoke_time() {
        return revoke_time;
    }

    public void setRevoke_time(String revoke_time) {
        this.revoke_time = revoke_time;
    }

    public String getRevoke_user() {
        return revoke_user;
    }

    public void setRevoke_user(String revoke_user) {
        this.revoke_user = revoke_user;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getReturn_audit_img() {
        return return_audit_img;
    }

    public void setReturn_audit_img(String return_audit_img) {
        this.return_audit_img = return_audit_img;
    }

    public String getReturn_img() {
        return return_img;
    }

    public void setReturn_img(String return_img) {
        this.return_img = return_img;
    }
}
