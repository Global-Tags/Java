package com.rappytv.globaltags.wrapper.http;

import com.rappytv.globaltags.wrapper.enums.ConnectionType;

import java.util.UUID;

/**
 * This class contains all routes which the <a href="https://github.com/Global-Tags/API">GlobalTagAPI</a> supports at the moment.
 */
public class Routes {

    /**
     * Cannot be implemented
     */
    private Routes() {}

    /**
     * <pre>
     * Route for
     * - GET /
     * </pre>
     *
     * @return The HTTP route
     * @deprecated Use {@link #getApiInfo()} instead.
     */
    @Deprecated(forRemoval = true)
    public static String getVersion() {
        return "/";
    }

    /**
     * <pre>
     * Route for
     * - GET /
     * </pre>
     *
     * @return The HTTP route
     */
    public static String getApiInfo() {
        return "/";
    }

    /**
     * <pre>
     * Route for
     * - GET /metrics
     * </pre>
     *
     * @return The HTTP route
     */
    public static String getMetrics() {
        return "/metrics";
    }

    /**
     * <pre>
     * Route for
     * - GET /referrals
     * </pre>
     *
     * @return The HTTP route
     */
    public static String getReferralLeaderboards() {
        return "/referrals";
    }

    /**
     * <pre>
     * Route for
     * - GET /players/{uuid}
     * - POST /players/{uuid}
     * - DELETE /players/{uuid}
     * </pre>
     *
     * @param uuid The {@link UUID} of the player you want to get, set or delete the tag of
     * @return The HTTP route
     */
    public static String player(UUID uuid) {
        return "/players/" + uuid;
    }

    /**
     * <pre>
     * Route for
     * - GET /players/{uuid}/history
     * </pre>
     *
     * @param uuid The {@link UUID} of the player you the tag history of
     * @return The HTTP route
     */
    public static String tagHistory(UUID uuid) {
        return "/players/" + uuid + "/history";
    }

    /**
     * <pre>
     * Route for
     * - POST /players/{uuid}/position
     * </pre>
     *
     * @param uuid The {@link UUID} of the player you want to set the position of
     * @return The HTTP route
     */
    public static String setPosition(UUID uuid) {
        return "/players/" + uuid + "/position";
    }

    /**
     * <pre>
     * Route for
     * - POST /players/{uuid}/icon
     * </pre>
     *
     * @param uuid The {@link UUID} of the player you want to set the icon of
     * @return The HTTP route
     */
    public static String setIcon(UUID uuid) {
        return "/players/" + uuid + "/icon";
    }

    /**
     * <pre>
     * Route for
     * - PATCH /players/{uuid}/icon/role-visibility
     * </pre>
     *
     * @param uuid The {@link UUID} of the player you want to update the role icon visibility of
     * @return The HTTP route
     */
    public static String roleIconVisiblity(UUID uuid) {
        return "/players/" + uuid + "/icon/role-visibility";
    }

    /**
     * <pre>
     * Route for
     * - GET /players/{uuid}/watchlist
     * - PATCH /players/{uuid}/watchlist
     * </pre>
     *
     * @param uuid The {@link UUID} of the player you want to get or update the watchlist status of
     * @return The HTTP route
     */
    public static String watchlist(UUID uuid) {
        return "/players/" + uuid + "/watchlist";
    }

    /**
     * <pre>
     * Route for
     * - GET /players/{uuid}/api-keys
     * - POST /players/{uuid}/api-keys
     * </pre>
     *
     * @param uuid The {@link UUID} of the player you want to get or update the watchlist status of
     * @return The HTTP route
     */
    public static String apiKeys(UUID uuid) {
        return "/players/" + uuid + "/api-keys";
    }

    /**
     * <pre>
     * Route for
     * - GET /players/{uuid}/api-keys/{key}
     * - PATCH /players/{uuid}/api-keys/{key}
     * - DELETE /players/{uuid}/api-keys/{key}
     * </pre>
     *
     * @param uuid The {@link UUID} of the player you want to manage the API key of
     * @param key  The ID of the key you want to manage
     * @return The HTTP route
     */
    public static String apiKey(UUID uuid, String key) {
        return "/players/" + uuid + "/api-keys/" + key;
    }

    /**
     * <pre>
     * Route for
     * - POST /players/{uuid}/refer
     * </pre>
     *
     * @param uuid The {@link UUID} of the player you want to refer to
     * @return The HTTP route
     */
    public static String referPlayer(UUID uuid) {
        return "/players/" + uuid + "/referral";
    }

