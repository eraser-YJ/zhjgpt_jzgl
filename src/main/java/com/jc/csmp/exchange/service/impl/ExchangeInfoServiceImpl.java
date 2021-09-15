package com.jc.csmp.exchange.service.impl;

import com.jc.common.def.DefItemVO;
import com.jc.common.def.DefUtil;
import com.jc.common.def.DefVO;
import com.jc.common.kit.vo.ResVO;
import com.jc.csmp.common.enums.EquiWorkStatusEnum;
import com.jc.csmp.common.log.Uog;
import com.jc.csmp.common.mongo.MongoDialect;
import com.jc.csmp.doc.common.MechType;
import com.jc.csmp.equipment.info.domain.EquipmentExinfo;
import com.jc.csmp.equipment.info.domain.EquipmentInfo;
import com.jc.csmp.equipment.info.service.IEquipmentExinfoService;
import com.jc.csmp.equipment.info.service.IEquipmentInfoService;
import com.jc.csmp.exchange.cache.CacheEquiInfo;
import com.jc.csmp.exchange.service.IExchangeInfoService;
import com.jc.csmp.exchange.vo.IEquiInfoVO;
import com.jc.csmp.warn.info.domain.WarnInfo;
import com.jc.csmp.warn.info.service.IWarnInfoService;
import com.jc.foundation.util.JsonUtil;
import com.jc.system.common.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Version 1.0
 */
@Service
public class ExchangeInfoServiceImpl implements IExchangeInfoService {
    //日志
    private Uog log = Uog.getInstanceOnPush();
    //日志
    private Uog log1 = Uog.getInstanceOnMongo();
    @Autowired
    private IWarnInfoService warnInfoService;
    @Autowired
    private IEquipmentInfoService equipmentInfoService;
    @Autowired
    private IEquipmentExinfoService exInfoService;


    /**
     * 添加或更新设备
     *
     * @param mapList
     * @param attVO
     * @return
     * @throws Exception
     */
    public ResVO addDeviceInfoEx(List<Map<String, String>> mapList, IEquiInfoVO attVO) throws Exception {
        if (mapList == null || mapList.size() == 0) {
            return ResVO.buildSuccess("空参数");
        }
        log.debug(JsonUtil.java2Json(mapList));
        List<String> codes = new ArrayList<>();
        String tempCode;
        for (Map<String, String> equiInfo : mapList) {
            tempCode = equiInfo.get(attVO.getPk());
            if (tempCode == null || tempCode.trim().length() <= 0) {
                continue;
            }
            codes.add(tempCode);
        }
        List<EquipmentInfo> dataList = equipmentInfoService.queryByCode(attVO.getType(), codes);
        if (dataList == null || dataList.size() == 0) {
            dataList = new ArrayList<>();
        }
        Map<String, EquipmentInfo> equipmentInfoMap = dataList.stream().collect(Collectors.toMap(item -> item.getEquipmentCode(), item -> item));
        EquipmentInfo info;
        for (Map<String, String> equiInfoMap : mapList) {
            tempCode = equiInfoMap.get(attVO.getPk());
            if (tempCode == null || tempCode.trim().length() <= 0) {
                continue;
            }
            info = equipmentInfoMap.get(tempCode);
            if (info == null) {
                try {
                    //插入数据
                    EquipmentInfo newInfo = new EquipmentInfo();
                    newInfo.setEquipmentType(attVO.getType().toString());
                    newInfo.setEquipmentCode(tempCode);
                    newInfo.setEquipmentName(equiInfoMap.get(attVO.getName()));
                    newInfo.setLatitude(equiInfoMap.get(attVO.getLatitude()));
                    newInfo.setDataStatus("0");
                    newInfo.setWorkStatus(EquiWorkStatusEnum.outline.toString());
                    newInfo.setWarnInfo("{}");
                    //equipmentInfoService.save(newInfo);
                } catch (Exception e) {
                    log.error("插入异常", e);
                }
            }
            //更新数据，已经绑定
            else if ("1".equalsIgnoreCase(info.getDataStatus())) {
                //没有需要更新的数据，暂不更新
                EquipmentInfo newInfo = new EquipmentInfo();
                newInfo.setId(info.getId());
                newInfo.setModifyDateNew(new Date());
                newInfo.setModifyDate(info.getModifyDate());
                newInfo.setLongitude(equiInfoMap.get(attVO.getLongitude()));
                newInfo.setLatitude(equiInfoMap.get(attVO.getLatitude()));
                newInfo.setDataStatus("1");
                equipmentInfoService.update(newInfo);
                //删除
                exInfoService.deleteById(info.getId());
                //插入数据
                String json = JsonUtil.java2Json(equiInfoMap);
                EquipmentExinfo exInfo = new EquipmentExinfo();
                exInfo.setId(info.getId());
                exInfo.setExtStr1(attVO.getType().toString());
                exInfo.setExtStr2(info.getEquipmentCode());
                exInfo.setExtStr3(info.getProjectCode());
                exInfo.setExtDate1(new Date());
                exInfo.setContent(json);
                exInfoService.saveEntity(exInfo);
            }
        }
        return ResVO.buildSuccess("addDeviceInfo");
    }

