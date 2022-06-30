package com.exceedit.auth.web.controller.api.response;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class ApiResponse {

    private final Logger logger = LoggerFactory.getLogger(ApiResponse.class);

    private final JSONObject response;
    private int status;

    public ApiResponse() {
        response = new JSONObject();
    }

    public ApiResponse setStatus(int status) {
        this.response.put("status", status);
        this.status = status;
        return this;
    }

    public ApiResponse shouldNotify(boolean shouldNotify) {
        this.response.put("shouldNotify", shouldNotify);
        return this;
    }

    public ApiResponse setMessage(String message) {
        this.response.put("message", message);
        return this;
    }

    public <T> ApiResponse addField(String key, T value) {
        this.response.put(key, value);
        return this;
    }

    public ResponseEntity<String> build() {
        return ResponseEntity
                .ok().contentType(MediaType.APPLICATION_JSON)
                .body(this.response.toString());
    }
}
