package com.tooploox.redditnews.api.event;

import com.tooploox.redditnews.api.model.User;

/**
 * Created by Piotr Trocki on 06/08/15.
 */
public class FetchUserInfoEvent {

    private final User user;

    public FetchUserInfoEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
