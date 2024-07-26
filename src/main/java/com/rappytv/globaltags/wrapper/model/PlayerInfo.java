package com.rappytv.globaltags.wrapper.model;

import com.rappytv.globaltags.wrapper.enums.GlobalIcon;
import com.rappytv.globaltags.wrapper.enums.GlobalPosition;
import com.rappytv.globaltags.wrapper.enums.GlobalRole;
import com.rappytv.globaltags.wrapper.GlobalTagsAPI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class PlayerInfo<T> {

    private final UUID uuid;
    private final T tag;
    private final String plainTag;
    private final String position;
    private final String icon;
    private final boolean referred;
    private final int referrals;
    private final List<GlobalRole> roles;
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
     * @param suspension The player's {@link Suspension}
     */
    public PlayerInfo(
            @NotNull GlobalTagsAPI<T> api,
            @NotNull UUID uuid,
            @Nullable String tag,
            @NotNull String position,
            @NotNull String icon,
            boolean referred,
            int referrals,
            @NotNull String[] roles,
            @Nullable Suspension suspension
    ) {
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
        this.suspension = suspension != null ? suspension : new Suspension();
    }

    /**
     * Returns the player's uuid
     */
    @NotNull
    public UUID getUUID() {
        return uuid;
    }

    /**
     * Returns the player's GlobalTag as a colored component
     */
    @Nullable
    public T getTag() {
        return !plainTag.isBlank() ? tag : null;
    }

    /**
     * Returns the player's GlobalTag as a plain string - with color codes
     */
    @NotNull
    public String getPlainTag() {
        return plainTag;
    }

    /**
     * Returns the player's GlobalTag position
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
     * Returns the {@link GlobalIcon} enum value which the player has selected
     */
    @NotNull
    public GlobalIcon getGlobalIcon() {
        try {
            return GlobalIcon.valueOf(icon);
        } catch (Exception ignored) {
            return GlobalIcon.NONE;
        }
    }

    /**
     * Returns the global icon's url of the player. See {@link GlobalIcon#getIconUrl()}
     */
    @Nullable
    public String getIconUrl() {
        return getGlobalIcon().getIconUrl();
    }

    /**
     * Returns if the player is a GlobalTag admin
     */
    public boolean isAdmin() {
        return roles.contains(GlobalRole.ADMIN);
    }

    /**
     * Returns if the player has referred to another player for inviting them
     */
    public boolean hasReferred() {
        return referred;
    }

    /**
     * Returns how many other players were invited by the player
     */
    public int getReferrals() {
        return referrals;
    }

    /**
     * Returns all of the players roles
     */
    @NotNull
    public List<GlobalRole> getRoles() {
        return roles;
    }

    /**
     * Returns the players highest role
     */
    @Nullable
    public GlobalRole getHighestRole() {
        for(GlobalRole role : GlobalRole.values()) {
            if(roles.contains(role)) return role;
        }
        return null;
    }

    /**
     * Shortcut to check if the player suspension is active
     */
    public boolean isSuspended() {
        return suspension.active;
    }

    /**
     * Gets the suspension object from a player
     */
    @NotNull
    public Suspension getSuspension() {
        return suspension;
    }

    @Override
    public String toString() {
        return String.format(
                "Playerinfo{uuid=%s, tag='%s', position='%s', icon='%s', referred=%s, referrals=%s, roles=%s, suspension=%s}",
                uuid,
                plainTag,
                getPosition().name().toLowerCase(),
                getGlobalIcon().name().toLowerCase(),
                referred,
                referrals,
                roles,
                suspension
        );
    }

    /**
     * An object representing a player's ban
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
         * Creates an active suspension
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
            return String.format(
                    "Suspension{active=%s, reason='%s', appealable=%s}",
                    active,
                    reason,
                    appealable
            );
        }
    }

    public static class Cache<T> {

        private final static Timer timer = new Timer();
        private final GlobalTagsAPI<T> api;

        public Cache(GlobalTagsAPI<T> api) {
            this.api = api;
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    api.getCache().clear();
                    if(api.getClientUUID() != null) api.getCache().resolveSelf();
                }
            }, api.getCacheLiveDuration(), api.getCacheLiveDuration());
        }

        private final Map<UUID, PlayerInfo<T>> cache = new HashMap<>();
        private final Set<UUID> resolving = new HashSet<>();

        /**
         * Adds a {@link PlayerInfo} to the cache
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
         * @param uuid The corresponding {@link UUID}
         */
        public void remove(UUID uuid) {
            cache.remove(uuid);
        }

        /**
         * Checks if a specific player is in the cache
         * @param uuid The player's {@link UUID}
         * @return If the player is in the cache
         */
        public boolean has(UUID uuid) {
            return cache.containsKey(uuid);
        }

        /**
         * Gets the cached {@link PlayerInfo} synchronously
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
         * @param consumer A consumer returning the resolved {@link PlayerInfo}
         */
        public void resolveSelf(Consumer<@Nullable PlayerInfo<T>> consumer) {
            resolve(api.getClientUUID(), consumer);
        }

        /**
         * Resolve a specific {@link UUID} into the cache
         */
        public void resolve(UUID uuid) {
            resolve(uuid, (info) -> {});
        }

        /**
         * Resolve a specific {@link UUID} into the cache
         * @param consumer A consumer returning the resolved {@link PlayerInfo}
         */
        public void resolve(UUID uuid, Consumer<@Nullable PlayerInfo<T>> consumer) {
            if(has(uuid)) {
                consumer.accept(get(uuid));
                return;
            }
            if(resolving.contains(uuid)) return;
            resolving.add(uuid);

            api.getApiHandler().getInfo(uuid, (info) -> {
                add(uuid, info);
                resolving.remove(uuid);
                resolve(uuid, consumer);
            });
        }

        /**
         * Clears the cache
         */
        public void clear() {
            cache.clear();
            resolving.clear();
        }
    }
}
