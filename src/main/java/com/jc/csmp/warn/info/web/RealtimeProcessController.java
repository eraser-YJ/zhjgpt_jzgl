package com.jc.csmp.warn.info.web;

import com.jc.common.kit.vo.ResVO;
import com.jc.csmp.equipment.info.domain.EquipmentInfo;
import com.jc.csmp.equipment.info.service.IEquipmentInfoService;
import com.jc.csmp.exchange.cache.CacheEquiInfo;
import com.jc.csmp.warn.info.dao.IRealtimeInfoDao;
import com.jc.csmp.warn.info.domain.RealtimeInfo;
import com.jc.csmp.warn.info.domain.validator.WarnInfoValidator;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.GlobalUtil;
import com.jc.foundation.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Version 1.0
 */
@Controller
@RequestMapping(value = "/monit/realtime/")
public class RealtimeProcessController extends BaseController {

    @Autowired
    private IEquipmentInfoService equipmentInfoService;
    @Autowired
    private IRealtimeInfoDao dao;

    @org.springframework.web.bind.annotation.InitBinder("warnInfo")
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(new WarnInfoValidator());
    }

    public RealtimeProcessController() {
    }


    /**
     * 分页查询方法
     *
     * @param page
     * @return
     */
    @RequestMapping(value = "manageList.action", method = RequestMethod.GET)
    @ResponseBody
    public PageManager manageList(RealtimeInfo cond, PageManager page) {
        try {
            page.setPageRows(1000);
            List<RealtimeInfo> dataList = dao.queryStatis(cond);
            page.setTotalCount(dataList.size());
            page.setTotalPages(1);
            page.setPage(0);
            page.setData(dataList);
            GlobalUtil.setTableRowNo(page, page.getPageRows());
            return page;
        } catch (Exception e) {
            e.printStackTrace();
            return page;
        }
    }

    /**
     * 查询方法
     *
     * @param
     * @return
     */
    @RequestMapping(value = "queryList.action", method = RequestMethod.GET)
    @ResponseBody
    public List<RealtimeInfo> queryList(RealtimeInfo cond) {
        try {
            List<RealtimeInfo> dataList = dao.queryStatis(cond);
            return dataList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping(value = "statis.action", method = RequestMethod.GET)
    public String statis(HttpServletRequest request) throws Exception {
        String type = request.getParameter("type");
        request.setAttribute("mechType", type);
        return "warn/process/realtimeProcessList";
    }

    @RequestMapping(value = "manage.action", method = RequestMethod.GET)
    public String manage(HttpServletRequest request) throws Exception {
        String type = request.getParameter("type");
        request.setAttribute("equipmentType", type);
        return "warn/process/realtimeDetailList";
    }

    @RequestMapping(value = "detailList.action")
    @ResponseBody
    public List<EquipmentInfo> detailList(HttpServletRequest request) throws Exception {
        String projectCode = request.getParameter("projectCode");
        String equipmentType = request.getParameter("equipmentType");
        EquipmentInfo cond = new EquipmentInfo();
        cond.setProjectCode(projectCode);
        if (equipmentType != null && equipmentType.trim().length() > 0) {
            cond.setEquipmentType(equipmentType);
        }
        List<EquipmentInfo> recordList = equipmentInfoService.queryAll(cond);
        return recordList;
    }


    @RequestMapping(value = "detail.action", method = RequestMethod.GET)
    public String detail(HttpServletRequest request) throws Exception {
        request.setAttribute("projectCode", request.getParameter("projectCode"));
        request.setAttribute("projectName", request.getParameter("projectName"));
        request.setAttribute("equipmentType", request.getParameter("equipmentType"));
        request.setAttribute("pageTitle", "智慧工地");
        return "warn/process/realtimeDetailList";
    }

    @RequestMapping(value = "tower.action", method = RequestMethod.GET)
    public String tower(HttpServletRequest request) throws Exception {
        request.setAttribute("id", request.getParameter("id"));
        request.setAttribute("code", request.getParameter("code"));
        return "mockequi/tower/index";
    }

    @RequestMapping(value = "getTowerRealInfo.action", method = RequestMethod.GET)
    @ResponseBody
    public ResVO getTowerRealInfo(HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        Map<String, Object> value = CacheEquiInfo.get(id);
        if (value != null && value.size() > 2) {
            return ResVO.buildSuccess(value);
        } else {
            return ResVO.buildFail("");
        }

    }

    @RequestMapping(value = "hoist.action", method = RequestMethod.GET)
    public String hoist(HttpServletRequest request) throws Exception {
        request.setAttribute("id", request.getParameter("id"));
        request.setAttribute("code", request.getParameter("code"));
        return "mockequi/hoist/index";
    }

    @RequestMapping(value = "getHoistRealInfo.action", method = RequestMethod.GET)
    @ResponseBody
    public ResVO getHoistRealInfo(HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        Map<String, Object> value = CacheEquiInfo.get(id);
        if (value != null && value.size() > 1) {
            return ResVO.buildSuccess(value);
        } else {
            return ResVO.buildFail("");
        }

    }
}

