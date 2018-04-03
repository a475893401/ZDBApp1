package com.zhuandanbao.app.entity;

import java.io.Serializable;

/**
 * Created by BFTECH on 2017/3/3.
 */

public class FinanceEntity implements Serializable {

    private String One_Withdraw_fee;
    private String account_balance;
    private String assure_credit;
    private String audit_state;
    private String available_balance;
    private String balance;
    private String bank_card_num;
    private String credit_money;
    private String frozen_fund;
    private String is_audit;
    private int is_set_paypass;
    private String month_Withdraw_num;
    private int month_free_Withdraw_max_num;
    private String trading_fund;
    private String today_withdraw_amount;
    private int today_withdraw_num;

    public FinanceEntity (){

    }

    public String getToday_withdraw_amount() {
        return today_withdraw_amount;
    }

    public void setToday_withdraw_amount(String today_withdraw_amount) {
        this.today_withdraw_amount = today_withdraw_amount;
    }

    public int getToday_withdraw_num() {
        return today_withdraw_num;
    }

    public void setToday_withdraw_num(int today_withdraw_num) {
        this.today_withdraw_num = today_withdraw_num;
    }

    public String getOne_Withdraw_fee() {
        return One_Withdraw_fee;
    }

    public void setOne_Withdraw_fee(String One_Withdraw_fee) {
        this.One_Withdraw_fee = One_Withdraw_fee;
    }

    public String getAccount_balance() {
        return account_balance;
    }

    public void setAccount_balance(String account_balance) {
        this.account_balance = account_balance;
    }

    public String getAssure_credit() {
        return assure_credit;
    }

    public void setAssure_credit(String assure_credit) {
        this.assure_credit = assure_credit;
    }

    public String getAudit_state() {
        return audit_state;
    }

    public void setAudit_state(String audit_state) {
        this.audit_state = audit_state;
    }

    public String getAvailable_balance() {
        return available_balance;
    }

    public void setAvailable_balance(String available_balance) {
        this.available_balance = available_balance;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getBank_card_num() {
        return bank_card_num;
    }

    public void setBank_card_num(String bank_card_num) {
        this.bank_card_num = bank_card_num;
    }

    public String getCredit_money() {
        return credit_money;
    }

    public void setCredit_money(String credit_money) {
        this.credit_money = credit_money;
    }

    public String getFrozen_fund() {
        return frozen_fund;
    }

    public void setFrozen_fund(String frozen_fund) {
        this.frozen_fund = frozen_fund;
    }

    public String getIs_audit() {
        return is_audit;
    }

    public void setIs_audit(String is_audit) {
        this.is_audit = is_audit;
    }

    public int getIs_set_paypass() {
        return is_set_paypass;
    }

    public void setIs_set_paypass(int is_set_paypass) {
        this.is_set_paypass = is_set_paypass;
    }

    public String getMonth_Withdraw_num() {
        return month_Withdraw_num;
    }

    public void setMonth_Withdraw_num(String month_Withdraw_num) {
        this.month_Withdraw_num = month_Withdraw_num;
    }

    public int getMonth_free_Withdraw_max_num() {
        return month_free_Withdraw_max_num;
    }

    public void setMonth_free_Withdraw_max_num(int month_free_Withdraw_max_num) {
        this.month_free_Withdraw_max_num = month_free_Withdraw_max_num;
    }

    public String getTrading_fund() {
        return trading_fund;
    }

    public void setTrading_fund(String trading_fund) {
        this.trading_fund = trading_fund;
    }
}
