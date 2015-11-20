package com.tooploox.redditnews.adapter;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tooploox.redditnews.R;
import com.tooploox.redditnews.api.model.CommentVertex;
import com.unnamed.b.atv.model.TreeNode;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CommentTreeItemHolder extends TreeNode.BaseNodeViewHolder<CommentTreeItemHolder.CommentTreeItem> {

    public static class CommentTreeItem {
        private final boolean isEmpty;
        private String body;
        private String author;
        private long time;

        public CommentTreeItem(CommentVertex comment) {
            this.body = comment.getCommentBody();
            this.author = comment.getCommentAuthor();
            this.time = comment.getComment().getCommentTime();
            this.isEmpty = comment.hasAnyChildren();
        }
    }

    @Bind(R.id.tv_comment_body)
    TextView tvCommentBody;

    @Bind(R.id.tv_comment_author)
    TextView tvCommentAuthor;

    @Bind(R.id.iv_comment_expand)
    ImageView ivCommentExpand;

    @Bind(R.id.tv_comment_show_more)
    TextView tvCommentShowMore;

    @Bind(R.id.iv_comment_collapse)
    ImageView ivCommentCollapse;

    @Bind(R.id.tv_comment_show_less)
    TextView tvCommentShowLess;

    private final LayoutInflater layoutInflater;

    private final Context context;

    public CommentTreeItemHolder(Context context) {
        super(context);
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View createNodeView(TreeNode node, CommentTreeItem value) {
        final View view = layoutInflater.inflate(R.layout.item_comment, node.getRoot().getViewHolder().getNodeItemsView(), false);
        ButterKnife.bind(this, view);
        CharSequence displayCommentTime = DateUtils.getRelativeDateTimeString(
                context,
                value.time,
                DateUtils.MINUTE_IN_MILLIS,
                DateUtils.WEEK_IN_MILLIS,
                DateUtils.FORMAT_SHOW_TIME);

        //I use it because String strip all the style information from the string.
        CharSequence authorWithTime = Html.fromHtml(String.format(
                context.getResources().getString(R.string.comment_author_and_time),
                value.author,
                displayCommentTime.toString()));

        //Set body and author(I mean "Andrej90 wrote 9 hours ago :")
        tvCommentAuthor.setText(authorWithTime);
        tvCommentBody.setText(value.body);

        //Set clickListener for node, which has child(ren)
        if (!value.isEmpty) {
            setExpandVisibility(View.VISIBLE);
            node.setClickListener(new TreeNode.TreeNodeClickListener() {
                @Override
                public void onClick(TreeNode treeNode, Object o) {
                    if (treeNode.isExpanded()) {
                        setCollapseVisibility(View.GONE);
                        setExpandVisibility(View.VISIBLE);
                    } else {
                        setCollapseVisibility(View.VISIBLE);
                        setExpandVisibility(View.GONE);
                    }
                }
            });
        }
        return view;
    }

    private void setCollapseVisibility(int visibility) {
        tvCommentShowLess.setVisibility(visibility);
        ivCommentCollapse.setVisibility(visibility);
    }

    private void setExpandVisibility(int visibility) {
        tvCommentShowMore.setVisibility(visibility);
        ivCommentExpand.setVisibility(visibility);
    }
}