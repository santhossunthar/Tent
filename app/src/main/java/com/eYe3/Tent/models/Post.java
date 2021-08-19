package com.eYe3.Tent.models;

import com.google.firebase.database.ServerValue;

public class Post {
    private String postKey;
    private String description;
    private String username;
    private String userphoto;
    private String userId;
    private String userstatus;
    private Object timestamp ;

    public Post(){

    }

    public Post(String postKey, String description, String userName, String userPhoto, String userId, String userStatus) {
        this.postKey = postKey;
        this.description = description;
        this.username = userName;
        this.userphoto = userPhoto;
        this.userId=userId;
        this.userstatus=userStatus;
        this.timestamp = ServerValue.TIMESTAMP;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserphoto() {
        return userphoto;
    }

    public void setUserphoto(String userphoto) {
        this.userphoto = userphoto;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserstatus() {
        return userstatus;
    }

    public void setUserstatus(String userstatus) {
        this.userstatus = userstatus;
    }
}

