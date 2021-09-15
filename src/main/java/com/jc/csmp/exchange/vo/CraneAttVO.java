package com.jc.csmp.exchange.vo;

import com.jc.csmp.doc.common.MechType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CraneAttVO implements IEquiInfoVO {

    @Override
    public String getPk() {
        return "black_box_id";
    }

    public String getName() {
        return "crane_name";
    }

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
        return "crane_alarm_reason";
    }

    @Override
    public String getOnlineStatus() {
        return "online_status";
    }

    @Override
    public String getRunTime() {
        return "crane_time";
    }

    @Override
    public MechType getType() {
        return MechType.tower_crane;
    }

    @Override
    public Map<String, String> getNumKey() {
        Map<String, String> map = new HashMap<>();
        map.put("crane_height","crane_height_num");
        map.put("crane_range","crane_range_num");
        return map;
    }
}
