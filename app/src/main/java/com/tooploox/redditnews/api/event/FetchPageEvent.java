package com.tooploox.redditnews.api.event;

import com.tooploox.redditnews.api.model.News;

import java.util.Collection;

/**
 * Created by Piotr Trocki on 28/07/15.
 */
public class FetchPageEvent {

    private final Collection<News> newsList;

    public FetchPageEvent(Collection<News> newsList) {
        this.newsList = newsList;
    }

    public Collection<News> getNewsList() {
        return newsList;
    }
}
