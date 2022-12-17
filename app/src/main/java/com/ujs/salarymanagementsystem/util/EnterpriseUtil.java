package com.ujs.salarymanagementsystem.util;

import static com.ujs.salarymanagementsystem.data.Constant.departmentList;
import static com.ujs.salarymanagementsystem.data.Constant.enterpriseChartList;
import static com.ujs.salarymanagementsystem.data.Constant.enterpriseList;

import android.content.Context;
import android.text.InputType;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.ujs.salarymanagementsystem.App;
import com.ujs.salarymanagementsystem.base.BaseFragment;
import com.ujs.salarymanagementsystem.base.HomePageController;
import com.ujs.salarymanagementsystem.base.HomeStatController;
import com.ujs.salarymanagementsystem.data.Constant;
import com.ujs.salarymanagementsystem.data.LoginRepository;
import com.ujs.salarymanagementsystem.data.model.ChartItem;
import com.ujs.salarymanagementsystem.data.model.Department;
import com.ujs.salarymanagementsystem.data.model.Enterprise;
import com.ujs.salarymanagementsystem.data.model.LoggedInUser;
import com.ujs.salarymanagementsystem.db.MySQLHelper;
import com.ujs.salarymanagementsystem.db.MySQLoperation;

import java.sql.SQLException;
import java.util.List;

public class EnterpriseUtil {
    public void AddEnterprise(Context context){
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(context);
        builder.setTitle("添加企业")
                .setPlaceholder("在此输入企业名称")
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("取消", (dialog, index) -> dialog.dismiss())
                .addAction("确定", (dialog, index) -> {
                    String text = builder.getEditText().getText().toString();
                    // 创建企业信息
                    Enterprise e = new Enterprise(1, text);
                    enterpriseList.add(e);
                    try {
                        MySQLoperation.addTableE(e, LoginRepository.getInstance().getUser());
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    // 生成企业的图表数据
                    SelectEnterprise(LoginRepository.getInstance().getUser(), true);
                    HomeStatController.adapter.notifyDataSetChanged();
                    HomePageController.adapter.notifyDataSetChanged();
                    Toast.makeText(context, "添加企业成功！", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                })
                .show();
    }

    // 根据企业ID删除企业
    public static void DeleteEnterpriseFromIDE(int position){
        Enterprise e = enterpriseList.get(position);
        try {
            // 先删除该企业的所有工资信息
            MySQLoperation.deleteTableW_3(e);
            // 最后删除该公司（级联删除职工、部门）
            MySQLoperation.deleteTableE(e);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        SelectEnterprise(LoginRepository.getInstance().getUser(), true);
        HomePageController.adapter.notifyDataSetChanged();
    }

    public void AlterEnterprise(Context context, int position){
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(context);
        builder.setTitle("企业更名")
                .setPlaceholder("在此输入企业名称")
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("取消", (dialog, index) -> dialog.dismiss())
                .addAction("确定", (dialog, index) -> {
                    String text = builder.getEditText().getText().toString();
                    // 更改企业名称
                    enterpriseList.get(position).setName_E(text);
                    try {
                        MySQLoperation.updateTableE(enterpriseList.get(position));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    SelectEnterprise(LoginRepository.getInstance().getUser(), true);
                    // 生成企业的图标数据
                    enterpriseChartList.get(position).setTitle(text);
                    HomeStatController.adapter.notifyDataSetChanged();
                    HomePageController.adapter.notifyDataSetChanged();
                    Toast.makeText(context, "企业更名成功！", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                })
                .show();
    }

    public static void SelectEnterprise(LoggedInUser loggedInUser, boolean need_refresh){
        List<Enterprise> list = null;
        enterpriseList.clear();
        enterpriseChartList.clear();
        try {
            list = MySQLoperation.selectEbyU(loggedInUser);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for(Enterprise e : list){
            if(null == e) continue;
            // 生成对应公司的表格数据
            Constant.enterpriseList.add(e);
            Constant.enterpriseChartList.add(new ChartItem(e.getName_E(), ChartUtil.generateEnterpriseData(e)));
        }
        if(need_refresh) {
            HomePageController.adapter.notifyDataSetChanged();
            HomeStatController.adapter.notifyDataSetChanged();
        }
    }
}
