package com.tooploox.redditnews.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tooploox.redditnews.R;
import com.tooploox.redditnews.adapter.SubscribeListAdapter;
import com.tooploox.redditnews.api.client.RedditApiFactory;
import com.tooploox.redditnews.listener.OnSubsListClickListener;
import com.tooploox.redditnews.listener.OnSubscribeClickListener;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SubscribeListFragment extends Fragment implements OnSubscribeClickListener {

    public static final String TAG = SubscribeListFragment.class.getSimpleName();

    private SubscribeListAdapter subscribeListAdapter;

    private OnSubsListClickListener onSubsListClickListener;

    @Bind(R.id.rv_subscribe_list)
    RecyclerView rvSubscribeList;

    public static SubscribeListFragment newInstance() {
        return new SubscribeListFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        onSubsListClickListener = (OnSubsListClickListener) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subscribeListAdapter = new SubscribeListAdapter(this, getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscribe_list, container, false);
        ButterKnife.bind(this, view);
        buildRecyclerView();
        return view;
    }

    @Override
    public void onDetach() {
        onSubsListClickListener = null;
        super.onDetach();
    }

    @Override
    public void onSubscribedClick(String subreddit) {
        onSubsListClickListener.onSubscribed(subreddit);
    }

    private void buildRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvSubscribeList.setLayoutManager(layoutManager);
        rvSubscribeList.setAdapter(subscribeListAdapter);
        subscribeListAdapter.setSubscribeList(RedditApiFactory.getApi().getSubscribeList());
    }
}