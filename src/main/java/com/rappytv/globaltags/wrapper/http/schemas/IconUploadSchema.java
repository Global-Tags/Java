package com.rappytv.globaltags.wrapper.http.schemas;

import org.jetbrains.annotations.NotNull;

public class IconUploadSchema extends MessageSchema {

    private final String hash;

    public IconUploadSchema(@NotNull String message, @NotNull String hash) {
        super(message);
        this.hash = hash;
    }

    public String getHash() {
        return this.hash;
    }

    @Override
    public String toString() {
        return "IconUploadSchema{" +
                "message='" + this.message + '\'' +
                ", hash='" + this.hash + '\'' +
                '}';
    }
}
