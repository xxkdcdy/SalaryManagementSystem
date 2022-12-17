package com.ujs.salarymanagementsystem.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ujs.salarymanagementsystem.R;
import com.ujs.salarymanagementsystem.data.model.ChartItem;
import com.ujs.salarymanagementsystem.util.ChartUtil;

import java.util.List;

public class EnterpriseChartAdapter extends RecyclerView.Adapter<EnterpriseChartAdapter.ViewHolder> {
    private List<ChartItem> enterpriseChartList;

    public EnterpriseChartAdapter(List<ChartItem> enterpriseChartList) {
        this.enterpriseChartList = enterpriseChartList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.item_chart_enterprise, parent, false);
        final ViewHolder holder = new ViewHolder( view );
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChartItem item = enterpriseChartList.get(position);
//        ChartUtil.initLineChart(holder.itemView.findViewById(R.id.chart_enterprise),
//                item.getData());
        ChartUtil.initPieChart(holder.itemView.findViewById(R.id.chart_enterprise), item.getData());
        holder.nameText.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return enterpriseChartList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        public ViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.item_name_chart_enterprise);
        }

    }
}
