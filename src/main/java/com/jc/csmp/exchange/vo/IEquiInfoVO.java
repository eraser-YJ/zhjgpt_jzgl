package com.jc.csmp.exchange.vo;

import com.jc.csmp.doc.common.MechType;

import java.util.List;
import java.util.Map;

public interface IEquiInfoVO {
    String getPk();
    String getName();

    String getLongitude();

    String getLatitude();

    String getAlarmReason();

    String getOnlineStatus();

    String getRunTime();

    MechType getType();

    Map<String, String> getNumKey();



    default boolean isOutLine(String onLineStatus) {
        if (onLineStatus != null) {
            if ("1".equalsIgnoreCase(onLineStatus) || "在线".equalsIgnoreCase(onLineStatus.trim())) {
                return false;
            }
        }
        return true;
    }
}
