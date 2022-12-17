package com.ujs.salarymanagementsystem.data.model;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.library.baseAdapters.BR;

public class Department extends BaseObservable {
    private int id_D;       // 部门ID
    private int id_E;       // 企业ID（外）
    private String Dname;   // 企业名称

    public Department(int id_D, int id_E, String dname) {
        this.id_D = id_D;
        this.id_E = id_E;
        Dname = dname;
    }

    @Override
    public String toString() {
        return "Department{" +
                "id_D=" + id_D +
                ", id_E=" + id_E +
                ", Dname='" + Dname + '\'' +
                '}';
    }

    public int getId_D() {
        return id_D;
    }

    public void setId_D(int id_D) {
        this.id_D = id_D;
    }

    public int getId_E() {
        return id_E;
    }

    public void setId_E(int id_E) {
        this.id_E = id_E;
    }

    public String getDname() {
        return Dname;
    }

    public void setDname(String dname) {
        Dname = dname;
        notifyPropertyChanged(BR._all);
    }
}
