package com.jc.csmp.plan.ctrl.web;

import com.jc.csmp.plan.ctrl.domain.ProjectYearPlanCtrl;
import com.jc.csmp.plan.ctrl.service.IProjectYearPlanCtrlService;
import com.jc.csmp.plan.domain.ProjectYearPlan;
import com.jc.csmp.plan.service.IProjectYearPlanService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.GlobalUtil;
import com.jc.foundation.util.MessageUtils;
import com.jc.foundation.util.StringUtil;
import com.jc.foundation.web.BaseController;
import com.jc.system.applog.ActionLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * 年度计划上报控制功能处理
 *
 * @Version 1.0
 */
@Controller
@RequestMapping(value = "/plan/ctrl/")
public class ProjectYearPlanCtrlController extends BaseController {

    @Autowired
    private IProjectYearPlanCtrlService projectYearPlanCtrlService;
    @Autowired
    private IProjectYearPlanService projectPlanService;


    public ProjectYearPlanCtrlController() {
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
    @ActionLog(operateModelNm = "年度计划上报控制功能", operateFuncNm = "save", operateDescribe = "新增操作")
    public Map<String, Object> save(@Valid ProjectYearPlanCtrl entity, BindingResult result, HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = validateBean(result);
        if (resultMap.size() > 0) {
            return resultMap;
        }
        if (!"false".equals(resultMap.get("success"))) {
            try {
                check(entity);
            } catch (Exception ex) {
                resultMap.put("success", "false");
                resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, ex.getMessage());
                return resultMap;
            }

            GlobalUtil.resultToMap(projectYearPlanCtrlService.saveEntity(entity), resultMap, getToken(request));
        }
        return resultMap;
    }

    /**
     * 检查
     *
     * @param data
     * @throws Exception
     */
    private void check(ProjectYearPlanCtrl data) throws Exception {
        if (data == null) {
            throw new Exception("保存数据不能为空");
        }
        if (data.getRequestStartDate() == null) {
            throw new Exception("开始时间不能为空");
        }

        if (data.getRequestEndDate() == null) {
            throw new Exception("结束时间不能为空");
        }
        Long now = System.currentTimeMillis();
        if (data.getRequestEndDate().getTime() < now) {
            throw new Exception("结束时间不能小于于当前时间");
        }
        if (data.getRequestEndDate().getTime() < data.getRequestEndDate().getTime()) {
            throw new Exception("结束时间不能早于开始时间");
        }
        ProjectYearPlanCtrl cond = new ProjectYearPlanCtrl();
        cond.setPlanYear(data.getPlanYear());
        List<ProjectYearPlanCtrl> dataList = projectYearPlanCtrlService.queryAll(cond);
        if (dataList != null) {
            Set<String> yearIndexMap = new HashSet<>();
            for (ProjectYearPlanCtrl item : dataList) {
                if (item.getId().equals(data.getId())) {
                    continue;
                }
                yearIndexMap.add(item.getPlanYear() + "_" + item.getSeqno());
            }
            if (yearIndexMap.contains(data.getPlanYear() + "_" + data.getSeqno())) {
                throw new Exception(data.getPlanYear()+"年度该上报类型已经存在");
            }
        }
        ProjectYearPlan planCount = new ProjectYearPlan();
        planCount.setPlanYear(data.getPlanYear());
        planCount.setPlanSeqno(data.getSeqno());
        List<ProjectYearPlan> allData = projectPlanService.queryAll(planCount);
        if (allData != null) {
            for (ProjectYearPlan item : allData) {
                if (item.getCreateDate().getTime() < data.getRequestStartDate().getTime()) {
                    throw new Exception("已有记录的上报时间小于该期的开始时间，不能保存");
                }
                if (item.getCreateDate().getTime() > data.getRequestEndDate().getTime()) {
                    throw new Exception("已有记录的上报时间大于该期的结束时间，不能保存");
                }
            }
        }
    }

    /**
     * 检查
     *
     * @throws Exception
     */
    private void checkDelete(String[] ids) throws Exception {
        for (String id : ids) {
            ProjectYearPlanCtrl cond = new ProjectYearPlanCtrl();
            cond.setId(id);
            ProjectYearPlanCtrl data = projectYearPlanCtrlService.get(cond);
            if (data == null) {
                return;
            }
            ProjectYearPlan planCount = new ProjectYearPlan();
            planCount.setPlanYear(data.getPlanYear());
            planCount.setPlanSeqno(data.getSeqno());
            Long count = projectPlanService.queryCount(planCount);
            if (count != null && count > 0) {
                throw new Exception("该条时间范围已经有上报记录，不能删除");
            }
        }

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
    @ActionLog(operateModelNm = "年度计划上报控制功能", operateFuncNm = "update", operateDescribe = "更新操作")
    public Map<String, Object> update(ProjectYearPlanCtrl entity, BindingResult result, HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = validateBean(result);
        if (resultMap.size() > 0) {
            return resultMap;
        }
        try {
            check(entity);
        } catch (Exception ex) {
            resultMap.put("success", "false");
            resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, ex.getMessage());
            return resultMap;
        }
        GlobalUtil.resultToMap(projectYearPlanCtrlService.updateEntity(entity), resultMap, getToken(request));
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
    public ProjectYearPlanCtrl get(ProjectYearPlanCtrl entity) throws Exception {
        return projectYearPlanCtrlService.get(entity);
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
        model.addAttribute("data", map);
        return "csmp/plan/ctrl/projectYearPlanCtrlForm";
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
    public PageManager manageList(ProjectYearPlanCtrl entity, PageManager page) {
        if (StringUtil.isEmpty(entity.getOrderBy())) {
            entity.addOrderByFieldDesc("t.CREATE_DATE");
        }
        PageManager page_ = projectYearPlanCtrlService.query(entity, page);
        GlobalUtil.setTableRowNo(page_, page_.getPageRows());
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
    public String manage() throws Exception {
        return "csmp/plan/ctrl/projectYearPlanCtrlList";
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
    @ActionLog(operateModelNm = "年度计划上报控制功能", operateFuncNm = "deleteByIds", operateDescribe = "删除操作")
    public Map<String, Object> deleteByIds(ProjectYearPlanCtrl entity, String ids) throws Exception {
        Map<String, Object> resultMap = new HashMap<>(2);
        entity.setPrimaryKeys(ids.split(","));
        try {
            checkDelete(ids.split(","));
        } catch (Exception ex) {
            resultMap.put("success", "false");
            resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, ex.getMessage());
            return resultMap;
        }
        if (projectYearPlanCtrlService.deleteByIds(entity) != 0) {
            resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
            resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
        } else {
            resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
            resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
        }
        return resultMap;
    }
}

