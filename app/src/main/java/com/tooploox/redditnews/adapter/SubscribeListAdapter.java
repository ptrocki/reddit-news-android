package com.tooploox.redditnews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tooploox.redditnews.R;
import com.tooploox.redditnews.listener.OnSubscribeClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Piotr Trocki on 18/08/15.
 */
public class SubscribeListAdapter extends RecyclerView.Adapter<SubscribeListAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_subscribe_title)
        TextView tvSubscribeTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private List<String> subscribeList;

    private LayoutInflater layoutInflater;

    OnSubscribeClickListener onSubscribeClickListener;

    public SubscribeListAdapter(OnSubscribeClickListener onSubscribeClickListener, Context context) {
        subscribeList = new ArrayList<>();
        this.onSubscribeClickListener = onSubscribeClickListener;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_subreddit_subscribe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String subredditName = subscribeList.get(position);
        holder.tvSubscribeTitle.setText(subredditName);
        holder.tvSubscribeTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubscribeClickListener.onSubscribedClick(subredditName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subscribeList.size();
    }

    public void setSubscribeList(List<String> subscribeList) {
        this.subscribeList.clear();
        this.subscribeList.addAll(subscribeList);
        notifyDataSetChanged();
    }
}
