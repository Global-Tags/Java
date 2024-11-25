package com.rappytv.globaltags.wrapper.model;

import com.rappytv.globaltags.wrapper.enums.GlobalIcon;
import com.rappytv.globaltags.wrapper.enums.GlobalPermission;
import com.rappytv.globaltags.wrapper.enums.GlobalPosition;
import com.rappytv.globaltags.wrapper.enums.GlobalRole;
import com.rappytv.globaltags.wrapper.GlobalTagsAPI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.Timer;
import java.util.function.Consumer;

/**
 * Represents player information associated with a UUID, including tags, roles, permissions, and suspension details.
 *
 * @param <T> The type used by {@link GlobalTagsAPI} to represent colored tags (e.g., formatted strings or components).
 */
@SuppressWarnings("unused")
public class PlayerInfo<T> {

    private final GlobalTagsAPI.Urls urls;
    private final UUID uuid;
    private final T tag;
    private final String plainTag;
    private final String position;
    private final Icon icon;
    private final boolean referred;
    private final int referrals;
    private final List<GlobalRole> roles;
    private final Map<GlobalPermission, Boolean> permissions;
    private final Suspension suspension;

    /**
     * Build a new PlayerInfo instance
     * @param api The {@link GlobalTagsAPI} for the {@link GlobalTagsAPI#translateColorCodes(String)} method
     * @param uuid The player's {@link UUID}
     * @param tag The player's plain tag including color codes
     * @param position The player's global position as a string
     * @param icon The player's global icon as a string
     * @param referred If the player has already marked someone as their inviter
     * @param referrals How many players the player has invited
     * @param roles The player's roles
     * @param permissions The player's permissions
     * @param suspension The player's {@link Suspension}
     */
    public PlayerInfo(
            @NotNull GlobalTagsAPI<T> api,
            @NotNull UUID uuid,
            @Nullable String tag,
            @NotNull String position,
            @NotNull Icon icon,
            boolean referred,
            int referrals,
            @NotNull String[] roles,
            @NotNull String[] permissions,
            @Nullable Suspension suspension
    ) {
        this.urls = api.getUrls();
        this.uuid = uuid;
        this.tag = api.translateColorCodes(tag);
        this.plainTag = tag != null ? tag : "";
        this.position = position;
        this.icon = icon;
        this.referred = referred;
        this.referrals = referrals;
        this.roles = new ArrayList<>();
        for(String role : roles) {
            try {
                this.roles.add(GlobalRole.valueOf(role.toUpperCase()));
            } catch (Exception ignored) {}
        }
        this.permissions = new HashMap<>();
        List<GlobalPermission> playerPermissions = new ArrayList<>();
        for(String permission : permissions) {
            GlobalPermission globalPermission;
            try {
                playerPermissions.add(GlobalPermission.valueOf(permission.toUpperCase()));
            } catch (Exception ignored) {}
        }
        for(GlobalPermission permission : GlobalPermission.values()) {
            this.permissions.put(permission, playerPermissions.contains(permission));
        }
        this.suspension = suspension != null ? suspension : new Suspension();
    }

    /**
     * Gets the player's UUID.
     *
     * @return The player's unique identifier (UUID).
     */
    @NotNull
    public UUID getUUID() {
        return uuid;
    }

    /**
     * Gets the player's translated tag, including formatting (e.g., color codes).
     *
     * @return The translated tag as a component, or {@code null} if no tag is set.
     */
    @Nullable
    public T getTag() {
        return !plainTag.isBlank() ? tag : null;
    }

    /**
     * Gets the player's tag as a plain string, including raw color codes.
     *
     * @return The player's tag as a plain string.
     */
    @NotNull
    public String getPlainTag() {
        return plainTag;
    }

    /**
     * Gets the player's tag position as a {@link GlobalPosition} enum.
     *
     * @return The player's tag position. Defaults to {@link GlobalPosition#ABOVE} if invalid.
     */
    @NotNull
    public GlobalPosition getPosition() {
        try {
            return GlobalPosition.valueOf(position);
        } catch (Exception ignored) {
            return GlobalPosition.ABOVE;
        }
    }

