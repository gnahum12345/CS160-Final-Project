package com.example.gabriel.sociala.exceptions;

public class CreateUserException extends Exception {

    public CreateUserException(String errorMessage) {
        super("Create User Exception:\n".concat(errorMessage));
    }
}
