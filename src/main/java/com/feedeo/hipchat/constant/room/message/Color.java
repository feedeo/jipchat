package com.feedeo.hipchat.constant.room.message;

public enum Color {
    YELLOW("yellow"),
    RED("red"),
    GREEN("green"),
    PURPLE("purple"),
    GRAY("gray"),
    RANDOM("random");

    private String name;

    private Color(String name) {
        this.name = name;
    }

    public String toString() {
        return name;
    }
}
