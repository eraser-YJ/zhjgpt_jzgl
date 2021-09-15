package com.jc.csmp.warn.info.web;

import com.jc.csmp.common.enums.EquiWorkStatusEnum;
import com.jc.csmp.common.groovy.GroovyUtil;
import com.jc.csmp.common.log.Uog;
import com.jc.csmp.common.mongo.MongoDialect;
import com.jc.csmp.common.tool.CacheManager;
import com.jc.csmp.common.tool.RuleContext;
import com.jc.csmp.equipment.info.domain.EquipmentInfo;
import com.jc.csmp.equipment.info.service.IEquipmentInfoService;
import com.jc.csmp.warn.info.domain.WarnInfo;
import com.jc.csmp.warn.info.service.IWarnInfoService;
import com.jc.csmp.warn.rule.domain.WarnRule;
import com.jc.csmp.warn.rule.service.IWarnRuleService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.foundation.web.BaseController;
import com.jc.system.dic.IDicManager;
import com.jc.system.dic.domain.Dic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Version 1.0
 */
@Controller
@RequestMapping(value = "/equi/sensor/")
public class EquiSensorInfoController extends BaseController {
    //日志
    private static Uog log = Uog.getInstanceOnRule();
    @Autowired
    private IEquipmentInfoService equipmentInfoService;
    @Autowired
    private IWarnInfoService warnInfoService;
    @Autowired
    private IWarnRuleService warnRuleService;

