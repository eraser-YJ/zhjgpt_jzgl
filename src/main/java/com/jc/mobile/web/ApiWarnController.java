package com.jc.mobile.web;

import com.jc.common.kit.vo.ResVO;
import com.jc.csmp.doc.project.service.IScsProjectInfoService;
import com.jc.csmp.equipment.info.domain.EquipmentInfo;
import com.jc.csmp.equipment.info.service.IEquipmentInfoService;
import com.jc.csmp.exchange.cache.CacheEquiInfo;
import com.jc.foundation.util.StringUtil;
import com.jc.mobile.basic.web.MobileController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Author liubq
 * @Date 2020/8/3 16:13
 * @Version 1.0
 */
@Controller
@RequestMapping(value = "/api/system/")
public class ApiWarnController extends MobileController {
    @Autowired
    private IEquipmentInfoService equipmentInfoService;
    @Autowired
    private IScsProjectInfoService projectInfoService;


    @RequestMapping(value = "equipmentList.action", method = RequestMethod.GET)
    @ResponseBody
    public List<EquipmentInfo> equipmentListPage(EquipmentInfo entity) throws Exception {
        if (entity.getProjectCode() == null) {
            return new ArrayList<>();
        }
        if (StringUtil.isEmpty(entity.getOrderBy())) {
            entity.addOrderByFieldDesc("t.CREATE_DATE");
        }
        entity.setDataStatus("1");
        List<EquipmentInfo> page_ = equipmentInfoService.queryAll(entity);
        return page_;
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
