package com.rappytv.globaltags.wrapper.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * An object representing gift codes
 */
public class GiftCode {

    private final String name;
    private final String code;
    private final List<UUID> uses;
    @SerializedName("max_uses")
    private final int maxUses;
    private final Gift gift;
    @SerializedName("created_at")
    private final Date createdAt;
    @SerializedName("expires_at")
    private final Date expiresAt;

    /**
     * Constructs a new GiftCode object
     * @param name The name of the gift code
     * @param code The code itself
     * @param uses A list of players who used the code
     * @param maxUses The maximum amount of uses
     * @param gift The reward object
     * @param createdAt When the code was created
     * @param expiresAt When the code expires
     */
    public GiftCode(String name, String code, List<UUID> uses, int maxUses, Gift gift, Date createdAt, Date expiresAt) {
        this.name = name;
        this.code = code;
        this.uses = uses;
        this.maxUses = maxUses;
        this.gift = gift;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    /**
     * @return the gift code name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the gift code
     */
    public String getCode() {
        return this.code;
    }

    /**
     * @return the list of players who used the code
     */
    public List<UUID> getUses() {
        return this.uses;
    }

    /**
     * @return the maximum amount of uses
     */
    public int getMaxUses() {
        return this.maxUses;
    }

    /**
     * @return the gift code reward
     */
    public Gift getGift() {
        return this.gift;
    }

    /**
     * @return when the code was created
     */
    public Date getCreatedAt() {
        return this.createdAt;
    }

    /**
     * @return when the code expires
     */
    public Date getExpiresAt() {
        return this.expiresAt;
    }

    @Override
    public String toString() {
        return "GiftCode{" +
                "name='" + this.name + '\'' +
                ", code='" + this.code + '\'' +
                ", uses=" + this.uses +
                ", maxUses=" + this.maxUses +
                ", gift=" + this.gift +
                ", createdAt=" + this.createdAt +
                ", expiresAt=" + this.expiresAt +
                '}';
    }

    /**
     * An object representing gift code rewards
     */
    public static class Gift {

        private final String type;
        private final String value;
        private final long duration;

        /**
         * Constructs a new Gift object
         * @param type The gift type
         * @param value The gift value
         * @param duration How long the gift should last
         */
        public Gift(String type, String value, long duration) {
            this.type = type;
            this.value = value;
            this.duration = duration;
        }

        /**
         * @return the gift type
         */
        public String getType() {
            return this.type;
        }

        /**
         * @return the gift value
         */
        public String getValue() {
            return this.value;
        }

        /**
         * @return the gift duration
         */
        public long getDuration() {
            return this.duration;
        }

        @Override
        public String toString() {
            return "Gift{" +
                    "type='" + this.type + '\'' +
                    ", value='" + this.value + '\'' +
                    ", duration=" + this.duration +
                    '}';
        }
    }
}
