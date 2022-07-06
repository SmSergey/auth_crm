package com.exceedit.auth.web.controller.api.response;

import com.exceedit.auth.utils.messages.ErrorMessages;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
                .status(this.status)
                .body(this.response.toString());
    }

    public ResponseEntity<String> buildAsBadJwt() {
        return new ApiResponse()
                .setStatus(403)
                .setMessage(ErrorMessages.BAD_AUTHORIZATION_TOKEN).build();
    }

    public ResponseEntity<String> buildAsUserNotFound() {
        return new ApiResponse()
                .setStatus(404)
                .setMessage(ErrorMessages.USER_NOT_FOUND).build();
    }
}
