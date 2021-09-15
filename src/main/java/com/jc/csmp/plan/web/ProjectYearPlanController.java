package com.jc.csmp.plan.web;

import com.jc.csmp.common.WorkflowVarUtil;
import com.jc.csmp.plan.ctrl.domain.ProjectYearPlanCtrl;
import com.jc.csmp.plan.ctrl.service.IProjectYearPlanCtrlService;
import com.jc.csmp.plan.domain.*;
import com.jc.csmp.plan.domain.validator.ProjectPlanValidator;
import com.jc.csmp.plan.kit.*;
import com.jc.csmp.plan.service.IProjectYearPlanItemService;
import com.jc.csmp.plan.service.IProjectYearPlanService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.*;
import com.jc.foundation.web.BaseController;
import com.jc.system.applog.ActionLog;
import com.jc.system.dic.domain.Dic;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.User;
import com.jc.system.security.service.IUserService;
import com.jc.system.util.menuUtil;
import com.jc.workflow.instance.service.IInstanceService;
import com.jc.workflow.util.WorkflowContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author
 * @version 2020-07-09
 * @title
 */

@Controller
@RequestMapping(value = "/plan/projectYearPlan")
public class ProjectYearPlanController extends BaseController {

    @Autowired
    private IProjectYearPlanCtrlService ctrlService;
    @Autowired
    private IInstanceService instanceService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IProjectYearPlanItemService projectYearPlanItemService;
    @Autowired
    private IProjectYearPlanCtrlService projectYearPlanCtrlService;
    @Autowired
    private IProjectYearPlanService projectPlanService;

    @org.springframework.web.bind.annotation.InitBinder("projectPlan")
    public void initBinder(WebDataBinder binder) {
        binder.setValidator(new ProjectPlanValidator());

    }

    public ProjectYearPlanController() {
    }

