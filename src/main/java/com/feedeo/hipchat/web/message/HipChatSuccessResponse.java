package com.feedeo.hipchat.web.message;

import java.util.Map;

public class HipChatSuccessResponse extends HipChatResponse {
    private Map<String, String> success;

    public Map<String, String> getSuccess() {
        return success;
    }

    public void setSuccess(Map<String, String> success) {
        this.success = success;
    }
}
