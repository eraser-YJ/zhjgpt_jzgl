package com.jc.csmp.equipment.info.web;

import com.jc.csmp.common.enums.EquiWorkStatusEnum;
import com.jc.csmp.equipment.info.domain.EquipmentInfo;
import com.jc.csmp.equipment.info.domain.validator.EquipmentInfoValidator;
import com.jc.csmp.equipment.info.service.IEquipmentInfoService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.*;
import com.jc.foundation.web.BaseController;
import com.jc.system.applog.ActionLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @Version 1.0
 */
@Controller
@RequestMapping(value = "/equipment/info/")
public class EquipmentInfoController extends BaseController {

    @Autowired
    private IEquipmentInfoService equipmentInfoService;

    @org.springframework.web.bind.annotation.InitBinder("equipmentInfo")
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(new EquipmentInfoValidator());
    }

    public EquipmentInfoController() {
    }

    /**
     * 保存方法
     *
     * @param entity
     * @param result
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "save.action", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> save(@Valid EquipmentInfo entity, BindingResult result, HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = validateBean(result);
        if (resultMap.size() > 0) {
            return resultMap;
        }
        if (!"false".equals(resultMap.get("success"))) {
            entity.setWorkStatus(EquiWorkStatusEnum.outline.toString());
            GlobalUtil.resultToMap(equipmentInfoService.saveEntity(entity), resultMap, getToken(request));
        }
        return resultMap;
    }

    /**
     * 修改方法
     *
     * @param entity
     * @param result
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "update.action", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> update(EquipmentInfo entity, BindingResult result, HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = validateBean(result);
        if (resultMap.size() > 0) {
            return resultMap;
        }
        GlobalUtil.resultToMap(equipmentInfoService.updateEntity(entity), resultMap, getToken(request));
        return resultMap;
    }

    /**
     * 获取单条记录方法
     *
     * @param entity
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "get.action", method = RequestMethod.GET)
    @ResponseBody
    public EquipmentInfo get(EquipmentInfo entity) throws Exception {
        return equipmentInfoService.get(entity);
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
    public Map<String, Object> getInfo(EquipmentInfo entity) throws Exception {
        Map<String, Object> resMap = new HashMap<>();
        EquipmentInfo data = equipmentInfoService.get(entity);
        resMap.put("data", data);
        return resMap;
    }


    /**
     * @return String form对话框所在位置
     * @throws Exception
     * @description 弹出对话框方法
     * @author
     * @version 2020-04-10
     */
    @RequestMapping(value = "loadForm.action", method = RequestMethod.GET)
    public String loadForm(Model model, HttpServletRequest request) throws Exception {
        Map<String, Object> map = new HashMap<>(1);
        map.put("id",request.getParameter("id"));
        model.addAttribute("data", map);
        return "equipment/info/equipmentInfoForm";
    }

    /**
     * 分页查询方法
     *
     * @param entity
     * @param page
     * @return
     */
    @RequestMapping(value = "manageList.action", method = RequestMethod.GET)
    @ResponseBody
    public PageManager manageList(EquipmentInfo entity, PageManager page) {
        if (StringUtil.isEmpty(entity.getOrderBy())) {
            entity.addOrderByFieldDesc("t.CREATE_DATE");
        }
        PageManager page_ = equipmentInfoService.query(entity, page);
        GlobalUtil.setTableRowNo(page_,page.getPageRows());
        return page_;
    }

    /**
     * @return String 跳转的路径
     * @throws Exception
     * @description 跳转方法
     * @author
     * @version 2020-04-10
     */
    @RequestMapping(value = "manage.action", method = RequestMethod.GET)
    @ActionLog(operateModelNm = "", operateFuncNm = "manage", operateDescribe = "对进行跳转操作")
    public String manage() throws Exception {
        return "equipment/info/equipmentInfoList";
    }

    /**
     * 删除方法
     *
     * @param entity
     * @param ids
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "deleteByIds.action", method = RequestMethod.POST)
    @ResponseBody
    @ActionLog(operateModelNm = "", operateFuncNm = "deleteByIds", operateDescribe = "对进行删除")
    public Map<String, Object> deleteByIds(EquipmentInfo entity, String ids) throws Exception {
        Map<String, Object> resultMap = new HashMap<>(2);
        entity.setPrimaryKeys(ids.split(","));
        if (equipmentInfoService.deleteByIds(entity) != 0) {
            resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
            resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
        } else {
            resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
            resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
        }
        return resultMap;
    }
}

