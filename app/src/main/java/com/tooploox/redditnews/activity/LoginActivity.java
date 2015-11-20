package com.tooploox.redditnews.activity;

import android.content.Intent;
import android.os.Bundle;

import com.tooploox.redditnews.ConnectionMonitor;
import com.tooploox.redditnews.R;
import com.tooploox.redditnews.fragment.LoginFragment;
import com.tooploox.redditnews.fragment.WebAuthFragment;
import com.tooploox.redditnews.listener.LoginListener;

public class LoginActivity extends BaseActivity implements LoginListener {

    @Override
    public void loginAsRedditUser() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_login_container, WebAuthFragment.newInstance(), WebAuthFragment.TAG)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onUserLoggedIn() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        showConnectionDialog();
        if (savedInstanceState == null) {
            createWelcomeFragment();
        }
    }

    private void createWelcomeFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_login_container, LoginFragment.newInstance(), LoginFragment.TAG)
                .commit();
    }

    private void showConnectionDialog() {
        if (!ConnectionMonitor.isConnected()) {
            buildSnackBar();
        }
    }
}