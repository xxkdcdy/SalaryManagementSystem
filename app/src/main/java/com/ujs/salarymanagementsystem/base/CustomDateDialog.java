package com.ujs.salarymanagementsystem.base;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ujs.salarymanagementsystem.App;
import com.ujs.salarymanagementsystem.R;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Yingyong Lao
 * 创建时间 2021/6/14 16:28
 * @version 1.0
 */
public class CustomDateDialog implements View.OnClickListener, DatePicker.OnDateChangedListener {
    private Dialog dialog;
    private TextView titleTv;//标题
    private DatePicker datePicker;//日期选择控件
    private TextView confirmTv;//底部的“确认”
    private View dialogView;
    private OnDateSetListener onDateSetListener;

    /**
     * 构造器
     * @param context 上下文
     */
    public CustomDateDialog(Context context){
        dialogView = LayoutInflater.from(context).inflate(R.layout.date_dialog, null);
        dialog=new Dialog(context,R.style.CustomDateDialog);
        titleTv=dialogView.findViewById(R.id.titleTv);
        datePicker=dialogView.findViewById(R.id.datePicker);
        ViewGroup viewGroup1= (ViewGroup) datePicker.getChildAt(0);
        ViewGroup viewGroup2= (ViewGroup) viewGroup1.getChildAt(0);//获取年月日的下拉列表项
        if(viewGroup2.getChildCount()==3){//有的机型没有竖线，只有年、月、日
            viewGroup2.getChildAt(2).setVisibility(View.GONE);
        }else if(viewGroup2.getChildCount()==5){//有的机型有有竖线
            viewGroup2.getChildAt(3).setVisibility(View.GONE);
            viewGroup2.getChildAt(4).setVisibility(View.GONE);
        }
        confirmTv=dialogView.findViewById(R.id.confirmTv);
        confirmTv.setOnClickListener(this);
    }

    /**
     * 显示对话框
     */
    public void show(){
        Window window = dialog.getWindow();
        window.setContentView(dialogView);//设置对话框窗口的内容视图(这里有个坑，参数不要传R.layout.date_dialog，否则会出现各种问题，比如按钮响应不了点击事件)
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);//设置对话框窗口的布局参数
        dialog.show();
        dialog.setCancelable(false);
    }

    /**
     * 返回规定格式的时间字串
     */
    public String getDate(){
        String year = String.valueOf(datePicker.getYear());
        String month = String.valueOf(datePicker.getMonth() + 1);
        while(year.length() < 4) year = "0" + year;
        while(month.length() < 2) month = "0" + month;
        String date = year + "-" + month;
        return date;
    }

    /**
     * 关闭对话框
     */
    public void dismiss(){
        if(dialog!=null&&dialog.isShowing()){
            dialog.dismiss();
        }
    }

    /**
     * 设置标题
     * @param title 标题
     */
    public void setTitle(String title){
        titleTv.setText(title);
    }

    public void setDate(int year,int month,OnDateSetListener onDateSetListener){
        Calendar calendar = Calendar.getInstance();
        datePicker.init(year,month,calendar.get(Calendar.DAY_OF_MONTH),this);
        this.onDateSetListener=onDateSetListener;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.confirmTv){
            dialog.dismiss();
            if(onDateSetListener!=null){
                datePicker.clearFocus();
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                onDateSetListener.onDateSet(year,month+1);
            }
        }
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        Date date = new Date();//现在的日期
        cal.setTime(date);
        Integer _year = cal.get(Calendar.YEAR);//获取年
        Integer _month = cal.get(Calendar.MONTH);//获取月（月份从0开始，如果按照中国的习惯，需要加一）
        if((year > _year) || (year == _year && monthOfYear > _month)){
            Toast.makeText(App.getContext(), "不能超过当前日期！", Toast.LENGTH_SHORT).show();
            year = _year;
            monthOfYear = _month;
        }
        datePicker.init(year,monthOfYear,dayOfMonth,this);
    }

    public interface OnDateSetListener{
        void onDateSet(int year,int month);
    }
}