package com.ujs.salarymanagementsystem.util;

import android.app.Application;

import java.lang.reflect.Method;

public class AppGlobals {
    private static Application application;

    public static Application getApplication(){
        if(application == null){
            try{
                Method currentApplication = Class.forName("android.app.ActivityThread").
                        getDeclaredMethod("currentApplication");
                application = (Application) currentApplication.invoke(null, new Object[]{});
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return application;
    }
}
