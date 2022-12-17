package com.ujs.salarymanagementsystem.util;

import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.ujs.salarymanagementsystem.App;
import com.ujs.salarymanagementsystem.R;
import com.ujs.salarymanagementsystem.data.Constant;
import com.ujs.salarymanagementsystem.data.model.Department;
import com.ujs.salarymanagementsystem.data.model.Enterprise;
import com.ujs.salarymanagementsystem.data.model.Staff;
import com.ujs.salarymanagementsystem.db.MySQLoperation;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;

public class ChartUtil {

    public static void initPieChart(PieChart pieChart, List<PieEntry> strings){

        PieDataSet dataSet = new PieDataSet(strings,"部门");

        ArrayList<Integer> colors = new ArrayList<>();
        for(int i = 0; i < strings.size(); i++){
            switch (i % 3){
                case 0:
                    colors.add(App.getContext().getResources().getColor(R.color.qmui_config_color_red));
                    break;
                case 1:
                    colors.add(App.getContext().getResources().getColor(R.color.app_color_green));
                    break;
                case 2:
                    colors.add(App.getContext().getResources().getColor(R.color.blue));
                    break;
            }
        }
        dataSet.setColors(colors);

        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(true);

        // 右下角的Description Label去掉
        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);

        // 把字体变大些
        pieData.setValueTextSize(12f);
        // 数字变成整数
        pieData.setValueFormatter(new LargeValueFormatter());

        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    public static void initBarChart(BarChart chart, List<Entry> values){
        chart.getDescription().setEnabled(false); // 不显示描述
        chart.setExtraOffsets(20,20,20,20); // 设置饼图的偏移量，类似于内边距 ，设置视图窗口大小
        chart.setDragEnabled(false);// 是否可以拖拽
        chart.setScaleEnabled(false);//是否可放大
        chart.setDrawGridBackground(false);//是否绘制网格线
        chart.setBackgroundColor(Color.WHITE);

        /**设置坐标轴**/
        // 设置x轴
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);  // 设置x轴显示在下方，默认在上方
        xAxis.setDrawGridLines(false); // 将此设置为true，绘制该轴的网格线。
        xAxis.setLabelCount(values.size());  // 设置x轴上的标签个数
        xAxis.setTextSize(15f); // x轴上标签的大小
        final String labelName[] = new String[values.size()];
        for(int i = 0; i < values.size(); i++){
            labelName[i] = String.valueOf((int) values.get(i).getX());
        }
        // 设置x轴显示的值的格式
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if ((int) value < labelName.length) {
                    return labelName[(int) value];
                } else {
                    return "";
                }
            }
        });
        xAxis.setYOffset(15); // 设置标签对x轴的偏移量，垂直方向
        //chart.animateX(1400,Easing.EasingOption.EaseInSine);// X轴动画

        // 设置y轴，y轴有两条，分别为左和右
        YAxis yAxis_right = chart.getAxisRight();
        yAxis_right.setAxisMaximum(55000f);  // 设置y轴的最大值
        yAxis_right.setAxisMinimum(0f);  // 设置y轴的最小值
        yAxis_right.setEnabled(false);  // 不显示右边的y轴

        YAxis yAxis_left = chart.getAxisLeft();
        yAxis_left.setAxisMaximum(55000f);
        yAxis_left.setAxisMinimum(0f);
        yAxis_left.setTextSize(15f); // 设置y轴的标签大小

        /**设置图例**/
        Legend legend = chart.getLegend();
        legend.setFormSize(12f); // 图例的图形大小
        legend.setTextSize(15f); // 图例的文字大小
        legend.setDrawInside(true); // 设置图例在图中
        legend.setOrientation(Legend.LegendOrientation.VERTICAL); // 图例的方向为垂直
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT); //显示位置，水平右对齐
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP); // 显示位置，垂直上对齐
        // 设置水平与垂直方向的偏移量
        legend.setYOffset(10f);
        legend.setXOffset(10f);
        /**设置数据**/
        List<IBarDataSet> sets = new ArrayList<>();
        // 此处有两个DataSet，所以有两条柱子，BarEntry（）中的x和y分别表示显示的位置和高度
        // x是横坐标，表示位置，y是纵坐标，表示高度
        List<BarEntry> barEntries = new ArrayList<>();
        for(int i = 0; i < values.size(); i++){
            barEntries.add(new BarEntry(i, values.get(i).getY()));
        }
        BarDataSet barDataSet = new BarDataSet(barEntries, "");
        barDataSet.setValueTextColor(Color.RED); // 值的颜色
        barDataSet.setValueTextSize(15f); // 值的大小
        barDataSet.setColor(Color.parseColor("#1AE61A")); // 柱子的颜色
        barDataSet.setLabel("工资"); // 设置标签之后，图例的内容默认会以设置的标签显示
        // 设置柱子上数据显示的格式
        barDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                // 此处的value默认保存一位小数
                return value + "元";
            }
        });

        sets.add(barDataSet);

        BarData barData = new BarData(sets);
        barData.setBarWidth(0.4f); // 设置柱子的宽度
        chart.setData(barData);
    }

    // 生成测试数据
    public static List<Entry> generateData(){
        PriorityQueue<Integer> heap = new PriorityQueue<>();
        Set<Integer> set = new HashSet<>();
        // 1. 每个数据是一个 Entry
        ArrayList<Entry> values = new ArrayList<>();
        Map<Integer, Float> map = new TreeMap<>();
        for(Map<String, String> mp : Constant.screenshotList){
            Integer date = Integer.parseInt(mp.get("date").replace("-", ""));
            float s = Float.parseFloat(mp.get("s"));
            if(set.contains(date)) continue;  // 出现重复月份，跳过
            // 小顶堆，当堆的size为5，同时需要新增元素时，将弹出堆顶元素
            if(heap.size() == 5){
                map.remove(heap.peek());   // 删除队头
                heap.poll();
            }
            set.add(date);
            heap.add(date);
            map.put(date, 0f);
        }

        // 每个月的工资数据累加
        for(Map<String, String> mp : Constant.screenshotList){
            Integer date = Integer.parseInt(mp.get("date").replace("-", ""));
            float s = Float.parseFloat(mp.get("s"));
            if(heap.contains(date)){
                map.put(date, map.get(date) + s);
            }
        }

        // 保存成规定格式待用
        Iterator<Map.Entry<Integer, Float>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Float> entry = iterator.next();
            values.add(new Entry(entry.getKey(), entry.getValue()));
        }
        return values;
    }

    public static List<PieEntry> generateEnterpriseData(Enterprise e){
        // 1. 每个数据是一个 Entry
        ArrayList<PieEntry> values = new ArrayList<>();

        try {
            // 获取企业的部门列表
            List<Department> list = MySQLoperation.selectDbyE(e);
            for(Department d : list){
                // 获取部门人数
                List<Staff> staffList = MySQLoperation.selectSbyD(d);
                values.add(new PieEntry(staffList.size(), d.getDname()));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return values;
    }
}
