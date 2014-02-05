package com.feedeo.hipchat.web.client.rest;

import com.feedeo.hipchat.constant.room.message.Color;
import com.feedeo.hipchat.exception.room.message.UnableToSendMessageToRoomException;
import com.feedeo.hipchat.model.room.Room;
import com.feedeo.hipchat.model.room.message.Message;
import com.feedeo.hipchat.model.room.message.TextMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.fest.assertions.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(MockitoJUnitRunner.class)
public class HipChatRestClientIntegrationTest {
    private HipChatRestClient target;

    private MockRestServiceServer server;

    private Room room;

    private Message message;

    private String apiKey;

    private String roomId;
    private String messageFrom;
    private String messageContent;
    private boolean messageNotify;
    private Color messageColor;

    @Before
    public void setUp() {
        apiKey = "kf03mb67msnwpdlwjr4u85";

        roomId = "435456";

        messageFrom = "Test client";
        messageContent = "Test message";
        messageNotify = false;
        messageColor = Color.YELLOW;

        room = new Room();
        room.setId(roomId);

        message = new TextMessage();
        message.setFrom(messageFrom);
        message.setContent(messageContent);
        message.setNotify(messageNotify);
        message.setColor(messageColor);

        target = new HipChatRestClient();
        target.setApiKey(apiKey);

        server = MockRestServiceServer.createServer(target.getHipChatRestTemplate().getRestTemplate());
    }

    @Test
    public void shouldSendMessageToRoom() throws UnableToSendMessageToRoomException {
        server.expect(requestTo(target.getHipChatRestTemplate().getHipchatApiBaseUrl() +
                "rooms/message?format=" + target.getHipChatRestTemplate().getHipchatApiFormat() + "&auth_token=" + apiKey))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(content().string(
                        "room_id=" + roomId +
                        "&message=" + messageContent.replace(" ", "+") +
                        "&message_format=" + message.getFormat() +
                        "&from=" + messageFrom.replace(" ", "+") +
                        "&color=" + messageColor.toString() +
                        "&notify=" + (messageNotify ? "1" : "0")
                ))
                .andRespond(withSuccess("{\"status\": \"sent\"}", MediaType.APPLICATION_JSON));

        target.sendMessageToRoom(room, message);

        server.verify();
    }

    @Test(expected = UnableToSendMessageToRoomException.class)
    public void shouldThrowUnableToSendMessageToRoomWhenServerError() throws UnableToSendMessageToRoomException {
        server.expect(requestTo(target.getHipChatRestTemplate().getHipchatApiBaseUrl() + "rooms/message?format="
                + target.getHipChatRestTemplate().getHipchatApiFormat() + "&auth_token=" + apiKey))
                .andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));

        try {
            target.sendMessageToRoom(room, message);
        } catch (UnableToSendMessageToRoomException e) {
            assertThat(e.getMessage()).isEqualTo("500 Internal Server Error");
            throw e;
        }

        server.verify();
    }


    @Test(expected = UnableToSendMessageToRoomException.class)
    public void shouldThrowUnableToSendMessageToRoomWhenClientError() throws UnableToSendMessageToRoomException {
        server.expect(requestTo(target.getHipChatRestTemplate().getHipchatApiBaseUrl() + "rooms/message?format=" +
                target.getHipChatRestTemplate().getHipchatApiFormat() + "&auth_token=" + apiKey))
                .andRespond(withStatus(HttpStatus.UNAUTHORIZED)
                        .body("{\"error\":{\"code\":401,\"type\":\"Unauthorized\",\"message\":\"Auth token invalid. Please see: https:\\/\\/www.hipchat.com\\/docs\\/api\\/auth\"}}"));

        try {
            target.sendMessageToRoom(room, message);
        } catch (UnableToSendMessageToRoomException e) {
            assertThat(e.getMessage()).isEqualTo("Auth token invalid. Please see: https://www.hipchat.com/docs/api/auth");
            throw e;
        }

        server.verify();
    }
}
