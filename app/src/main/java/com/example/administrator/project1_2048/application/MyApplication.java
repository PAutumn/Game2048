package com.example.administrator.project1_2048.application;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2015/8/6 0006.
 */
public class MyApplication extends Application {

    public SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        //创建一个sharePreference
        sharedPreferences = getSharedPreferences("userSetting",MODE_PRIVATE);

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
