package com.ujs.salarymanagementsystem.data.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.library.baseAdapters.BR;

public class Enterprise extends BaseObservable {
    int id_E;         // 企业ID
    String name_E;    // 企业名称

    public Enterprise(int id_E, String name_E) {
        this.id_E = id_E;
        this.name_E = name_E;
    }

    public int getId_E() {
        return id_E;
    }

    public void setId_E(int id_E) {
        this.id_E = id_E;
    }

    public String getName_E() {
        return name_E;
    }

    public void setName_E(String name_E) {
        this.name_E = name_E;
        notifyPropertyChanged(BR._all);
    }
}
