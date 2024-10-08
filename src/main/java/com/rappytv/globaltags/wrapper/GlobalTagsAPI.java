package com.rappytv.globaltags.wrapper;

import com.rappytv.globaltags.wrapper.enums.AuthProvider;
import com.rappytv.globaltags.wrapper.http.ApiHandler;
import com.rappytv.globaltags.wrapper.model.PlayerInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * A util class for the GlobalTags API
 * @param <T> The result type of the {@link #translateColorCodes(String)} method
 */
public abstract class GlobalTagsAPI<T> {

    /**
     * Default cache options
     */
    private final PlayerInfo.Cache<T> cache = new PlayerInfo.Cache<>(this);
    /**
     * Default API handler
     */
    private final ApiHandler<T> apiHandler = new ApiHandler<>(this);

    /**
     * Get the API base for the GlobalTags API. Implementation note: Please don't add a trailing slash
     * @return The Globaltags API base
     */
    @NotNull
    public String getApiBase() {
        return "https://api.globaltags.xyz";
    }

    /**
     * Get the current user agent, version and minecraft version to be identified by the API. Example:
     * <blockquote><pre>
     * new Agent("LabyAddon", "v1.2.0", "1.21");
     * </pre></blockquote>
     * @return The user agent
     */
    @NotNull
    public abstract Agent getAgent();

    /**
     * Get the language which is sent to the API on any request
     * @return The language for any API responses
     */
    @NotNull
    public String getLanguageCode() {
        return "en_us";
    }

    /**
     * Get a {@link T} representation in color of the input parameter
     * @param input The text to translate the colors of
     * @return A colored {@link T} representation of the input parameter
     */
    @NotNull
    public abstract T translateColorCodes(@Nullable String input);

    /**
     * If this runs on the client side this will return a client UUID
     * @return The UUID of the current client
     */
    @Nullable
    public abstract UUID getClientUUID();

    /**
     * Get the tag cache
     * @return Returns a instance of {@link PlayerInfo.Cache}
     */
    @NotNull
    public PlayerInfo.Cache<T> getCache() {
        return cache;
    }

    /**
     * Get the api handler
     * @return Returns a instance of {@link ApiHandler}
     */
    @NotNull
    public ApiHandler<T> getApiHandler() {
        return apiHandler;
    }

    /**
     * Get the authentication type used to authenticate with the api
     * @return The authentication type used to authenticate with the api
     */
    @NotNull
    public abstract AuthProvider getAuthType();

    /**
     * Get the authorization to send to the API
     * @return The current authorization key to authenticate with the API
     */
    @Nullable
    public abstract String getAuthorization();

    /**
     * Get the authorization header
     * @return The authorization header for the API.
     */
    @NotNull
    public String getAuthorizationHeader() {
        String auth = getAuthorization();
        if(auth == null) return "";
        return String.format("%s %s", getAuthType().getId(), auth);
    }

    /**
     * A class representing the request agent for better identification on behalf of the API
     */
    public static class Agent {

        private final String agent;
        private final String agentVersion;
        private final String minecraftVersion;

        /**
         * Build a new agent without a minecraft version
         * @param agent The agent name
         * @param agentVersion The agent version
         */
        public Agent(@NotNull String agent, @NotNull String agentVersion) {
            this(agent, agentVersion, null);
        }

        /**
         * Build a new agent including a minecraft version
         * @param agent The agent name
         * @param agentVersion The agent version
         * @param minecraftVersion The minecraft version
         */
        public Agent(@NotNull String agent, @NotNull String agentVersion, @Nullable String minecraftVersion) {
            this.agent = agent;
            this.agentVersion = agentVersion;
            this.minecraftVersion = minecraftVersion;
        }

        /**
         * Get the agent name
         * @return The agent name
         */
        @NotNull
        public String getAgent() {
            return agent;
        }

        /**
         * Get the agent version
         * @return The agent version
         */
        @NotNull
        public String getAgentVersion() {
            return agentVersion;
        }

        /**
         * Get the Minecraft version
         * @return The minecraft version
         */
        @Nullable
        public String getMinecraftVersion() {
            return minecraftVersion;
        }

        @Override
        public @NotNull String toString() {
            return String.format(
                    "%s v%s%s",
                    this.agent,
                    this.agentVersion,
                    minecraftVersion != null ? " - " + minecraftVersion : ""
            );
        }
    }
}
