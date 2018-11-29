package com.example.mdjahirulislam.doobbi.controller.helper;

import android.app.Application;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class MyApplication extends Application {

    private static final String TAG = MyApplication.class.getSimpleName();
    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("doobbiDB.realm")
                .schemaVersion(4)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        Log.d( TAG, "setConnectivityListener: " );
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}