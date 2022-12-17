package com.ujs.salarymanagementsystem.db;


import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.service.controls.ControlsProviderService.TAG;

public class MySQLHelper {
    private static final String DBname="ESMS";
    public static Connection getConnection() {
        Connection conn = null;
        try {
            //连接数据库的操作在子线程中执行
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("com.mysql.jdbc.Driver").newInstance(); //加载驱动
            String ip = "rm-uf6tho9jy157akqj50o.mysql.rds.aliyuncs.com";
            conn =(Connection) DriverManager.getConnection(
                    "jdbc:mysql://" + ip + ":3306/" + MySQLHelper.DBname,
                    "xxkdcdy", "5352948#Cdy");
        } catch (SQLException ex) {//错误捕捉
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return conn;//返回Connection型变量conn用于后续连接
    }

    public static Connection Close(Connection conn) throws SQLException {
        conn.close();
        return null;
    }
}