    @RequestMapping(value = "recv.action", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    @SuppressWarnings("rawtypes")
    public Map<String, Object> recv(HttpServletRequest request) throws Exception {
        MongoDialect dialect = new MongoDialect();
        Map<String, Object> resultMap = new HashMap<>(2);
        String str = request.getParameter("content");

        Map<Object, Object> inMap = (Map<Object, Object>) JsonUtil.json2Java(str, Map.class);
        Map<String, Object> paramMap = new HashMap<>();
        if (inMap == null || inMap.size() == 0) {
            resultMap.put("code", "0001");

        } else {
            for (Map.Entry<Object, Object> entry : inMap.entrySet()) {
                paramMap.put(entry.getKey().toString(), entry.getValue());
            }
        }
        dialect.appendSensorRecord(paramMap);
        return resultMap;
    }

    @RequestMapping(value = "test.action", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    @SuppressWarnings("rawtypes")
    public Map<String, Object> recv1(HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = new HashMap<>(2);
        try {
            Map<String, Object> paramMap = new HashMap<>(0);
            paramMap.put("equiId", "ac9a7434d4154b49a8aa1ce5c77a8361");
            paramMap.put("actWeigth", 1.12);
            paramMap.put("weigthRate", 90.1);
            paramMap.put("actPersonNum", 5);
            paramMap.put("actHeigth", 8.12);
            paramMap.put("heigthRate", 99);
            paramMap.put("actSpeed", 2000);
            paramMap.put("actAngle", 12);
            paramMap.put("angleRate", 60);
            //保存检查
            saveAndCheck(paramMap);
            resultMap.put("code", "0000");
            resultMap.put("message", "保存成功");
            return resultMap;
        } catch (Exception ex) {
            resultMap.put("code", "0001");
            resultMap.put("message", ex.getMessage());
            return resultMap;
        }

    }

    /**
     * 保存检查
     *
     * @param paraMap
     * @throws Exception
     */
    private void saveAndCheck(Map<String, Object> paraMap) throws Exception {
        Date now = new Date();
        long start = System.currentTimeMillis();
        IDicManager dicManager = SpringContextHolder.getBean(IDicManager.class);
        try {
            if (paraMap == null || paraMap.size() == 0) {
                log.error("参数错误");
                throw new Exception("参数错误");
            }
            String equikey = (String) paraMap.get("equiId");
            if (equikey == null || equikey.trim().length() <= 0) {
                log.error("参数错误");
                throw new Exception("参数错误");
            }
            //检查
            EquipmentInfo entityCond = new EquipmentInfo();
            entityCond.setId(equikey);
            EquipmentInfo info = equipmentInfoService.get(entityCond);
            if (info == null) {
                log.error(equikey + "该设备不在监控范围");
                throw new Exception("该设备不在监控范围");
            }
            paraMap.put("runTime", now);
            //保存
            MongoDialect dialect = new MongoDialect();
            dialect.appendSensorRecord(paraMap);
            //取得标准
            Map<String, Object> stdMap = getStdMap(info);
            log.info(now.getTime() + "查询基础数据耗时：" + (System.currentTimeMillis() - start));
            start = System.currentTimeMillis();
            //取得规则
            WarnRule rule = getRule(info);
            if (rule != null) {
                //执行脚本
                log.info(now.getTime() + "执行脚本开始");
                Object value = GroovyUtil.action(rule.getRuleTxt(), paraMap, stdMap);
                log.info(now.getTime() + "执行脚本耗时：" + (System.currentTimeMillis() - start));
                start = System.currentTimeMillis();
                WarnInfo warnInfo = (WarnInfo) value;
                //发生一次正常执行
                if (warnInfo != null && warnInfo.getWarnReasonCode() != null && warnInfo.getWarnReasonCode().length() > 0) {
                    warnInfo.setWarnStatus("init");
                    warnInfo.setTargetType(info.getEquipmentType());
                    warnInfo.setTargetId(info.getId());
                    warnInfo.setTargetCode(info.getEquipmentCode());
                    warnInfo.setTargetProjectCode(info.getProjectCode());
                    warnInfo.setTargetProjectName(info.getProjectName());
                    warnInfo.setWarnTime(new Date());
                    if (warnInfo.getWarnReasonCode() != null) {
                        Dic dic = dicManager.getDic(info.getEquipmentType(), "warntype", warnInfo.getWarnReasonCode());
                        if (dic == null) {
                            warnInfo.setWarnReasonCode("10000");
                            warnInfo.setWarnReasonCodeValue("未定义报警类型");
                        } else {
                            warnInfo.setWarnReasonCode(dic.getCode());
                            warnInfo.setWarnReasonCodeValue(dic.getValue());
                        }
                    } else {
                        warnInfo.setWarnReasonCode("10000");
                        warnInfo.setWarnReasonCodeValue("未定义报警类型");
                    }

                    warnInfoService.save(warnInfo);
                    log.info(now.getTime() + "保存警告耗时：" + (System.currentTimeMillis() - start));
                    start = System.currentTimeMillis();
                    //修改设备状态
                    if (!EquiWorkStatusEnum.warn.toString().equalsIgnoreCase(info.getWorkStatus())) {
                        equipmentInfoService.updateEquiStatus(info.getId(), EquiWorkStatusEnum.warn);
                        log.info(now.getTime() + "更新主表报警状态：报警，耗时：" + (System.currentTimeMillis() - start));
                    } else {
                        log.info(now.getTime() + "主表报警状态，已经为报警，不需要更新");
                    }

                } else {
                    //修改设备状态
                    if (!EquiWorkStatusEnum.normarl.toString().equalsIgnoreCase(info.getWorkStatus())) {
                        equipmentInfoService.updateEquiStatus(info.getId(), EquiWorkStatusEnum.normarl);
                        log.info(now.getTime() + "更新主表报警状态:正常，耗时：" + (System.currentTimeMillis() - start));
                    } else {
                        log.info(now.getTime() + "主表报警状态，已经为正常，不需要更新");
                    }
                }
            } else {
                log.info(now.getTime() + "," + info.getEquipmentType() + "没有定义规则，不进行检查");
            }
        } catch (Exception ex1) {
            log.error(now.getTime() + "保存并检查异常：" + ex1.getMessage(), ex1);
        }
    }


    /**
     * 取得规则
     *
     * @param info
     * @return
     * @throws CustomException
     */
    private WarnRule getRule(EquipmentInfo info) throws CustomException {
        WarnRule rule = RuleContext.get(info.getEquipmentType());
        if (rule == null) {
            rule = warnRuleService.getByCode(info.getEquipmentType());
            RuleContext.put(rule.getRuleCode(), rule);
        }
        return rule;
    }

    //标准缓存
    private CacheManager<Map<String, Object>> stdCache = new CacheManager<Map<String, Object>>();

    /**
     * 取得标准
     *
     * @param info
     * @return
     */
    private Map<String, Object> getStdMap(EquipmentInfo info) {
        if (info.getWarnInfo() == null || info.getWarnInfo().length() <= 0) {
            return new HashMap<>();
        }
        Map<String, Object> stdMap = stdCache.get(info.getWarnInfo());
        if (stdMap == null) {
            //检查
            stdMap = (Map<String, Object>) JsonUtil.json2Java(info.getWarnInfo(), Map.class);
            if (stdMap == null) {
                stdMap = new HashMap<>();
            }
            stdCache.put(info.getWarnInfo(), stdMap);
        }
        return stdMap;
    }

}

