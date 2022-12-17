package com.ujs.salarymanagementsystem.data;

import androidx.databinding.BaseObservable;
import androidx.databinding.library.baseAdapters.BR;

import com.ujs.salarymanagementsystem.data.model.LoggedInUser;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
// 登录仓库，用来保存登录的用户
public class LoginRepository extends BaseObservable {

    private static volatile LoginRepository instance;    // 仓库单例，保证全局只有唯一登录用户
    private LoginDataSource dataSource = new LoginDataSource();                  // 保存登录数据源
    private LoggedInUser user = null;                    // 保存登录用户

    // private constructor : singleton access   单例，构造函数私有
    private LoginRepository() {
    }

    // 返回用户仓库的单例
    public static LoginRepository getInstance() {
        if (instance == null) {
            instance = new LoginRepository();
        }
        return instance;
    }

    // 判断是否登录
    public boolean isLoggedIn() {
        return user != null;
    }

    // 登出
    public void logout() {
        user = null;
        dataSource.logout();
        notifyPropertyChanged(BR._all);
    }

    // 保存登录用户
    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        notifyPropertyChanged(BR._all);
    }

    // 获取登录用户
    public LoggedInUser getUser(){
        if(!isLoggedIn()) return null;
        else return user;
    }

    public String getUserName(){
        if(isLoggedIn()) return user.getDisplayName();
        else return "未登录";
    }

    public Result<LoggedInUser> login(String username, String password) {
        // handle login
        Result<LoggedInUser> result = dataSource.login(username, password);
        if (result instanceof Result.Success) {
            setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
        }
        return result;
    }
}