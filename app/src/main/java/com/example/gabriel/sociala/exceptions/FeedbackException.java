package com.example.gabriel.sociala.exceptions;

public class FeedbackException extends Exception {

    public FeedbackException(String errorMessage) {
        super("Missing Data!!!\n".concat(errorMessage));
    }
}
