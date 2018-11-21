package com.example.mdjahirulislam.doobbi.controller.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConnectivityReceiver extends BroadcastReceiver{
    private static final String TAG = ConnectivityReceiver.class.getSimpleName();


    public static ConnectivityReceiverListener connectivityReceiverListener;
    public Context context;

    public ConnectivityReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent arg1) {
        this.context = context;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        Log.d( TAG, "onReceive: "+isConnected );

        if (connectivityReceiverListener != null) {
            Log.d( TAG, "onReceive: connectivityReceiverListener" );
            connectivityReceiverListener.onNetworkConnectionChanged( isConnected );
        }
    }

    public static boolean isConnected(Context context) {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) MyApplication.getInstance().getApplicationContext().getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();


        if(connectivityManager != null)
        {
            //Check Mobile data or Wifi net is present
            if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED)
            {
                //we are connected to a network
                connected = true;
                Log.d( TAG, "isConnected: "+connected );
                return activeNetwork != null && activeNetwork.isConnectedOrConnecting();

            }
            else
            {
                connected = false;
                Log.d( TAG, "isConnected: "+connected );

            }
            return connected;
        }
        else
        {
            Log.d( TAG, "isConnected: "+connected );

            return connected;
        }


    }


    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}