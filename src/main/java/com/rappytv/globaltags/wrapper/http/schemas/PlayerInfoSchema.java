package com.rappytv.globaltags.wrapper.http.schemas;

import com.rappytv.globaltags.wrapper.enums.GlobalPermission;
import com.rappytv.globaltags.wrapper.model.BanInfo;
import com.rappytv.globaltags.wrapper.model.PlayerInfo;

import java.util.List;

public class PlayerInfoSchema {

    public String tag;
    public String position;
    public PlayerInfo.Icon icon;
    public PlayerInfo.ReferralInfo referrals;
    public String roleIcon;
    public boolean hideRoleIcon;
    public List<String> roles;
    public List<GlobalPermission> permissions;
    public BanInfo ban;
}
