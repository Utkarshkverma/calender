package com.freightfox.ai.Meeting.Calendar.Assistant.exception;

public class ResourceNotFoundException extends RuntimeException {

    String msg;

    public ResourceNotFoundException(String msg) {
        super(msg);
    }
}
