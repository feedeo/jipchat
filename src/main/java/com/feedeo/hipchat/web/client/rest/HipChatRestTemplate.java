package com.feedeo.hipchat.web.client.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.feedeo.hipchat.web.message.HipChatErrorResponse;
import com.feedeo.hipchat.web.message.HipChatResponse;
import com.feedeo.hipchat.web.message.HipChatSuccessResponse;
import com.google.common.collect.ImmutableMap;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class HipChatRestTemplate {
    private RestTemplate restTemplate;

    private final static String HIPCHAT_API_FORMAT = "json";
    private final static String HIPCHAT_API_BASE_URL = "http://api.hipchat.com/v1/";

    public HipChatRestTemplate() {
        restTemplate = new RestTemplate();

        restTemplate.getMessageConverters().clear();
        restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }

    public HipChatResponse post(String apiKey, String resource, MultiValueMap map) throws IOException, RestClientException {
        HipChatResponse response = null;

        try {
            response = restTemplate
                    .postForObject(
                            HIPCHAT_API_BASE_URL + resource + "?format={format}&auth_token={apiKey}",
                            map,
                            HipChatSuccessResponse.class,
                            ImmutableMap.of("format", HIPCHAT_API_FORMAT, "apiKey", apiKey)
                    );
        } catch (HttpClientErrorException e) {
            response = new ObjectMapper().readValue(e.getResponseBodyAsString(), HipChatErrorResponse.class);
            response.setStatus(e.getMessage());
        } catch (RestClientException e) {
            response = new HipChatErrorResponse();
            response.setStatus(e.getMessage());
        }

        return response;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public static String getHipchatApiFormat() {
        return HIPCHAT_API_FORMAT;
    }

    public static String getHipchatApiBaseUrl() {
        return HIPCHAT_API_BASE_URL;
    }
}
