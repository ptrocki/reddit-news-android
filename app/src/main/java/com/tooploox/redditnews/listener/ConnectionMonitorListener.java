package com.tooploox.redditnews.listener;

/**
 * Created by Piotr Trocki on 14/09/15.
 */
public interface ConnectionMonitorListener {

    void onConnect();

    void onDisconnect();
}