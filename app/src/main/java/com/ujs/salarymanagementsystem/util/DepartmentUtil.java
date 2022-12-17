package com.ujs.salarymanagementsystem.util;

import static com.ujs.salarymanagementsystem.data.Constant.departmentList;
import static com.ujs.salarymanagementsystem.data.Constant.enterpriseChartList;
import static com.ujs.salarymanagementsystem.data.Constant.enterpriseList;
import static com.ujs.salarymanagementsystem.data.Constant.staffList;

import android.content.Context;
import android.text.InputType;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.ujs.salarymanagementsystem.base.HomePageController;
import com.ujs.salarymanagementsystem.base.HomeStatController;
import com.ujs.salarymanagementsystem.data.Constant;
import com.ujs.salarymanagementsystem.data.LoginRepository;
import com.ujs.salarymanagementsystem.data.model.ChartItem;
import com.ujs.salarymanagementsystem.data.model.Department;
import com.ujs.salarymanagementsystem.data.model.Enterprise;
import com.ujs.salarymanagementsystem.data.model.Staff;
import com.ujs.salarymanagementsystem.db.MySQLoperation;
import com.ujs.salarymanagementsystem.ui.fragment.DepartmentFragment;
import com.ujs.salarymanagementsystem.ui.fragment.WagesFragment;

import java.sql.SQLException;
import java.util.List;

public class DepartmentUtil {
    public void AddEnterprise(Context context){
        if(null == Constant.enterprise) return;
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(context);
        builder.setTitle("添加部门")
                .setPlaceholder("在此输入部门名称")
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("取消", (dialog, index) -> dialog.dismiss())
                .addAction("确定", (dialog, index) -> {
                    String text = builder.getEditText().getText().toString();
                    // 创建部门信息
                    Department d = new Department(1, Constant.enterprise.getId_E(), text);
                    try {
                        MySQLoperation.addTableD(Constant.enterprise, d);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    SelectDepartment();
                    DepartmentFragment.adapter.notifyDataSetChanged();
                    Toast.makeText(context, "添加部门成功！", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                })
                .show();
    }

    public static void DeleteDepartmentFromIDD(int position){
        Department d = departmentList.get(position);
        try {
            // 先删除该部门的所有工资信息
            MySQLoperation.deleteTableW_4(d);
            // 然后删除该部门所有职工
            MySQLoperation.deleteTableS_1(d);
            // 最后删除该部门
            MySQLoperation.deleteTableD(d);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        SelectDepartment();
        DepartmentFragment.adapter.notifyDataSetChanged();
    }

    public void AlterDepartment(Context context, int position){
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(context);
        builder.setTitle("部门更名")
                .setPlaceholder("在此输入部门名称")
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("取消", (dialog, index) -> dialog.dismiss())
                .addAction("确定", (dialog, index) -> {
                    String text = builder.getEditText().getText().toString();
                    // 更改企业名称
                    departmentList.get(position).setDname(text);
                    try {
                        MySQLoperation.updateTableD(departmentList.get(position));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    SelectDepartment();
                    DepartmentFragment.adapter.notifyDataSetChanged();
                    Toast.makeText(context, "部门更名成功！", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                })
                .show();
    }

    public static void SelectDepartment(){
        try {
            if(null == Constant.enterpriseList) return;
            Constant.departmentList.clear();
            List<Department> list = MySQLoperation.selectDbyE(Constant.enterprise);
            for(Department d : list){
                Constant.departmentList.add(d);
            }
            DepartmentFragment.adapter.notifyDataSetChanged();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
