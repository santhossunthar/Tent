package com.eYe3.Tent.models;

public class ComReply {
    private String content,uid,userphoto,username,userstatus,comKey;
    private Object timestamp;

    public ComReply (){

    }

    public ComReply(String content, String uid, String userphoto, String username, String userStatus,String comKey, Object timestamp) {
        this.content = content;
        this.uid = uid;
        this.userphoto = userphoto;
        this.username = username;
        this.userstatus=userStatus;
        this.comKey = comKey;
        this.timestamp = timestamp;
    }

    public ComReply(String content, String uid, String userphoto, String username,String userStatus, Object timestamp, String comKey) {
        this.content = content;
        this.uid = uid;
        this.userphoto = userphoto;
        this.username = username;
        this.userstatus=userStatus;
        this.comKey=comKey;
        this.timestamp = timestamp;
    }

    public String getUserstatus() {
        return userstatus;
    }

    public void setUserstatus(String userstatus) {
        this.userstatus = userstatus;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserphoto() {
        return userphoto;
    }

    public void setUserphoto(String userphoto) {
        this.userphoto = userphoto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    public String getComKey() {
        return comKey;
    }

    public void setComKey(String comKey) {
        this.comKey = comKey;
    }
}