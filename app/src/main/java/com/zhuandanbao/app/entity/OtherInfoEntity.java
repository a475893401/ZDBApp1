package com.zhuandanbao.app.entity;

import java.io.Serializable;

/**
 * Created by BFTECH on 2017/3/9.
 */

public class OtherInfoEntity  implements Serializable {
    private String id;
    private String order_sn;
    private String source_type;
    private String sid;
    private String app_id;
    private String vender_id;
    private String vender_shop;
    private String buyer_name;
    private String buyer_mobile;
    private String buyer_phone;
    private String consignee;
    private String province;
    private String city;
    private String county;
    private String areainfo;
    private String address;
    private String zipcode;
    private String mobile;
    private String phone;
    private String email;
    private String pay_type;
    private String pay_status;
    private String total_amount;
    private String order_amount;
    private String items_amount;
    private String balance_used;
    private String order_discount;
    private String delivery_type;
    private String delivery_name;
    private String delivery_fee;
    private String delivery_date;
    private String delivery_time;
    private String order_remarks;
    private String seller_remark;
    private String if_invoice;
    private String invoice_info;
    private String if_card;
    private String card_con;
    private String card_fee;
    private String express;
    private String express_name;
    private String express_sn;
    private String create_time;
    private String update_time;
    private String down_time;
    private String order_status;
    private String sanfang_status;
    private String sign_user;
    private String sign_time;
    private String sign_voucher;
    private String repeal_user;
    private String repeal_reason;
    private String process;
    private String process_remark;
    private String receive_id;
    private String zdb_order_sn;
    private String fadan_amount;
    private String fadan_time;
    private String fadan_user;
    private int single_way;
    private String sny_time;
    private String sny_status;
    private String action_logs;
    private String order_items;
    private String order_status_remark;
    private String source_shop_name;
    private String zdb_order_status_remark;
    private String zhuandanOrder;
    private String issues_order;
    private String issues_type;
    private String issues_reason;
    private int refund_status;

    public int getRefund_status() {
        return refund_status;
    }

    public void setRefund_status(int refund_status) {
        this.refund_status = refund_status;
    }

    public String getIssues_order() {
        return issues_order;
    }

    public void setIssues_order(String issues_order) {
        this.issues_order = issues_order;
    }

    public String getIssues_type() {
        return issues_type;
    }

    public void setIssues_type(String issues_type) {
        this.issues_type = issues_type;
    }

    public String getIssues_reason() {
        return issues_reason;
    }

    public void setIssues_reason(String issues_reason) {
        this.issues_reason = issues_reason;
    }

    public String getZhuandanOrder() {
        return zhuandanOrder;
    }

    public void setZhuandanOrder(String zhuandanOrder) {
        this.zhuandanOrder = zhuandanOrder;
    }

    public String getZdb_order_status_remark() {
        return zdb_order_status_remark;
    }

    public void setZdb_order_status_remark(String zdb_order_status_remark) {
        this.zdb_order_status_remark = zdb_order_status_remark;
    }

    public String getSource_shop_name() {
        return source_shop_name;
    }

    public void setSource_shop_name(String source_shop_name) {
        this.source_shop_name = source_shop_name;
    }

    public String getOrder_status_remark() {
        return order_status_remark;
    }

    public void setOrder_status_remark(String order_status_remark) {
        this.order_status_remark = order_status_remark;
    }

