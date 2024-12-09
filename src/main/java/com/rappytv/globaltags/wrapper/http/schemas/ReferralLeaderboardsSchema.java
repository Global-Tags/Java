package com.rappytv.globaltags.wrapper.http.schemas;

import com.google.gson.annotations.SerializedName;

public class ReferralLeaderboardsSchema {

    @SerializedName("total")
    public ReferralLeaderboardEntrySchema[] totalLeaderboard;

    @SerializedName("current_month")
    public ReferralLeaderboardEntrySchema[] currentMonthLeaderboard;

    public static class ReferralLeaderboardEntrySchema {

        public String uuid;

        @SerializedName("total_referrals")
        public int totalReferrals;

        @SerializedName("current_month_referrals")
        public int currentMonthReferrals;
    }
}
