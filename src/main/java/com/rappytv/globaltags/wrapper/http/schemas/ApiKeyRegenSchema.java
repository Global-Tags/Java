package com.rappytv.globaltags.wrapper.http.schemas;

import org.jetbrains.annotations.NotNull;

public class ApiKeyRegenSchema extends MessageSchema {

    protected final String key;

    /**
     * @param key The regenerated key
     */
    public ApiKeyRegenSchema(@NotNull String message, @NotNull String key) {
        super(message);
        this.key = key;
    }

    /**
     * @return the regenerated API key.
     */
    @NotNull
    public String getKey() {
        return this.key;
    }

    @Override
    public String toString() {
        return "ApiKeyRegenSchema{" +
                "message='" + this.message + '\'' +
                ", key='" + this.key + '\'' +
                '}';
    }
}
