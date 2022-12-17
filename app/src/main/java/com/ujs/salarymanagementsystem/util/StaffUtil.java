package com.ujs.salarymanagementsystem.util;

import static com.ujs.salarymanagementsystem.data.Constant.departmentList;
import static com.ujs.salarymanagementsystem.data.Constant.enterpriseChartList;
import static com.ujs.salarymanagementsystem.data.Constant.enterpriseList;
import static com.ujs.salarymanagementsystem.data.Constant.staffList;
import static com.ujs.salarymanagementsystem.data.Constant.wagesList;

import android.content.Context;
import android.text.InputType;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.ujs.salarymanagementsystem.base.HomePageController;
import com.ujs.salarymanagementsystem.base.HomeStatController;
import com.ujs.salarymanagementsystem.base.StaffDialogBuilder;
import com.ujs.salarymanagementsystem.base.UserDialogBuilder;
import com.ujs.salarymanagementsystem.data.Constant;
import com.ujs.salarymanagementsystem.data.LoginRepository;
import com.ujs.salarymanagementsystem.data.model.ChartItem;
import com.ujs.salarymanagementsystem.data.model.Enterprise;
import com.ujs.salarymanagementsystem.data.model.Staff;
import com.ujs.salarymanagementsystem.data.model.Wages;
import com.ujs.salarymanagementsystem.db.MySQLHelper;
import com.ujs.salarymanagementsystem.db.MySQLoperation;
import com.ujs.salarymanagementsystem.ui.fragment.DepartmentFragment;
import com.ujs.salarymanagementsystem.ui.fragment.StaffFragment;
import com.ujs.salarymanagementsystem.ui.fragment.WagesFragment;

import java.sql.SQLException;
import java.util.List;

public class StaffUtil {
    public void addStaff(Context context){
        StaffDialogBuilder staffDialogBuilder = new StaffDialogBuilder(context, null);
        staffDialogBuilder.setTitle("增加职工")
                .addAction("取消", (dialog12, index) -> dialog12.dismiss())
                .addAction("确定", (dialog12, index) -> {
                    dialog12.dismiss();
                    String name = staffDialogBuilder.getnameText().getText().toString();
                    String repo = staffDialogBuilder.getrepoText().getText().toString();
                    Staff s = new Staff(-1, Constant.enterprise.getId_E(), name, repo);
                    try {
                        MySQLoperation.addTable_S(Constant.department, s);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(context, "添加职工成功！", Toast.LENGTH_SHORT).show();
                    SelectStaff();
                    StaffFragment.adapter.notifyDataSetChanged();
                })
                .show();
    }

    // 根据职工ID删除职工
    public static void DeleteStaffFromIDS(int position){
        Staff s = staffList.get(position);
        try {
            // 先删除该职工的所有工资信息
            MySQLoperation.deleteTableW_2(s);
            // 然后删除该职工
            MySQLoperation.deleteTabelS_2(s);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        SelectStaff();
        StaffFragment.adapter.notifyDataSetChanged();
    }

    public void AlterStaff(Context context, int position){
        Staff s = Constant.staffList.get(position);
        StaffDialogBuilder staffDialogBuilder = new StaffDialogBuilder(context, s);
        staffDialogBuilder.setTitle("修改职工信息")
                .addAction("取消", (dialog12, index) -> dialog12.dismiss())
                .addAction("确定", (dialog12, index) -> {
                    dialog12.dismiss();
                    String name = staffDialogBuilder.getnameText().getText().toString();
                    String repo = staffDialogBuilder.getrepoText().getText().toString();
                    s.setSname(name);
                    s.setSposition(repo);
                    try {
                        MySQLoperation.updateTable_S_1(s);
                        MySQLoperation.updateTable_S_2(s);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    SelectStaff();
                    Toast.makeText(context, "修改职工信息成功！", Toast.LENGTH_SHORT).show();
                    StaffFragment.adapter.notifyDataSetChanged();
                })
                .show();
    }

    public static void SelectStaff(){
        if(null == Constant.department) return;
        Constant.staffList.clear();
        try {
            List<Staff> list = MySQLoperation.selectSbyD(Constant.department);
            for(Staff s : list){
                Constant.staffList.add(s);
            }
            StaffFragment.adapter.notifyDataSetChanged();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
