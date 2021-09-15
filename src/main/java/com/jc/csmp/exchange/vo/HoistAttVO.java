package com.jc.csmp.exchange.vo;

import com.jc.csmp.doc.common.MechType;

import java.util.HashMap;
import java.util.Map;

public class HoistAttVO implements IEquiInfoVO {

    @Override
    public String getPk() {
        return "hoist_box_id";
    }

    public String getName(){return "hoist_name";}

    @Override
    public String getLongitude() {
        return "longitude";
    }

    @Override
    public String getLatitude() {
        return "latitude";
    }

    @Override
    public String getAlarmReason() {
        return "hoist_alarm_reason";
    }

    @Override
    public String getOnlineStatus() {
        return "online_status";
    }

    @Override
    public String getRunTime() {
        return "hoist_time";
    }

    @Override
    public MechType getType() {
        return MechType.building_hoist;
    }
    @Override
    public Map<String, String> getNumKey() {
        Map<String, String> map = new HashMap<>();
        map.put("real_time_height","hoist_height_num");
        return map;
    }
}
