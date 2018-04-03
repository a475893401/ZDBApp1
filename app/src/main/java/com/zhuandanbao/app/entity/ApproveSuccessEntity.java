package com.zhuandanbao.app.entity;

import java.io.Serializable;

/**
 * Created by BFTECH on 2017/2/21.
 */

public class ApproveSuccessEntity implements Serializable {
    private String audit;
    private String audit_time;
    private String back_img;
    private String business_license_id;
    private String business_scanning;
    private String card_id;
    private String corporation_name;
    private String food_circulation_permit;
    private String food_circulation_permit_required;
    private String food_circulation_permit_scanning;
    private String front_img;
    private String last_time;
    private String person_name;
    private String reason;
    private String sid;
    private String status;
    private String facade_photo;
    private String letter_of_application;

    public String getLetter_of_application() {
        return letter_of_application;
    }

    public void setLetter_of_application(String letter_of_application) {
        this.letter_of_application = letter_of_application;
    }

    public ApproveSuccessEntity(){

    }

    public String getAudit() {
        return audit;
    }

    public void setAudit(String audit) {
        this.audit = audit;
    }

    public String getAudit_time() {
        return audit_time;
    }

    public void setAudit_time(String audit_time) {
        this.audit_time = audit_time;
    }

    public String getBack_img() {
        return back_img;
    }

    public void setBack_img(String back_img) {
        this.back_img = back_img;
    }

    public String getBusiness_license_id() {
        return business_license_id;
    }

    public void setBusiness_license_id(String business_license_id) {
        this.business_license_id = business_license_id;
    }

    public String getBusiness_scanning() {
        return business_scanning;
    }

    public void setBusiness_scanning(String business_scanning) {
        this.business_scanning = business_scanning;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }

    public String getCorporation_name() {
        return corporation_name;
    }

    public void setCorporation_name(String corporation_name) {
        this.corporation_name = corporation_name;
    }

    public String getFood_circulation_permit() {
        return food_circulation_permit;
    }

    public void setFood_circulation_permit(String food_circulation_permit) {
        this.food_circulation_permit = food_circulation_permit;
    }

    public String getFood_circulation_permit_required() {
        return food_circulation_permit_required;
    }

    public void setFood_circulation_permit_required(String food_circulation_permit_required) {
        this.food_circulation_permit_required = food_circulation_permit_required;
    }

    public String getFood_circulation_permit_scanning() {
        return food_circulation_permit_scanning;
    }

    public void setFood_circulation_permit_scanning(String food_circulation_permit_scanning) {
        this.food_circulation_permit_scanning = food_circulation_permit_scanning;
    }

    public String getFront_img() {
        return front_img;
    }

    public void setFront_img(String front_img) {
        this.front_img = front_img;
    }

    public String getLast_time() {
        return last_time;
    }

    public void setLast_time(String last_time) {
        this.last_time = last_time;
    }

    public String getPerson_name() {
        return person_name;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public String getFacade_photo() {
        return facade_photo;
    }

    public void setFacade_photo(String facade_photo) {
        this.facade_photo = facade_photo;
    }
}
