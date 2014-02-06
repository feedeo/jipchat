package com.feedeo.hipchat;

import com.feedeo.hipchat.service.room.RoomService;
import com.feedeo.hipchat.web.client.HipChatClient;
import com.feedeo.hipchat.web.client.rest.HipChatRestClient;

public class HipChat {
    private HipChatClient hipChatClient;
    private RoomService roomService;

    public HipChat(String apiKey) {
        hipChatClient = new HipChatRestClient();
        hipChatClient.setApiKey(apiKey);

        roomService = new RoomService(hipChatClient);
    }

    public RoomService getRoomService() {
        return roomService;
    }
}
