package com.jc.csmp.scs.web;

import com.jc.csmp.doc.project.domain.ScsProjectInfo;
import com.jc.csmp.doc.project.service.IScsProjectInfoService;
import com.jc.foundation.util.GlobalContext;
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
@RequestMapping(value = "/desktop/manage/")
public class ScsManageController extends BaseController {
    @Autowired
    private IScsProjectInfoService projectInfoService;

    private String arcGisAddress = GlobalContext.getProperty("arc.gis.address");

    public ScsManageController() {
    }


    @RequestMapping(value = "index.action", method = RequestMethod.GET)
    public String index(HttpServletRequest request) throws Exception {
        String sysPath = request.getContextPath();
        request.setAttribute("sysPath", sysPath);
        request.setAttribute("arcGisAddress", arcGisAddress);
        return "TDTLib/tdt";
    }

    @RequestMapping(value = "mapInfo.action", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> mapInfo(HttpServletRequest request) throws Exception {
        List<Map<String, Object>> map = new ArrayList<>();
        ScsProjectInfo cond = new ScsProjectInfo();
        cond.addOrderByFieldDesc("t.projectNumber");
        cond.setCanPosition("Y");
        List<ScsProjectInfo> dataList = projectInfoService.queryAll(cond);
        if (dataList != null) {
            for (ScsProjectInfo info : dataList) {
                if (info.getLongitude() == null || info.getLatitude() == null) {
                    continue;
                }
                Map<String, Object> item = new HashMap<>();
                item.put("id", info.getId());
                item.put("title", info.getProjectName());
                item.put("code", info.getProjectNumber());
                item.put("x_point", info.getLongitude());
                item.put("y_point", info.getLatitude());
                item.put("content", info.getProjectAddress());
                item.put("attrBox", "");
                map.add(item);
            }
        }

        return map;
    }

    @RequestMapping(value = "projectDesktop.action", method = RequestMethod.GET)
    public String projectDesktop(HttpServletRequest request) throws Exception {
        request.setAttribute("projectCode", request.getParameter("projectCode"));
        return "scs/projectDesktopInfoForm";
    }

    @RequestMapping(value = "projectInfo.action", method = RequestMethod.GET)
    @ResponseBody
    public ScsProjectInfo projectInfo(HttpServletRequest request) throws Exception {
        String projectCode = request.getParameter("projectCode");
        if (projectCode == null || projectCode.trim().length() <= 0) {
            return null;
        }
        ScsProjectInfo cond = new ScsProjectInfo();
        cond.setId(projectCode);
        return projectInfoService.get(cond);
    }


}