    /**
     * Gets the player's selected {@link GlobalIcon}.
     *
     * @return The selected global icon. Defaults to {@link GlobalIcon#NONE} if invalid.
     */
    @NotNull
    public GlobalIcon getGlobalIcon() {
        try {
            return GlobalIcon.valueOf(icon.type);
        } catch (Exception ignored) {
            return GlobalIcon.NONE;
        }
    }

    /**
     * Gets the hash of the player's custom global icon, if available.
     *
     * @return The custom global icon hash, or {@code null} if none exists.
     */
    @Nullable
    public String getGlobalIconHash() {
        return icon.hash;
    }

    /**
     * Checks if the player has a custom global icon.
     *
     * @return {@code true} if the player has a custom global icon; otherwise {@code false}.
     */
    public boolean hasCustomGlobalIcon() {
        return getGlobalIcon() == GlobalIcon.CUSTOM && icon.hash != null;
    }

    /**
     * Gets the URL for the player's global icon.
     *
     * @return The URL of the player's global icon, either custom or default.
     */
    @NotNull
    public String getIconUrl() {
        if(hasCustomGlobalIcon()) return urls.getCustomIcon(uuid, icon.hash);
        return urls.getDefaultIcon(getGlobalIcon());
    }

    /**
     * Checks if the player has a specific permission.
     *
     * @param permission The permission to check.
     * @return {@code true} if the player has the specified permission; otherwise {@code false}.
     */
    public boolean hasPermission(GlobalPermission permission) {
        return permissions.containsKey(permission) && permissions.get(permission);
    }

    /**
     * Checks if the player has referred another player as their inviter.
     *
     * @return {@code true} if the player has referred another player; otherwise {@code false}.
     */
    public boolean hasReferred() {
        return referred;
    }

    /**
     * Gets the number of players this player has invited.
     *
     * @return The number of players invited by this player.
     */
    public int getReferrals() {
        return referrals;
    }

    /**
     * Gets all roles assigned to the player.
     *
     * @return A list of the player's roles.
     */
    @NotNull
    public List<GlobalRole> getRoles() {
        return roles;
    }

    /**
     * Gets the player's highest role based on hierarchy.
     *
     * @return The player's highest role, or {@code null} if no roles are assigned.
     */
    @Nullable
    public GlobalRole getHighestRole() {
        for(GlobalRole role : GlobalRole.values()) {
            if(roles.contains(role)) return role;
        }
        return null;
    }

    /**
     * Gets the icon URL for the player's highest role.
     *
     * @return The role icon URL, or {@code null} if no roles are assigned.
     */
    @Nullable
    public String getHighestRoleIcon() {
        GlobalRole role = getHighestRole();
        if(role == null) return null;
        return urls.getRoleIcon(role);
    }

    /**
     * Checks if the player is currently suspended.
     *
     * @return {@code true} if the player is suspended; otherwise {@code false}.
     */
    public boolean isSuspended() {
        return suspension.active;
    }

    /**
     * Gets the player's suspension details.
     *
     * @return The player's {@link Suspension} object.
     */
    @NotNull
    public Suspension getSuspension() {
        return suspension;
    }

    @Override
    public String toString() {
        return String.format(
                "Playerinfo{uuid=%s, tag='%s', position='%s', icon='%s', referred=%s, referrals=%s, roles=%s, permissions=%s, suspension=%s}",
                uuid,
                plainTag,
                getPosition().name().toLowerCase(),
                icon,
                referred,
                referrals,
                roles,
                permissions,
                suspension
        );
    }

    /**
     * Represents an icon for a player in the response body.
     */
    public static class Icon {

        private final String type;
        private final String hash;

        /**
         * Creates a new icon.
         *
         * @param type The {@link GlobalIcon} type.
         * @param hash The custom icon hash.
         */
        public Icon(String type, String hash) {
            this.type = type;
            this.hash = hash;
        }
    }

