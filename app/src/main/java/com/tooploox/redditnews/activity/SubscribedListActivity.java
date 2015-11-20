package com.tooploox.redditnews.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.tooploox.redditnews.R;
import com.tooploox.redditnews.fragment.SubscribeListFragment;
import com.tooploox.redditnews.listener.OnSubsListClickListener;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SubscribedListActivity extends BaseActivity implements OnSubsListClickListener {

    public static final String SUBREDDIT_TITLE_KEY = "subreddit_title";

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    public static Intent newIntent(Context context) {
        return new Intent(context, SubscribedListActivity.class);
    }

    @Override
    public void onSubscribed(String subreddit) {
        Intent data = new Intent();
        data.putExtra(SUBREDDIT_TITLE_KEY, subreddit);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribed_list);
        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fl_container, SubscribeListFragment.newInstance(), SubscribeListFragment.TAG)
                    .commit();
        }
        setToolbar();
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.title_activity_subscribed_list));
        }
    }
}
