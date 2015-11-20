package com.tooploox.redditnews.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.tooploox.redditnews.R;
import com.tooploox.redditnews.fragment.DetailsFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailActivity extends BaseActivity {

    public static final String URL_SUBMISSION = "url_submission";
    public static final String ID_SUBMISSION = "id_submission";
    public static final String TITLE_SUBMISSION = "title_submission";

    private String title;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    public static Intent prepareIntent(Context context, String submissionId, String url, String title) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(ID_SUBMISSION, submissionId);
        intent.putExtra(URL_SUBMISSION, url);
        intent.putExtra(TITLE_SUBMISSION, title);
        return intent;
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
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setToolbar();
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fl_container,
                            DetailsFragment.newInstance(
                                    getIntent().getStringExtra(URL_SUBMISSION),
                                    getIntent().getStringExtra(ID_SUBMISSION),
                                    title),
                            DetailsFragment.TAG)
                    .commit();
        }
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        title = getIntent().getStringExtra(TITLE_SUBMISSION);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
        }
    }
}
