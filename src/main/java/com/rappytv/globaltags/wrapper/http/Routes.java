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
     * @return The HTTP route
     */
    public static String getVersion() {
        return "/";
    }

    /**
     * <pre>
     * Route for
     * - GET /metrics
     * </pre>
     * @return The HTTP route
     */
    public static String getMetrics() {
        return "/metrics";
    }

    /**
     * <pre>
     * Route for
     * - GET /players/{uuid}
     * - POST /players/{uuid}
     * - DELETE /players/{uuid}
     * </pre>
     * @param uuid The {@link UUID} of the player you want to get, set or delete the tag of
     * @return The HTTP route
     */
    public static String player(UUID uuid) {
        return "/players/" + uuid;
    }

    /**
     * <pre>
     * Route for
     * - POST /players/{uuid}/position
     * </pre>
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
     * @param uuid The {@link UUID} of the player you want to set the icon of
     * @return The HTTP route
     */
    public static String setIcon(UUID uuid) {
        return "/players/" + uuid + "/icon";
    }

    /**
     * <pre>
     * Route for
     * - POST /players/{uuid}/watch
     * </pre>
     * @param uuid The {@link UUID} of the player you want to watch
     * @return The HTTP route
     */
    public static String watchPlayer(UUID uuid) {
        return "/players/" + uuid + "/watch";
    }

    /**
     * <pre>
     * Route for
     * - POST /players/{uuid}/unwatch
     * </pre>
     * @param uuid The {@link UUID} of the player you want to unwatch
     * @return The HTTP route
     */
    public static String unwatchPlayer(UUID uuid) {
        return "/players/" + uuid + "/unwatch";
    }

    /**
     * <pre>
     * Route for
     * - POST /players/{uuid}/refer
     * </pre>
     * @param uuid The {@link UUID} of the player you want to refer to
     * @return The HTTP route
     */
    public static String referPlayer(UUID uuid) {
        return "/players/" + uuid + "/referral";
    }

    /**
     * <pre>
     * Route for
     * - POST /players/{uuid}/report
     * </pre>
     * @param uuid The {@link UUID} of the player you want to report
     * @return The HTTP route
     */
    public static String reportPlayer(UUID uuid) {
        return "/players/" + uuid + "/report";
    }

    /**
     * <pre>
     * Route for
     * - POST /players/{uuid}/connections/discord
     * - DELETE /players/{uuid}/connections/discord
     * </pre>
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
     * - GET /players/{uuid}/ban
     * - POST /players/{uuid}/ban
     * - PUT /players/{uuid}/ban
     * - DELETE /players/{uuid}/ban
     * </pre>
     * @param uuid The {@link UUID} of the player you want to manage the ban of
     * @return The HTTP route
     */
    public static String ban(UUID uuid) {
        return "/players/" + uuid + "/ban";
    }

    /**
     * <pre>
     * Route for
     * - POST /players/{uuid}/ban/appeal
     * </pre>
     * @param uuid The {@link UUID} of the player you want to appeal the ban of
     * @return The HTTP route
     */
    public static String appealBan(UUID uuid) {
        return "/players/" + uuid + "/ban/appeal";
    }
}
