package com.jc.csmp.warn.info.web;

import com.jc.common.def.DefItemVO;
import com.jc.common.def.DefUtil;
import com.jc.common.def.DefVO;
import com.jc.common.def.PageAttributeRow;
import com.jc.csmp.warn.info.domain.WarnInfo;
import com.jc.csmp.warn.info.domain.validator.WarnInfoValidator;
import com.jc.csmp.warn.info.service.IWarnInfoService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.*;
import com.jc.foundation.web.BaseController;
import com.jc.system.applog.ActionLog;
import com.jc.system.dic.domain.Dic;
import com.jc.system.dic.service.IDicService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Version 1.0
 */
@Controller
@RequestMapping(value = "/warn/info/")
public class WarnInfoController extends BaseController {
    @Autowired
    private IWarnInfoService warnInfoService;

    @org.springframework.web.bind.annotation.InitBinder("warnInfo")
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(new WarnInfoValidator());
    }

    public WarnInfoController() {
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
    @ActionLog(operateModelNm = "", operateFuncNm = "save", operateDescribe = "对进行新增操作")
    public Map<String, Object> save(@Valid WarnInfo entity, BindingResult result, HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = validateBean(result);
        if (resultMap.size() > 0) {
            return resultMap;
        }
        resultMap = validateToken(request);
        if (resultMap.size() > 0) {
            return resultMap;
        }
        if (!"false".equals(resultMap.get("success"))) {
            GlobalUtil.resultToMap(warnInfoService.saveEntity(entity), resultMap, getToken(request));
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
    @ActionLog(operateModelNm = "", operateFuncNm = "update", operateDescribe = "对进行更新操作")
    public Map<String, Object> update(WarnInfo entity, BindingResult result, HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = validateBean(result);
        if (resultMap.size() > 0) {
            return resultMap;
        }
        GlobalUtil.resultToMap(warnInfoService.updateEntity(entity), resultMap, getToken(request));
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
    public WarnInfo get(WarnInfo entity) throws Exception {
        return warnInfoService.get(entity);
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
        String token = getToken(request);
        map.put(GlobalContext.SESSION_TOKEN, token);
        model.addAttribute("data", map);
        return "warn/info/warnInfoForm";
    }

    @RequestMapping(value = "loadResult.action", method = RequestMethod.GET)
    public String loadResult(Model model, HttpServletRequest request) throws Exception {
        return "warn/info/warnInfoResult";
    }

    @RequestMapping(value = "loadView.action", method = RequestMethod.GET)
    public String loadView(Model model, HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        request.setAttribute("id", id);
        String type = request.getParameter("type");
        request.setAttribute("mechType", type);
        return "warn/detail/" + type + "/dataForm";
    }

    @RequestMapping(value = "updateResult.action", method = RequestMethod.POST)
    @ResponseBody
    @ActionLog(operateModelNm = "", operateFuncNm = "update", operateDescribe = "对进行更新操作")
    public Map<String, Object> updateResult(WarnInfo entity, BindingResult result, HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = validateBean(result);
        if (resultMap.size() > 0) {
            return resultMap;
        }
        GlobalUtil.resultToMap(warnInfoService.updateResult(entity), resultMap, getToken(request));
        return resultMap;
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
    public PageManager manageList(WarnInfo entity, PageManager page) {
        if (StringUtil.isEmpty(entity.getOrderBy())) {
            entity.addOrderByFieldDesc("t.CREATE_DATE");
        }
        PageManager page_ = warnInfoService.query(entity, page);
        GlobalUtil.setTableRowNo(page_,page_.getPageRows());
        return page_;
    }

    @RequestMapping(value = "dicList.action", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, String>> manageList(HttpServletRequest request) {
        String type = request.getParameter("mechType");
        IDicService dicService = SpringContextHolder.getBean(IDicService.class);
        Dic dicCond = new Dic();
        dicCond.setParentId(type);
        List<Dic> dicList = dicService.query(dicCond);
        List<Map<String, String>> resList = new ArrayList<>();
        for (Dic dic : dicList) {
            Map<String, String> itemMap = new HashMap<>();
            itemMap.put("name", dic.getCode());
            itemMap.put("value", dic.getValue());
            resList.add(itemMap);
        }
        return resList;

    }


    @RequestMapping(value = "manage.action", method = RequestMethod.GET)
    public String manage() throws Exception {
        return "warn/info/warnInfoList";
    }

    @RequestMapping(value = "message.action", method = RequestMethod.GET)
    public String message(HttpServletRequest request) throws Exception {
        String type = request.getParameter("type");
        request.setAttribute("mechType", type);
        return "warn/detail/" + type + "/dataList";
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
    public Map<String, Object> deleteByIds(WarnInfo entity, String ids) throws Exception {
        Map<String, Object> resultMap = new HashMap<>(2);
        entity.setPrimaryKeys(ids.split(","));
        if (warnInfoService.deleteByIds(entity) != 0) {
            resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
            resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
        } else {
            resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
            resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
        }
        return resultMap;
    }
}

