package com.zebia.model;

import java.util.Date;

public class Item {
	private String id;
    private String fromUserName;
    private String fromUser;
    private String fromUserId;
	private String text;
	private String textLong;
	private boolean sync = false;
	private Date createdAt;

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
