package com.rappytv.globaltags.wrapper.model;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents an entry in the tag history, including the tag and a list of flagged words associated with it.
 */
public class TagHistoryEntry {

    private final String tag;
    private final List<String> flaggedWords;

    /**
     * Creates a new TagHistoryEntry instance.
     *
     * @param tag the tag associated with this entry
     * @param flaggedWords the list of flagged words associated with this tag
     */
    public TagHistoryEntry(@NotNull String tag, @NotNull String[] flaggedWords) {
        this.tag = tag;
        this.flaggedWords = List.of(flaggedWords);
    }

    /**
     * Retrieves the tag associated with this entry.
     *
     * @return the tag, never null
     */
    @NotNull
    public String getTag() {
        return tag;
    }

    /**
     * Retrieves the list of flagged words associated with this tag entry.
     *
     * @return the list of flagged words, never null
     */
    @NotNull
    public List<String> getFlaggedWords() {
        return flaggedWords;
    }
}
