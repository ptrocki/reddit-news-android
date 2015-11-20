package com.tooploox.redditnews.api.client;

import com.tooploox.redditnews.ConnectionMonitor;
import com.tooploox.redditnews.api.event.FetchCommentsEvent;
import com.tooploox.redditnews.api.event.FetchPageEvent;
import com.tooploox.redditnews.api.event.GetMoreNewsEvent;
import com.tooploox.redditnews.api.model.CommentVertex;
import com.tooploox.redditnews.api.model.News;

import net.dean.jraw.RedditClient;
import net.dean.jraw.http.oauth.OAuthHelper;
import net.dean.jraw.models.CommentNode;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.paginators.Sorting;
import net.dean.jraw.paginators.SubredditPaginator;
import net.dean.jraw.paginators.TimePeriod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.greenrobot.event.EventBus;

/**
 * Created by Piotr Trocki on 18/08/15.
 */
public class BaseRedditApi {
    protected static final int PAGE_LIMIT = 50;
    protected static final String CLIENT_ID = "9P8aaBQ3tl8jHw";
    protected static final String REDIRECT_URL = "https://com.tooploox.redditnews";
    protected static final String FRONT_PAGE = "front";
    protected static final TimePeriod DEFAULT_TIME_PERIOD = TimePeriod.MONTH;
    protected static final Sorting DEFAULT_SORTING = Sorting.HOT;
    protected static final boolean PERMANENT = true;

    protected RedditClient redditClient;
    protected final ExecutorService executorService;
    protected SubredditPaginator subredditPaginator;
    protected OAuthHelper helper;
    protected String currentSubreddit;

    protected BaseRedditApi() {
        executorService = Executors.newSingleThreadExecutor();
        redditClient = ClientCreator.getClient();
    }

    public void createPaginator() {
        createPaginator(FRONT_PAGE);
    }

    public void createPaginator(final String subreddit) {
        createPaginator(subreddit, DEFAULT_SORTING);
    }

    public void createPaginator(final String subreddit, final Sorting sorting) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                if (ConnectionMonitor.isConnected()) {
                    currentSubreddit = subreddit;
                    if (subreddit.equals(FRONT_PAGE)) {
                        subredditPaginator = new SubredditPaginator(redditClient);
                    } else {
                        subredditPaginator = new SubredditPaginator(redditClient, subreddit);
                    }
                    subredditPaginator.setLimit(PAGE_LIMIT);
                    subredditPaginator.setTimePeriod(DEFAULT_TIME_PERIOD);
                    subredditPaginator.setSorting(sorting);
                    EventBus.getDefault().post(new FetchPageEvent(fetchPage(subredditPaginator)));
                }
            }
        });
    }

    public void getMoreNews() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                if (ConnectionMonitor.isConnected()) {
                    EventBus.getDefault().post(new GetMoreNewsEvent(fetchPage(subredditPaginator)));
                }
            }
        });
    }

    public List<String> searchSubreddit(final String subreddit) {
        if (ConnectionMonitor.isConnected()) {
            return redditClient.searchSubreddits(subreddit, true);
        } else {
            return new ArrayList<>();
        }
    }

    public Collection<News> fetchPage(SubredditPaginator subredditPaginator) {
        if (ConnectionMonitor.isConnected()) {
            Listing<Submission> submissions = subredditPaginator.next();
            Collection<News> newsList = new ArrayList<>(submissions.size());
            for (Submission submission : submissions) {
                News news = new News(submission);
                newsList.add(news);
            }
            return newsList;
        } else {
            return new ArrayList<>();
        }
    }

    public void fetchSubmissionComments(final String submissionId) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                if (ConnectionMonitor.isConnected()) {
                    Submission submission = redditClient.getSubmission(submissionId);
                    CommentNode rootNode = submission.getComments();
                    Iterable<CommentNode> commentTree = rootNode.walkTree();
                    Collection<CommentVertex> commentVertexList = new ArrayList<>();
                    for (CommentNode node : commentTree) {
                        CommentVertex commentVertex = new CommentVertex(node);
                        commentVertexList.add(commentVertex);
                    }
                    EventBus.getDefault().post(new FetchCommentsEvent(commentVertexList));
                }
            }
        });
    }

    public String getCurrentSubreddit() {
        return currentSubreddit;
    }

    public String getFrontPage() {
        return FRONT_PAGE;
    }
}