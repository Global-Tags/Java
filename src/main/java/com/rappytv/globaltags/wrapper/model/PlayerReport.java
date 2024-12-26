package com.rappytv.globaltags.wrapper.model;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.UUID;

/**
 * Represents a report associated with a player.
 */
public class PlayerReport {

    private final String id;
    private final String reason;
    private final String reportedTag;
    private final UUID by;
    private final Date createdAt;

    /**
     * Constructs a new {@code PlayerReport}.
     *
     * @param id          The unique identifier for the report.
     * @param reason      The reason for the report.
     * @param reportedTag The tag which the player was reported for
     * @param by          The UUID of the reporting player.
     * @param createdAt   The timestamp when the report was created, in milliseconds since the epoch.
     */
    public PlayerReport(
            @NotNull String id,
            @NotNull String reason,
            @NotNull String reportedTag,
            @NotNull String by,
            long createdAt
    ) {
        this.id = id;
        this.reason = reason;
        this.reportedTag = reportedTag;
        this.by = UUID.fromString(by);
        this.createdAt = new Date(createdAt);
    }

    /**
     * Gets the unique identifier for the report.
     *
     * @return The report's unique identifier.
     */
    @NotNull
    public String getId() {
        return this.id;
    }

    /**
     * Gets the report reason.
     *
     * @return The report reason.
     */
    @NotNull
    public String getReason() {
        return this.reason;
    }

    /**
     * Returns the tag that the player was reported for.
     *
     * @return The reported tag.
     */
    @NotNull
    public String getReportedTag() {
        return this.reportedTag;
    }

    /**
     * Gets the UUID of the author who created the report.
     *
     * @return The author's UUID.
     */
    @NotNull
    public UUID getReportingPlayer() {
        return this.by;
    }

    /**
     * Gets the creation date of the report.
     *
     * @return The date and time when the report was created.
     */
    public Date getCreatedAt() {
        return this.createdAt;
    }

    @Override
    public String toString() {
        return "PlayerReport{" +
                "id='" + this.id + '\'' +
                ", text='" + this.reason + '\'' +
                ", reportedTag='" + this.reportedTag + '\'' +
                ", by=" + this.by +
                ", createdAt=" + this.createdAt +
                '}';
    }
}
