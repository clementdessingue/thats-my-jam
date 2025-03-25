package com.cledess.thats_my_jam.spotify.exceptions;

public class EmptyResponseException extends Exception {

    public EmptyResponseException(String message) {
        super(message);
    }

    public EmptyResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyResponseException() {
        super("No response from API");
    }
}
