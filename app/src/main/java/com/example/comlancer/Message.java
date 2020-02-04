package com.example.comlancer;

import java.io.Serializable;

public class Message implements Serializable {

    //  private String sender;
    private String sender;
    private String content;
    private boolean isBelongsToCurrentUser;

    public Message(String sender, String content, boolean isBelongsToCurrentUser) {
        this.sender = sender;
        this.content = content;
        this.isBelongsToCurrentUser = isBelongsToCurrentUser;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public boolean getIsBelongsToCurrentUser() {
        return isBelongsToCurrentUser;
    }
}
