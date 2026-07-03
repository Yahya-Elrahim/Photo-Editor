package com.johnapps.smartphotoeditor;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

import com.google.firebase.messaging.FirebaseMessaging;
import com.onesignal.OneSignal;


public class Polish extends Application {
    private static Polish queShot;

    public void onCreate() {
        super.onCreate();
        queShot = this;
        FirebaseMessaging.getInstance().subscribeToTopic("all");

    }

    public static Context getContext() {
        return queShot.getContext();
    }

}
