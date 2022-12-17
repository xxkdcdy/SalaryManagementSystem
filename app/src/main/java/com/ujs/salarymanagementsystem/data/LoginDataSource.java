package com.ujs.salarymanagementsystem.data;

import android.widget.Toast;

import com.ujs.salarymanagementsystem.App;
import com.ujs.salarymanagementsystem.base.HomePageController;
import com.ujs.salarymanagementsystem.base.HomeStatController;
import com.ujs.salarymanagementsystem.data.model.ChartItem;
import com.ujs.salarymanagementsystem.data.model.Enterprise;
import com.ujs.salarymanagementsystem.data.model.LoggedInUser;
import com.ujs.salarymanagementsystem.db.MySQLoperation;
import com.ujs.salarymanagementsystem.util.ChartUtil;
import com.ujs.salarymanagementsystem.util.EnterpriseUtil;

import java.io.IOException;
import java.util.List;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    // 登录操作，在这里跟数据库交互
    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            LoggedInUser loggedInUser = MySQLoperation.selectUbyUup(username, password);
            if(null == loggedInUser){
                MySQLoperation.addTableU(username, password);
                loggedInUser = MySQLoperation.selectUbyUup(username, password);
            }
            return new Result.Success<>(loggedInUser);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}