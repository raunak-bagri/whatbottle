package com.whatbottle.exception;

/**
 * Created by raunak.bagri
 */
public class WhatbottleException extends RuntimeException {

    public WhatbottleException(String message) {
        super(message);
    }

    public WhatbottleException(String message, Throwable cause) {
        super(message, cause);
    }
}
