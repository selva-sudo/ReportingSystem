package com.cadmin.myadmin;

import android.app.Application;
import android.graphics.Typeface;
import android.util.Log;

class MyApplication extends Application {

    private static MyApplication sThis;
    private static String TAG = MyApplication.class.getSimpleName();
    @Override
    public void onCreate() {
        super.onCreate();

        sThis = this;

    }

    public static MyApplication getThis() {
        return sThis;
    }
}
