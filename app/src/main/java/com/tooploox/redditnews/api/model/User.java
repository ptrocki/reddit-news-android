package com.tooploox.redditnews.api.model;

import net.dean.jraw.models.Account;

import java.util.List;

/**
 * Created by Piotr Trocki on 06/08/15.
 */
public class User {

    private Integer commentKarma;
    private Integer linkKarma;
    private String username;
    private List<String> subscribeList;

    public User() {
    }

    public User(Account account, String username, List<String> subscribeList) {
        this.commentKarma = account.getCommentKarma();
        this.linkKarma = account.getLinkKarma();
        this.username = username;
        this.subscribeList = subscribeList;
    }

    public Integer getCommentKarma() {
        return commentKarma;
    }

    public Integer getLinkKarma() {
        return linkKarma;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getSubscribeList() {
        return subscribeList;
    }
}
