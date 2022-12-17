package com.ujs.salarymanagementsystem.data;

import com.ujs.salarymanagementsystem.data.model.ChartItem;
import com.ujs.salarymanagementsystem.data.model.Department;
import com.ujs.salarymanagementsystem.data.model.Enterprise;
import com.ujs.salarymanagementsystem.data.model.Staff;
import com.ujs.salarymanagementsystem.data.model.Wages;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Constant {
    public static Enterprise enterprise = null;    // 当前正在浏览的企业信息
    public static Department department = null;    // 当前正在浏览的部门信息
    public static Staff staff = null;              // 当前正在浏览的员工信息
    public static List<Map<String, String>> screenshotList = null;   // 当前需要截屏的数据
    public static List<Enterprise> enterpriseList = new ArrayList<>();   // 企业列表
    public static List<ChartItem> enterpriseChartList = new ArrayList<>();  // 企业图表列表
    public static List<Department> departmentList = new ArrayList<>();      // 部门列表
    public static List<Staff> staffList = new ArrayList<>();                // 员工列表
    public static List<Wages> wagesList = new ArrayList<>();                // 工资列表
}
