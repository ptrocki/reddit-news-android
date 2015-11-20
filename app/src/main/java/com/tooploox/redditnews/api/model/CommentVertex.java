package com.tooploox.redditnews.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tooploox.redditnews.api.model.parcel.CommentVertexList;

import net.dean.jraw.models.CommentNode;

/**
 * Created by Piotr Trocki on 20/08/15.
 */
public class CommentVertex implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CommentVertex> CREATOR = new Parcelable.Creator<CommentVertex>() {
        @Override
        public CommentVertex createFromParcel(Parcel in) {
            return new CommentVertex(in);
        }

        @Override
        public CommentVertex[] newArray(int size) {
            return new CommentVertex[size];
        }
    };

    private boolean anyChildren;
    private boolean moreComments;
    private int depth;
    private boolean topLevel;
    private int childrenSize;
    private int totalChildrenSize;

    private String parentId;
    private String commentId;
    private String commentBody;
    private String commentAuthor;

    private CommentVertexList children;
    private SingleComment comment;

    public CommentVertex(CommentNode node) {
        this.anyChildren = node.isEmpty();
        this.moreComments = node.hasMoreComments();
        this.depth = node.getDepth();
        this.topLevel = node.isTopLevel();
        this.childrenSize = node.getImmediateSize();
        this.totalChildrenSize = node.getTotalSize();
        this.commentId = node.getComment().getId();
        this.commentBody = node.getComment().getBody();
        this.commentAuthor = node.getComment().getAuthor();
        children = new CommentVertexList(node.getChildren());
        parentId = node.getComment().getParentId();
        comment = new SingleComment(node.getComment());
    }

    protected CommentVertex(Parcel in) {
        parentId = in.readString();
        anyChildren = in.readByte() != 0x00;
        moreComments = in.readByte() != 0x00;
        depth = in.readInt();
        topLevel = in.readByte() != 0x00;
        childrenSize = in.readInt();
        totalChildrenSize = in.readInt();
        commentId = in.readString();
        commentBody = in.readString();
        commentAuthor = in.readString();
        children = (CommentVertexList) in.readValue(CommentVertexList.class.getClassLoader());
        comment = (SingleComment) in.readValue(SingleComment.class.getClassLoader());
    }

    public int getChildrenSize() {
        return childrenSize;
    }

    public int getDepth() {
        return depth;
    }

    public boolean hasAnyChildren() {
        return anyChildren;
    }

    public boolean isMoreComments() {
        return moreComments;
    }

    public boolean isTopLevel() {
        return topLevel;
    }

    public int getTotalChildrenSize() {
        return totalChildrenSize;
    }

    public CommentVertexList getChildren() {
        return children;
    }

    public String getCommentAuthor() {
        return commentAuthor;
    }

    public String getCommentBody() {
        return commentBody;
    }

    public String getCommentId() {
        return commentId;
    }

    //return parent id without t*_ tag
    public String getParentId() {
        return parentId.substring(3);
    }

    //return parent id with t*_ tag
    public String getFullParentId() {
        return parentId;
    }

    public SingleComment getComment() {
        return comment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(parentId);
        dest.writeByte((byte) (anyChildren ? 0x01 : 0x00));
        dest.writeByte((byte) (moreComments ? 0x01 : 0x00));
        dest.writeInt(depth);
        dest.writeByte((byte) (topLevel ? 0x01 : 0x00));
        dest.writeInt(childrenSize);
        dest.writeInt(totalChildrenSize);
        dest.writeString(commentId);
        dest.writeString(commentBody);
        dest.writeString(commentAuthor);
        dest.writeValue(children);
        dest.writeValue(comment);
    }

    @Override
    public String toString() {
        return "CommentNode {" +
                "commentId='" + commentId + '\'' +
                ", parent=" + parentId +
                ", depth=" + depth +
                '}';
    }
}