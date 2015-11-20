package com.tooploox.redditnews.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tooploox.redditnews.ConnectionMonitor;
import com.tooploox.redditnews.R;
import com.tooploox.redditnews.api.client.RedditApiFactory;
import com.tooploox.redditnews.listener.LoginListener;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends Fragment {

    public static final String TAG = LoginFragment.class.getSimpleName();

    LoginListener loginListener;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @OnClick(R.id.btn_login)
    public void onLoginClick() {
        if (ConnectionMonitor.isConnected()) {
            loginListener.loginAsRedditUser();
        }
    }

    @OnClick(R.id.btn_userless)
    public void onLoginlessClick() {
        if (ConnectionMonitor.isConnected()) {
            RedditApiFactory.setLoginSession(false);
            RedditApiFactory.getApi().authorize();
            loginListener.onUserLoggedIn();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        loginListener = (LoginListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDetach() {
        loginListener = null;
        super.onDetach();
    }
}