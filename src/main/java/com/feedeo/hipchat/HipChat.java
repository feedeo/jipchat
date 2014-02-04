package com.feedeo.hipchat;

import com.feedeo.hipchat.service.room.RoomService;
import com.feedeo.hipchat.web.client.HipChatRestClient;

public class HipChat {
    private HipChatRestClient hipChatClient;
    private RoomService roomService;

    public HipChat(String apiKey) {
        hipChatClient = new HipChatRestClient();
        hipChatClient.setApiKey(apiKey);

        roomService = new RoomService();
        roomService.setHipChatClient(hipChatClient);
    }

    public RoomService getRoomService() {
        return roomService;
    }

    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }
}