    /**
     * Represents a player's ban or suspension status.
     */
    public static class Suspension {

        private final boolean active;
        private final String reason;
        private final boolean appealable;

        /**
         * Creates an inactive suspension
         */
        public Suspension() {
            this.active = false;
            this.reason = null;
            this.appealable = false;
        }

        /**
         * Creates an active suspension.
         *
         * @param reason The reason for the suspension.
         * @param appealable Whether the suspension is appealable.
         */
        public Suspension(String reason, boolean appealable) {
            this.active = true;
            this.reason = reason;
            this.appealable = appealable;
        }

        /**
         * Returns if the suspension is active or not
         * @return If the suspension is active or not
         */
        public boolean isActive() {
            return active;
        }

        /**
         * Returns the suspension reason
         * @return Returns the suspension reason
         */
        @Nullable
        public String getReason() {
            return reason;
        }

        /**
         * Returns if the suspension can be appealed
         * @return If the suspension can be appealed
         */
        public boolean isAppealable() {
            return appealable;
        }

        @Override
        public String toString() {
            return "Suspension{" +
                    "active=" + active +
                    ", reason='" + reason + '\'' +
                    ", appealable=" + appealable +
                    '}';
        }
    }

    /**
     * Manages caching of {@link PlayerInfo} instances.
     *
     * @param <T> The data type managed by the associated {@link GlobalTagsAPI}.
     */
    public static class Cache<T> {

        private final static Timer timer = new Timer();
        private final GlobalTagsAPI<T> api;

        /**
         * Initializes a cache with default cleanup intervals.
         *
         * @param api The associated {@link GlobalTagsAPI} instance.
         */
        public Cache(GlobalTagsAPI<T> api) {
            this(api, new Options() {
                @Override
                public long getCacheClearInterval() {
                    // Default is 30 minutes
                    return 1000 * 60 * 30;
                }

                @Override
                public long getCacheRenewInterval() {
                    // Default is 5 minutes
                    return 1000 * 60 * 30;
                }
            });
        }

