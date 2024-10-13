package com.rappytv.globaltags.wrapper.enums;

import org.jetbrains.annotations.NotNull;

/**
 * This enum represents all default roles and their icon urls which are configured in the <a href="https://github.com/Global-Tags/API/blob/master/config.json.example">API config file</a>.
 */
public enum GlobalRole {
    ADMIN,
    DEVELOPER,
    MODERATOR,
    PARTNER,
    SUPPORTER;

    private final String iconUrl;

    GlobalRole() {
        this.iconUrl = String.format(
                "https://cdn.rappytv.com/globaltags/icons/role/%s.png",
                name().toLowerCase()
        );
    }

    /**
     * Get the staff icon url of the {@link GlobalRole}
     * @return The staff icon url of the {@link GlobalRole}
     */
    @NotNull
    public String getIconUrl() {
        return iconUrl;
    }
}
