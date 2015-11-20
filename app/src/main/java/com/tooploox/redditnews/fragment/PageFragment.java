package com.tooploox.redditnews.fragment;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.tooploox.redditnews.ConnectionMonitor;
import com.tooploox.redditnews.R;
import com.tooploox.redditnews.activity.DetailActivity;
import com.tooploox.redditnews.adapter.NewsListAdapter;
import com.tooploox.redditnews.api.client.RedditApiFactory;
import com.tooploox.redditnews.api.event.FailAuthEvent;
import com.tooploox.redditnews.api.event.FetchPageEvent;
import com.tooploox.redditnews.api.event.GetMoreNewsEvent;
import com.tooploox.redditnews.listener.ChangeToolbarVisibilityListener;
import com.tooploox.redditnews.ui.DividerItemDecoration;

import net.dean.jraw.paginators.Sorting;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class PageFragment extends Fragment implements NewsListAdapter.NewsListListener {

    public static final String TAG = PageFragment.class.getSimpleName();

    private static final String SUBREDDIT_NAME = "subreddit_category";
    private static final int SCROLL_AMOUNT = 20;

    private final EventBus eventBus = EventBus.getDefault();

    private NewsListAdapter newsListAdapter;

    private ChangeToolbarVisibilityListener changeToolbarVisibilityListener;

    @Bind(R.id.rv_front_page)
    RecyclerView rvFrontPage;

    @Bind(R.id.srl_page)
    SwipeRefreshLayout srlPage;

    public static PageFragment newInstance(String subreddit) {
        Bundle bundle = new Bundle();
        PageFragment pageFragment = new PageFragment();
        bundle.putString(SUBREDDIT_NAME, subreddit);
        pageFragment.setArguments(bundle);
        return pageFragment;
    }

    public static PageFragment newInstance() {
        return PageFragment.newInstance(RedditApiFactory.getApi().getFrontPage());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        changeToolbarVisibilityListener = (ChangeToolbarVisibilityListener) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newsListAdapter = new NewsListAdapter(this, getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_front_page, container, false);
        ButterKnife.bind(this, view);
        srlPage.setEnabled(false);
        // when I press back I don't want to fetch again data with default sorting
        if (newsListAdapter.getItemCount() == 0) {
            srlPage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        srlPage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        srlPage.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                    showProgress();
                }
            });
            if (RedditApiFactory.getApi().isAuthorized()) {
                RedditApiFactory.getApi().createPaginator(getArguments().getString(SUBREDDIT_NAME));
            }
        }
        buildRecyclerView();
        return view;
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

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();

    }

    @Override
    public void onDetach() {
        changeToolbarVisibilityListener = null;
        super.onDetach();
    }

    public void onEventMainThread(final FetchPageEvent event) {
        newsListAdapter.setListNews(event.getNewsList());
        hideProgress();
    }

    public void onEventMainThread(GetMoreNewsEvent event) {
        newsListAdapter.addListNews(event.getNewsList());
        hideProgress();
    }

    public void onEventMainThread(FailAuthEvent event) {
        Toast.makeText(getActivity(), getResources().getText(R.string.auth_fail), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getMoreItems() {
        RedditApiFactory.getApi().getMoreNews();
        showProgress();
    }

    @Override
    public void onNewsClick(String url, String submissionId, String title) {
        startActivity(DetailActivity.prepareIntent(getActivity(), submissionId, url, title));
    }

    public void setNewPage(Sorting sorting) {
        showProgress();
        RedditApiFactory.getApi().createPaginator(RedditApiFactory.getApi().getCurrentSubreddit(), sorting);
    }

    private void buildRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvFrontPage.setLayoutManager(layoutManager);
        rvFrontPage.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.divider));
        rvFrontPage.setAdapter(newsListAdapter);
        /*rvFrontPage.setOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean hideToolBar = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (hideToolBar) {
                    changeToolbarVisibilityListener.hideToolbar();
                } else {
                    changeToolbarVisibilityListener.showToolbar();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //TODO use AppBarLayout and CoordinatorLayout instead of this.
                if (dy > SCROLL_AMOUNT) {
                    hideToolBar = true;
                } else if (dy < -SCROLL_AMOUNT) {
                    hideToolBar = false;
                }
            }
        });*/

    }

    private void showProgress() {
        if(ConnectionMonitor.isConnected()){
            srlPage.setRefreshing(true);
        }
    }

    private void hideProgress() {
        srlPage.setRefreshing(false);
    }
}