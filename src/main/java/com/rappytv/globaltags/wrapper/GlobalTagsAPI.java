package com.rappytv.globaltags.wrapper;

import com.rappytv.globaltags.wrapper.enums.AuthProvider;
import com.rappytv.globaltags.wrapper.enums.GlobalIcon;
import com.rappytv.globaltags.wrapper.http.ApiHandler;
import com.rappytv.globaltags.wrapper.model.PlayerInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * A util class for the GlobalTags API
 *
 * @param <T> The result type of the {@link #translateColorCodes(String)} method
 */
public abstract class GlobalTagsAPI<T> {

    /*+
     * Default urls
     */
    private final Urls urls = new Urls();
    /**
     * Default cache options
     */
    private final PlayerInfo.Cache<T> cache = new PlayerInfo.Cache<>(this);
    /**
     * Default API handler
     */
    private final ApiHandler<T> apiHandler = new ApiHandler<>(this);

    /**
     * Get the holder of the important API URLs
     * @return The URL holder
     */
    @NotNull
    public Urls getUrls() {
        return this.urls;
    }

    /**
     * Get the current user agent, version and minecraft version to be identified by the API. Example:
     * <blockquote><pre>
     * new Agent("LabyAddon", "1.2.0", "1.21");
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
     * @return Returns an instance of {@link PlayerInfo.Cache}
     */
    @NotNull
    public PlayerInfo.Cache<T> getCache() {
        return this.cache;
    }

    /**
     * Get the api handler
     * @return Returns an instance of {@link ApiHandler}
     */
    @NotNull
    public ApiHandler<T> getApiHandler() {
        return this.apiHandler;
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
        String auth = this.getAuthorization();
        if(auth == null) return "";
        return String.format("%s %s", this.getAuthType().getId(), auth);
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
            return this.agent;
        }

        /**
         * Get the agent version
         * @return The agent version
         */
        @NotNull
        public String getAgentVersion() {
            return this.agentVersion;
        }

        /**
         * Get the Minecraft version
         * @return The minecraft version
         */
        @Nullable
        public String getMinecraftVersion() {
            return this.minecraftVersion;
        }

        @Override
        public @NotNull String toString() {
            return String.format(
                    "%s v%s%s",
                    this.agent,
                    this.agentVersion,
                    this.minecraftVersion != null ? " - " + this.minecraftVersion : ""
            );
        }
    }

    /**
     * This class contains all important base urls the API uses.
     */
    public static class Urls {

        /**
         * Get the API base for the GlobalTags API. Implementation note: Please omit the trailing slash
         * @return The Globaltags API base
         */
        @NotNull
        public String getApiBase() {
            return "https://api.globaltags.xyz";
        }

        /**
         * Get the url of a default pre-defined icon
         * @param icon The icon
         * @return The url of the icon
         */
        @NotNull
        public String getDefaultIcon(GlobalIcon icon) {
            return String.format(
                    "https://cdn.rappytv.com/globaltags/icons/%s.png",
                    icon.name().toLowerCase()
            );
        }

        /**
         * Get the icon url of a player role
         * @param role The role
         * @return The url of the icon
         */
        @NotNull
        public String getRoleIcon(String role) {
            return String.format(
                    "https://cdn.rappytv.com/globaltags/icons/role/%s.png",
                    role.toLowerCase()
            );
        }

        /**
         * Get the icon url of a custom player-uploaded icon
         * @param uuid The player's uuid
         * @param hash The icon's hash
         * @return The icon url of a custom player-uploaded icon
         */
        @NotNull
        public String getCustomIcon(UUID uuid, String hash) {
            return String.format(
                    "%s/players/%s/icon/%s",
                    this.getApiBase(),
                    uuid,
                    hash
            );
        }
    }
}
