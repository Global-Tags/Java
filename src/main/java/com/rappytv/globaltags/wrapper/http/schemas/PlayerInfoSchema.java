package com.rappytv.globaltags.wrapper.http.schemas;

import com.rappytv.globaltags.wrapper.model.PlayerInfo;

public class PlayerInfoSchema {

    public String tag;
    public String position;
    public PlayerInfo.Icon icon;
    public boolean referred;
    public int referrals;
    public String[] roles;
    public String[] permissions;
    public PlayerInfo.Suspension ban;
}