        /**
         * Initializes a cache with custom cleanup intervals.
         *
         * @param api     The associated {@link GlobalTagsAPI} instance.
         * @param options Custom cache interval options.
         */
        public Cache(@NotNull GlobalTagsAPI<T> api, @NotNull Options options) {
            Objects.requireNonNull(api, "api must not be null");
            Objects.requireNonNull(options, "options may not be null");
            this.api = api;
            if(options.getCacheClearInterval() > -1) {
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        api.getCache().clear();
                        if(api.getClientUUID() != null) api.getCache().resolveSelf();
                    }
                }, options.getCacheClearInterval(), options.getCacheClearInterval());
            }

            if(options.getCacheRenewInterval() > -1) {
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        api.getCache().renewAll();
                    }
                }, options.getCacheRenewInterval(), options.getCacheRenewInterval());
            }
        }

        private final Map<UUID, PlayerInfo<T>> cache = new HashMap<>();
        private final Set<UUID> resolving = new HashSet<>();

        /**
         * Adds a {@link PlayerInfo} to the cache
         *
         * @param uuid The corresponding {@link UUID}
         * @param info The {@link PlayerInfo}
         */
        public void add(UUID uuid, PlayerInfo<T> info) {
            cache.put(uuid, info);
        }

        /**
         * Removes the {@link GlobalTagsAPI#getClientUUID()} from the cache
         */
        public void removeSelf() {
            remove(api.getClientUUID());
        }

        /**
         * Removes a {@link PlayerInfo} from the cache
         *
         * @param uuid The corresponding {@link UUID}
         */
        public void remove(UUID uuid) {
            cache.remove(uuid);
        }

        /**
         * Checks if a specific player is in the cache
         *
         * @param uuid The player's {@link UUID}
         * @return If the player is in the cache
         */
        public boolean has(UUID uuid) {
            return cache.containsKey(uuid);
        }

        /**
         * Gets the cached {@link PlayerInfo} synchronously
         *
         * @param uuid The player's {@link UUID}
         * @return The player's cached {@link PlayerInfo} or null if the info is not cached
         */
        @Nullable
        public PlayerInfo<T> get(UUID uuid) {
            return cache.get(uuid);
        }

        /**
         * Resolve the UUID {@link GlobalTagsAPI#getClientUUID()} into the cache
         */
        public void resolveSelf() {
            resolveSelf((info) -> {});
        }

        /**
         * Resolve the UUID {@link GlobalTagsAPI#getClientUUID()} into the cache
         *
         * @param consumer A consumer returning the resolved {@link PlayerInfo}
         */
        public void resolveSelf(Consumer<@Nullable PlayerInfo<T>> consumer) {
            resolve(api.getClientUUID(), consumer);
        }

        /**
         * Resolve a specific {@link UUID} into the cache
         *
         * @param uuid The uuid which should be resolved
         */
        public void resolve(UUID uuid) {
            resolve(uuid, (info) -> {});
        }

        /**
         * Resolve a specific {@link UUID} into the cache
         *
         * @param uuid The uuid which should be resolved
         * @param consumer A consumer returning the resolved {@link PlayerInfo}
         */
        public void resolve(UUID uuid, Consumer<@Nullable PlayerInfo<T>> consumer) {
            if(has(uuid)) {
                consumer.accept(get(uuid));
                return;
            }
            fetch(uuid, consumer);
        }

        /**
         * Fetches a specific {@link UUID}
         *
         * @param uuid The uuid which should be fetched
         * @param consumer A consumer returning the resolved {@link PlayerInfo}
         */
        private void fetch(UUID uuid, Consumer<@Nullable PlayerInfo<T>> consumer) {
            if(resolving.contains(uuid)) return;
            resolving.add(uuid);

            api.getApiHandler().getInfo(uuid, (info) -> {
                add(uuid, info.getData());
                resolving.remove(uuid);
                resolve(uuid, consumer);
            });
        }

        /**
         * Renews tag data of {@link GlobalTagsAPI#getClientUUID()}
         */
        public void renewSelf() {
            renewSelf((info) -> {});
        }

        /**
         * Renews tag data of {@link GlobalTagsAPI#getClientUUID()}
         *
         * @param consumer A consumer returning the renewed {@link PlayerInfo}
         */
        public void renewSelf(Consumer<@Nullable PlayerInfo<T>> consumer) {
            renew(api.getClientUUID(), consumer);
        }

        /**
         * Renews tag data of a specific uuid
         *
         * @param uuid The uuid which should be renewed
         */
        public void renew(UUID uuid) {
            renew(uuid, (info) -> {});
        }

        /**
         * Renews tag data of a specific uuid
         *
         * @param uuid The uuid which should be renewed
         * @param consumer A consumer returning the renewed {@link PlayerInfo}
         */
        public void renew(UUID uuid, Consumer<@Nullable PlayerInfo<T>> consumer) {
            fetch(uuid, (info) -> {
                cache.put(uuid, info);
                consumer.accept(info);
            });
        }

        /**
         * Renews tag data of all cached uuids
         */
        public void renewAll() {
            for(UUID uuid : cache.keySet()) {
                renew(uuid);
            }
        }

        /**
         * Clears the cache
         */
        public void clear() {
            cache.clear();
            resolving.clear();
        }

        /**
         * Interface for custom cache interval options.
         */
        public interface Options {

            /**
             * The interval in which the cache should be cleared automatically in milliseconds. If you want to disable automatic cache cleanup, pass -1.
             *
             * @return The interval in which the cache is being cleared.
             */
            long getCacheClearInterval();

            /**
             * The interval in which the cache should be renewed automatically in milliseconds. If you want to disable the automatic renewal of cache, pass -1.
             *
             * @return The interval in which the cache is being renewed.
             */
            long getCacheRenewInterval();
        }
    }
}
