package com.tooploox.redditnews.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import com.tooploox.redditnews.api.client.RedditApiFactory;

import java.util.List;

/**
 * Created by Piotr Trocki on 10/08/15.
 */
public class SubredditSearchAdapter extends ArrayAdapter<String> {

    private class SubredditFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint == null) {
                filterResults.count = 0;
                return filterResults;
            }
            List<String> subredditList = RedditApiFactory.getApi().searchSubreddit(String.valueOf(constraint));
            filterResults.values = subredditList;
            filterResults.count = subredditList.size();
            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count > 0 && results.values != null) {
                clear();
                addAll(((List<String>) results.values));
                notifyDataSetChanged();
            }
        }
    }

    private SubredditFilter filter;

    public SubredditSearchAdapter(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new SubredditFilter();
        }
        return filter;
    }
}