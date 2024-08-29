package com.rappytv.globaltags.wrapper.http;

import com.rappytv.globaltags.wrapper.model.PlayerInfo;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * This class is for resolving bodies in {@link ApiRequest}
 */
public class ResponseBody {

    /**
     * Default gson constructor
     */
    public ResponseBody() {}

    // Version
    public String version;

    /**
     * For {@link ApiHandler#getInfo(UUID, Consumer)}
     */
    public String tag;
    public String position;
    public String icon;
    public boolean referred;
    public int referrals;
    public String[] roles;
    public String[] permissions;
    public PlayerInfo.Suspension ban;

    /**
     * For connection requests which provide an auth code
     */
    public String code;

    /**
     * For success messages
     */
    public String message;

    /**
     * For error messages
     */
    public String error;

}
