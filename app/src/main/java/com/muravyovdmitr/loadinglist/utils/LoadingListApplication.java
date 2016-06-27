package com.muravyovdmitr.loadinglist.utils;

import android.app.Application;

/**
 * Created by Dima Muravyov on 27.06.2016.
 */
public class LoadingListApplication extends Application{
    private static LoadingListApplication loadingListApplication;

    @Override
    public void onCreate() {
        super.onCreate();

        loadingListApplication = this;
    }

    public static LoadingListApplication getInstance(){
        return loadingListApplication;
    }
}
