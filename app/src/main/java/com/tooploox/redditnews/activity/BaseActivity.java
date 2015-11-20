package com.tooploox.redditnews.activity;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tooploox.redditnews.ConnectionMonitor;
import com.tooploox.redditnews.R;
import com.tooploox.redditnews.listener.ConnectionMonitorListener;

public class BaseActivity extends AppCompatActivity implements ConnectionMonitorListener {

    private static final IntentFilter connectionFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");

    private ConnectionMonitor connectionMonitor;

    private Snackbar snackbar;

    @Override
    public void onConnect() {
        if (snackbar != null) {
            snackbar.dismiss();
        }
    }

    @Override
    public void onDisconnect() {
        buildSnackBar();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionMonitor = new ConnectionMonitor(this);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(connectionMonitor);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(connectionMonitor, connectionFilter);
    }

    protected void buildSnackBar() {
        int resId;
        if(findViewById(R.id.fl_login_container) == null){
            resId = R.id.fl_container;
        } else {
            resId = R.id.fl_login_container;
        }
        snackbar = Snackbar.make(findViewById(resId), getString(R.string.connection_lost), Snackbar.LENGTH_INDEFINITE);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.error_color));
        snackbar.show();
    }
}
