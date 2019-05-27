package com.whatbottle.exception;

/**
 * Created by gunaas
 */
public class WhatbottleException extends RuntimeException {

    public WhatbottleException(String message) {
        super(message);
    }

    public WhatbottleException(String message, Throwable cause) {
        super(message, cause);
    }
}