    /**
     * @return success 是否成功， errorMessage错误信息
     * @throws Exception
     * @description 保存方法
     * @author
     * @version 2020-07-09
     */
    @RequestMapping(value = "saveWorkflow.action", method = RequestMethod.POST)
    @ResponseBody
    @ActionLog(operateModelNm = "建设管理-项目计划", operateFuncNm = "save", operateDescribe = "对建设管理-项目计划进行新增操作")
    public Map<String, Object> saveWorkflow(@Valid ProjectYearPlan projectPlan, BindingResult result,
                                            HttpServletRequest request) throws Exception {

        Map<String, Object> resultMap = validateBean(result);
        if (resultMap.size() > 0) {
            return resultMap;
        }
        // 验证token
        resultMap = validateToken(request);
        if (resultMap.size() > 0) {
            return resultMap;
        }

        if (!"false".equals(resultMap.get("success"))) {
            ProjectYearPlanCtrl ctrl;
            if (ReqType.CODE_1.getCode().equals(projectPlan.getPlanSeqno())) {
                ctrl = ctrlService.getReqCrtl(projectPlan.getPlanYear());
                if (ctrl == null) {
                    resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
                    resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, "非上报期间，不能上报");
                    return resultMap;
                }
            } else if (ReqType.CODE_2.getCode().equals(projectPlan.getPlanSeqno())) {
                ctrl = ctrlService.getChangeCrtl(projectPlan.getPlanYear());
                if (ctrl == null) {
                    resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
                    resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, "非调整期间，不能上报");
                    return resultMap;
                }
            } else {
                resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
                resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, "请选择类型");
                return resultMap;
            }

            projectPlan.setPlanSeqno(ctrl.getSeqno());
            projectPlan.setPlanSeqnoeqnoname(ctrl.getSeqnoname());
            resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
            Integer flag = projectPlanService.saveWorkflow(projectPlan);
            if (flag == 1) {
                resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
                resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_WORKFLOW_001"));
            } else {
                String workflowMessage = WorkflowContext.getErrMsg(flag);
                if (StringUtil.isEmpty(workflowMessage)) {
                    resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
                    resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_WORKFLOW_002"));
                } else {
                    resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
                    resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, workflowMessage);
                }
            }
        }
        return resultMap;
    }

    /**
     * workflowParamTemp修改方法
     *
     * @return Integer 更新的数目
     * @author
     * @version 2020-07-09
     */
    @RequestMapping(value = "updateWorkflow.action", method = RequestMethod.POST)
    @ResponseBody
    @ActionLog(operateModelNm = "建设管理-项目计划", operateFuncNm = "update", operateDescribe = "对建设管理-项目计划进行更新操作")
    public Map<String, Object> updateWorkflow(ProjectYearPlan projectPlan, BindingResult result,
                                              HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = validateBean(result);
        if (resultMap.size() > 0) {
            return resultMap;
        }
        String token = getToken(request);
        resultMap.put(GlobalContext.SESSION_TOKEN, token);
        projectPlan.setPlanSeqno(null);
        Integer flag = projectPlanService.updateWorkflow(projectPlan);

        if (flag == 1) {
            resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
            resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_WORKFLOW_001"));
        } else {
            String workflowMessage = WorkflowContext.getErrMsg(flag);
            if (StringUtil.isEmpty(workflowMessage)) {
                resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
                resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_WORKFLOW_002"));
            } else {
                resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
                resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, workflowMessage);
            }
        }

        return resultMap;
    }

    /**
     * @return String 表单所在地址
     * @throws Exception
     * @description 发起流程进入的方法
     */
    @RequestMapping(value = "startWorkflow.action", method = RequestMethod.GET)
    public String startWorkflow(ProjectYearPlan projectPlan, Model model, HttpServletRequest request) throws Exception {
        Map<String, Object> workflowBean = instanceService.getStartNodeInfo(projectPlan.getWorkflowBean());
        model.addAttribute("workflowBean", workflowBean);
        menuUtil.saveMenuID("/plan/projectYearPlan/startWorkflow.action", request);
        String token = getToken(request);
        model.addAttribute(GlobalContext.SESSION_TOKEN, token);
        String condition = request.getParameter("condition");
        String[] str = condition.split(",");
        String selectId = str[0];
        ProjectYearPlan dbData = null;
        if ("0".equalsIgnoreCase(selectId)) {
            selectId = IdUtil.createId();
        } else {
            dbData = projectPlanService.queryById(selectId);
        }
        request.setAttribute("selectId", selectId);

        if (str.length > 1) {
            request.setAttribute("selectYear", str[1]);
            if (str.length > 2) {
                request.setAttribute("areaCode", str[2]);
            }
            if (str.length > 3) {
                request.setAttribute("areaName", str[3]);
            }
            if (str.length > 4) {
                request.setAttribute("planSeqno", str[4]);
                if (str[4] != null) {
                    try {
                        Integer seqno = Integer.valueOf(str[4]);
                        if (seqno > 1) {
                            ProjectYearPlan cond = new ProjectYearPlan();
                            cond.setPlanYear(Integer.valueOf(str[1].trim()));
                            cond.setPlanAreaCode(str[2].trim());
                            cond.setPlanSeqno(seqno - 1);
                            ProjectYearPlan lastData = projectPlanService.get(cond);
                            if (lastData != null) {
                                request.getSession().setAttribute("lastData", lastData);
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();

                    }
                }
            }
        } else {
            if (dbData != null) {
                request.setAttribute("selectYear", dbData.getPlanYear());
                request.setAttribute("areaCode", dbData.getPlanAreaCode());
                request.setAttribute("areaName", dbData.getPlanAreaName());
                request.setAttribute("planSeqno", dbData.getPlanSeqno());
            }
        }


        request.setAttribute("pageMode", "NEW");
        Map<String, Object> varMap = WorkflowVarUtil.buildItemCtrl(workflowBean, projectPlan.getWorkflowBean().getOpenType_());
        request.setAttribute("workflowConfig", varMap);
        return "csmp/plan/projectYearPlan/projectYearPlan_workflow_form";
    }


    /**
     * @return String 表单所在地址
     * @throws Exception
     * @description 打开流程进入的方法
     */
    @RequestMapping(value = "getData.action")
    @ResponseBody
    public Map<String, Object> getData(ProjectYearPlan projectPlan, HttpServletRequest request) throws Exception {
        Map<String, Object> resMap = new HashMap<>();
        ProjectYearPlan dbData = projectPlanService.get(projectPlan);
        resMap.put("head", dbData);
        resMap.put("dataNew", "N");
        if (dbData == null) {
            ProjectYearPlan lastData = (ProjectYearPlan) request.getSession().getAttribute("lastData");
            if (lastData != null) {
                //清理数据
                request.getSession().removeAttribute("lastData");
                ProjectYearPlanItem itemCond = new ProjectYearPlanItem();
                itemCond.setHeadId(lastData.getId());
                itemCond.addOrderByField("t.ext_num1");
                List<ProjectYearPlanItem> itemList = projectYearPlanItemService.queryAll(itemCond);

                //清洗
                lastData.setId(projectPlan.getId());
                lastData.setPiId(null);
                lastData.setModifyDate(null);
                lastData.setPlanSeqno(ReqType.CODE_2.getCode());
                lastData.setCreateDate(null);
                for (ProjectYearPlanItem item : itemList) {
                    item.setId(IdUtil.createId());
                    item.setHeadId(lastData.getId());
                    item.setModifyDate(null);
                    item.setCreateDate(null);
                }

                resMap.put("head", lastData);
                if (lastData.getPlanType() != null) {
                    XyDicAgg agg = (XyDicAgg) JsonUtil.json2Java(lastData.getPlanType(), XyDicAgg.class);
                    resMap.put("dicInfo", agg);
                    request.getSession().setAttribute("dicAllInfo", resMap.get("dicInfo"));
                }
                resMap.put("itemList", itemList);
                resMap.put("dataNew", "Y");
                return resMap;

            } else {
                List<Dic> list = projectPlanService.selectPPTDic();
                resMap.put("dicInfo", DicUtil.buildTree(list));
                request.getSession().setAttribute("dicAllInfo", resMap.get("dicInfo"));
                return resMap;
            }
        } else {

            if (dbData.getPlanType() != null) {
                XyDicAgg agg = (XyDicAgg) JsonUtil.json2Java(dbData.getPlanType(), XyDicAgg.class);
                resMap.put("dicInfo", agg);
                request.getSession().setAttribute("dicAllInfo", resMap.get("dicInfo"));
            }
            ProjectYearPlanItem itemCond = new ProjectYearPlanItem();
            itemCond.setHeadId(dbData.getId());
            itemCond.addOrderByField("t.ext_num1");
            List<ProjectYearPlanItem> itemList = projectYearPlanItemService.queryAll(itemCond);
            resMap.put("itemList", itemList);
        }


        return resMap;
    }

    @RequestMapping(value = "showItem.action", method = RequestMethod.GET)
    public String showItem(ProjectYearPlan projectPlan, Model model, HttpServletRequest request) throws Exception {
        return "csmp/plan/projectYearPlan/projectYearPlan_item_form";
    }

    @RequestMapping(value = "showViewItem.action", method = RequestMethod.GET)
    public String showViewItem(ProjectYearPlan projectPlan, Model model, HttpServletRequest request) throws Exception {
        return "csmp/plan/projectYearPlan/projectYearPlan_item_viewform";
    }

    /**
     * @return String 表单所在地址
     * @throws Exception
     * @description 打开流程进入的方法
     */
    @RequestMapping(value = "loadWorkflow.action", method = RequestMethod.GET)
    public String loadWorkflow(ProjectYearPlan projectPlan, Model model, HttpServletRequest request) throws Exception {
        ProjectYearPlan projectPlan_temp = new ProjectYearPlan();
        projectPlan_temp.setPiId(projectPlan.getWorkflowBean().getBusiness_Key_());
        projectPlan_temp = projectPlanService.get(projectPlan_temp);
        fillPageInfo(model, request);
        Map<String, Object> workflowBean = instanceService.getDefaultNodeInfo(projectPlan.getWorkflowBean());
        model.addAttribute("projectPlan", projectPlan_temp);
        model.addAttribute("businessJson", JsonUtil.java2Json(projectPlan_temp));
        model.addAttribute("workflowBean", workflowBean);
        String token = getToken(request);
        model.addAttribute(GlobalContext.SESSION_TOKEN, token);
        Map<String, Object> varMap = WorkflowVarUtil.buildItemCtrl(workflowBean, projectPlan.getWorkflowBean().getOpenType_());
        request.setAttribute("workflowConfig", varMap);
        request.setAttribute("pageMode", "EDIT");
        return "csmp/plan/projectYearPlan/projectYearPlan_workflow_form";
    }


    /**
     * @return String 表单所在地址
     * @throws Exception
     * @description 打开流程进入的方法
     */
    @RequestMapping(value = "loadView.action", method = RequestMethod.GET)
    public String loadView(ProjectYearPlan projectPlanCond, Model model, HttpServletRequest request) throws Exception {
        fillPageInfo(model, request);
        ProjectYearPlan projectPlan = projectPlanService.get(projectPlanCond);
        request.setAttribute("selectId", projectPlan.getId());
        request.setAttribute("selectYear", projectPlan.getPlanYear());
        request.setAttribute("areaCode", projectPlan.getPlanAreaCode());
        request.setAttribute("areaName", projectPlan.getPlanAreaName());
        request.setAttribute("pageMode", "VIEW");
        //环境变量
        Map<String, Object> varMap = new HashMap<>();
        request.setAttribute("workflowConfig", varMap);
        List<SuggestVO> suggestList = projectPlanService.querySuggest(projectPlan);
        request.setAttribute("suggestList", suggestList);

        return "csmp/plan/projectYearPlan/projectYearPlan_workflow_form";
    }

    /**
     * 查询意见域
     */
    @RequestMapping(value = "loadSuggest.action")
    @ResponseBody
    public List<SuggestVO> loadSuggest(ProjectYearPlan projectPlanCond) throws Exception {
        ProjectYearPlan projectPlan = projectPlanService.get(projectPlanCond);
        List<SuggestVO> suggestList = projectPlanService.querySuggest(projectPlan);
        return suggestList;
    }

    /**
     * /instance/toStartProcess.action?definitionKey_=yearplan001
     *
     * @return String 跳转的路径
     * @throws Exception
     * @description 列表
     * @author
     * @version 2020-07-09
     */
    @RequestMapping(value = "manage.action", method = RequestMethod.GET)
    public String manage(Model model, HttpServletRequest request) throws Exception {
        menuUtil.saveMenuID("/plan/projectYearPlan/manage.action", request);
        fillPageInfo(model, request);
        return "csmp/plan/projectYearPlan/projectYearPlan_workflow_list";
    }

    /**
     * /instance/toStartProcess.action?definitionKey_=yearplan001
     *
     * @return String 跳转的路径
     * @throws Exception
     * @description 列表
     * @author
     * @version 2020-07-09
     */
    @RequestMapping(value = "manageReq.action", method = RequestMethod.GET)
    public String manageReq(Model model, HttpServletRequest request) throws Exception {
        menuUtil.saveMenuID("/plan/projectYearPlan/manageReq.action", request);
        request.setAttribute("planSeqno", "1");
        request.setAttribute("pageMode", "EDIT");
        fillPageInfo(model, request);
        return "csmp/plan/projectYearPlan/projectYearPlan_workflow_list";
    }

    /**
     * /instance/toStartProcess.action?definitionKey_=yearplan001
     *
     * @return String 跳转的路径
     * @throws Exception
     * @description 列表
     * @author
     * @version 2020-07-09
     */
    @RequestMapping(value = "manageChange.action", method = RequestMethod.GET)
    public String manageChange(Model model, HttpServletRequest request) throws Exception {
        menuUtil.saveMenuID("/plan/projectYearPlan/manageChange.action", request);
        request.setAttribute("planSeqno", "2");
        request.setAttribute("pageMode", "EDIT");
        fillPageInfo(model, request);
        return "csmp/plan/projectYearPlan/projectYearPlan_workflow_list2";
    }

    /**
     * /instance/toStartProcess.action?definitionKey_=yearplan001
     *
     * @return String 跳转的路径
     * @throws Exception
     * @description 列表
     * @author
     * @version 2020-07-09
     */
    @RequestMapping(value = "manageView.action", method = RequestMethod.GET)
    public String manageView(Model model, HttpServletRequest request) throws Exception {
        menuUtil.saveMenuID("/plan/projectYearPlan/manageView.action", request);
        request.setAttribute("pageMode", "VIEW");
        fillPageInfo(model, request);
        return "csmp/plan/projectYearPlan/projectYearPlan_workflow_search";
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
        request.setAttribute("planSeqno", request.getParameter("planSeqno"));
        return "/csmp/plan/projectYearPlan/projectYearPlan_workflow_index";
    }

    /**
     * @param projectPlan 实体类
     * @param page        分页实体类
     * @return PagingBean 查询结果
     * @throws Exception
     * @description 待办数据
     * @author
     * @version 2020-07-09
     */
    @RequestMapping(value = "manageList.action", method = RequestMethod.GET)
    @ResponseBody
    public PageManager manageList(ProjectYearPlan projectPlan, PageManager page) {
        //默认排序
        if (StringUtil.isEmpty(projectPlan.getOrderBy())) {
            projectPlan.addOrderByFieldDesc("t.CREATE_DATE");
        }
        PageManager page_ = null;
        User user = SystemSecurityUtils.getUser();
//        String code = DeptCacheUtil.getCodeById(user.getDeptId());
//        projectPlan.setRoleCode(code);
        try {
            page_ = projectPlanService.query(projectPlan, page);
            List<ProjectYearPlan> dataList = (List<ProjectYearPlan>) page_.getData();
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
                for (ProjectYearPlan item : dataList) {
                    User u = userMap.get(item.getCreateUser());
                    if (u != null) {
                        item.setOwer(u.getDisplayName());
                        if (item.getCreateUser().equalsIgnoreCase(user.getId())) {
                            item.setCanChange("Y");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        GlobalUtil.setTableRowNo(page_, page_.getPageRows());
        return page_;
    }


    /**
     * @param projectPlan 实体类
     * @param page        分页实体类
     * @return PagingBean 查询结果
     * @throws Exception
     * @description 待办数据
     * @author
     * @version 2020-07-09
     */
    @RequestMapping(value = "manageList2.action", method = RequestMethod.GET)
    @ResponseBody
    public PageManager manageList2(ProjectYearPlan projectPlan, PageManager page) {
        //默认排序
        if (StringUtil.isEmpty(projectPlan.getOrderBy())) {
            projectPlan.addOrderByFieldDesc("t.CREATE_DATE");
        }
        int nowPlanSeqno = ReqType.CODE_2.getCode();
        PageManager page_ = null;
        User user = SystemSecurityUtils.getUser();
        //是否可以调整
        int year = LocalDate.now().getYear();
        ProjectYearPlanCtrl nowCtrl = projectYearPlanCtrlService.getChangeCrtl(year);
        boolean canNew = false;
        if (nowCtrl != null && nowCtrl.getSeqno().equals(nowPlanSeqno)) {
            canNew = true;
        }
        //调整期
        if (canNew) {

            projectPlan.setQueryCanChangeSeqNo(nowPlanSeqno);
            projectPlan.setPlanSeqno(null);
            //分析哪些地区是需要显示上一期的
            List<ProjectYearPlan> dbList = projectPlanService.queryByYear(year);
            if (dbList != null && dbList.size() > 0) {
                Map<String, ProjectYearPlan> areaMap = new HashMap<>();
                ProjectYearPlan temp;
                for (ProjectYearPlan item : dbList) {
                    temp = areaMap.get(item.getPlanAreaCode());
                    if (temp == null) {
                        areaMap.put(item.getPlanAreaCode(), item);
                    } else if (temp.getPlanSeqno() < item.getPlanSeqno()) {
                        areaMap.put(item.getPlanAreaCode(), item);
                    }
                }
                List<String> ids = new ArrayList<>();
                for (ProjectYearPlan areaItem : areaMap.values()) {
                    if (areaItem.getPlanSeqno() < nowPlanSeqno && PlanStatusUtil.pass.equals(areaItem.getPlanStatus())) {
                        ids.add(areaItem.getId());
                    }
                }
                if (ids.size() > 0) {
                    projectPlan.setSpecialIds(ids.toArray(new String[0]));
                }
            }
        }
//        String code = DeptCacheUtil.getCodeById(user.getDeptId());
//        projectPlan.setRoleCode(code);
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");

        try {
            page_ = projectPlanService.query(projectPlan, page);
            List<ProjectYearPlan> dataList = (List<ProjectYearPlan>) page_.getData();
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
                for (ProjectYearPlan item : dataList) {
                    if (ReqType.CODE_1.getCode().equals(item.getPlanSeqno())) {
                        item.setCanChange("NEW");
                    } else {
                        User u = userMap.get(item.getCreateUser());
                        if (u != null) {
                            item.setOwer(u.getDisplayName());
                            if (item.getCreateUser().equalsIgnoreCase(user.getId())) {
                                item.setCanChange("Y");
                            }
                        }
                    }
                    if (item.getPlanYear().equals(year) && item.getPlanSeqno().equals(ReqType.CODE_1.getCode())) {
                        item.setChanageDateBegin(nowCtrl.getRequestStartDate());
                        item.setChanageDateEnd(nowCtrl.getRequestEndDate());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        GlobalUtil.setTableRowNo(page_, page_.getPageRows());
        return page_;
    }

    /**
     * @return String 跳转的路径
     * @throws Exception
     * @description 待办列表
     * @author
     * @version 2020-07-09
     */
    @RequestMapping(value = "todoList.action", method = RequestMethod.GET)
    @ActionLog(operateModelNm = "建设管理-项目计划", operateFuncNm = "manageForApplyList", operateDescribe = "对建设管理-项目计划进行申请列表跳转工作")
    public String todoList(Model model, HttpServletRequest request) throws Exception {
        menuUtil.saveMenuID("/plan/projectYearPlan/todoList.action", request);
        fillPageInfo(model, request);
        return "/csmp/plan/projectYearPlan/projectYearPlan_workflow_todoList";
    }

    /**
     * @param projectPlan 实体类
     * @param page        分页实体类
     * @return PagingBean 查询结果
     * @throws Exception
     * @description 待办数据
     * @author
     * @version 2020-07-09
     */
    @RequestMapping(value = "manageTodoList.action", method = RequestMethod.GET)
    @ResponseBody
    public PageManager manageTodoList(ProjectYearPlan projectPlan, PageManager page) {
        //默认排序
        if (StringUtil.isEmpty(projectPlan.getOrderBy())) {
            projectPlan.addOrderByFieldDesc("t.CREATE_DATE");
        }
        PageManager page_ = null;
        projectPlan.setCurUserId(SystemSecurityUtils.getUser().getId() + "");
        try {
            page_ = projectPlanService.workFlowTodoQueryByPage(projectPlan, page);
            GlobalUtil.setTableRowNo(page_, page_.getPageRows());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return page_;
    }

    /**
     * @return String 跳转的路径
     * @throws Exception
     * @description 已办列表
     * @author
     * @version 2020-07-09
     */
    @RequestMapping(value = "doneList.action", method = RequestMethod.GET)
    @ActionLog(operateModelNm = "建设管理-项目计划", operateFuncNm = "projectPlanSearch", operateDescribe = "对建设管理-项目计划进行查询列表跳转工作")
    public String doneList(Model model, HttpServletRequest request) throws Exception {
        menuUtil.saveMenuID("plan/projectYearPlan/doneList.action", request);
        fillPageInfo(model, request);
        return "/csmp/plan/projectYearPlan/projectYearPlan_workflow_doneList";
    }

    /**
     * @param projectPlan 实体类
     * @param page        分页实体类
     * @return PagingBean 查询结果
     * @throws Exception
     * @description 已办数据
     * @author
     * @version 2020-07-09
     */
    @RequestMapping(value = "manageDoneList.action", method = RequestMethod.GET)
    @ResponseBody
    public PageManager manageDoneList(ProjectYearPlan projectPlan, PageManager page, HttpServletRequest request) throws CustomException {
        //默认排序
        if (StringUtil.isEmpty(projectPlan.getOrderBy())) {
            projectPlan.addOrderByFieldDesc("t.CREATE_DATE");
        }
        PageManager page_ = null;
        projectPlan.setCurUserId(SystemSecurityUtils.getUser().getId() + "");
        try {
            page_ = projectPlanService.workFlowDoneQueryByPage(projectPlan, page);
            GlobalUtil.setTableRowNo(page_, page_.getPageRows());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return page_;
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
    public Map<String, Object> deleteByIds(ProjectYearPlan entity, String ids) throws Exception {
        Map<String, Object> resultMap = new HashMap<>(2);
        entity.setPrimaryKeys(ids.split(","));
        if (projectPlanService.deleteByIds(entity) != 0) {
            resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
            resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
        } else {
            resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
            resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
        }
        return resultMap;
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
        String yearStr = request.getParameter("selectYear");
        String areaCode = request.getParameter("selectAreaCode");
        String planSeqno = request.getParameter("planSeqno");
        try {
            ProjectYearPlanCtrl ctrl = null;
            if (ReqType.CODE_1.getCode().equals(Integer.valueOf(planSeqno))) {
                ctrl = ctrlService.getReqCrtl(Integer.valueOf(yearStr));
                if (ctrl == null) {
                    return Result.failure(1003, "非上报期间，不能制定年度计划");
                }
                if (!planSeqno.equalsIgnoreCase(String.valueOf(ctrl.getSeqno()))) {
                    return Result.failure(1003, "非上报期间，不能制定年度计划");
                }
            } else if (ReqType.CODE_2.getCode().equals(Integer.valueOf(planSeqno))) {
                ctrl = ctrlService.getChangeCrtl(Integer.valueOf(yearStr));
                if (ctrl == null) {
                    return Result.failure(1003, "非调整期间，不能制定年度计划");
                }
                if (!planSeqno.equalsIgnoreCase(String.valueOf(ctrl.getSeqno()))) {
                    return Result.failure(1003, "非调整期间，不能制定年度计划");
                }
            }
            ProjectYearPlan cond = new ProjectYearPlan();
            cond.setPlanYear(Integer.valueOf(yearStr));
            cond.setPlanAreaCode(areaCode);
            cond.setPlanSeqno(ctrl.getSeqno());
            List<ProjectYearPlan> dataList = projectPlanService.queryAll(cond);
            if (dataList == null || dataList.size() == 0) {
                return Result.success();
            } else {
                if (ReqType.CODE_1.getCode().toString().equals(planSeqno)) {
                    return Result.failure(1001, "该年份已经制定过计划，不能重复制定！");
                } else {
                    return Result.failure(1001, "年度计划已调整，请刷新后重试！");
                }
            }
        } catch (Exception ex) {
            return Result.failure(1000, ex.getMessage());
        }
    }

    /**
     * 导入Excel
     *
     * @param excel
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "importExcel.action")
    public void importExcel(@RequestParam MultipartFile excel, HttpServletRequest request, HttpServletResponse response) throws Exception {
        long start = System.currentTimeMillis();
        log.info("importBill start");
        try {
            XyDicAgg agg = (XyDicAgg) request.getSession().getAttribute("dicAllInfo");
            List<ProjectYearPlanItem> dataList = importBill(excel, agg);
            request.getSession().setAttribute("dataList", dataList);
            returnMsg(response, "true", "");
        } catch (Exception ex) {
            ex.printStackTrace();
            returnMsg(response, "false", ex.getMessage());
        } finally {
            log.info("importBill end,耗时：" + (System.currentTimeMillis() - start));
        }

    }

    /**
     * 返回前台信息
     *
     * @param response
     * @param success
     * @param message
     */
    private void returnMsg(HttpServletResponse response, String success, String message) {
        try {
            response.setHeader("Content-Type", "text/html; charset=utf-8");
            Writer writer = response.getWriter();
            writer.write("{\"success\":\"" + success + "\",\"message\":\"" + message + "\"}");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "getImportExcelData.action")
    @ResponseBody
    public List<ProjectYearPlanItem> getImportExcelData(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return (List<ProjectYearPlanItem>) request.getSession().getAttribute("dataList");
    }

    /**
     * 导入
     *
     * @param excelFile
     * @return
     * @throws CustomException
     * @throws IOException
     */
    private List<ProjectYearPlanItem> importBill(MultipartFile excelFile, XyDicAgg agg) throws CustomException {
        List<ProjectYearPlanItem> dataList = null;
        // 构建数据
        try {
            dataList = ItemImportTool.assembleList(excelFile, agg);
            if (dataList == null || dataList.size() == 0) {
                throw new CustomException("文件内容为空，导入失败");
            }
            return dataList;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new CustomException(ex.getMessage());
        }

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
        String headId = request.getParameter("id");
        ProjectYearPlan headCond = new ProjectYearPlan();
        headCond.setId(headId);
        ProjectYearPlan nowPlan = projectPlanService.get(headCond);
        if (nowPlan == null) {
            Map<String, String> resMap = new HashMap<>();
            resMap.put("code", "405");
            resMap.put("message", "单据不存在");
            ResponseUtil.returnJons(response, JsonUtil.java2Json(resMap));
        }

        ProjectYearPlanItem itemCond = new ProjectYearPlanItem();
        itemCond.setHeadId(headId);
        itemCond.addOrderByField("t.ext_num1");
        List<ProjectYearPlanItem> itemList = projectYearPlanItemService.queryAll(itemCond);
        XyDicAgg agg = (XyDicAgg) JsonUtil.json2Java(nowPlan.getPlanType(), XyDicAgg.class);
        YearExportExcel mee = new YearExportExcel(nowPlan, itemList, agg);
        mee.build(response);
    }


}