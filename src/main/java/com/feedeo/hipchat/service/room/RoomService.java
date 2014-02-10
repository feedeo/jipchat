package com.feedeo.hipchat.service.room;

import com.feedeo.hipchat.constant.room.message.Color;
import com.feedeo.hipchat.exception.room.message.UnableToSendMessageToRoomException;
import com.feedeo.hipchat.model.room.Room;
import com.feedeo.hipchat.model.room.message.Message;
import com.feedeo.hipchat.web.client.HipChatWebClient;
import com.google.common.base.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class RoomService {
    private HipChatWebClient hipChatClient;

    private final static int HIPCHAT_API_ROOM_MESSAGE_FROM_MIN_LENGTH = 1;
    private final static int HIPCHAT_API_ROOM_MESSAGE_FROM_MAX_LENGTH = 15;
    private final static int HIPCHAT_API_ROOM_MESSAGE_MIN_LENGTH = 1;
    private final static int HIPCHAT_API_ROOM_MESSAGE_MAX_LENGTH = 10000;

    public RoomService(HipChatWebClient hipChatClient) {
        this.hipChatClient = hipChatClient;
    }

    public void sendMessage(Room room, Message message) throws UnableToSendMessageToRoomException {

        try {
            // required fields
            checkNotNull(room);
            checkNotNull(room.getId(), "Room Id field is required");
            checkNotNull(message);
            checkNotNull(message.getFrom(), "Message from field is required");
            checkNotNull(message.getContent(), "Message content field is required");

            // required length
            checkArgument(HIPCHAT_API_ROOM_MESSAGE_FROM_MIN_LENGTH < message.getFrom().length() &&
                    message.getFrom().length() < HIPCHAT_API_ROOM_MESSAGE_FROM_MAX_LENGTH,
                    "Message from field must be between " + HIPCHAT_API_ROOM_MESSAGE_FROM_MIN_LENGTH +
                            " and " + HIPCHAT_API_ROOM_MESSAGE_FROM_MAX_LENGTH + " characters long");
            checkArgument(HIPCHAT_API_ROOM_MESSAGE_MIN_LENGTH < message.getContent().length() &&
                    message.getContent().length() < HIPCHAT_API_ROOM_MESSAGE_MAX_LENGTH,
                    "Message content field must be between " + HIPCHAT_API_ROOM_MESSAGE_MIN_LENGTH +
                            " and " + HIPCHAT_API_ROOM_MESSAGE_MAX_LENGTH + " characters long");
        } catch (Exception e) {
            throw new UnableToSendMessageToRoomException(e);
        }

        // optional with default
        message.setNotify(Optional.fromNullable(message.getNotify()).or(false));
        message.setColor(Optional.fromNullable(message.getColor()).or(Color.YELLOW));

        hipChatClient.sendMessageToRoom(room, message);
    }
}
