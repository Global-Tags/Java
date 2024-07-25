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

    public PlayerInfo(
            GlobalTagsAPI<T> api,
            UUID uuid,
            String tag,
            String position,
            String icon,
            boolean referred,
            int referrals,
            String[] roles,
            Suspension suspension
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
        this.suspension = suspension;
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
        return !plainTag.isEmpty() ? tag : null;
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
         */
        public boolean isActive() {
            return active;
        }

        /**
         * Returns the suspension reason
         */
        @Nullable
        public String getReason() {
            return reason;
        }

        /**
         * Returns if the suspension can be appealed
         */
        public boolean isAppealable() {
            return appealable;
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

        public void add(UUID uuid, PlayerInfo<T> info) {
            cache.put(uuid, info);
        }
        public void remove(UUID uuid) {
            cache.remove(uuid);
        }
        public boolean has(UUID uuid) {
            return cache.containsKey(uuid);
        }
        public PlayerInfo<T> get(UUID uuid) {
            return cache.get(uuid);
        }
        public void resolveSelf() {
            resolveSelf((info) -> {});
        }
        public void resolveSelf(Consumer<PlayerInfo<T>> consumer) {
            resolve(api.getClientUUID(), consumer);
        }
        public void resolve(UUID uuid) {
            resolve(uuid, (info) -> {});
        }
        public void resolve(UUID uuid, Consumer<PlayerInfo<T>> consumer) {
            if(has(uuid)) {
                consumer.accept(get(uuid));
                return;
            }
            if(resolving.contains(uuid)) return;
            resolving.add(uuid);

            api.getApiHandler().getInfo((info) -> {
                add(uuid, info);
                resolving.remove(uuid);
                resolve(uuid, consumer);
            });
        }
        public void clear() {
            cache.clear();
        }
    }
}
