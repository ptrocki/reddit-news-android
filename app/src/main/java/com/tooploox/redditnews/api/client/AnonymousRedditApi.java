package com.tooploox.redditnews.api.client;

import com.tooploox.redditnews.ConnectionMonitor;
import com.tooploox.redditnews.api.event.AuthEvent;
import com.tooploox.redditnews.api.event.FailAuthEvent;
import com.tooploox.redditnews.listener.AuthListener;

import net.dean.jraw.http.NetworkException;
import net.dean.jraw.http.oauth.Credentials;
import net.dean.jraw.http.oauth.OAuthData;
import net.dean.jraw.http.oauth.OAuthException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.greenrobot.event.EventBus;

/**
 * Created by Piotr Trocki on 18/08/15.
 */
public class AnonymousRedditApi extends BaseRedditApi implements RedditApi {

    private boolean authFinished = false;

    public static RedditApi newInstance() {
        return new AnonymousRedditApi();
    }

    protected AnonymousRedditApi() {
        super();
    }

    @Override
    public void authorize() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                if (ConnectionMonitor.isConnected()) {
                    try {
                        Credentials credentials = Credentials.userlessApp(CLIENT_ID, UUID.randomUUID());
                        OAuthData authData = redditClient.getOAuthHelper().easyAuth(credentials);
                        redditClient.authenticate(authData);
                        EventBus.getDefault().post(new AuthEvent());
                        authFinished = true;
                        createPaginator();
                    } catch (OAuthException e) {
                        e.printStackTrace();
                        EventBus.getDefault().post(new FailAuthEvent());
                    } catch (NetworkException e) {
                        EventBus.getDefault().post(new FailAuthEvent());
                    }
                }
            }
        });
    }

    @Override
    public void userChallengeTask(String url) {
    }

    @Override
    public void authorize(AuthListener authListener) {
    }

    @Override
    public void fetchUserInfo() {
    }

    @Override
    public List<String> getSubscribeList() {
        // TODO implement list from share prefs
        return new ArrayList<>();
    }

    @Override
    public boolean isAuthorized() {
        return authFinished;
    }
}