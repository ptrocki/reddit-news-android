package com.tooploox.redditnews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.tooploox.redditnews.R;
import com.tooploox.redditnews.adapter.SubredditSearchAdapter;
import com.tooploox.redditnews.api.client.RedditApiFactory;
import com.tooploox.redditnews.api.event.FetchUserInfoEvent;
import com.tooploox.redditnews.fragment.PageFragment;
import com.tooploox.redditnews.listener.ChangeToolbarVisibilityListener;

import net.dean.jraw.paginators.Sorting;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class MainActivity extends BaseActivity implements ChangeToolbarVisibilityListener {

    public static final int SUBREDDIT_REQUEST_CODE = 0;

    private static final int DEFAULT_HIGHLIGHTED_MENU_ITEM = R.id.drawer_hot;

    private final EventBus eventBus = EventBus.getDefault();

    private MenuItem defaultHighlightedMenuItem;

    private FragmentManager fragmentManager;

    private ActionBar actionBar;

    private String subredditTilte;

    private boolean changedSubredditTitle = false;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.dl_main)
    DrawerLayout drawerLayout;

    @Bind(R.id.navigation_view)
    NavigationView navigationView;

    @Bind(R.id.tv_username)
    TextView tvUsername;

    @Bind(R.id.actv_search_subreddit)
    AutoCompleteTextView actvSearchSubreddit;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.search_menu:
                toggleAutoCompleteTextViewVisibility();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showToolbar() {
        actionBar.show();
    }

    @Override
    public void hideToolbar() {
        actionBar.hide();
    }

    @Override
    public void onStart() {
        super.onStart();
        eventBus.register(this);
    }

    @Override
    public void onStop() {
        eventBus.unregister(this);
        super.onStop();
    }

    public void onEventMainThread(FetchUserInfoEvent event) {
        tvUsername.setText(event.getUser().getUsername());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setToolbar();
        setNavigationView();
        if (savedInstanceState == null) {
            fragmentManager = getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(R.id.fl_container, PageFragment.newInstance(RedditApiFactory.getApi().getFrontPage()), PageFragment.TAG)
                    .commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SUBREDDIT_REQUEST_CODE && resultCode == RESULT_OK) {
            subredditTilte = data.getStringExtra(SubscribedListActivity.SUBREDDIT_TITLE_KEY);
            changedSubredditTitle = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //I want call it when I choose subreddit form subscribed list
        if (changedSubredditTitle) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fl_container,
                            PageFragment.newInstance(subredditTilte),
                            PageFragment.TAG)
                    .commit();
            defaultHighlightedMenuItem.setChecked(true);
            toolbar.setTitle(getString(R.string.toolbar_subreddit_title, subredditTilte));
            setToolbar();
            changedSubredditTitle = false;
        }
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        buildAutoCompleteTextView();
    }

    private void setNavigationView() {
        //AS can't find this method navigationView.setCheckedItem(int id). I dunno why.
        defaultHighlightedMenuItem = navigationView.getMenu().findItem(DEFAULT_HIGHLIGHTED_MENU_ITEM);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.drawer_hot:
                        setNewNewsPage(Sorting.HOT);
                        break;
                    case R.id.drawer_new:
                        setNewNewsPage(Sorting.NEW);
                        break;
                    case R.id.drawer_rising:
                        setNewNewsPage(Sorting.RISING);
                        break;
                    case R.id.drawer_controversial:
                        setNewNewsPage(Sorting.CONTROVERSIAL);
                        break;
                    case R.id.drawer_top:
                        setNewNewsPage(Sorting.TOP);
                        break;
                    case R.id.drawer_subscribe_list:
                        startActivityForResult(SubscribedListActivity.newIntent(MainActivity.this), SUBREDDIT_REQUEST_CODE);
                        break;
                }
                drawerLayout.closeDrawer(Gravity.LEFT);
                menuItem.setChecked(true);
                return true;
            }

            private void setNewNewsPage(Sorting sorting) {
                PageFragment fragment = (PageFragment) fragmentManager.findFragmentByTag(PageFragment.TAG);
                fragment.setNewPage(sorting);
            }
        });
    }

    private void buildAutoCompleteTextView() {
        final SubredditSearchAdapter adapter = new SubredditSearchAdapter(this,
                R.layout.item_subreddit_search);
        actvSearchSubreddit.setAdapter(adapter);
        actvSearchSubreddit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PageFragment fragment = PageFragment.newInstance(actvSearchSubreddit.getText().toString());
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.fl_container, fragment, PageFragment.TAG)
                        .commit();
                toolbar.setTitle(getString(R.string.toolbar_subreddit_title, actvSearchSubreddit.getText()));
                actvSearchSubreddit.setText("");
                actvSearchSubreddit.setVisibility(View.GONE);
                hideKeyboard();
                defaultHighlightedMenuItem.setChecked(true);
            }
        });
        actvSearchSubreddit.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.getFilter().filter(s.toString());
            }
        });
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void toggleAutoCompleteTextViewVisibility() {
        if (actvSearchSubreddit.getVisibility() == View.VISIBLE) {
            actvSearchSubreddit.setVisibility(View.GONE);
        } else {
            actvSearchSubreddit.setVisibility(View.VISIBLE);
        }
    }
}