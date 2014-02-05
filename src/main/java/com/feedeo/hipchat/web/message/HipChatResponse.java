package com.feedeo.hipchat.web.message;

public abstract class HipChatResponse {
    protected String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
