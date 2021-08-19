package com.eYe3.Tent.models;

public class Message {
    private String image;
    private String message;
    private String url;

    public Message(){

    }

    public Message(String image, String message, String url ) {
        this.image = image;
        this.message = message;
        this.url=url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
