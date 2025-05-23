package com.rappytv.globaltags.wrapper.http.schemas;

import org.jetbrains.annotations.NotNull;

public class GiftCodeCreationSchema extends MessageSchema {

    private final String code;

    /**
     * @param message The success message
     * @param code    The gift code
     */
    public GiftCodeCreationSchema(@NotNull String message, @NotNull String code) {
        super(message);
        this.code = code;
    }

    /**
     * @return the gift code
     */
    @NotNull
    public String getCode() {
        return this.code;
    }

    @Override
    public String toString() {
        return "GiftCodeCreationSchema{" +
                "code='" + this.code + '\'' +
                ", message='" + this.message + '\'' +
                '}';
    }
}
