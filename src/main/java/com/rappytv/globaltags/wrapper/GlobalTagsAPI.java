package com.rappytv.globaltags.wrapper;

import com.rappytv.globaltags.wrapper.enums.AuthProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * A util interface for the GlobalTags API
 * @param <T> The result type of the {@link #translateColorCodes(String)} method
 */
public interface GlobalTagsAPI<T> {

    /**
     * Get the API base for the GlobalTags API
     * @return The Globaltags API base
     */
    @NotNull
    String getApiBase();

    /**
     * Get a {@link T} representation in color of the input parameter
     * @param input The text to translate the colors of
     * @return A colored {@link T} representation of the input parameter
     */
    @NotNull
    T translateColorCodes(String input);

    /**
     * If this runs on the client side this will return a client UUID
     * @return The UUID of the current client
     */
    @Nullable
    UUID getClientUUID();

    /**
     * Get the authentication type used to authenticate with the api
     * @return The authentication type used to authenticate with the api
     */
    @NotNull
    AuthProvider getAuthType();

    /**
     * Get the authorization to send to the API
     * @return The current authorization key to authenticate with the API
     */
    @Nullable
    String getAuthorization();

    /**
     * Get the authorization header
     * @return The authorization header for the API. Returns null if {@link #getAuthorization()} returns null
     */
    @Nullable
    default String getAuthorizationHeader() {
        String auth = getAuthorization();
        if(auth == null) return null;
        return String.format("%s %s", getAuthType().getId(), auth);
    }
}