package com.feedeo.hipchat.web.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedeo.hipchat.exception.room.message.UnableToSendMessageToRoomException;
import com.feedeo.hipchat.model.room.Room;
import com.feedeo.hipchat.model.room.message.Message;
import com.feedeo.hipchat.web.message.HipChatResponse;
import com.google.common.collect.ImmutableMap;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HipChatRestClient {
    private RestTemplate restTemplate;

    private static String apiKey;

    private final static String HIPCHAT_API_FORMAT = "json";
    private final static String HIPCHAT_API_BASE_URL = "http://api.hipchat.com/v1/";

    public HipChatRestClient() {
        restTemplate = new RestTemplate();

        List<HttpMessageConverter<?>> httpMessageConverters = new ArrayList<HttpMessageConverter<?>>();
        httpMessageConverters.add(new FormHttpMessageConverter());
        httpMessageConverters.add(new MappingJackson2HttpMessageConverter());
        restTemplate.setMessageConverters(httpMessageConverters);
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
            HipChatResponse response = post("rooms/message", map);
            if (response.getStatus() != null && !response.getStatus().equals("sent")) {

                if (response.getError() != null) {
                    throw new UnableToSendMessageToRoomException(
                            response.getError().containsKey("message") ?
                                    response.getError().get("message"): response.getStatus()
                    );
                } else {
                    throw new UnableToSendMessageToRoomException(response.getStatus());
                }

            }
        } catch (IOException e) {
            throw new UnableToSendMessageToRoomException(e);
        }
    }

    private HipChatResponse post(String resource, MultiValueMap map) throws IOException, RestClientException {
        HipChatResponse response = null;

        try {
            response = restTemplate
                    .postForObject(
                            HIPCHAT_API_BASE_URL + resource + "?format={format}&auth_token={apiKey}",
                            map,
                            HipChatResponse.class,
                            ImmutableMap.of("format", HIPCHAT_API_FORMAT, "apiKey", apiKey)
                    );
        } catch (HttpClientErrorException e) {
            response = new ObjectMapper().readValue(e.getResponseBodyAsString(), HipChatResponse.class);
            response.setStatus(e.getMessage());
        } catch (RestClientException e) {
            response = new HipChatResponse();
            response.setStatus(e.getMessage());
        }

        return response;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public static String getApiKey() {
        return apiKey;
    }

    public static String getHipchatApiFormat() {
        return HIPCHAT_API_FORMAT;
    }

    public static String getHipchatApiBaseUrl() {
        return HIPCHAT_API_BASE_URL;
    }
}
