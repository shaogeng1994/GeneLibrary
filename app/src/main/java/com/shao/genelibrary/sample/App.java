package com.shao.genelibrary.sample;

import android.app.Application;

/**
 * Created by Administrator on 2018/3/17.
 */

public class App extends Application {

    private static App mInstance;

    public static App getInstance() {
        return mInstance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
}
