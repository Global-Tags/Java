package com.rappytv.globaltags.wrapper.model;

import org.jetbrains.annotations.NotNull;

/**
 * Represents information about the API, including the version, number of requests, and commit details.
 */
public class ApiInfo {

    private final String version;
    private final int requests;
    private final CommitData commit;

    /**
     * Creates a new instance of ApiInfo.
     *
     * @param version the version of the API, must not be null
     * @param requests the number of requests made to the API
     * @param branch the branch associated with the commit, must not be null
     * @param sha the commit SHA hash, must not be null
     * @param tree the tree SHA hash, must not be null
     */
    public ApiInfo(@NotNull String version, int requests, @NotNull String branch, @NotNull String sha, @NotNull String tree) {
        this.version = version;
        this.requests = requests;
        this.commit = new CommitData(branch, sha, tree);
    }

    /**
     * Retrieves the version of the API.
     *
     * @return the API version, never null
     */
    @NotNull
    public String getVersion() {
        return this.version;
    }

    /**
     * Retrieves the number of requests made to the API.
     *
     * @return the number of requests
     */
    public int getRequests() {
        return this.requests;
    }

    /**
     * Retrieves the commit data associated with the API.
     *
     * @deprecated The API does not support these fields anymore
     * @return the commit data, never null
     */
    @NotNull
    @Deprecated(since = "1.2.2", forRemoval = true)
    public CommitData getCommit() {
        return this.commit;
    }

    /**
     * Represents data related to a Git commit, including the branch, SHA, and tree information.
     *
     * @deprecated The API does not support these fields anymore
     */
    @Deprecated(since = "1.2.2", forRemoval = true)
    public static class CommitData {

        private final String branch;
        private final String sha;
        private final String tree;

        /**
         * Creates a new instance of CommitData.
         *
         * @param branch the branch associated with the commit, must not be null
         * @param sha    the commit SHA hash, must not be null
         * @param tree   the tree SHA hash, must not be null
         */
        public CommitData(@NotNull String branch, @NotNull String sha, @NotNull String tree) {
            this.branch = branch;
            this.sha = sha;
            this.tree = tree;
        }

        /**
         * Retrieves the branch associated with this commit.
         *
         * @return the branch name, never null
         */
        @NotNull
        public String getBranch() {
            return this.branch;
        }

        /**
         * Retrieves the SHA hash of this commit.
         *
         * @return the commit SHA hash, never null
         */
        @NotNull
        public String getSha() {
            return this.sha;
        }

        /**
         * Retrieves the tree SHA hash associated with this commit.
         *
         * @return the tree SHA hash, never null
         */
        @NotNull
        public String getTree() {
            return this.tree;
        }
    }
}
