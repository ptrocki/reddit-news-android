package com.tooploox.redditnews.api.event;

import com.tooploox.redditnews.api.model.CommentVertex;

import java.util.Collection;

/**
 * Created by Piotr Trocki on 17/08/15.
 */
public class FetchCommentsEvent {
    Collection<CommentVertex> commentList;

    public FetchCommentsEvent(Collection<CommentVertex> commentList) {
        this.commentList = commentList;
    }

    public Collection<CommentVertex> getCommentList() {
        return commentList;
    }
}