    /**
     * 更新在线离线状态
     *
     * @param mapList
     * @param attVO
     * @return
     * @throws Exception
     */
    public ResVO updateDeviceOnline(List<Map<String, String>> mapList, IEquiInfoVO attVO) throws Exception {
        if (mapList == null || mapList.size() == 0) {
            return ResVO.buildSuccess("空参数");
        }
        log.debug(JsonUtil.java2Json(mapList));
        List<String> codes = new ArrayList<>();
        String tempCode;
        for (Map<String, String> equiInfo : mapList) {
            tempCode = equiInfo.get(attVO.getPk());
            if (tempCode == null || tempCode.trim().length() <= 0) {
                continue;
            }
            codes.add(tempCode);
        }
        List<EquipmentInfo> dataList = equipmentInfoService.queryUsedByCode(attVO.getType(), codes);
        if (dataList == null || dataList.size() == 0) {
            return ResVO.buildSuccess("提交过来的设备，没有登记，全部跳过");
        }
        Map<String, EquipmentInfo> equipmentInfoMap = dataList.stream().collect(Collectors.toMap(item -> item.getEquipmentCode(), item -> item));
        EquipmentInfo info;
        for (Map<String, String> equiInfoMap : mapList) {
            tempCode = equiInfoMap.get(attVO.getPk());
            if (tempCode == null || tempCode.trim().length() <= 0) {
                continue;
            }
            info = equipmentInfoMap.get(tempCode);
            if (info == null) {
                continue;
            }
            //没有关联过的数据不需要更新
            if ("0".equalsIgnoreCase(info.getDataStatus())) {
                continue;
            }
            EquipmentInfo newInfo = new EquipmentInfo();
            newInfo.setId(info.getId());
            newInfo.setModifyDateNew(new Date());
            newInfo.setModifyDate(info.getModifyDate());
            //是否离线
            if (attVO.isOutLine(equiInfoMap.get(attVO.getOnlineStatus()))) {
                if (!info.getWorkStatus().equalsIgnoreCase(EquiWorkStatusEnum.outline.toString())) {
                    newInfo.setWorkStatus(EquiWorkStatusEnum.outline.toString());
                    log.info(info.getEquipmentCode() + "报警状态updateDeviceAlarm，更新为离线");
                    equipmentInfoService.update(newInfo);
                } else {
                    log.info(info.getEquipmentCode() + "报警状态updateDeviceAlarm，已经为离线，不用更新");
                }
            }
        }
        return ResVO.buildSuccess("updateDeviceAlarm");
    }


