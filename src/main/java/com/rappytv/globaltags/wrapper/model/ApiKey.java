package com.rappytv.globaltags.wrapper.model;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

public class ApiKey {

    private final String name;
    @SerializedName("created_at")
    private final Date createdAt;
    @SerializedName("last_used")
    private final Date lastUsed;

    /**
     * Constructs a new API key object
     *
     * @param name      The name of the API key
     * @param createdAt When the API key was created
     * @param lastUsed  When the API key was last used
     */
    public ApiKey(@NotNull String name, @NotNull Date createdAt, @Nullable Date lastUsed) {
        this.name = name;
        this.createdAt = createdAt;
        this.lastUsed = lastUsed;
    }

    /**
     * @return the API key name
     */
    @NotNull
    public String getName() {
        return this.name;
    }

    /**
     * @return the API key creation date
     */
    @NotNull
    public Date getCreatedAt() {
        return this.createdAt;
    }

    /**
     * @return the date of the last usage of the API key
     */
    @Nullable
    public Date getLastUsed() {
        return this.lastUsed;
    }

    @Override
    public String toString() {
        return "ApiKey{" +
                "name='" + this.name + '\'' +
                ", createdAt=" + this.createdAt +
                ", lastUsed=" + this.lastUsed +
                '}';
    }
}
