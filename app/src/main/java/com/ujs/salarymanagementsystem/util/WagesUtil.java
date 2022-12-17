package com.ujs.salarymanagementsystem.util;

import static com.ujs.salarymanagementsystem.data.Constant.enterpriseList;
import static com.ujs.salarymanagementsystem.data.Constant.wagesList;

import android.content.Context;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.ujs.salarymanagementsystem.App;
import com.ujs.salarymanagementsystem.base.HomePageController;
import com.ujs.salarymanagementsystem.base.HomeStatController;
import com.ujs.salarymanagementsystem.base.StaffDialogBuilder;
import com.ujs.salarymanagementsystem.base.WagesDialogBuilder;
import com.ujs.salarymanagementsystem.data.Constant;
import com.ujs.salarymanagementsystem.data.model.Enterprise;
import com.ujs.salarymanagementsystem.data.model.Staff;
import com.ujs.salarymanagementsystem.data.model.Wages;
import com.ujs.salarymanagementsystem.db.MySQLoperation;
import com.ujs.salarymanagementsystem.ui.fragment.StaffFragment;
import com.ujs.salarymanagementsystem.ui.fragment.WagesFragment;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WagesUtil {
    public void addWages(Context context){
        WagesDialogBuilder wagesDialogBuilder = new WagesDialogBuilder(context, null);
        wagesDialogBuilder.setTitle("增加工资信息")
                .addAction("取消", (dialog12, index) -> dialog12.dismiss())
                .addAction("确定", (dialog12, index) -> {
                    dialog12.dismiss();
                    String date = wagesDialogBuilder.getDateText().getText().toString();
                    String p = wagesDialogBuilder.getPText().getText().toString();
                    String d = wagesDialogBuilder.getDText().getText().toString();
                    if(null == p || null == d || p.length() == 0 || d.length() == 0){
                        Toast.makeText(App.getContext(), "请输入正确的工资信息", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Wages w = new Wages(-1, Constant.enterprise.getId_E(),
                            Constant.staff.getId_S(), date, Double.parseDouble(p), Double.parseDouble(d));
                    if(!CheckWages(w)) return;
                    try {
                        MySQLoperation.addTableW(Constant.enterprise, Constant.staff, w);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(context, "添加工资信息成功！", Toast.LENGTH_SHORT).show();
                    SelectWages();
                    WagesFragment.adapter.notifyDataSetChanged();
                })
                .show();
    }

    // 根据工资ID删除工资信息
    public static void deleteWagesFromIDW(int position){
        Wages w = wagesList.get(position);
        try {
            MySQLoperation.deleteTableW_1(w);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        SelectWages();
        WagesFragment.adapter.notifyDataSetChanged();
    }

    public void AlterWages(Context context, int position){
        Wages w = Constant.wagesList.get(position);
        WagesDialogBuilder wagesDialogBuilder = new WagesDialogBuilder(context, w);
        wagesDialogBuilder.setTitle("修改工资信息")
                .addAction("取消", (dialog12, index) -> dialog12.dismiss())
                .addAction("确定", (dialog12, index) -> {
                    dialog12.dismiss();
                    String date = wagesDialogBuilder.getDateText().getText().toString();
                    String p = wagesDialogBuilder.getPText().getText().toString();
                    String d = wagesDialogBuilder.getDText().getText().toString();
                    if(null == p || null == d || p.length() == 0 || d.length() == 0){
                        Toast.makeText(App.getContext(), "请输入正确的工资信息", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(!CheckWages(new Wages(-1, -1, -1, "",
                            Double.parseDouble(p), Double.parseDouble(d)))) return;
                    w.setData(date);
                    w.setAmount_p(Double.parseDouble(p));
                    w.setAmount_d(Double.parseDouble(d));
                    w.setAmount_s();
                    try {
                        MySQLoperation.updateTableW_1(w);
                        MySQLoperation.updateTableW_2(w);
                        MySQLoperation.updateTableW_3(w);
                        MySQLoperation.updateTableW_4(w);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(context, "修改工资信息成功！", Toast.LENGTH_SHORT).show();
                    SelectWages();
                    StaffFragment.adapter.notifyDataSetChanged();
                })
                .show();
    }

    public static void SelectWages(){
        if(null == Constant.staff) return;
        Constant.wagesList.clear();
        try {
            List<Wages> list = MySQLoperation.selectWbyS(Constant.staff);
            for(Wages w : list){
                Constant.wagesList.add(w);
            }
            WagesFragment.adapter.notifyDataSetChanged();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean CheckWages(Wages wages){
        if(wages.getAmount_d() > wages.getAmount_p() * 0.2){
            Toast.makeText(App.getContext(), "依据我国相关法律的规定，用人单位依法对劳动者工资进行扣除的，" +
                    "不得超过当月资的20%，扣除后低于最低工资标准的，按最低工资标准支付。", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static String getNowDateStr(){
        Calendar cal = Calendar.getInstance();
        Date date = new Date();//现在的日期
        cal.setTime(date);
        Integer _year = cal.get(Calendar.YEAR);//获取年
        Integer _month = cal.get(Calendar.MONTH) + 1;//获取月（月份从0开始，如果按照中国的习惯，需要加一）
        String str = _year + "-";
        if(_month < 10) str += "0";
        return str + _month;
    }
}
