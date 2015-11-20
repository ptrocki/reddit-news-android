package com.tooploox.redditnews.api.client;

import net.dean.jraw.RedditClient;
import net.dean.jraw.http.UserAgent;

/**
 * Created by Piotr Trocki on 01/09/15.
 */
public class ClientCreator {

    private static class InstanceHolder {
        private static final String APPLICATION_NAME = "Reddit News";

        private static final RedditClient INSTANCE = new RedditClient(UserAgent.of(APPLICATION_NAME));
    }

    public static RedditClient getClient() {
        return InstanceHolder.INSTANCE;
    }

    private ClientCreator() {
    }
}
