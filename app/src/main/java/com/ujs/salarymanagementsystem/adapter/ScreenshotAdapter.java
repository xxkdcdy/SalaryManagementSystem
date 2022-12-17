package com.ujs.salarymanagementsystem.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ujs.salarymanagementsystem.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScreenshotAdapter extends RecyclerView.Adapter<ScreenshotAdapter.ViewHolder> {
    private List<Map<String, String>> list = new ArrayList<>();

    public ScreenshotAdapter(List<Map<String, String>> list) {
        super();
        Map<String, String> title = new HashMap<>();
        title.put("name", "员工姓名");
        title.put("department", "所属部门");
        title.put("date", "发薪日期");
        title.put("p", "应发金额");
        title.put("d", "应扣金额");
        title.put("s", "实发金额");
        this.list.add(title);
        this.list.addAll(list);
    }

    @Override
    public ScreenshotAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_screenshot, parent, false);
        ScreenshotAdapter.ViewHolder viewHolder = new ScreenshotAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ScreenshotAdapter.ViewHolder holder, int position) {
        holder.nameText.setText(list.get(position).get("name"));
        holder.departmentText.setText(list.get(position).get("department"));
        holder.dateText.setText(list.get(position).get("date"));
        holder.pText.setText(list.get(position).get("p"));
        holder.dText.setText(list.get(position).get("d"));
        holder.sText.setText(list.get(position).get("s"));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, departmentText, dateText, pText, dText, sText;
        ViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.screenshot_name);
            departmentText = itemView.findViewById(R.id.screenshot_department);
            dateText = itemView.findViewById(R.id.screenshot_date);
            pText = itemView.findViewById(R.id.screenshot_p);
            dText = itemView.findViewById(R.id.screenshot_d);
            sText = itemView.findViewById(R.id.screenshot_s);
        }
    }
}
