package com.tooploox.redditnews.api.event;

import com.tooploox.redditnews.api.model.News;

import java.util.Collection;

/**
 * Created by Piotr Trocki on 31/07/15.
 */
public class GetMoreNewsEvent {

    private final Collection<News> newsList;

    public GetMoreNewsEvent(Collection<News> newsList) {
        this.newsList = newsList;
    }

    public Collection<News> getNewsList() {
        return newsList;
    }
}