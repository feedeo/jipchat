package com.feedeo.hipchat.web.message;

import java.util.Map;

public class HipChatErrorResponse extends HipChatResponse {
    private Map<String, String> error;

    public Map<String, String> getError() {
        return error;
    }

    public void setError(Map<String, String> error) {
        this.error = error;
    }
}
