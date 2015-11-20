package com.tooploox.redditnews.api.client;

/**
 * Created by Piotr Trocki on 18/08/15.
 */
public class RedditApiFactory {

    private static RedditApi api;

    public static void setLoginSession(boolean isLoggedIn) {
        if (isLoggedIn) {
            api = LoggedInRedditApi.newInstance();
        } else {
            api = AnonymousRedditApi.newInstance();
        }
    }

    private RedditApiFactory() {
    }

    public static RedditApi getApi() {
        return api;
    }
}