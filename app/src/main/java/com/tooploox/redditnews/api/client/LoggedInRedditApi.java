package com.tooploox.redditnews.api.client;

import com.tooploox.redditnews.ConnectionMonitor;
import com.tooploox.redditnews.api.event.FetchUserInfoEvent;
import com.tooploox.redditnews.api.model.User;
import com.tooploox.redditnews.listener.AuthListener;

import net.dean.jraw.http.oauth.Credentials;
import net.dean.jraw.http.oauth.OAuthData;
import net.dean.jraw.http.oauth.OAuthException;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Subreddit;
import net.dean.jraw.paginators.UserSubredditsPaginator;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Piotr Trocki on 18/08/15.
 */
public class LoggedInRedditApi extends BaseRedditApi implements RedditApi {

    private static final String WHERE_PARAM = "subscriber";

    private static final String[] scopes = {"identity", "read", "mysubreddits"};

    private User user;

    private boolean authFinished = false;

    private Credentials credentials;

    protected LoggedInRedditApi() {
        super();
    }

    public static RedditApi newInstance() {
        return new LoggedInRedditApi();
    }

    @Override
    public void authorize() {
    }

    @Override
    public void userChallengeTask(final String url) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                if (ConnectionMonitor.isConnected()) {
                    try {
                        OAuthData oAuthData;
                        oAuthData = helper.onUserChallenge(url, credentials);
                        redditClient.authenticate(oAuthData);
                        authFinished = true;
                        createPaginator();
                        fetchUserInfo();
                    } catch (OAuthException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void authorize(final AuthListener authListener) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                if (ConnectionMonitor.isConnected()) {
                    URL authorizationUrl;
                    credentials = Credentials.installedApp(CLIENT_ID, REDIRECT_URL);
                    helper = redditClient.getOAuthHelper();
                    authorizationUrl = helper.getAuthorizationUrl(credentials, PERMANENT, scopes);
                    authListener.getAuthData(authorizationUrl);
                }
            }
        });
    }

    @Override
    public void fetchUserInfo() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                if (ConnectionMonitor.isConnected()) {
                    UserSubredditsPaginator paginator = new UserSubredditsPaginator(redditClient, WHERE_PARAM);
                    Listing<Subreddit> subreddits = paginator.next(true);
                    List<String> subscribedList = new ArrayList<>();
                    for (Subreddit subreddit : subreddits) {
                        if (subreddit.isUserSubscriber()) {
                            subscribedList.add(subreddit.getDisplayName());
                        }
                    }
                    final String currentUser = redditClient.me().getFullName();
                    user = new User(redditClient.getUser(currentUser), currentUser, subscribedList);
                    EventBus.getDefault().post(new FetchUserInfoEvent(user));
                }
            }
        });
    }

    @Override
    public List<String> getSubscribeList() {
        if (ConnectionMonitor.isConnected()) {
            return user.getSubscribeList();
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public boolean isAuthorized() {
        return authFinished;
    }
}