    /**
     * 推送实时数据
     *
     * @param mapList
     * @param attVO
     * @return
     * @throws Exception
     */
    public ResVO realDeviceDataEx(List<Map<String, String>> mapList, IEquiInfoVO attVO) throws Exception {
        if (mapList == null || mapList.size() == 0) {
            return ResVO.buildSuccess("空参数");
        }
        String json = JsonUtil.java2Json(mapList);
        log.debug(json);
        Set<String> codes = new HashSet<>();
        String tempCode;
        for (Map<String, String> equiInfo : mapList) {
            tempCode = equiInfo.get(attVO.getPk());
            if (tempCode == null || tempCode.trim().length() <= 0) {
                continue;
            }
            codes.add(tempCode);
        }
        List<EquipmentInfo> dataList = equipmentInfoService.queryUsedByCode(attVO.getType(), new ArrayList<>(codes));
        if (dataList == null || dataList.size() == 0) {
            return ResVO.buildSuccess("提交过来的设备，没有登记，全部跳过");
        }
        //设备类型
        MechType mechType = attVO.getType();
        DefVO def = DefUtil.getXml("displaywarn", mechType.toString());
        Map<String, EquipmentInfo> equipmentInfoMap = dataList.stream().collect(Collectors.toMap(item -> item.getEquipmentCode(), item -> item));
        EquipmentInfo info;
        String alarmReason = null;
        String runTime = null;
        Date runTimeDate = null;

        Map<String, Object> bean = new HashMap<>();
        for (Map<String, String> equiInfoMap : mapList) {
            tempCode = equiInfoMap.get(attVO.getPk());
            alarmReason = equiInfoMap.get(attVO.getAlarmReason());
            runTime = equiInfoMap.get(attVO.getRunTime());
            try {
                runTimeDate = new Date(Long.valueOf(runTime));
            } catch (Exception ex) {
                runTimeDate = new Date();
            }
            if (tempCode == null || tempCode.trim().length() <= 0) {
                continue;
            }
            info = equipmentInfoMap.get(tempCode);
            if (info == null) {
                continue;
            }
            //没有关联过的数据不需要更新
            if ("0".equalsIgnoreCase(info.getDataStatus())) {
                continue;
            }
            for (Map.Entry<String, String> entry : equiInfoMap.entrySet()) {
                bean.put(entry.getKey(), entry.getValue());
            }

            bean.put("runTime", runTimeDate);
            bean.put("equiId", info.getId());
            bean.put("equiCode", info.getEquipmentCode());
            bean.put("equiType", mechType.toString());
            bean.put("alarmReason", alarmReason);
            bean.put("alarmStatus", "N");
            bean.put("warnId", "0000000000");
            //缓存最新数据
            Map<String, Object> bean3 = new HashMap<>();
            bean3.put("id",info.getId());
            bean3.putAll(bean);
            Map<String, String> attMap = attVO.getNumKey();
            if (attMap != null) {
                for (String key : attMap.keySet()) {
                    String value = DefUtil.getLastNum1(bean3.get(key));
                    if (value != null) {
                        bean3.put(attMap.get(key), value);
                    }
                }
            }
            CacheEquiInfo.put(info.getId(), bean3);
            log.info(info.getEquipmentCode() + "缓存数据已经更新");
            log.info("$$$$$$$$$$$$$$$$$$$$$$$$$$$   alarmReason:"+alarmReason);
            //发送警告
            if (alarmReason != null && alarmReason.trim().length() > 0 && !alarmReason.equalsIgnoreCase("0") && !alarmReason.equalsIgnoreCase("正常")) {
                bean.put("alarmStatus", "Y");
                Object value;
                Object newValue;
                //赋值
                Map<String, Object> bean2 = new HashMap<>();
                bean2.putAll(bean);
                for (DefItemVO item : def.getItemList()) {
                    if (item.getTargetCode() == null) {
                        continue;
                    }
                    value = equiInfoMap.get(item.getTargetCode());
                    newValue = DefUtil.getItemShowValue(item, value);
                    if (newValue != null) {
                        bean2.put(item.getItemCode(), DefUtil.getItemRealValue(item, value));
                    }
                }
                //转换为对象
                WarnInfo warnInfo = BeanUtil.map2Object(bean2, WarnInfo.class);
                warnInfo.setTargetType(mechType.toString());
                warnInfo.setTargetId(info.getId());
                warnInfo.setWarnTime(runTimeDate);
                warnInfo.setTargetCode(info.getEquipmentCode());
                warnInfo.setTargetProjectCode(info.getProjectCode());
                warnInfo.setTargetProjectName(info.getProjectName());
                warnInfo.setWarnReason(alarmReason);
                warnInfoService.saveEntity(warnInfo);
                bean.put("warnId", warnInfo.getId());
                //修改设备状态
                if (!EquiWorkStatusEnum.warn.toString().equalsIgnoreCase(info.getWorkStatus())) {
                    equipmentInfoService.updateEquiStatus(info.getId(), EquiWorkStatusEnum.warn);
                    log.info(info.getEquipmentCode() + "报警状态realDeviceDataEx，已经为报警");
                } else {
                    log.info(info.getEquipmentCode() + "报警状态realDeviceDataEx，已经为报警，不需要更新");
                }
            } else {
                //修改设备状态
                if (!EquiWorkStatusEnum.normarl.toString().equalsIgnoreCase(info.getWorkStatus())) {
                    equipmentInfoService.updateEquiStatus(info.getId(), EquiWorkStatusEnum.normarl);
                    log.info(info.getEquipmentCode() + "报警状态realDeviceDataEx，已经为正常");
                } else {
                    log.info(info.getEquipmentCode() + "报警状态realDeviceDataEx，已经为正常，不需要更新");
                }
            }
            //保存实时数据
            log.info(info.getEquipmentCode() + "插入MongoDB 开始");
            log1.error(info.getEquipmentCode() + "插入MongoDB 开始");
            try{
                log1.debug(json);
                MongoDialect dialect = new MongoDialect();
                dialect.appendSensorRecord(bean);
                log.info(info.getEquipmentCode() + "插入MongoDB 结束");
                log1.error(info.getEquipmentCode() + "插入MongoDB 结束");
            }catch (Exception ex){
                ex.printStackTrace();
                log.error(info.getEquipmentCode() + "插入MongoDB 异常",ex);
                log1.error(info.getEquipmentCode() + "插入MongoDB 异常",ex);
            }
        }
        return ResVO.buildSuccess();
    }
}

