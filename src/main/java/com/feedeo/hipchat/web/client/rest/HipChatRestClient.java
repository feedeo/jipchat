package com.feedeo.hipchat.web.client.rest;

import com.feedeo.hipchat.exception.room.message.UnableToSendMessageToRoomException;
import com.feedeo.hipchat.model.room.Room;
import com.feedeo.hipchat.model.room.message.Message;
import com.feedeo.hipchat.web.client.HipChatWebClient;
import com.feedeo.hipchat.web.message.HipChatErrorResponse;
import com.feedeo.hipchat.web.message.HipChatResponse;
import com.feedeo.hipchat.web.message.HipChatSuccessResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;

public class HipChatRestClient implements HipChatWebClient {
    private HipChatRestTemplate hipChatRestTemplate;

    private String apiKey;

    public HipChatRestClient() {
        hipChatRestTemplate = new HipChatRestTemplate();
    }

    public void sendMessageToRoom(Room room, Message message) throws UnableToSendMessageToRoomException {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();

        map.add("room_id", room.getId());
        map.add("message", message.getContent());
        map.add("message_format", message.getFormat());
        map.add("from", message.getFrom());
        map.add("color", message.getColor().toString());
        map.add("notify", message.getNotify() ? "1" : "0");

        try {
            HipChatResponse response = hipChatRestTemplate.post(apiKey, "rooms/message", map);

            if(!(response instanceof HipChatSuccessResponse) ||
                    !response.getStatus().equals("sent")) {

                HipChatErrorResponse errorResponse = (HipChatErrorResponse) response;
                if (errorResponse.getError() != null) {
                    throw new UnableToSendMessageToRoomException(
                            errorResponse.getError().containsKey("message") ?
                                    errorResponse.getError().get("message"): response.getStatus()
                    );
                } else {
                    throw new UnableToSendMessageToRoomException(response.getStatus());
                }

            }
        } catch (IOException e) {
            throw new UnableToSendMessageToRoomException(e);
        }
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    public HipChatRestTemplate getHipChatRestTemplate() {
        return hipChatRestTemplate;
    }

    public void setHipChatRestTemplate(HipChatRestTemplate hipChatRestTemplate) {
        this.hipChatRestTemplate = hipChatRestTemplate;
    }
}
