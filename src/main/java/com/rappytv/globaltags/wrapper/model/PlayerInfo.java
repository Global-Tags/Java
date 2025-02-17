package com.rappytv.globaltags.wrapper.model;

import com.google.gson.annotations.SerializedName;
import com.rappytv.globaltags.wrapper.GlobalTagsAPI;
import com.rappytv.globaltags.wrapper.enums.GlobalIcon;
import com.rappytv.globaltags.wrapper.enums.GlobalPermission;
import com.rappytv.globaltags.wrapper.enums.GlobalPosition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

/**
 * Represents player information associated with a UUID, including tags, roles, permissions, and ban info.
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
    private final ReferralInfo referralInfo;
    private final String roleIcon;
    private final List<String> roles;
    private final Map<GlobalPermission, Boolean> permissions;
    private final BanInfo banInfo;

    /**
     * Build a new PlayerInfo instance
     *
     * @param api          The {@link GlobalTagsAPI} for the {@link GlobalTagsAPI#translateColorCodes(String)} method
     * @param uuid         The player's {@link UUID}
     * @param tag          The player's plain tag including color codes
     * @param position     The player's global position as a string
     * @param icon         The player's global icon as a string
     * @param referralInfo The player's referral info
     * @param roleIcon     The player's role icon
     * @param roles        The player's roles
     * @param permissions  The player's permissions
     * @param banInfo      The player's {@link BanInfo}
     */
    public PlayerInfo(
            @NotNull GlobalTagsAPI<T> api,
            @NotNull UUID uuid,
            @Nullable String tag,
            @NotNull String position,
            @NotNull Icon icon,
            ReferralInfo referralInfo,
            @Nullable String roleIcon,
            @NotNull String[] roles,
            @NotNull String[] permissions,
            @Nullable BanInfo banInfo
    ) {
        this.urls = api.getUrls();
        this.uuid = uuid;
        this.tag = api.translateColorCodes(tag);
        this.plainTag = tag != null ? tag : "";
        this.position = position;
        this.icon = icon;
        this.referralInfo = referralInfo;
        this.roleIcon = roleIcon;
        this.roles = List.of(roles);
        this.permissions = new HashMap<>();
        List<GlobalPermission> playerPermissions = new ArrayList<>();
        for (String permission : permissions) {
            GlobalPermission globalPermission;
            try {
                playerPermissions.add(GlobalPermission.valueOf(permission.toUpperCase()));
            } catch (Exception ignored) {
            }
        }
        for (GlobalPermission permission : GlobalPermission.values()) {
            this.permissions.put(permission, playerPermissions.contains(permission));
        }
        this.banInfo = banInfo;
    }

    /**
     * Gets the player's UUID.
     *
     * @return The player's unique identifier (UUID).
     */
    @NotNull
    public UUID getUUID() {
        return this.uuid;
    }

    /**
     * Gets the player's translated tag, including formatting (e.g., color codes).
     *
     * @return The translated tag as a component, or {@code null} if no tag is set.
     */
    @Nullable
    public T getTag() {
        return !this.plainTag.isBlank() ? this.tag : null;
    }

    /**
     * Gets the player's tag as a plain string, including raw color codes.
     *
     * @return The player's tag as a plain string.
     */
    @NotNull
    public String getPlainTag() {
        return this.plainTag;
    }

    /**
     * Gets the player's tag position as a {@link GlobalPosition} enum.
     *
     * @return The player's tag position. Defaults to {@link GlobalPosition#ABOVE} if invalid.
     */
    @NotNull
    public GlobalPosition getPosition() {
        try {
            return GlobalPosition.valueOf(this.position.toUpperCase());
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
            return GlobalIcon.valueOf(this.icon.type.toUpperCase());
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
        return this.icon.hash;
    }

    /**
     * Checks if the player has a custom global icon.
     *
     * @return {@code true} if the player has a custom global icon; otherwise {@code false}.
     */
    public boolean hasCustomGlobalIcon() {
        return this.getGlobalIcon() == GlobalIcon.CUSTOM && this.icon.hash != null;
    }

    /**
     * Gets the URL for the player's global icon.
     *
     * @return The URL of the player's global icon, either custom or default.
     */
    @NotNull
    public String getIconUrl() {
        if (this.hasCustomGlobalIcon()) return this.urls.getCustomIcon(this.uuid, this.icon.hash);
        return this.urls.getDefaultIcon(this.getGlobalIcon());
    }

    /**
     * Checks if the player has a specific permission.
     *
     * @param permission The permission to check.
     * @return {@code true} if the player has the specified permission; otherwise {@code false}.
     */
    public boolean hasPermission(GlobalPermission permission) {
        return this.permissions.containsKey(permission) && this.permissions.get(permission);
    }

    /**
     * Gets the number of players this player has invited.
     *
     * @return The number of players invited by this player.
     */
    public ReferralInfo getReferralInfo() {
        return this.referralInfo;
    }

    /**
     * Checks if the player has referred another player as their inviter.
     *
     * @return {@code true} if the player has referred another player; otherwise {@code false}.
     */
    public boolean hasReferred() {
        return this.getReferralInfo().hasReferred();
    }

    /**
     * Gets the total number of referrals the player has made.
     *
     * @return The total number of referrals.
     */
    public int getTotalReferrals() {
        return this.getReferralInfo().getTotalReferrals();
    }

    /**
     * Gets the number of referrals the player has made in the current month.
     *
     * @return The number of referrals in the current month.
     */
    public int getCurrentMonthReferrals() {
        return this.getReferralInfo().getCurrentMonthReferrals();
    }

    /**
     * Gets highest displayable role icon IF the player has role icons enabled
     * @return The highest displayable role icon of the player
     */
    @Nullable
    public String getRoleIcon() {
        return this.roleIcon;
    }

    /**
     * Gets all roles assigned to the player.
     *
     * @return A list of the player's roles.
     */
    @NotNull
    public List<String> getRoles() {
        return this.roles;
    }

    /**
     * Gets the player's highest role based on hierarchy.
     *
     * @return The player's highest role, or {@code null} if no roles are assigned.
     */
    @Nullable
    public String getHighestRole() {
        if(this.roles.isEmpty()) return null;
        return this.roles.get(0);
    }

    /**
     * Gets the icon URL for the player's highest role.
     *
     * @deprecated Use {@link #getRoleIcon()} instead
     * @return The role icon URL, or {@code null} if no roles are assigned.
     */
    @Deprecated(since = "1.2.1", forRemoval = true)
    @Nullable
    public String getHighestRoleIcon() {
        String role = this.getHighestRole();
        if (role == null) return null;
        return this.urls.getRoleIcon(role);
    }

    /**
     * Checks if the player is currently banned.
     *
     * @return {@code true} if the player is banned; otherwise {@code false}.
     */
    public boolean isBanned() {
        return this.banInfo != null;
    }

    /**
     * Gets the player's ban details.
     *
     * @return The player's {@link BanInfo} object.
     */
    @NotNull
    public BanInfo getBanInfo() {
        return this.banInfo;
    }

    @Override
    public String toString() {
        return "PlayerInfo{" +
                "uuid=" + this.uuid +
                ", tag=" + this.tag +
                ", plainTag='" + this.plainTag + '\'' +
                ", position=" + this.getPosition() +
                ", icon=PlayerIcon{type=" + this.getGlobalIcon() +
                ", hash=" + this.getGlobalIconHash() + '}' +
                ", referralInfo=" + this.referralInfo +
                ", roleIcon='" + this.roleIcon + '\'' +
                ", roles=" + this.roles +
                ", permissions=" + this.permissions +
                ", banInfo=" + this.banInfo +
                '}';
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

        @Override
        public String toString() {
            return "Icon{" +
                    "type='" + this.type + '\'' +
                    ", hash='" + this.hash + '\'' +
                    '}';
        }
    }

    /**
     * Holds information about a player's referrals.
     */
    public static class ReferralInfo {

        @SerializedName("has_referred")
        private final boolean hasReferred;

        @SerializedName("total_referrals")
        private final int totalReferrals;

        @SerializedName("current_month_referrals")
        private final int currentMonthReferrals;

        /**
         * Builds a new {@link ReferralInfo}
         *
         * @param hasReferred           If the player has already marked someone as their inviter
         * @param totalReferrals        The total number of players the player has invited
         * @param currentMonthReferrals The number of players the player has invited in the current month
         */
        public ReferralInfo(boolean hasReferred, int totalReferrals, int currentMonthReferrals) {
            this.hasReferred = hasReferred;
            this.totalReferrals = totalReferrals;
            this.currentMonthReferrals = currentMonthReferrals;
        }

        /**
         * Checks if the player has already marked someone as their inviter.
         *
         * @return {@code true} if the player has already marked someone as their inviter; otherwise {@code false}.
         */
        public boolean hasReferred() {
            return this.hasReferred;
        }

        /**
         * Gets the total number of players the player has invited.
         *
         * @return The total number of players the player has invited.
         */
        public int getTotalReferrals() {
            return this.totalReferrals;
        }

        /**
         * Gets the number of players the player has invited in the current month.
         *
         * @return The number of players the player has invited in the current month.
         */
        public int getCurrentMonthReferrals() {
            return this.currentMonthReferrals;
        }

        @Override
        public String toString() {
            return "ReferralInfo{" +
                    "hasReferred=" + this.hasReferred +
                    ", totalReferrals=" + this.totalReferrals +
                    ", currentMonthReferrals=" + this.currentMonthReferrals +
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
        private final Map<UUID, PlayerInfo<T>> cache = new HashMap<>();
        private final Set<UUID> resolving = new HashSet<>();

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
            if (options.getCacheClearInterval() > -1) {
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        api.getCache().clear();
                        if (api.getClientUUID() != null) api.getCache().resolveSelf();
                    }
                }, options.getCacheClearInterval(), options.getCacheClearInterval());
            }

            if (options.getCacheRenewInterval() > -1) {
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        api.getCache().renewAll();
                    }
                }, options.getCacheRenewInterval(), options.getCacheRenewInterval());
            }
        }

        /**
         * Adds a {@link PlayerInfo} to the cache
         *
         * @param uuid The corresponding {@link UUID}
         * @param info The {@link PlayerInfo}
         */
        public void add(UUID uuid, PlayerInfo<T> info) {
            this.cache.put(uuid, info);
        }

        /**
         * Removes the {@link GlobalTagsAPI#getClientUUID()} from the cache
         */
        public void removeSelf() {
            this.remove(this.api.getClientUUID());
        }

        /**
         * Removes a {@link PlayerInfo} from the cache
         *
         * @param uuid The corresponding {@link UUID}
         */
        public void remove(UUID uuid) {
            this.cache.remove(uuid);
        }

        /**
         * Checks if a specific player is in the cache
         *
         * @param uuid The player's {@link UUID}
         * @return If the player is in the cache
         */
        public boolean has(UUID uuid) {
            return this.cache.containsKey(uuid);
        }

        /**
         * Gets the cached {@link PlayerInfo} synchronously
         *
         * @param uuid The player's {@link UUID}
         * @return The player's cached {@link PlayerInfo} or null if the info is not cached
         */
        @Nullable
        public PlayerInfo<T> get(UUID uuid) {
            return this.cache.get(uuid);
        }

        /**
         * Resolve the UUID {@link GlobalTagsAPI#getClientUUID()} into the cache
         */
        public void resolveSelf() {
            this.resolveSelf((info) -> {
            });
        }

        /**
         * Resolve the UUID {@link GlobalTagsAPI#getClientUUID()} into the cache
         *
         * @param consumer A consumer returning the resolved {@link PlayerInfo}
         */
        public void resolveSelf(Consumer<@Nullable PlayerInfo<T>> consumer) {
            this.resolve(this.api.getClientUUID(), consumer);
        }

        /**
         * Resolve a specific {@link UUID} into the cache
         *
         * @param uuid The uuid which should be resolved
         */
        public void resolve(UUID uuid) {
            this.resolve(uuid, (info) -> {
            });
        }

        /**
         * Resolve a specific {@link UUID} into the cache
         *
         * @param uuid     The uuid which should be resolved
         * @param consumer A consumer returning the resolved {@link PlayerInfo}
         */
        public void resolve(UUID uuid, Consumer<@Nullable PlayerInfo<T>> consumer) {
            if (this.has(uuid)) {
                consumer.accept(this.get(uuid));
                return;
            }
            this.fetch(uuid, consumer);
        }

        /**
         * Fetches a specific {@link UUID}
         *
         * @param uuid     The uuid which should be fetched
         * @param consumer A consumer returning the resolved {@link PlayerInfo}
         */
        private void fetch(UUID uuid, Consumer<@Nullable PlayerInfo<T>> consumer) {
            if (this.resolving.contains(uuid)) return;
            this.resolving.add(uuid);

            this.api.getApiHandler().getInfo(uuid, (info) -> {
                this.add(uuid, info.getData());
                this.resolving.remove(uuid);
                this.resolve(uuid, consumer);
            });
        }

        /**
         * Renews tag data of {@link GlobalTagsAPI#getClientUUID()}
         */
        public void renewSelf() {
            this.renewSelf((info) -> {
            });
        }

        /**
         * Renews tag data of {@link GlobalTagsAPI#getClientUUID()}
         *
         * @param consumer A consumer returning the renewed {@link PlayerInfo}
         */
        public void renewSelf(Consumer<@Nullable PlayerInfo<T>> consumer) {
            this.renew(this.api.getClientUUID(), consumer);
        }

        /**
         * Renews tag data of a specific uuid
         *
         * @param uuid The uuid which should be renewed
         */
        public void renew(UUID uuid) {
            this.renew(uuid, (info) -> {
            });
        }

        /**
         * Renews tag data of a specific uuid
         *
         * @param uuid     The uuid which should be renewed
         * @param consumer A consumer returning the renewed {@link PlayerInfo}
         */
        public void renew(UUID uuid, Consumer<@Nullable PlayerInfo<T>> consumer) {
            this.fetch(uuid, (info) -> {
                this.cache.put(uuid, info);
                consumer.accept(info);
            });
        }

        /**
         * Renews tag data of all cached uuids
         */
        public void renewAll() {
            for (UUID uuid : this.cache.keySet()) {
                this.renew(uuid);
            }
        }

        /**
         * Clears the cache
         */
        public void clear() {
            this.cache.clear();
            this.resolving.clear();
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
