package com.feedeo.hipchat.exception.room.message;

public class UnableToSendMessageToRoomException extends Exception {

    public UnableToSendMessageToRoomException() {
        super();
    }

    public UnableToSendMessageToRoomException(Exception exception) {
        super(exception);
    }

    public UnableToSendMessageToRoomException(String message) {
        super(message);
    }
}
