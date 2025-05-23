package com.rappytv.globaltags.wrapper.http.schemas;

import org.jetbrains.annotations.NotNull;

public class ApiKeyCreationSchema extends ApiKeyRegenSchema {

    private final String name;

    /**
     * @param message The success message
     * @param name    The API key name
     * @param key     The regenerated key
     */
    public ApiKeyCreationSchema(@NotNull String message, @NotNull String name, @NotNull String key) {
        super(message, key);
        this.name = name;
    }

    /**
     * @return the API key name
     */
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "ApiKeyCreationSchema{" +
                "name='" + this.name + '\'' +
                ", key='" + this.key + '\'' +
                ", message='" + this.message + '\'' +
                '}';
    }
}