    public OtherInfoEntity(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getSource_type() {
        return source_type;
    }

    public void setSource_type(String source_type) {
        this.source_type = source_type;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getVender_id() {
        return vender_id;
    }

    public void setVender_id(String vender_id) {
        this.vender_id = vender_id;
    }

    public String getVender_shop() {
        return vender_shop;
    }

    public void setVender_shop(String vender_shop) {
        this.vender_shop = vender_shop;
    }

    public String getBuyer_name() {
        return buyer_name;
    }

    public void setBuyer_name(String buyer_name) {
        this.buyer_name = buyer_name;
    }

    public String getBuyer_mobile() {
        return buyer_mobile;
    }

    public void setBuyer_mobile(String buyer_mobile) {
        this.buyer_mobile = buyer_mobile;
    }

    public String getBuyer_phone() {
        return buyer_phone;
    }

    public void setBuyer_phone(String buyer_phone) {
        this.buyer_phone = buyer_phone;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getAreainfo() {
        return areainfo;
    }

    public void setAreainfo(String areainfo) {
        this.areainfo = areainfo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getPay_status() {
        return pay_status;
    }

    public void setPay_status(String pay_status) {
        this.pay_status = pay_status;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }

    public String getItems_amount() {
        return items_amount;
    }

    public void setItems_amount(String items_amount) {
        this.items_amount = items_amount;
    }

    public String getBalance_used() {
        return balance_used;
    }

    public void setBalance_used(String balance_used) {
        this.balance_used = balance_used;
    }

    public String getOrder_discount() {
        return order_discount;
    }

    public void setOrder_discount(String order_discount) {
        this.order_discount = order_discount;
    }

    public String getDelivery_type() {
        return delivery_type;
    }

    public void setDelivery_type(String delivery_type) {
        this.delivery_type = delivery_type;
    }

    public String getDelivery_name() {
        return delivery_name;
    }

    public void setDelivery_name(String delivery_name) {
        this.delivery_name = delivery_name;
    }

    public String getDelivery_fee() {
        return delivery_fee;
    }

    public void setDelivery_fee(String delivery_fee) {
        this.delivery_fee = delivery_fee;
    }

    public String getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(String delivery_date) {
        this.delivery_date = delivery_date;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public String getOrder_remarks() {
        return order_remarks;
    }

    public void setOrder_remarks(String order_remarks) {
        this.order_remarks = order_remarks;
    }

    public String getSeller_remark() {
        return seller_remark;
    }

    public void setSeller_remark(String seller_remark) {
        this.seller_remark = seller_remark;
    }

    public String getIf_invoice() {
        return if_invoice;
    }

    public void setIf_invoice(String if_invoice) {
        this.if_invoice = if_invoice;
    }

    public String getInvoice_info() {
        return invoice_info;
    }

    public void setInvoice_info(String invoice_info) {
        this.invoice_info = invoice_info;
    }

    public String getIf_card() {
        return if_card;
    }

    public void setIf_card(String if_card) {
        this.if_card = if_card;
    }

    public String getCard_con() {
        return card_con;
    }

    public void setCard_con(String card_con) {
        this.card_con = card_con;
    }

    public String getCard_fee() {
        return card_fee;
    }

    public void setCard_fee(String card_fee) {
        this.card_fee = card_fee;
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }

    public String getExpress_name() {
        return express_name;
    }

    public void setExpress_name(String express_name) {
        this.express_name = express_name;
    }

    public String getExpress_sn() {
        return express_sn;
    }

    public void setExpress_sn(String express_sn) {
        this.express_sn = express_sn;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getDown_time() {
        return down_time;
    }

    public void setDown_time(String down_time) {
        this.down_time = down_time;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getSanfang_status() {
        return sanfang_status;
    }

    public void setSanfang_status(String sanfang_status) {
        this.sanfang_status = sanfang_status;
    }

    public String getSign_user() {
        return sign_user;
    }

    public void setSign_user(String sign_user) {
        this.sign_user = sign_user;
    }

    public String getSign_time() {
        return sign_time;
    }

    public void setSign_time(String sign_time) {
        this.sign_time = sign_time;
    }

    public String getSign_voucher() {
        return sign_voucher;
    }

    public void setSign_voucher(String sign_voucher) {
        this.sign_voucher = sign_voucher;
    }

    public String getRepeal_user() {
        return repeal_user;
    }

    public void setRepeal_user(String repeal_user) {
        this.repeal_user = repeal_user;
    }

    public String getRepeal_reason() {
        return repeal_reason;
    }

    public void setRepeal_reason(String repeal_reason) {
        this.repeal_reason = repeal_reason;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getProcess_remark() {
        return process_remark;
    }

    public void setProcess_remark(String process_remark) {
        this.process_remark = process_remark;
    }

    public String getReceive_id() {
        return receive_id;
    }

    public void setReceive_id(String receive_id) {
        this.receive_id = receive_id;
    }

    public String getZdb_order_sn() {
        return zdb_order_sn;
    }

    public void setZdb_order_sn(String zdb_order_sn) {
        this.zdb_order_sn = zdb_order_sn;
    }

    public String getFadan_amount() {
        return fadan_amount;
    }

    public void setFadan_amount(String fadan_amount) {
        this.fadan_amount = fadan_amount;
    }

    public String getFadan_time() {
        return fadan_time;
    }

    public void setFadan_time(String fadan_time) {
        this.fadan_time = fadan_time;
    }

    public String getFadan_user() {
        return fadan_user;
    }

    public void setFadan_user(String fadan_user) {
        this.fadan_user = fadan_user;
    }

    public int getSingle_way() {
        return single_way;
    }
    public void setSingle_way(int single_way) {
        this.single_way = single_way;
    }

    public String getSny_time() {
        return sny_time;
    }

    public void setSny_time(String sny_time) {
        this.sny_time = sny_time;
    }

    public String getSny_status() {
        return sny_status;
    }

    public void setSny_status(String sny_status) {
        this.sny_status = sny_status;
    }

    public String getAction_logs() {
        return action_logs;
    }

    public void setAction_logs(String action_logs) {
        this.action_logs = action_logs;
    }

    public String getOrder_items() {
        return order_items;
    }

    public void setOrder_items(String order_items) {
        this.order_items = order_items;
    }
}
