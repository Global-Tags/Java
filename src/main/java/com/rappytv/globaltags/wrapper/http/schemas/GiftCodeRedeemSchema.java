package com.rappytv.globaltags.wrapper.http.schemas;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

public class GiftCodeRedeemSchema extends MessageSchema {

    @SerializedName("expires_at")
    private final Date expiresAt;

    /**
     * @param expiresAt When the gift expires
     */
    public GiftCodeRedeemSchema(@Nullable Date expiresAt) {
        this.expiresAt = expiresAt;
    }

    /**
     * @return the gift expiration date
     */
    @Nullable
    public Date getExpiresAt() {
        return this.expiresAt;
    }

    @Override
    public String toString() {
        return "GiftCodeRedeemSchema{" +
                "expiresAt=" + this.expiresAt +
                '}';
    }
}
