package com.ujs.salarymanagementsystem.data.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.library.baseAdapters.BR;

import com.ujs.salarymanagementsystem.adapter.WagesAdapter;

public class Wages extends BaseObservable {
    private int id_W;
    private int id_E;
    private int id_S;
    private String data;
    private double amount_p = 0;
    private double amount_d = 0;
    private double amount_s = 0;

    public Wages(int id_W, int id_E, int id_S, String data, double amount_p, double amount_d) {
        this.id_W = id_W;            // 工资ID（主）
        this.id_E = id_E;            // 企业ID（外）
        this.id_S = id_S;            // 职工ID（外）
        this.data = data;            // 日期
        this.amount_p = amount_p;    // 应发金额
        this.amount_d = amount_d;    // 应扣金额
        this.amount_s = this.amount_p - this.amount_d;
    }

    @Override
    public String toString() {
        return "wages{" +
                "id_W=" + id_W +
                ", id_E=" + id_E +
                ", id_S=" + id_S +
                ", data='" + data + '\'' +
                ", amount_p=" + amount_p +
                ", amount_d=" + amount_d +
                '}';
    }

    public int getId_W() {
        return id_W;
    }

    public void setId_W(int id_W) {
        this.id_W = id_W;
    }

    public int getId_E() {
        return id_E;
    }

    public void setId_E(int id_E) {
        this.id_E = id_E;
    }

    public int getId_S() {
        return id_S;
    }

    public void setId_S(int id_S) {
        this.id_S = id_S;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
        notifyPropertyChanged(BR._all);
    }

    public double getAmount_p() {
        return amount_p;
    }

    public void setAmount_p(double amount_p) {
        this.amount_p = amount_p;
        this.setAmount_s();
        notifyPropertyChanged(BR._all);
    }

    public double getAmount_d() {
        return amount_d;
    }

    public void setAmount_d(double amount_d) {
        this.amount_d = amount_d;
        this.setAmount_s();
        notifyPropertyChanged(BR._all);
    }

    public double getAmount_s() {
        return amount_s;
    }

    public void setAmount_s() {
        this.amount_s = this.amount_p - this.amount_d;
        notifyPropertyChanged(BR._all);
    }

    // 转换文本
    public String getAmount_P() {
        return String.format("%.2f", amount_p);
    }

    public String getAmount_D() {
        return String.format("%.2f", amount_d);
    }

    public String getAmount_S() { return String.format("%.2f", amount_s); }
}
