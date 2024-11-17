package com.rappytv.globaltags.wrapper.model;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Represents an entry in the tag history, including the tag and a list of flagged words associated with it.
 *
 * @param tag the tag associated with this entry
 * @param flaggedWords the list of flagged words associated with this tag
 */
public record TagHistoryEntry(@NotNull String tag, @NotNull List<String> flaggedWords) {

    /**
     * Retrieves the tag associated with this entry.
     *
     * @return the tag, never null
     */
    @Override
    @NotNull
    public String tag() {
        return tag;
    }

    /**
     * Retrieves the list of flagged words associated with this tag entry.
     *
     * @return the list of flagged words, never null
     */
    @Override
    @NotNull
    public List<String> flaggedWords() {
        return flaggedWords;
    }
}
