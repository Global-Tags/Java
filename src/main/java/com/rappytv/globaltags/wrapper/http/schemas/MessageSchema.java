package com.rappytv.globaltags.wrapper.http.schemas;

import org.jetbrains.annotations.NotNull;

public class MessageSchema {

    protected final String message;

    public MessageSchema(@NotNull String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
