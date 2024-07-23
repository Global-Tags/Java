package com.rappytv.globaltags.wrapper.enums;

public enum AuthProvider {
    BEARER("Bearer"),
    YGGDRASIL("Minecraft"),
    LABYCONNECT("LabyConnect");

    private final String id;

    AuthProvider(String id) {
        this.id = id;
    }

    /**
     * Get the internal identifier of the authentication provider
     * @return The internal identifier of the authentication provider
     */
    public String getId() {
        return id;
    }
}
