package com.example.postgresdemo.util;

public class Response {
    private Object data;
    private Boolean shouldNotify = false;

    public Response(Object item) {
        this.data = item;
    }


    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Boolean getShouldNotify() {
        return shouldNotify;
    }

    public void setShouldNotify(Boolean shouldNotify) {
        this.shouldNotify = shouldNotify;
    }
}
