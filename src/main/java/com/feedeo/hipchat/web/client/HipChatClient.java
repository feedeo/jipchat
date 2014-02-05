package com.feedeo.hipchat.web.client;

import com.feedeo.hipchat.exception.room.message.UnableToSendMessageToRoomException;
import com.feedeo.hipchat.model.room.Room;
import com.feedeo.hipchat.model.room.message.Message;

public interface HipChatClient {
    public void setApiKey(String apiKey);
    public void sendMessageToRoom(Room room, Message message) throws UnableToSendMessageToRoomException;
}
