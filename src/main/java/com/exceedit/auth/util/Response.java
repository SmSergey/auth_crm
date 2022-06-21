package com.exceedit.auth.util;

import lombok.Data;

@Data
public class Response {
    private Object data;
    private Boolean shouldNotify = false;

    public Response(Object item) {
        this.data = item;
    }
}
