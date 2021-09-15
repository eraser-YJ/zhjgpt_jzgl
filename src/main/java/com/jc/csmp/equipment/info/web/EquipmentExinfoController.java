package com.jc.csmp.equipment.info.web;

import com.jc.common.def.DefItemVO;
import com.jc.common.def.DefUtil;
import com.jc.common.def.DefVO;
import com.jc.common.def.PageAttributeRow;
import com.jc.csmp.equipment.info.domain.EquipmentExinfo;
import com.jc.csmp.equipment.info.service.IEquipmentExinfoService;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备扩展信息处理
 *
 * @Version 1.0
 */
@Controller
@RequestMapping(value = "/equipment/exinfo/")
public class EquipmentExinfoController extends BaseController {
    @Autowired
    private IEquipmentExinfoService equipmentExinfoService;


    public EquipmentExinfoController() {
    }


    /**
     * 获取单条记录方法
     *
     * @param entity
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "getInfo.action", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getInfo(EquipmentExinfo entity) throws Exception {
        EquipmentExinfo data = equipmentExinfoService.get(entity);
        if(data == null){
            return new HashMap<>();
        }
        Map<String, Object> dataMap = (Map<String, Object>) JsonUtil.json2Java(data.getContent(), Map.class);
        return dataMap;
    }

    /**
     * @return String form对话框所在位置
     * @throws Exception
     * @description 弹出对话框方法
     * @author
     * @version 2020-04-10
     */
    @RequestMapping(value = "loadInfo.action", method = RequestMethod.GET)
    public String loadInfo(Model model, HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        request.setAttribute("id", id);
        String type = request.getParameter("type");
        return "equipment/detail/" + type;
    }
}

