package com.tooploox.redditnews.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import net.dean.jraw.models.Comment;

/**
 * Created by Piotr Trocki on 13/08/15.
 */
public class SingleComment implements Parcelable {

    public static final Parcelable.Creator<SingleComment> CREATOR = new Parcelable.Creator<SingleComment>() {

        public SingleComment createFromParcel(Parcel in) {
            return new SingleComment(in);
        }

        @Override
        public SingleComment[] newArray(int size) {
            return new SingleComment[0];
        }
    };

    private final String id;
    private final String body;
    private final String author;
    private final String parentId;
    private final long commentTime;

    public SingleComment(Comment comment) {
        this.author = comment.getAuthor();
        this.body = comment.getBody();
        this.parentId = comment.getParentId();
        this.id = comment.getId();
        this.commentTime = comment.getCreatedUtc().getTime();
    }

    private SingleComment(Parcel in) {
        id = in.readString();
        body = in.readString();
        author = in.readString();
        parentId = in.readString();
        commentTime = in.readLong();
    }

    public String getAuthor() {
        return author;
    }

    public String getBody() {
        return body;
    }

    public String getId() {
        return id;
    }

    public String getParentId() {
        return parentId;
    }

    public long getCommentTime() {
        return commentTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(body);
        dest.writeString(author);
        dest.writeString(parentId);
        dest.writeLong(commentTime);
    }
}