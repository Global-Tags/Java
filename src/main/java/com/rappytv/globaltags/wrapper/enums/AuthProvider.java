package com.rappytv.globaltags.wrapper.enums;

/**
 * An enum representing the authentication method which will be used by the API. For creating new auth providers see <a href="https://github.com/Global-Tags/API/blob/master/CONTRIBUTING.md#implementing-a-custom-authprovider">CONTRIBUTING.md</a>
 */
public enum AuthProvider {
    /**
     * The Bearer auth provider is used to authenticate with GlobalTag's own Bearer tokens. At the moment the only way to receive one is by contacting the Database admin. See <a href="https://github.com/Global-Tags/API/blob/master/src/auth/providers/ApiKeyProvider.ts">ApiKeyProvider.ts</a>
     */
    BEARER("Bearer"),
    /**
     * The Yggdrasil auth provider is used to authenticate with Minecraft session tokens. These can be obtained by the Minecraft session provider. See <a href="https://github.com/Global-Tags/API/blob/master/src/auth/providers/YggdrasilProvider.ts">YggdrasilProvider.ts</a>
     */
    YGGDRASIL("Minecraft"),
    /**
     * The LabyConnect auth provider is used to authenticate with JWTs of LabyMods auth mecanism LabyConnect. These can be obtained by the LabyConnect session provider. See <a href="https://github.com/Global-Tags/API/blob/master/src/auth/providers/LabyConnectProvider.ts">LabyConnectProvider.ts</a>
     */
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
        return this.id;
    }
}
