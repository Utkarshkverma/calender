package com.freightfox.ai.Meeting.Calendar.Assistant.exception;

public class ResponseException extends RuntimeException {

    String msg;
    public ResponseException(String msg) {
        super(msg);
    }
}
