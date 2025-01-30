package com.rappytv.globaltags.wrapper.model;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.UUID;

/**
 * Represents a player ban object
 */
public class BanInfo {

    private final boolean appealable;
    private final boolean appealed;
    @SerializedName("banned_at")
    private final Date bannedAt;
    @SerializedName("expires_at")
    private final Date expiresAt;
    private final String id;
    private final String reason;
    private final UUID staff;

    /**
     * Creates a new ban object
     * @param appealable If the ban is appealable by the player
     * @param appealed If the ban was already appealed by the player
     * @param bannedAt When the player was banned
     * @param expiresAt When the ban expires
     * @param id The ban ID
     * @param reason The ban reason
     * @param staff The staff member who executed the ban
     */
    public BanInfo(
            boolean appealable,
            boolean appealed,
            @NotNull Date bannedAt,
            @Nullable Date expiresAt,
            @NotNull String id,
            @NotNull String reason,
            @NotNull UUID staff
    ) {
        this.appealable = appealable;
        this.appealed = appealed;
        this.bannedAt = bannedAt;
        this.expiresAt = expiresAt;
        this.id = id;
        this.reason = reason;
        this.staff = staff;
    }

    /**
     * @return if the ban is appealable by the player
     */
    public boolean isAppealable() {
        return this.appealable;
    }

    /**
     * @return if the player has already appealed the ban
     */
    public boolean isAppealed() {
        return this.appealed;
    }

    /**
     * @return when the player was banned
     */
    public Date getBannedAt() {
        return this.bannedAt;
    }

    /**
     * @return when the ban expires, or null on permanent bans
     */
    @Nullable
    public Date getExpiresAt() {
        return this.expiresAt;
    }

    /**
     * @return the ban ID
     */
    @NotNull
    public String getId() {
        return this.id;
    }

    /**
     * @return the ban reason
     */
    @NotNull
    public String getReason() {
        return this.reason;
    }

    /**
     * @return The UUID of the staff member who executed the ban
     */
    @NotNull
    public UUID getStaff() {
        return this.staff;
    }

    @Override
    public String toString() {
        return "BanInfo{" +
                "appealable=" + this.appealable +
                ", appealed=" + this.appealed +
                ", bannedAt=" + this.bannedAt +
                ", expiresAt=" + this.expiresAt +
                ", id='" + this.id + '\'' +
                ", reason='" + this.reason + '\'' +
                ", staff=" + this.staff +
                '}';
    }
}