    /**
     * <pre>
     * Route for
     * - GET /players/{uuid}/reports
     * - POST /players/{uuid}/reports
     * </pre>
     *
     * @param uuid The {@link UUID} of the player you want to manage the reports of
     * @return The HTTP route
     */
    public static String playerReports(UUID uuid) {
        return "/players/" + uuid + "/reports";
    }

    /**
     *
     * @deprecated Use {@link #playerReports(UUID)} instead
     * @param uuid The {@link UUID} of the player you want to report
     * @return The HTTP route
     */
    @Deprecated(since = "1.2.1", forRemoval = true)
    public static String reportPlayer(UUID uuid) {
        return playerReports(uuid);
    }

    /**
     * <pre>
     * Route for
     * - POST /players/{uuid}/connections/discord
     * - DELETE /players/{uuid}/connections/discord
     * </pre>
     *
     * @param uuid The {@link UUID} of the player you want to set/delete the connection of
     * @param type The connection type
     * @return The HTTP route
     */
    public static String connection(UUID uuid, ConnectionType type) {
        return "/players/" + uuid + "/connections/" + type.name().toLowerCase();
    }

    /**
     * <pre>
     * Route for
     * - POST /players/{uuid}/connections/email/{code}
     * </pre>
     *
     * @param uuid The {@link UUID} of the player you want to verify the email of
     * @param code The verification code which was received via email
     * @return The HTTP route
     */
    public static String verifyEmail(UUID uuid, String code) {
        return "/players/" + uuid + "/connections/email/" + code;
    }

    /**
     * <pre>
     * Route for
     * - GET /players/{uuid}/bans
     * - POST /players/{uuid}/bans
     * - PATCH /players/{uuid}/bans
     * - DELETE /players/{uuid}/bans
     * </pre>
     *
     * @deprecated Use {@link #bans(UUID)} instead
     * @param uuid The {@link UUID} of the player you want to manage the ban of
     * @return The HTTP route
     */
    @Deprecated(since = "1.2.2")
    public static String ban(UUID uuid) {
        return bans(uuid);
    }

    /**
     * <pre>
     * Route for
     * - GET /players/{uuid}/bans/{id}
     * </pre>
     *
     * @param uuid The {@link UUID} of the player you want to get the ban of
     * @param id The ID of the ban you want to get
     * @return The HTTP route
     */
    public static String ban(UUID uuid, String id) {
        return "/players/" + uuid + "/bans/" + id;
    }

    /**
     * <pre>
     * Route for
     * - GET /players/{uuid}/bans
     * - POST /players/{uuid}/bans
     * - PATCH /players/{uuid}/bans
     * - DELETE /players/{uuid}/bans
     * </pre>
     *
     * @param uuid The {@link UUID} of the player you want to manage the ban of
     * @return The HTTP route
     */
    public static String bans(UUID uuid) {
        return "/players/" + uuid + "/bans";
    }

    /**
     * <pre>
     * Route for
     * - POST /players/{uuid}/ban/appeal
     * </pre>
     *
     * @param uuid The {@link UUID} of the player you want to appeal the ban of
     * @return The HTTP route
     */
    public static String appealBan(UUID uuid) {
        return bans(uuid) + "/appeal";
    }

    /**
     * <pre>
     * Route for
     * - GET /players/{uuid}/notes
     * - POST /players/{uuid}/notes
     * </pre>
     *
     * @param uuid The player you want to get or create notes for
     * @return The HTTP route
     */
    public static String notes(UUID uuid) {
        return "/players/" + uuid + "/notes";
    }

    /**
     * <pre>
     * Route for
     * - GET /players/{uuid}/notes/{id}
     * - DELETE /players/{uuid}/notes/{id}
     * </pre>
     *
     * @param uuid The player you want to get or delete a note for
     * @param id The note ID
     * @return The HTTP route
     */
    public static String note(UUID uuid, String id) {
        return "/players/" + uuid + "/notes/" + id;
    }

    /**
     * <pre>
     * Route for
     * - GET /gift-codes
     * - POST /gift-codes
     * </pre>
     *
     * @return The HTTP route
     */
    public static String giftCodes() {
        return "/gift-codes";
    }

    /**
     * <pre>
     * Route for
     * - GET /gift-codes/{code}
     * - DELETE /gift-codes/{code}
     * </pre>
     *
     * @param code The gift code
     * @return The HTTP route
     */
    public static String giftCode(String code) {
        return "/gift-codes/" + code;
    }

    /**
     * <pre>
     * Route for
     * - POST /gift-codes/{code}/redeem
     * </pre>
     *
     * @param code The gift code
     * @return The HTTP route
     */
    public static String redeemGiftCode(String code) {
        return "/gift-codes/" + code + "/redeem";
    }
}
