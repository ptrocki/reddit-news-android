package com.tooploox.redditnews.api.client;

import com.tooploox.redditnews.api.model.News;
import com.tooploox.redditnews.listener.AuthListener;

import net.dean.jraw.paginators.Sorting;
import net.dean.jraw.paginators.SubredditPaginator;

import java.util.Collection;
import java.util.List;

/**
 * Created by Piotr Trocki on 18/08/15.
 */
public interface RedditApi {
    void authorize();

    void userChallengeTask(final String url);

    void authorize(AuthListener authListener);

    void fetchUserInfo();

    List<String> getSubscribeList();

    void createPaginator();

    void createPaginator(final String subreddit);

    void createPaginator(final String subreddit, final Sorting sorting);

    void getMoreNews();

    List<String> searchSubreddit(final String subreddit);

    Collection<News> fetchPage(SubredditPaginator subredditPaginator) ;

    void fetchSubmissionComments(final String submissionId);

    String getCurrentSubreddit();

    String getFrontPage();

    boolean isAuthorized();
}