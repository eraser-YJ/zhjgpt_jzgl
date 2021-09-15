package com.jc.csmp.doc.common;

/**
 * 增加类型注意到
 * 1.MechType，添加一个类型；
 * 2.js目录下equipment/mechtype目录下加相应的js文件，例如添加报警阀值设定；
 * 3.字典warntype增加一个类型，添加子报警类型；
 * 4.scs_warn_config表增加一条记录，用来配置报警信息展示界面，需要显示的字段；
 * 5.规则管理，增加一个检查规则；
 * @author liubq
 *
 */
public enum MechType {
    tower_crane("塔式起重机"), building_hoist("施工升降机"), monitors("视频监控");
    private String disName;

    MechType(String inName) {
        disName = inName;
    }

    public String getDisName() {
        return disName;
    }

    public static MechType getByCode(String code) {
        try {
            MechType nowStatus = MechType.valueOf(code);
            if (nowStatus != null) {
                return nowStatus;
            }
            return MechType.tower_crane;
        } catch (Exception ex) {
            return MechType.tower_crane;
        }
    }
}
