package com.rappytv.globaltags.wrapper.enums;

import org.jetbrains.annotations.NotNull;

/**
 * This enum represents all default roles and their icon urls which are configured <a href="https://github.com/Global-Tags/API/blob/master/config.json.example#L60-L105">here</a>.
 */
public enum GlobalRole {
    ADMIN("purple"),
    DEVELOPER("aqua"),
    MODERATOR("orange"),
    SUPPORTER("green");

    private final String iconUrl;

    GlobalRole(String color) {
        this.iconUrl = String.format(
                "https://cdn.rappytv.com/globaltags/icons/staff/%s.png",
                color
        );
    }

    @NotNull
    public String getIconUrl() {
        return iconUrl;
    }
}
