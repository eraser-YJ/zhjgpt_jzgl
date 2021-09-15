package com.jc.csmp.warn.info.web;

import com.jc.common.kit.vo.ResVO;
import com.jc.csmp.doc.common.MechType;
import com.jc.csmp.doc.project.domain.ScsProjectInfo;
import com.jc.csmp.doc.project.service.IScsProjectInfoService;
import com.jc.csmp.equipment.info.domain.EquipmentInfo;
import com.jc.csmp.equipment.info.service.IEquipmentInfoService;
import com.jc.csmp.exchange.adapter.HkPorxy;
import com.jc.csmp.warn.info.dao.IRealtimeInfoDao;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.GlobalUtil;
import com.jc.foundation.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Version 1.0
 */
@Controller
@RequestMapping(value = "/monitors/manage/")
public class MonitorsManageController extends BaseController {
    @Autowired
    private IScsProjectInfoService projectInfoService;
    @Autowired
    private IEquipmentInfoService equipmentInfoService;
    @Autowired
    private IRealtimeInfoDao dao;


    public MonitorsManageController() {
    }


    @RequestMapping(value = "realtime.action", method = RequestMethod.GET)
    public String realtime(HttpServletRequest request) throws Exception {
        //open=ycjz
        request.setAttribute("open", request.getParameter("open"));
        return "warn/video/realtimeProjectList";
    }

    @RequestMapping(value = "playback.action", method = RequestMethod.GET)
    public String playback(HttpServletRequest request) throws Exception {
        request.setAttribute("open", request.getParameter("open"));
        return "warn/video/playbackProjectList";
    }

    @RequestMapping(value = "realtimeInfo.action", method = RequestMethod.GET)
    public String realtimeInfo(HttpServletRequest request) throws Exception {
        String sysPath = request.getContextPath();
        request.setAttribute("sysPath", sysPath);
        request.setAttribute("videoConfig_appKey", HkPorxy.m_appKey);
        request.setAttribute("videoConfig_secret", HkPorxy.m_appSecret);
        request.setAttribute("videoConfig_ip", HkPorxy.m_host2);
        return "warn/video/realtimeInfo";
    }

    @RequestMapping(value = "playbackInfo.action", method = RequestMethod.GET)
    public String playbackInfo(HttpServletRequest request) throws Exception {
        String sysPath = request.getContextPath();
        request.setAttribute("sysPath", sysPath);
        request.setAttribute("videoConfig_appKey", HkPorxy.m_appKey);
        request.setAttribute("videoConfig_secret", HkPorxy.m_appSecret);
        request.setAttribute("videoConfig_ip", HkPorxy.m_host2);
        return "warn/video/playbackInfo";
    }

    /**
     * 分页查询方法
     *
     * @param page
     * @return
     */
    @RequestMapping(value = "manageList.action", method = RequestMethod.GET)
    @ResponseBody
    public PageManager manageList(ScsProjectInfo cond, PageManager page) {
        try {
            PageManager pageRes = projectInfoService.query(cond, page);
            GlobalUtil.setTableRowNo(pageRes, pageRes.getPageRows());
            return pageRes;
        } catch (Exception e) {
            e.printStackTrace();
            return page;
        }
    }

    /**
     * 分页查询方法
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "checkCodes.action", method = RequestMethod.POST)
    @ResponseBody
    public ResVO checkCodes(HttpServletRequest request) {
        String projectCodeStr = request.getParameter("codes");
        String[] projectCodes = projectCodeStr.split(",");
        boolean first = true;
        ScsProjectInfo info;
        List<Map<String, Object>> resList = new ArrayList<>();
        for (String projectCode : projectCodes) {
            info = projectInfoService.queryByCode(projectCode);
            if (info == null) {
                continue;
            }
            List<EquipmentInfo> equiList = equipmentInfoService.queryByPorjectAndType(info.getProjectNumber(), MechType.monitors.toString());
            if (equiList != null && equiList.size() > 0) {
                Map<String, Object> data = new HashMap<>();
                data.put("id", info.getId());
                data.put("name", info.getProjectName());
                data.put("pId", "-1");
                data.put("nocheck", "true");
                if (first) {
                    first = false;
                    data.put("open", "true");
                }
                resList.add(data);
                for (EquipmentInfo equi : equiList) {
                    Map<String, Object> dataLeaf = new HashMap<>();
                    dataLeaf.put("id", equi.getEquipmentCode());
                    dataLeaf.put("name", equi.getEquipmentName());
                    dataLeaf.put("pId", info.getId());
                    resList.add(dataLeaf);
                }
            }
        }
        if (resList.size() > 0) {
            request.getSession().setAttribute("videoTreeData", resList);
            return ResVO.buildSuccess();
        } else {
            return ResVO.buildFail("所选项目都没有视频设备！");
        }

    }


    /**
     * 分页查询方法
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "queryVideoTree.action", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> queryVideoTree(HttpServletRequest request) {
        List<Map<String, Object>> resList = (List<Map<String, Object>>) request.getSession().getAttribute("videoTreeData");
        return resList;
    }
}

