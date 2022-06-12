package com.example.bookstore.exception;

public class RecordNotFoundException extends RuntimeException {
    public RecordNotFoundException(){
        super("No Record Found");
    }
    public RecordNotFoundException(String message) {
        super(message);
    }
    public RecordNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
