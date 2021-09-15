package com.jc.mobile.web;

import com.jc.common.kit.DateUtil;
import com.jc.common.kit.vo.ResVO;
import com.jc.csmp.doc.common.MechType;
import com.jc.csmp.doc.project.domain.ScsProjectInfo;
import com.jc.csmp.doc.project.service.IScsProjectInfoService;
import com.jc.csmp.equipment.info.domain.EquipmentInfo;
import com.jc.csmp.equipment.info.service.IEquipmentInfoService;
import com.jc.csmp.exchange.adapter.HkPorxy;
import com.jc.csmp.exchange.cache.CacheEquiInfo;
import com.jc.csmp.warn.info.dao.IRealtimeInfoDao;
import com.jc.csmp.warn.info.domain.WarnInfo;
import com.jc.csmp.warn.info.domain.WarnStatisInfo;
import com.jc.csmp.warn.info.service.IWarnInfoService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.foundation.util.StringUtil;
import com.jc.mobile.basic.web.MobileController;
import com.jc.mobile.util.MobileApiResponse;
import com.jc.system.security.domain.User;
import com.jc.system.security.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author liubq
 * @Date 2020/8/3 16:13
 * @Version 1.0
 */
@Controller
@RequestMapping(value = "/mobile/warn")
public class MobileWarnController extends MobileController {
    @Autowired
    private IWarnInfoService warnInfoService;
    @Autowired
    private IEquipmentInfoService equipmentInfoService;
    @Autowired
    private IRealtimeInfoDao dao;
    @Autowired
    private IScsProjectInfoService projectInfoService;

    private String smartsitStart = GlobalContext.getProperty("smartsit.start");

