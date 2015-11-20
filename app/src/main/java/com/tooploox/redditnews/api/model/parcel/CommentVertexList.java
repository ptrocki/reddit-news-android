package com.tooploox.redditnews.api.model.parcel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tooploox.redditnews.api.model.CommentVertex;

import net.dean.jraw.models.CommentNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Piotr Trocki on 20/08/15.
 */
public class CommentVertexList implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CommentVertexList> CREATOR = new Parcelable.Creator<CommentVertexList>() {
        @Override
        public CommentVertexList createFromParcel(Parcel in) {
            return new CommentVertexList(in);
        }

        @Override
        public CommentVertexList[] newArray(int size) {
            return new CommentVertexList[size];
        }
    };

    private ArrayList<CommentVertex> childrenCommentList;

    public CommentVertexList() {
    }

    public CommentVertexList(List<CommentNode> children) {
        childrenCommentList = new ArrayList<>();
        if (children == null) {
            return;
        }
        for (CommentNode commentNode : children) {
            CommentVertex commentVertex = new CommentVertex(commentNode);
            childrenCommentList.add(commentVertex);
        }
    }

    public ArrayList<CommentVertex> getChildrenCommentList() {
        return childrenCommentList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (childrenCommentList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(childrenCommentList);
        }
    }

    protected CommentVertexList(Parcel in) {
        if (in.readByte() == 0x01) {
            childrenCommentList = new ArrayList<>();
            in.readList(childrenCommentList, CommentVertex.class.getClassLoader());
        } else {
            childrenCommentList = null;
        }
    }
}
