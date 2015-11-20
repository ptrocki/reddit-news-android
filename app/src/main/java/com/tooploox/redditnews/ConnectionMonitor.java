package com.tooploox.redditnews;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.tooploox.redditnews.listener.ConnectionMonitorListener;

/**
 * Created by Piotr Trocki on 03/08/15.
 */
public class ConnectionMonitor extends BroadcastReceiver {

    private static boolean connected = false;

    private ConnectionMonitorListener connectionMonitorListener;

    public static boolean isConnected() {
        return connected;
    }

    public ConnectionMonitor(ConnectionMonitorListener connectionMonitorListener) {
        this.connectionMonitorListener = connectionMonitorListener;
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        connected = (networkInfo != null && networkInfo.isConnected());
        if (connected) {
            connectionMonitorListener.onConnect();
        } else {
            connectionMonitorListener.onDisconnect();
        }
    }
}
