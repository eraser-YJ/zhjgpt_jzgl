package com.jc.csmp.plan.web;

import com.jc.common.kit.vo.ResVO;
import com.jc.csmp.common.enums.ProjectQuestionEnum;
import com.jc.csmp.plan.ctrl.domain.ProjectYearPlanCtrl;
import com.jc.csmp.plan.domain.*;
import com.jc.csmp.plan.kit.*;
import com.jc.csmp.plan.service.*;
import com.jc.csmp.project.domain.CmProjectInfo;
import com.jc.csmp.project.service.ICmProjectInfoService;
import com.jc.foundation.domain.Attach;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.*;
import com.jc.foundation.web.BaseController;
import com.jc.system.applog.ActionLog;
import com.jc.system.content.service.IAttachService;
import com.jc.system.dic.domain.Dic;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.User;
import com.jc.system.security.service.IUserService;
import com.jc.system.security.util.DeptCacheUtil;
import com.jc.workflow.instance.service.IInstanceService;
import io.swagger.models.auth.In;
import org.apache.commons.io.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 月度计划处理
 *
 * @Version 1.0
 */
@Controller
@RequestMapping(value = "/plan/projectMonthPlan/")
public class ProjectMonthPlanController extends BaseController {

    @Autowired
    private IProjectMonthPlanService projectMonthPlanService;
    @Autowired
    private IProjectMonthPlanItemService projectMonthPlanItemService;
    @Autowired
    private IProjectYearPlanItemService projectYearPlanItemService;
    @Autowired
    private IProjectYearPlanService projectPlanService;
    @Autowired
    private ICmProjectInfoService projectInfoService;
    @Autowired
    private IUserService userService;

    private String uploadType = GlobalContext.getProperty("FILE_TRANS_TYPE");
    private boolean absolutePath = Boolean.parseBoolean(GlobalContext.getProperty("ABSOLUTE_PATH"));
    public String getUploadBaseDir() {
        return String.valueOf(GlobalContext.getProperty("FILE_PATH"));
    }


