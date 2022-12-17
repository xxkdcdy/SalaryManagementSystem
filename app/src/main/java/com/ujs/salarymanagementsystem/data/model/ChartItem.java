package com.ujs.salarymanagementsystem.data.model;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;

import java.util.List;

public class ChartItem {
    private String title;
    private List<PieEntry> data;

    public ChartItem(String title, List<PieEntry> data) {
        this.title = title;
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<PieEntry> getData() {
        return data;
    }

    public void setData(List<PieEntry> data) {
        this.data = data;
    }
}
