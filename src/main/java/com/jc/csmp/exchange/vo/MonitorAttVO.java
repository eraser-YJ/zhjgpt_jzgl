package com.jc.csmp.exchange.vo;

import com.jc.csmp.doc.common.MechType;

import java.util.HashMap;
import java.util.Map;

public class MonitorAttVO implements IEquiInfoVO {

    @Override
    public String getPk() {
        return "equiCode";
    }

    @Override
    public String getName() {
        return "equiName";
    }

    @Override
    public String getLongitude() {
        return "longitude";
    }

    @Override
    public String getLatitude() {
        return "";
    }

    @Override
    public String getAlarmReason() {
        return "eventXyName";
    }

    @Override
    public String getOnlineStatus() {
        return "online_status";
    }

    @Override
    public String getRunTime() {
        return "event_time";
    }

    @Override
    public MechType getType() {
        return MechType.monitors;
    }

    @Override
    public Map<String, String> getNumKey() {
        Map<String, String> map = new HashMap<>();

        return map;
    }
}
