package com.feedeo.hipchat.web.message;

import java.util.Map;

public class HipChatResponse {
    private String status;
    private Map<String, String> success;
    private Map<String, String> error;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, String> getError() {
        return error;
    }

    public void setError(Map<String, String> error) {
        this.error = error;
    }

    public Map<String, String> getSuccess() {
        return success;
    }

    public void setSuccess(Map<String, String> success) {
        this.success = success;
    }
}
