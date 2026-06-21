package com.project.todo.exception;

public class EmailExistsException extends RuntimeException {
    public EmailExistsException(String msg) {
        super(msg);
    }
}
