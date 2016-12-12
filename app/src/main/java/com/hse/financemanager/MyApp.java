package com.hse.financemanager;

import android.app.Application;
import android.content.Context;

import com.orm.SugarContext;

/**
 * Created by Никита on 19.10.2016.
 */

public class MyApp extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        SugarContext.init(context);
    }

    public static Context getContext() {
        return context;
    }
}
