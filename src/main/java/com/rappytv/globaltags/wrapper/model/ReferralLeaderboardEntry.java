package com.rappytv.globaltags.wrapper.model;

import java.util.UUID;

/**
 * A class representing an entry in a referral leaderboard
 */
public class ReferralLeaderboardEntry {

    private final int rank;
    private final UUID uuid;
    private final int totalReferrals;
    private final int currentMonthReferrals;

    /**
     * Creates a new entry in the referral leaderboard
     *
     * @param rank                  The rank of the entry
     * @param uuid                  The UUID of the player
     * @param totalReferrals        The total number of referrals
     * @param currentMonthReferrals The number of referrals in the current month
     */
    public ReferralLeaderboardEntry(int rank, UUID uuid, int totalReferrals, int currentMonthReferrals) {
        this.rank = rank;
        this.uuid = uuid;
        this.totalReferrals = totalReferrals;
        this.currentMonthReferrals = currentMonthReferrals;
    }

    /**
     * Gets the rank of the entry
     *
     * @return The rank
     */
    public int getRank() {
        return this.rank;
    }

    /**
     * Gets the UUID of the player
     *
     * @return The UUID
     */
    public UUID getUUID() {
        return this.uuid;
    }

    /**
     * Gets the total number of referrals
     *
     * @return The total number of referrals
     */
    public int getTotalReferrals() {
        return this.totalReferrals;
    }

    /**
     * Gets the number of referrals in the current month
     *
     * @return The number of referrals in the current month
     */
    public int getCurrentMonthReferrals() {
        return this.currentMonthReferrals;
    }

    @Override
    public String toString() {
        return "ReferralLeaderboardEntry{" +
                "rank=" + this.rank +
                ", uuid=" + this.uuid +
                ", totalReferrals=" + this.totalReferrals +
                ", currentMonthReferrals=" + this.currentMonthReferrals +
                '}';
    }
}
