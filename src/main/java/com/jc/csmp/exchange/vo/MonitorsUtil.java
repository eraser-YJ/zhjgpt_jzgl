package com.jc.csmp.exchange.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonitorsUtil {

    public static List<MonitorsEvent> eventList = new ArrayList<>();
    public  static Map<Long, MonitorsEvent> eventMap = new HashMap<>();

    static {
        eventList.add(new MonitorsEvent(131329L, "视频丢失", XyMonitorsEventType.site_warn));
        eventList.add(new MonitorsEvent(131330L, "视频遮挡", XyMonitorsEventType.site_warn));
        eventList.add(new MonitorsEvent(131331L, "移动侦测", XyMonitorsEventType.site_warn));
        eventList.add(new MonitorsEvent(131612L, "场景变更", XyMonitorsEventType.site_warn));
        eventList.add(new MonitorsEvent(131613L, "虚焦", XyMonitorsEventType.site_warn));
        eventList.add(new MonitorsEvent(589825L, "报警输入", XyMonitorsEventType.site_warn));
        eventList.add(new MonitorsEvent(196355L, "可视域事件", XyMonitorsEventType.site_warn));
        eventList.add(new MonitorsEvent(851969L, "GPS采集", XyMonitorsEventType.site_warn));
        eventList.add(new MonitorsEvent(131588L, "区域入侵", XyMonitorsEventType.area_warn));
        eventList.add(new MonitorsEvent(131585L, "越界侦测", XyMonitorsEventType.area_warn));
        eventList.add(new MonitorsEvent(131586L, "进入区域", XyMonitorsEventType.area_warn));
        eventList.add(new MonitorsEvent(131587L, "离开区域", XyMonitorsEventType.area_warn));
        eventList.add(new MonitorsEvent(131590L, "徘徊侦测", XyMonitorsEventType.work_warn));
        eventList.add(new MonitorsEvent(131593L, "人员聚集", XyMonitorsEventType.person_warn));
        eventList.add(new MonitorsEvent(131592L, "快速移动", XyMonitorsEventType.work_warn));
        eventList.add(new MonitorsEvent(131591L, "停车侦测", XyMonitorsEventType.work_warn));
        eventList.add(new MonitorsEvent(131594L, "物品遗留", XyMonitorsEventType.work_warn));
        eventList.add(new MonitorsEvent(131595L, "物品拿取", XyMonitorsEventType.work_warn));
        eventList.add(new MonitorsEvent(131664L, "人数异常", XyMonitorsEventType.person_warn));
        eventList.add(new MonitorsEvent(131665L, "间距异常", XyMonitorsEventType.person_warn));
        eventList.add(new MonitorsEvent(131596L, "剧烈运动", XyMonitorsEventType.person_warn));
        eventList.add(new MonitorsEvent(131603L, "离岗", XyMonitorsEventType.person_warn));
        eventList.add(new MonitorsEvent(131605L, "倒地", XyMonitorsEventType.person_warn));
        eventList.add(new MonitorsEvent(131597L, "攀高", XyMonitorsEventType.person_warn));
        eventList.add(new MonitorsEvent(131610L, "重点人员起身", XyMonitorsEventType.person_warn));
        eventList.add(new MonitorsEvent(131608L, "如厕超时", XyMonitorsEventType.person_warn));
        eventList.add(new MonitorsEvent(131666L, "人员站立", XyMonitorsEventType.person_warn));
        eventList.add(new MonitorsEvent(131667L, "静坐", XyMonitorsEventType.person_warn));
        eventList.add(new MonitorsEvent(131609L, "防风场滞留", XyMonitorsEventType.person_warn));
        eventList.add(new MonitorsEvent(131598L, "起身", XyMonitorsEventType.person_warn));
        eventList.add(new MonitorsEvent(131599L, "人靠近ATM", XyMonitorsEventType.person_warn));
        eventList.add(new MonitorsEvent(131600L, "操作超时", XyMonitorsEventType.work_warn));
        eventList.add(new MonitorsEvent(131601L, "贴纸条", XyMonitorsEventType.work_warn));
        eventList.add(new MonitorsEvent(131602L, "安装读卡器", XyMonitorsEventType.work_warn));
        eventList.add(new MonitorsEvent(131604L, "尾随", XyMonitorsEventType.person_warn));
        eventList.add(new MonitorsEvent(131606L, "声强突变", XyMonitorsEventType.work_warn));
        eventList.add(new MonitorsEvent(131607L, "折线攀高", XyMonitorsEventType.work_warn));
        eventList.add(new MonitorsEvent(131611L, "折线警戒面", XyMonitorsEventType.work_warn));
        eventList.add(new MonitorsEvent(192518L, "温差报警", XyMonitorsEventType.work_warn));
        eventList.add(new MonitorsEvent(192517L, "温度报警", XyMonitorsEventType.work_warn));
        eventList.add(new MonitorsEvent(192516L, "船只检测", XyMonitorsEventType.site_warn));
        eventList.add(new MonitorsEvent(192515L, "火点检测", XyMonitorsEventType.site_warn));
        eventList.add(new MonitorsEvent(192514L, "烟火检测", XyMonitorsEventType.site_warn));
        eventList.add(new MonitorsEvent(192513L, "烟雾检测", XyMonitorsEventType.site_warn));
        eventList.add(new MonitorsEvent(889196545L, "监控点离线", XyMonitorsEventType.site_warn));

        //事件
        eventList.forEach(item -> {
            eventMap.put(item.getHkCode(), item);
        });
    }
}
