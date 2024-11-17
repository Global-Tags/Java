package com.rappytv.globaltags.wrapper.model;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.UUID;

/**
 * Represents a note associated with a player.
 */
public class PlayerNote {

    private final String id;
    private final String text;
    private final UUID author;
    private final Date createdAt;

    /**
     * Constructs a new {@code PlayerNote}.
     *
     * @param id        The unique identifier for the note.
     * @param text      The content of the note.
     * @param author    The UUID of the author as a string.
     * @param createdAt The timestamp when the note was created, in milliseconds since the epoch.
     */
    public PlayerNote(
            @NotNull String id,
            @NotNull String text,
            @NotNull String author,
            long createdAt
    ) {
        this.id = id;
        this.text = text;
        this.author = UUID.fromString(author);
        this.createdAt = new Date(createdAt);
    }

    /**
     * Gets the unique identifier for the note.
     *
     * @return The note's unique identifier.
     */
    @NotNull
    public String getId() {
        return id;
    }

    /**
     * Gets the content of the note.
     *
     * @return The note's content.
     */
    @NotNull
    public String getText() {
        return text;
    }

    /**
     * Gets the UUID of the author who created the note.
     *
     * @return The author's UUID.
     */
    @NotNull
    public UUID getAuthor() {
        return author;
    }

    /**
     * Gets the creation date of the note.
     *
     * @return The date and time when the note was created.
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "PlayerNote{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", author=" + author +
                ", createdAt=" + createdAt +
                '}';
    }
}
