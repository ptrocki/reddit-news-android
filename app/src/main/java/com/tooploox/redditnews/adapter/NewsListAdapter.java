package com.tooploox.redditnews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tooploox.redditnews.R;
import com.tooploox.redditnews.api.model.News;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Piotr Trocki on 27/07/15.
 */
public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {

    public interface NewsListListener {

        void getMoreItems();

        void onNewsClick(String url, String submissionId, String title);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_news_title)
        TextView tvNewsTitle;

        @Bind(R.id.tv_news_author)
        TextView tvNewsAuthor;

        @Bind(R.id.iv_thumb_news)
        ImageView ivThumb;

        @Bind(R.id.rl_item)
        RelativeLayout layoutNews;

        @Bind(R.id.tv_news_comment_count)
        TextView tvNewsCommentCount;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private final int numberOfItemForRefresh = 2;

    private NewsListListener newsListListener;

    private List<News> listNews;

    private LayoutInflater layoutInflater;

    public NewsListAdapter(NewsListListener newsListListener, Context context) {
        this.listNews = new ArrayList<>();
        this.newsListListener = newsListListener;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public NewsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_news, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NewsListAdapter.ViewHolder holder, int position) {
        final News news = listNews.get(position);

        Context context = holder.itemView.getContext();

        if (position == getItemCount() - numberOfItemForRefresh) {
            newsListListener.getMoreItems();
        }

        holder.tvNewsTitle.setText(news.getTitle());
        holder.tvNewsAuthor
                .setText(context.getString(R.string.display_author, news.getAuthor()));
        if (news.getCommentCount() == 0) {
            holder.tvNewsCommentCount
                    .setText(context.getString(R.string.display_comment_zero, news.getCommentCount()));
        } else {
            holder.tvNewsCommentCount
                    .setText(context.getResources()
                            .getQuantityString(R.plurals.comments, news.getCommentCount(), news.getCommentCount()));
        }

        holder.layoutNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newsListListener.onNewsClick(news.getUrl(), news.getSubmissionId(), news.getTitle());
            }
        });
        if (news.getThumbnailUrl() != null) {
            Glide.with(context)
                    .load(news.getThumbnailUrl())
                    .centerCrop()
                    .into(holder.ivThumb);
        } else {
            holder.ivThumb.setImageResource(R.drawable.no_photo);
        }
    }

    @Override
    public int getItemCount() {
        return listNews.size();
    }

    public void setListNews(Collection<News> listNews) {
        this.listNews.clear();
        this.listNews.addAll(listNews);
        notifyDataSetChanged();
    }

    public void addListNews(Collection<News> newListNews) {
        int initialSize = listNews.size();
        listNews.addAll(newListNews);
        notifyItemRangeInserted(initialSize + 1, newListNews.size());
    }
}