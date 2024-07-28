package com.rappytv.globaltags.wrapper.enums;

import org.jetbrains.annotations.Nullable;

/**
 * This enum represents all valid icons which the official cdn supports.
 */
public enum GlobalIcon {
    NONE,
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
    STEAM,
    TELEGRAM,
    THREADS,
    TIKTOK,
    TWITCH,
    STATSFM,
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
}
