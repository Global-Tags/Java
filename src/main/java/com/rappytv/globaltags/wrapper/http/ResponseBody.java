package com.rappytv.globaltags.wrapper.http;

import com.rappytv.globaltags.wrapper.model.PlayerInfo;

public class ResponseBody {

    // Version
    public String version;

    // GetInfo
    public String tag;
    public String position;
    public String icon;
    public boolean referred;
    public int referrals;
    public String[] roles;
    public PlayerInfo.Suspension suspension;

    // Connections
    public String code;

    // Success
    public String message;

    // Error
    public String error;

}