    public ProjectMonthPlanController() {
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
    @ActionLog(operateModelNm = "月度计划", operateFuncNm = "save", operateDescribe = "新增操作")
    public Map<String, Object> save(@Valid ProjectMonthPlan entity, BindingResult result, HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = validateBean(result);
        if (resultMap.size() > 0) {
            return resultMap;
        }
        if (!"false".equals(resultMap.get("success"))) {
            GlobalUtil.resultToMap(projectMonthPlanService.saveEntity(entity), resultMap, getToken(request));
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
    @ActionLog(operateModelNm = "月度计划", operateFuncNm = "update", operateDescribe = "更新操作")
    public Map<String, Object> update(ProjectMonthPlan entity, BindingResult result, HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = validateBean(result);
        if (resultMap.size() > 0) {
            return resultMap;
        }
        GlobalUtil.resultToMap(projectMonthPlanService.updateEntity(entity), resultMap, getToken(request));
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
    public ProjectMonthPlan get(ProjectMonthPlan entity) throws Exception {
        return projectMonthPlanService.get(entity);
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
        request.setAttribute("id", request.getParameter("id"));
        request.setAttribute("pageMode", request.getParameter("pageMode"));
        return "csmp/plan/month/projectMonthPlanForm";
    }

    /**
     * @return String form对话框所在位置
     * @throws Exception
     * @description 弹出对话框方法
     * @author
     * @version 2020-04-10
     */
    @RequestMapping(value = "loadFormData.action", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> loadFormData(HttpServletRequest request) throws Exception {
        Map<String, Object> map = new HashMap<>();
        String id = request.getParameter("id");
        ProjectMonthPlan cond = new ProjectMonthPlan();
        cond.setId(id);
        ProjectMonthPlan nowPlan = projectMonthPlanService.get(cond);
        ProjectMonthPlanItem itemCond = new ProjectMonthPlanItem();
        itemCond.setHeadId(nowPlan.getId());
        itemCond.addOrderByField("t.ext_num1");
        List<ProjectMonthPlanItem> itemList = projectMonthPlanItemService.queryAll(itemCond);
        map.put("head", nowPlan);
        map.put("itemList", itemList);
        XyDicAgg agg = (XyDicAgg) JsonUtil.json2Java(nowPlan.getPlanType(), XyDicAgg.class);
        map.put("dicInfo", agg);
        return map;
    }

    /**
     * @return String form对话框所在位置
     * @throws Exception
     * @description 弹出对话框方法
     * @author
     * @version 2020-04-10
     */
    @RequestMapping(value = "startFormData.action", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> startForm(Model model, HttpServletRequest request) throws Exception {
        Map<String, Object> map = new HashMap<>();
        ProjectYearPlan nowPlan = (ProjectYearPlan) request.getSession().getAttribute("nowYearPlan");
        String selectMonth = (String) request.getSession().getAttribute("nowSelectMonth");
        ProjectYearPlanItem itemCond = new ProjectYearPlanItem();
        itemCond.setHeadId(nowPlan.getId());
        itemCond.addOrderByField("t.ext_num1");
        List<ProjectYearPlanItem> itemList = projectYearPlanItemService.queryAll(itemCond);

        //产生新数据
        ProjectMonthPlan monthNowPlan = ConvertUtil.convert(nowPlan);
        monthNowPlan.setId(IdUtil.createId());
        monthNowPlan.setPlanMonth(Integer.valueOf(selectMonth));

        List<ProjectMonthPlanItem> monthItemList = ConvertUtil.convert(itemList);
        for (ProjectMonthPlanItem item : monthItemList) {
            item.setHeadId(monthNowPlan.getId());
        }
        List<Dic> list = projectPlanService.selectPPTDic();
        map.put("dicInfo", DicUtil.buildTree(list));
        map.put("head", monthNowPlan);
        map.put("itemList", monthItemList);
        return map;
    }

    /**
     * @return String 跳转的路径
     * @throws Exception
     * @description 待办列表
     * @author
     * @version 2020-07-09
     */
    @RequestMapping(value = "index.action", method = RequestMethod.GET)
    public String index(Model model, HttpServletRequest request) throws Exception {
        return "csmp/plan/month/projectMonthPlanFormIndex";
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
    public PageManager manageList(ProjectMonthPlan entity, PageManager page) {
        if (StringUtil.isEmpty(entity.getOrderBy())) {
            entity.addOrderByFieldDesc("t.CREATE_DATE");
        }
        try {
            User user = SystemSecurityUtils.getUser();
//            String code = DeptCacheUtil.getCodeById(user.getDeptId());
//            entity.setRoleCode(code);
            PageManager page_ = projectMonthPlanService.query(entity, page);
            List<ProjectMonthPlan> dataList = (List<ProjectMonthPlan>) page_.getData();
            if (dataList != null && dataList.size() > 0) {
                Set<String> userIds = new HashSet<>();
                dataList.forEach(item -> {
                    if (item.getCreateUser() != null) {
                        userIds.add(item.getCreateUser());
                    }
                });
                List<User> users = userService.queryUserByIds(ListUtil.join(userIds));
                Map<String, User> userMap = new HashMap<>();
                if (users != null) {
                    users.forEach(u -> {
                        userMap.put(u.getId(), u);
                    });
                }
                for (ProjectMonthPlan item : dataList) {
                    User u = userMap.get(item.getCreateUser());
                    if (u != null) {
                        item.setOwer(u.getDisplayName());
                        if (item.getCreateUser().equalsIgnoreCase(user.getId())) {
                            item.setCanChange("Y");
                        }
                    }
                }
            }
            GlobalUtil.setTableRowNo(page_, page_.getPageRows());
            return page_;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return page;
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
        return "csmp/plan/month/projectMonthPlanList";
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
    @ActionLog(operateModelNm = "月度计划", operateFuncNm = "deleteByIds", operateDescribe = "删除操作")
    public Map<String, Object> deleteByIds(ProjectMonthPlan entity, String ids) throws Exception {
        Map<String, Object> resultMap = new HashMap<>(2);
        entity.setPrimaryKeys(ids.split(","));
        if (projectMonthPlanService.deleteByIds(entity) != 0) {
            resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
            resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
        } else {
            resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
            resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
        }
        return resultMap;
    }

    /**
     * @param request
     * @param response
     * @throws Exception
     */
    public void writeDic(HttpServletRequest request, HttpServletResponse response) throws Exception {

    }

    /**
     * @return String 跳转的路径
     * @throws Exception
     * @description 已办列表
     * @author
     * @version 2020-07-09
     */
    @RequestMapping(value = "checkRepetition.action")
    @ResponseBody
    public Result checkRepetition(HttpServletRequest request) throws Exception {
        String selectMonth = request.getParameter("selectMonth");
        String yearStr = request.getParameter("selectYear");
        String areaCode = request.getParameter("selectAreaCode");
        ProjectYearPlan yearPlan = new ProjectYearPlan();
        yearPlan.setPlanYear(Integer.valueOf(yearStr));
        yearPlan.setPlanAreaCode(areaCode);
        yearPlan.setPlanStatus(20);
        List<ProjectYearPlan> yearPlanList = projectPlanService.queryAll(yearPlan);
        if (yearPlanList == null || yearPlanList.size() == 0) {
            return Result.failure(1001, "该地区没有审批通过的年度计划，不能制定月度计划！");
        }
        Collections.sort(yearPlanList, new Comparator<ProjectYearPlan>() {
            @Override
            public int compare(ProjectYearPlan t0, ProjectYearPlan t1) {
                return Integer.valueOf(t1.getPlanSeqno()).compareTo(Integer.valueOf(t0.getPlanSeqno()));
            }
        });
        request.getSession().setAttribute("nowYearPlan", yearPlanList.get(0));
        request.getSession().setAttribute("nowSelectMonth", selectMonth);
        try {

            ProjectMonthPlan cond = new ProjectMonthPlan();
            cond.setPlanYear(Integer.valueOf(yearStr));
            cond.setPlanAreaCode(areaCode);
            cond.setPlanMonth(Integer.valueOf(selectMonth));
            List<ProjectMonthPlan> dataList = projectMonthPlanService.queryAll(cond);
            if (dataList == null || dataList.size() == 0) {
                return Result.success();
            } else {
                return Result.failure(1001, "该地区月度的计划已经存在，不能重复制定！");
            }
        } catch (Exception ex) {
            return Result.failure(1000, ex.getMessage());
        }
    }

    @RequestMapping(value = "getAttach.action")
    @ResponseBody
    public List<Attach> getAttach(HttpServletRequest request) throws Exception {
        String attachIds = request.getParameter("attachId");
        if (attachIds == null || attachIds.trim().length() <= 0) {
            return new ArrayList<>();
        }
        ProjectMonthPlanItem attCond = new ProjectMonthPlanItem();
        attCond.setAttachList(attachIds.split(","));
        List<Attach> attchList = projectMonthPlanItemService.getAttachList(attCond);
        return attchList;
    }

    @RequestMapping(value = "getXxjd.action")
    @ResponseBody
    public Map<String, Object> getXxjd(HttpServletRequest request) throws Exception {
        String prjectId = request.getParameter("projectId");
        ProjectMonthPlanItem itemCond = new ProjectMonthPlanItem();
        String month = LocalDate.now().getYear()+"-"+LocalDate.now().getMonthValue();
        itemCond.setProjectId(prjectId);
        ProjectMonthPlanItem itemNow = projectMonthPlanItemService.queryNewXxdj(itemCond);
        itemCond.setQueryMonth(month);
        ProjectMonthPlanItem itemNum = projectMonthPlanItemService.queryNewNum(itemCond);
        Map<String, Object> resMap = new HashMap<String, Object>();
        resMap.put("xxjd", "");
        resMap.put("xxjdAttchList","");
        resMap.put("projectMonthPlanInvest", "");
        resMap.put("projectMonthActInvest","");
        if(itemNum!=null){
            resMap.put("projectMonthPlanInvest", itemNum.getProjectMonthPlanInvest());
            resMap.put("projectMonthActInvest",itemNum.getProjectMonthActInvest());
        }
        if (itemNow != null) {
            resMap.put("xxjd", itemNow.getXxjd());
            String attachIdList = itemNow.getXxjdAttchList();

            if (attachIdList != null && attachIdList.length() > 0) {
                ProjectMonthPlanItem attCond = new ProjectMonthPlanItem();
                attCond.setAttachList(attachIdList.split(","));
                List<Attach> attchList = projectMonthPlanItemService.getAttachList(attCond);
                if (attchList != null) {
                    StringBuilder txt = new StringBuilder();
                    Attach attach;
                    for (int index = 0; index < attchList.size(); index++) {
                        attach = attchList.get(index);
                        txt.append(attach.getId()).append(",").append(attach.getFileName()).append(",").append(attach.getResourcesName()).append("#");

                    }
                    resMap.put("xxjdAttchList", txt);
                }
            }
        }
        return resMap;
    }

    @RequestMapping(value = "getQuestion.action")
    @ResponseBody
    public Map<String, Object> getQuestion(HttpServletRequest request) throws Exception {
        String prjectId = request.getParameter("projectId");
        ProjectMonthPlanItem itemCond = new ProjectMonthPlanItem();
        itemCond.setProjectId(prjectId);
        List<ProjectMonthPlanItem> questions = projectMonthPlanItemService.queryNewQuestion(itemCond);
        Map<String, Object> resMap = new HashMap<String, Object>();
        resMap.put("solveProblem", "");
        StringBuilder txt = new StringBuilder("");
        if (questions != null) {

            MapList<String, ProjectMonthPlanItem> mapList = new MapList<>();
            for (ProjectMonthPlanItem q : questions) {
                mapList.put(q.getSolveProblemType(), q);
            }
            ProjectQuestionEnum now;
            for (String key : mapList.getKeys()) {
                now = ProjectQuestionEnum.getByCode(key);
                if (now != null) {
                    txt.append(now.getTitle()).append("：");
                    List<ProjectMonthPlanItem> subList = mapList.get(key);
                    for (int i = 0; i < subList.size(); i++) {
                        if (i > 0&& !txt.toString().endsWith("，")) {
                            txt.append("，");
                        }
                        txt.append(subList.get(i).getSolveProblem());
                    }
                    txt.append(";\n");
                }
            }

        }
        resMap.put("solveProblem", txt.toString());
        return resMap;
    }


    /**
     * @return String 跳转的路径
     * @throws Exception
     * @description 已办列表
     * @author
     * @version 2020-07-09
     */
    @RequestMapping(value = "export.action")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String root = !absolutePath ? request.getRealPath("/") : "";
        String pre = root + getUploadBaseDir();
        String headId = request.getParameter("id");
        ProjectMonthPlan headCond = new ProjectMonthPlan();
        headCond.setId(headId);
        ProjectMonthPlan monthNowPlan = projectMonthPlanService.get(headCond);
        if (monthNowPlan == null) {
            Map<String, String> resMap = new HashMap<>();
            resMap.put("code", "405");
            resMap.put("message", "单据不存在");
            ResponseUtil.returnJons(response, JsonUtil.java2Json(resMap));
        }

        ProjectMonthPlanItem itemCond = new ProjectMonthPlanItem();
        itemCond.setHeadId(headId);
        itemCond.addOrderByField("t.ext_num1");
        List<ProjectMonthPlanItem> itemList = projectMonthPlanItemService.queryAll(itemCond);
        XyDicAgg agg = (XyDicAgg) JsonUtil.json2Java(monthNowPlan.getPlanType(), XyDicAgg.class);
        MonthExportExcel mee = new MonthExportExcel(pre,monthNowPlan, itemList, agg);
        mee.build(response);



    }


    @RequestMapping(value = "showItem.action", method = RequestMethod.GET)
    public String showItem(ProjectYearPlan projectPlan, Model model, HttpServletRequest request) throws Exception {
        return "csmp/plan/month/month_item_form";
    }

    @RequestMapping(value = "projectNumDlg.action", method = RequestMethod.GET)
    public String projectNumDlg(ProjectYearPlan projectPlan, Model model, HttpServletRequest request) throws Exception {
        return "csmp/plan/month/projectInfoDlgList";
    }


    @RequestMapping(value = "manageProjectList.action", method = RequestMethod.GET)
    @ResponseBody
    public PageManager manageProjectList(CmProjectInfo entity, PageManager page) {
        if (StringUtil.isEmpty(entity.getOrderBy())) {
            entity.addOrderByFieldDesc("t.CREATE_DATE");
        }
        try {
            User user = SystemSecurityUtils.getUser();
            PageManager page_ = projectInfoService.query(entity, page);
            return page_;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return page;
    }


}

