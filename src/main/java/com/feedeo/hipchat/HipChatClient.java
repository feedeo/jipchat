package com.feedeo.hipchat;

import com.feedeo.hipchat.service.room.RoomService;
import com.feedeo.hipchat.web.client.HipChatWebClient;
import com.feedeo.hipchat.web.client.rest.HipChatRestClient;

public class HipChatClient {
    private HipChatWebClient hipChatClient;
    private RoomService roomService;

    public HipChatClient(String apiKey) {
        hipChatClient = new HipChatRestClient();
        hipChatClient.setApiKey(apiKey);

        roomService = new RoomService(hipChatClient);
    }

    public RoomService getRoomService() {
        return roomService;
    }
}
