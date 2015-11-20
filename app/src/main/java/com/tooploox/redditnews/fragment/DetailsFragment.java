package com.tooploox.redditnews.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.tooploox.redditnews.R;
import com.tooploox.redditnews.api.client.RedditApiFactory;
import com.tooploox.redditnews.api.event.FailFetchCommentEvent;
import com.tooploox.redditnews.api.event.FetchCommentsEvent;
import com.tooploox.redditnews.api.model.CommentVertex;

import java.util.ArrayList;
import java.util.Collection;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class DetailsFragment extends Fragment {

    public static final String TAG = DetailsFragment.class.getSimpleName();

    public static final String URL_SUBMISSION = "url_submission";
    public static final String ID_SUBMISSION = "id_submission";
    public static final String TITLE_SUBMISSION = "title_submission";

    private final EventBus eventBus = EventBus.getDefault();

    private Collection<CommentVertex> commentList;

    @Bind(R.id.wv_details)
    WebView wvDetails;

    public static DetailsFragment newInstance(String url, String submissionId, String title) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putString(URL_SUBMISSION, url);
        args.putString(ID_SUBMISSION, submissionId);
        args.putString(TITLE_SUBMISSION, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        commentList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);
        RedditApiFactory.getApi().fetchSubmissionComments(getArguments().getString(ID_SUBMISSION));
        wvDetails.getSettings().setJavaScriptEnabled(true);
        wvDetails.getSettings().setBuiltInZoomControls(true);
        wvDetails.loadUrl(getArguments().getString(URL_SUBMISSION));
        wvDetails.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                wvDetails.loadUrl("file:///android_asset/no_connection.html");
            }
        });

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu_detail, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.comment_menu).setVisible(!commentList.isEmpty());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.comment_menu:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.abc_slide_in_bottom,
                                R.anim.abc_slide_out_bottom,
                                R.anim.abc_slide_in_bottom,
                                R.anim.abc_slide_out_bottom)
                        .replace(R.id.fl_container, CommentFragment.newInstance(commentList), CommentFragment.TAG)
                        .addToBackStack(null)
                        .commit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onEventMainThread(final FetchCommentsEvent event) {
        commentList.addAll(event.getCommentList());
        getActivity().invalidateOptionsMenu();
    }

    public void onEventMainThread(FailFetchCommentEvent event) {
        Toast.makeText(getActivity(), getString(R.string.comment_fetch_fail), Toast.LENGTH_SHORT).show();
    }
}