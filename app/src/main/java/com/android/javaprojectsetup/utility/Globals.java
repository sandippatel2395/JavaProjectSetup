package com.android.javaprojectsetup.utility;

import android.app.Application;
import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;

import com.android.javaprojectsetup.sharedpreference.SharedPreference;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class Globals extends Application {

    private static Context context; // private static Globals context;
    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext(); // context = this ;
        SharedPreference.getInstance().setEditor(getApplicationContext());
    }

    public static Context getContext() {
        return context;// context.getApplicationContext()
    }
}