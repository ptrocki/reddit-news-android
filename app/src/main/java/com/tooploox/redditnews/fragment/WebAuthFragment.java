package com.tooploox.redditnews.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tooploox.redditnews.R;
import com.tooploox.redditnews.api.client.RedditApiFactory;
import com.tooploox.redditnews.listener.AuthListener;
import com.tooploox.redditnews.listener.LoginListener;

import java.net.URL;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WebAuthFragment extends Fragment implements AuthListener{

    public static final String TAG = WebAuthFragment.class.getSimpleName();

    LoginListener loginListener;

    @Bind(R.id.wv_user_login)
    WebView wvUserLogin;

    public static WebAuthFragment newInstance() {
        return new WebAuthFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        loginListener = (LoginListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web_auth, container, false);
        ButterKnife.bind(this, view);
        RedditApiFactory.setLoginSession(true);
        RedditApiFactory.getApi().authorize(this);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDetach() {
        loginListener = null;
        super.onDetach();
    }

    @Override
    public void getAuthData(final URL url) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                wvUserLogin.getSettings().setJavaScriptEnabled(true);
                wvUserLogin.loadUrl(url.toExternalForm());
                wvUserLogin.getSettings().setBuiltInZoomControls(true);
                wvUserLogin.setWebViewClient(new WebViewClient() {
                    private boolean opened = false;

                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        return false;
                    }

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        if (url.contains("code=") && !opened) {
                            opened = true;
                            RedditApiFactory.getApi().userChallengeTask(url);
                            loginListener.onUserLoggedIn();
                        }
                    }

                    @Override
                    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                        super.onReceivedError(view, errorCode, description, failingUrl);
                        //TODO Do better UI for it
                        wvUserLogin.loadUrl("file:///android_asset/no_connection.html");
                    }
                });
            }
        });
    }
}