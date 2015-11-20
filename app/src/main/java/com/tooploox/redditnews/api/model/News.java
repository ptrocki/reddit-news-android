package com.tooploox.redditnews.api.model;

import net.dean.jraw.models.Submission;

/**
 * Created by Piotr Trocki on 27/07/15.
 */
public class News {

    private final String title;
    private final String url;
    private final String subreddit;
    private final String author;
    private final String thumbnailUrl;
    private final String submissionId;
    private final Integer commentCount;
    private final long newsTime;

    public News(Submission submission) {
        title = submission.getTitle();
        url = submission.getUrl();
        subreddit = submission.getSubredditName();
        author = submission.getAuthor();
        thumbnailUrl = submission.getThumbnail();
        submissionId = submission.getId();
        commentCount = submission.getCommentCount();
        newsTime = submission.getCreated().getTime();
    }

    @Override
    public String toString() {
        return subreddit + ": " + title;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getAuthor() {
        return author;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getSubmissionId() {
        return submissionId;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public long getNewsTime() {
        return newsTime;
    }
}
