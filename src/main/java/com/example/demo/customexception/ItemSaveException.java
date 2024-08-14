package com.example.demo.customexception;

public class ItemSaveException extends RuntimeException{
    public ItemSaveException(String message, Throwable cause) {

        super(message, cause);
    }
    public ItemSaveException(String message) {
        super(message);
    }


}
