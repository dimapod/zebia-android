package com.zebia.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Item {

	private String id;

    @SerializedName("from_user_name")
    private String fromUserName;

    @SerializedName("from_user")
    private String fromUser;

    @SerializedName("from_user_id")
    private String fromUserId;
	private String text;

    @SerializedName("text_long")
	private String textLong;

    //@SerializedName("created_at")
	private Date createdAt;

	private boolean sync = false;

    public Item() {
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Item setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getFromUser() {
        return fromUser;
    }

    public Item setFromUser(String fromUser) {
        this.fromUser = fromUser;
        return this;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public Item setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
        return this;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public Item setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
        return this;
    }

    public String getId() {
        return id;
    }

    public Item setId(String id) {
        this.id = id;
        return this;
    }

    public boolean isSync() {
        return sync;
    }

    public Item setSync(boolean sync) {
        this.sync = sync;
        return this;
    }

    public String getText() {
        return text;
    }

    public Item setText(String text) {
        this.text = text;
        return this;
    }

    public String getTextLong() {
        return textLong;
    }

    public Item setTextLong(String textLong) {
        this.textLong = textLong;
        return this;
    }

    @Override
    public String toString() {
        return "[" + id + "] User " + fromUser + " wrote '" + text + "'";
    }
}
