package com.rappytv.globaltags.wrapper.http.schemas;

import com.rappytv.globaltags.wrapper.model.BanInfo;
import com.rappytv.globaltags.wrapper.model.PlayerInfo;

public class PlayerInfoSchema {

    public String tag;
    public String position;
    public PlayerInfo.Icon icon;
    public PlayerInfo.ReferralInfo referrals;
    public String roleIcon;
    public boolean hideRoleIcon;
    public String[] roles;
    public String[] permissions;
    public BanInfo ban;
}
