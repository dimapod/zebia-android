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

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTextLong() {
        return textLong;
    }

    public void setTextLong(String textLong) {
        this.textLong = textLong;
    }

    @Override
    public String toString() {
        return "[" + id + "] User " + fromUser + " wrote '" + text + "'";
    }
}
