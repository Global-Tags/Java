package com.rappytv.globaltags.wrapper.enums;

import org.jetbrains.annotations.Nullable;

public enum GlobalIcon {
    NONE,
    BEREAL,
    DISCORD,
    EBIO,
    EPICGAMES,
    GITHUB,
    GITLAB,
    INSTAGRAM,
    KICK,
    PLAYSTATION,
    SNAPCHAT,
    STEAM,
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
}