    @RequestMapping(value = "towerCranePage.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse towerCranePage(WarnInfo entity, PageManager page, HttpServletRequest request) {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        if (StringUtil.isEmpty(entity.getOrderBy())) {
            entity.addOrderByFieldDesc("t.CREATE_DATE");
        }
        entity.setTargetType(MechType.tower_crane.toString());
        PageManager page_ = warnInfoService.query(entity, page);
        return MobileApiResponse.ok(page_);
    }

    @RequestMapping(value = "hoistPage.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse hoistPage(WarnInfo entity, PageManager page, HttpServletRequest request) {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
//        String userId = apiResponse.getUserId();
        if (StringUtil.isEmpty(entity.getOrderBy())) {
            entity.addOrderByFieldDesc("t.CREATE_DATE");
        }
        entity.setTargetType(MechType.building_hoist.toString());
        PageManager page_ = warnInfoService.query(entity, page);
        return MobileApiResponse.ok(page_);
    }


    @RequestMapping(value = "monitorsPage.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse monitorPage(WarnInfo entity, PageManager page, HttpServletRequest request) {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        if (StringUtil.isEmpty(entity.getOrderBy())) {
            entity.addOrderByFieldDesc("t.CREATE_DATE");
        }
        entity.setTargetType(MechType.monitors.toString());
        PageManager page_ = warnInfoService.query(entity, page);
        return MobileApiResponse.ok(page_);
    }

    @RequestMapping(value = "getById.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse detailById(WarnInfo entity, HttpServletRequest request) {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        try {
            if (entity.getId() == null || entity.getId().trim().length() <= 0) {
                return MobileApiResponse.error("数据不存在");
            }
            List<WarnInfo> dataList = warnInfoService.queryAll(entity);
            if (dataList != null && dataList.size() > 0) {
                return MobileApiResponse.ok(dataList.get(0));
            }
            return MobileApiResponse.error("数据不存在");
        } catch (Exception ex) {
            return MobileApiResponse.error(ex.getMessage());
        }
    }

    @RequestMapping(value = "saveResult.action", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public MobileApiResponse saveResult(@RequestBody WarnInfo entity, HttpServletRequest request) throws Exception {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        try {
//            System.out.println("**************************"+entity.getId()+" "+entity.getProcessResult());
            if (entity.getProcessResult() == null || entity.getProcessResult().trim().length() <= 0) {
                return MobileApiResponse.error("处理内容为空");
            }
            String userId = apiResponse.getUserId();
            entity.setProcessTime(new Date());
            entity.setProcessUser(userId);
            IUserService userService = SpringContextHolder.getBean(IUserService.class);
            if (userService != null) {
                User cond = new User();
                cond.setId(userId);
                User resUser = userService.getUserById(cond);
                if (resUser != null) {
                    entity.setProcessUserName(resUser.getDisplayName());
                }
            }
            warnInfoService.updateResult(entity);
            return MobileApiResponse.ok();
        } catch (Exception ex) {
            return MobileApiResponse.error(ex.getMessage());
        }
    }


    @RequestMapping(value = "projectGisList.action", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public MobileApiResponse projectList(HttpServletRequest request) throws Exception {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        try {
            List<Map<String, Object>> map = new ArrayList<>();
            ScsProjectInfo cond = new ScsProjectInfo();
            cond.setProjectCate("projectCate1");
            List<ScsProjectInfo> dataList = projectInfoService.queryAll(cond);
            if (dataList != null) {
                for (ScsProjectInfo info : dataList) {
                    if (info.getLongitude() == null || info.getLatitude() == null) {
                        continue;
                    }
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", info.getId());
                    item.put("code", info.getProjectNumber());
                    item.put("title", info.getProjectName());
                    item.put("buildDeptId", info.getBuildDeptId());
                    item.put("x_point", info.getLongitude());
                    item.put("y_point", info.getLatitude());
                    item.put("content", info.getProjectAddress());
                    item.put("attrBox", "");
                    map.add(item);
                }
            }
            return MobileApiResponse.ok(map);
        } catch (Exception ex) {
            return MobileApiResponse.error(ex.getMessage());
        }
    }

    @RequestMapping(value = "equipmentList.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse equipmentListPage(EquipmentInfo entity, PageManager page, HttpServletRequest request) throws Exception {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        if (entity.getProjectCode() == null) {
            return MobileApiResponse.error("项目编码projectCode不能为空");
        }
        if (StringUtil.isEmpty(entity.getOrderBy())) {
            entity.addOrderByFieldDesc("t.CREATE_DATE");
        }
        entity.setDataStatus("1");
        PageManager page_ = equipmentInfoService.query(entity, page);
        if (page_.getData() != null) {
            List<EquipmentInfo> dataList = (List<EquipmentInfo>) page_.getData();
            List<String> codes = new ArrayList<>();
            for (EquipmentInfo data : dataList) {
                codes.add(data.getEquipmentCode());
            }
            WarnStatisInfo sInfo = new WarnStatisInfo();
            sInfo.setEquipmentCodes(codes.toArray(new String[0]));
            Date now = new Date();
            sInfo.setWarnTimeBegin(DateUtil.begin(now));
            sInfo.setWarnTimeEnd(DateUtil.end(now));
            List<WarnStatisInfo> sList = dao.queryWarnStatis(sInfo);
            if (sList != null) {
                Map<String, WarnStatisInfo> sMap = sList.stream().collect(Collectors.toMap(item -> item.getEquipmentCode(), item2 -> item2));
                WarnStatisInfo temp;
                for (EquipmentInfo data : dataList) {
                    temp = sMap.get(data.getEquipmentCode());
                    if (temp != null && temp.getWarnNum() != null) {
                        data.setWarnNum(temp.getWarnNum());
                    }

                }
            }
        }
        return MobileApiResponse.ok(page_);
    }

    @RequestMapping(value = "rtmpUrlByCode.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse rtmpUrlByCode(EquipmentInfo entity, HttpServletRequest request) throws Exception {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        if (entity.getEquipmentCode() == null) {
            return MobileApiResponse.error("编码不能为空");
        }
        if (StringUtil.isEmpty(entity.getOrderBy())) {
            entity.addOrderByFieldDesc("t.CREATE_DATE");
        }

//        if ("true".equalsIgnoreCase(smartsitStart)) {
            System.out.println("设备编码：" + entity.getEquipmentCode());
            return MobileApiResponse.ok(HkPorxy.callPreviewURLByCode(entity.getEquipmentCode()));
//        } else {
//            return MobileApiResponse.ok("rtmp://58.200.131.2:1935/livetv/hunantv");
//        }
    }

    @RequestMapping(value = "getRealtimeInfo.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse getRealtimeInfo(HttpServletRequest request) throws Exception {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        String id = request.getParameter("id");
        Map<String, Object> value = CacheEquiInfo.get(id);
        return MobileApiResponse.ok(value);
    }

}
