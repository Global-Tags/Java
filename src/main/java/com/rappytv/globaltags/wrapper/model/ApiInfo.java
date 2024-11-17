package com.rappytv.globaltags.wrapper.model;

import org.jetbrains.annotations.NotNull;

/**
 * Represents information about the API, including the version, number of requests, and commit details.
 *
 * @param version the version of the API, must not be null
 * @param requests the number of requests made to the API
 * @param commit the commit data related to the API, must not be null
 */
public record ApiInfo(@NotNull String version, int requests, @NotNull CommitData commit) {

    /**
     * Retrieves the version of the API.
     *
     * @return the API version, never null
     */
    @Override
    @NotNull
    public String version() {
        return version;
    }

    /**
     * Retrieves the number of requests made to the API.
     *
     * @return the number of requests
     */
    @Override
    public int requests() {
        return requests;
    }

    /**
     * Retrieves the commit data associated with the API.
     *
     * @return the commit data, never null
     */
    @Override
    @NotNull
    public CommitData commit() {
        return commit;
    }

    /**
     * Represents data related to a Git commit, including the branch, SHA, and tree information.
     *
     * @param branch the branch associated with the commit, must not be null
     * @param sha    the commit SHA hash, must not be null
     * @param tree   the tree SHA hash, must not be null
     */
    public record CommitData(@NotNull String branch, @NotNull String sha, @NotNull String tree) {

        /**
         * Retrieves the branch associated with this commit.
         *
         * @return the branch name, never null
         */
        @Override
        @NotNull
        public String branch() {
            return branch;
        }

        /**
         * Retrieves the SHA hash of this commit.
         *
         * @return the commit SHA hash, never null
         */
        @Override
        @NotNull
        public String sha() {
            return sha;
        }

        /**
         * Retrieves the tree SHA hash associated with this commit.
         *
         * @return the tree SHA hash, never null
         */
        @Override
        @NotNull
        public String tree() {
            return tree;
        }
    }
}
