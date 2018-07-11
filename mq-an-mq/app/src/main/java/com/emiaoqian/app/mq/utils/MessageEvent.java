package com.emiaoqian.app.mq.utils;

public class MessageEvent {

    public static final String TAG = MessageEvent.class.getSimpleName();

    public static final int URL_LOGOUT = 0;

    public static final int URL_LOGIN = 1;

    public static final int URL_SECOND = 2;
    public static final int URL_COMMON = 3;
    public String message = null;
    public int code = 0;

    public MessageEvent(int code) {
        this.code = code;
    }

    public MessageEvent(int code, String message) {
        this.code = code;
        this.message = message;
    }
}