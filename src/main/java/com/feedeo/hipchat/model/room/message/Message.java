package com.feedeo.hipchat.model.room.message;

import com.feedeo.hipchat.constant.room.message.Color;

public abstract class Message {
    protected String from;
    protected String content;
    protected boolean notify;
    protected Color color;
    protected String format;

    protected Message(String format) {
        this.format = format;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }

    public boolean isNotify() {
        return notify;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public String getFormat() {
        return format;
    }

    public boolean getNotify() {
        return notify;
    }
}
