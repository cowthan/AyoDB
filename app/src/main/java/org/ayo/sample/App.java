package org.ayo.sample;

import android.app.Application;

import org.ayo.Ayo;
import org.ayo.CrashHandler;

/**
 * Created by Administrator on 2016/8/29.
 */
public class App extends Application {

    public static Application app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;

        //初始化AyoSDK
        Ayo.init(this, "ayo", true, true);
        Ayo.debug = true;

        //初始化全局异常处理
        CrashHandler crashHandler = CrashHandler.getInstance();
        Thread.setDefaultUncaughtExceptionHandler(crashHandler);

        //TODO：初始化数据库

        //TODO：初始化Http库和下载相关

        //TODO: 初始化数据统计

        //TODO: 初始化推送

        //TODO：初始化地图

        //TODO：初始化IM相关

        //TODO：初始化视频相关


        //TODO: 其他初始化
    }
}