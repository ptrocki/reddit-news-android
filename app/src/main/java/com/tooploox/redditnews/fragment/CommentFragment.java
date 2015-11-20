package com.tooploox.redditnews.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tooploox.redditnews.R;
import com.tooploox.redditnews.adapter.CommentTreeItemHolder;
import com.tooploox.redditnews.api.model.CommentVertex;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

public class CommentFragment extends Fragment {

    public static final String TAG = CommentFragment.class.getSimpleName();

    private static final String COMMENT_LIST = "comment_list";

    private TreeNode root;

    public static CommentFragment newInstance(Collection<CommentVertex> commentList) {
        CommentFragment fragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(COMMENT_LIST, new ArrayList<>(commentList));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_comment, null, false);
        ViewGroup containerView = (ViewGroup) rootView.findViewById(R.id.rl_comment_container);
        createCommentTree();
        AndroidTreeView treeView = new AndroidTreeView(getActivity(), root);
        //TODO this animation for big count of comment sucks.
        treeView.setDefaultAnimation(true);
        treeView.setDefaultContainerStyle(R.style.TreeNode, false);
        containerView.addView(treeView.getView());
        return rootView;
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    private void createCommentTree() {
        root = TreeNode.root();
        ArrayList<CommentVertex> list = getArguments().getParcelableArrayList(COMMENT_LIST);
        Map<String, TreeNode> commentNodes = new HashMap<>();
        for (CommentVertex comment : list) {
            TreeNode node = new TreeNode(new CommentTreeItemHolder.CommentTreeItem(comment))
                    .setViewHolder(new CommentTreeItemHolder(getActivity()));
            if (comment.getDepth() == 1) {
                root.addChild(node);
            } else {
                TreeNode parentNode = commentNodes.get(comment.getParentId());
                parentNode.addChild(node);
            }
            commentNodes.put(comment.getCommentId(), node);
        }
    }
}