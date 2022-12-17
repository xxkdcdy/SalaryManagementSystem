package com.ujs.salarymanagementsystem.data.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.library.baseAdapters.BR;

public class Staff extends BaseObservable {
    private int id_S;           // 职工ID（主）
    private int id_D;           // 部门ID（外）
    private String Sname;       // 职工名称
    private String Sposition;   // 职位

    public Staff(int id_S, int id_D, String sname, String sposition) {
        this.id_S = id_S;
        this.id_D = id_D;
        Sname = sname;
        Sposition = sposition;
    }

    @Override
    public String toString() {
        return "Staff{" +
                "id_S=" + id_S +
                ", id_D=" + id_D +
                ", Sname='" + Sname + '\'' +
                ", Sposition='" + Sposition + '\'' +
                '}';
    }

    public int getId_S() {
        return id_S;
    }

    public void setId_S(int id_S) {
        this.id_S = id_S;
    }

    public int getId_D() {
        return id_D;
    }

    public void setId_D(int id_D) {
        this.id_D = id_D;
    }

    public String getSname() {
        return Sname;
    }

    public void setSname(String sname) {
        Sname = sname;
        notifyPropertyChanged(BR._all);
    }

    public String getSposition() {
        return Sposition;
    }

    public void setSposition(String sposition) {
        Sposition = sposition;
        notifyPropertyChanged(BR._all);
    }
}
