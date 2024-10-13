package com.rappytv.globaltags.wrapper.enums;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * This enum represents all valid icons which the official cdn supports.
 */
public enum GlobalIcon {
    NONE,
    CUSTOM,
    ANDROID,
    APPLE,
    BEREAL,
    CROWN,
    DISCORD,
    DUOLINGO,
    EBIO,
    EPICGAMES,
    GAMESCOM,
    GITHUB,
    GITLAB,
    HEART,
    INSTAGRAM,
    KICK,
    LABYNET,
    PAYPAL,
    PINTEREST,
    PLAYSTATION,
    REDDIT,
    SNAPCHAT,
    SOUNDCLOUD,
    SPOTIFY,
    STAR,
    STATSFM,
    STEAM,
    TELEGRAM,
    THREADS,
    TIKTOK,
    TWITCH,
    X,
    XBOX,
    YOUTUBE;

    private final String iconUrl;

    GlobalIcon() {
        this.iconUrl = String.format(
                "https://cdn.rappytv.com/globaltags/icons/%s.png",
                this.name().toLowerCase()
        );
    }

    /**
     * Get the icon url of the {@link GlobalIcon}
     * @return The icon url or null if the {@link GlobalIcon} equals {@link GlobalIcon#NONE}
     */
    @Nullable
    public String getIconUrl() {
        return this != NONE ? iconUrl : null;
    }

    /**
     * Get the icon url of a custom player-uploaded icon
     * @param uuid The player's uuid
     * @param hash The icon's hash
     * @return The icon url of a custom player-uploaded icon
     */
    @NotNull
    public static String getCustomIconUrl(UUID uuid, String hash) {
        return String.format(
                "https://api.globaltags.xyz/players/%s/icon/%s",
                uuid,
                hash
        );
    }
}
