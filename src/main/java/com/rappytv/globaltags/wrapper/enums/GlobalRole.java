package com.rappytv.globaltags.wrapper.enums;

import org.jetbrains.annotations.NotNull;

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
