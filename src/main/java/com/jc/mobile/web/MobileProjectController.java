package com.jc.mobile.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jc.csmp.common.enums.WorkflowAuditStatusEnum;
import com.jc.csmp.productionReport.domain.ProjectProductionReport;
import com.jc.csmp.productionReport.service.IProjectProductionReportService;
import com.jc.csmp.project.domain.*;
import com.jc.csmp.project.plan.domain.CmProjectPlan;
import com.jc.csmp.project.plan.domain.CmProjectPlanImages;
import com.jc.csmp.project.plan.domain.CmProjectPlanStage;
import com.jc.csmp.project.plan.service.ICmProjectPlanImagesService;
import com.jc.csmp.project.plan.service.ICmProjectPlanService;
import com.jc.csmp.project.plan.service.ICmProjectPlanStageService;
import com.jc.csmp.project.service.*;
import com.jc.csmp.ptProject.domain.ProjectInfo;
import com.jc.csmp.ptProject.service.IProjectInfoService;
import com.jc.foundation.domain.Attach;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.*;
import com.jc.mobile.basic.web.MobileController;
import com.jc.mobile.log.domain.MobileReportLog;
import com.jc.mobile.log.enums.MobileReportLogEnum;
import com.jc.mobile.log.service.IMobileReportLogService;
import com.jc.mobile.util.MobileApiResponse;
import com.jc.resource.enums.ResourceEnums;
import com.jc.resource.enums.service.IResourceService;
import com.jc.resource.util.HttpClientUtil;
import com.jc.system.common.service.ICommonService;
import com.jc.system.content.service.IAttachService;
import com.jc.system.number.service.Number;
import com.jc.system.number.service.interfaces.INumber;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.Department;
import com.jc.system.security.domain.User;
import com.jc.system.security.service.IDepartmentService;
import com.jc.system.security.util.DeptCacheUtil;
import com.jc.system.security.util.UserUtils;
import com.jc.workflow.definition.service.IDefinitionService;
import com.jc.workflow.definition.service.IRuntimeInfoService;
import com.jc.workflow.external.WorkflowBean;
import com.jc.workflow.task.service.operation.CreateOperationAbstractImpl;
import com.jc.workflow.task.service.operation.OperationAbstract;
import com.jc.workflow.task.service.operation.SubmitOperationAbstractImpl;
import com.jc.workflow.util.CustomWorkflowUtil;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author 常鹏
 * @Date 2020/8/3 16:13
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/mobile/project")
public class MobileProjectController extends MobileController {
    @Autowired
    private ICmProjectInfoService cmProjectInfoService;
    @Autowired
    private IDepartmentService departmentService;
    @Autowired
    private ICmProjectPlanService cmProjectPlanService;
    @Autowired
    private IDefinitionService definitionService;
    @Autowired
    private ICmProjectQuestionService cmProjectQuestionService;
    @Autowired
    protected IRuntimeInfoService runtimeInfoService;
    @Autowired
    private ICmProjectPlanStageService cmProjectPlanStageService;
    @Autowired
    private ICmProjectWeeklyService cmProjectWeeklyService;
    @Autowired
    private ICmProjectWeeklyItemService cmProjectWeeklyItemService;
    @Autowired
    private IMobileReportLogService mobileReportLogService;
    @Autowired
    private ICmProjectPlanImagesService cmProjectPlanImagesService;
    @Autowired
    private IProjectProductionReportService projectProductionReportService;
    @Autowired
    private IAttachService attachService;
    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private ICommonService commonService;
    @Autowired
    private ICmProjectPersonLnglatService cmProjectPersonLnglatService;

    /**
     * 根据编号获取详细信息
     * @param projectNumber
     * @param request
     * @return
     */
    @RequestMapping(value="getByNumber.action",method= RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse getByNumber(@RequestParam("projectNumber") String projectNumber, HttpServletRequest request){
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        return MobileApiResponse.ok(this.cmProjectInfoService.getbyProjectNumber(projectNumber));
    }
    /**
     * 项目详细信息
     * @param projectId
     * @param request
     * @return
     */
    @RequestMapping(value="projectDetail.action",method= RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse projectDetail(@RequestParam("projectId") String projectId, HttpServletRequest request){
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        return MobileApiResponse.ok(this.cmProjectInfoService.getById(projectId));
    }

    /**
     * 获取项目信息
     * @param entity
     * @param page
     * @param request
     * @return
     */
    @RequestMapping(value="projectPage.action",method= RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse projectPage(CmProjectInfo entity, PageManager page, HttpServletRequest request){
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        String userId = apiResponse.getUserId();
        if(StringUtil.isEmpty(entity.getOrderBy())) {
            entity.addOrderByFieldDesc("t.CREATE_DATE");
        }
        if (entity.getProjectName() == null) {
            String projectName = request.getParameter("projectName");
            if (!StringUtil.isEmpty(projectName)) {
                entity.setProjectName(projectName);
            }
        }
        User user = UserUtils.getUser(userId);
        entity.setDeptCodeCondition(DeptCacheUtil.getCodeById(user.getDeptId()));
        PageManager page_ = cmProjectInfoService.query(entity, page);
        return MobileApiResponse.ok(page_);
    }

    /**
     * 根据登录用户获取参与项目
     * @param request
     * @return
     * @throws CustomException
     */
    @RequestMapping(value="projectListByUser.action",method= RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse projectPage(HttpServletRequest request) throws CustomException {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        CmProjectInfo entity = new CmProjectInfo();
        String userId = apiResponse.getUserId();
        if(StringUtil.isEmpty(entity.getOrderBy())) {
            entity.addOrderByFieldDesc("t.CREATE_DATE");
        }
        User user = UserUtils.getUser(userId);
        entity.setDeptCodeCondition(DeptCacheUtil.getCodeById(user.getDeptId()));
        return MobileApiResponse.ok(cmProjectInfoService.queryAll(entity));
    }

    /**
     * 根据项目获取阶段
     * @param projectId
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/plan/stageListByProjectId.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse stageListByProjectId(@RequestParam("projectId") String projectId, HttpServletRequest request) throws Exception {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        CmProjectPlanStage stage = new CmProjectPlanStage();
        stage.setProjectId(projectId);
        stage.addOrderByField(" t.queue ");
        return MobileApiResponse.ok(this.cmProjectPlanStageService.queryAll(stage));
    }

    /**
     * 根据项目获取计划
     * @param projectId
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/plan/planListByProjectId.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse planListByProjectId(@RequestParam("projectId") String projectId, HttpServletRequest request) throws Exception {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        CmProjectPlan plan = new CmProjectPlan();
        plan.setProjectId(projectId);
        String stageId = request.getParameter("stageId");
        if (!StringUtil.isEmpty(stageId)) {
            plan.setStageId(stageId);
        }
        plan.addOrderByField(" t.queue ");
        return MobileApiResponse.ok(this.cmProjectPlanService.queryAll(plan));
    }

    /**
     * 根据项目id获取项目计划信息
     * @param entity
     * @param page
     * @param request
     * @return
     */
    @RequestMapping(value="/plan/pageList.action", method=RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse planPageList(CmProjectPlan entity, PageManager page, HttpServletRequest request) {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        entity.addOrderByField(" stage.queue, t.queue ");
        return MobileApiResponse.ok(this.cmProjectPlanService.query(entity, page));
    }

    /**
     * 获取计划详情
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value="/plan/detail.action", method=RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse planDetail(@RequestParam("id") String id, HttpServletRequest request) {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        return MobileApiResponse.ok(this.cmProjectPlanService.getById(id));
    }

    /**
     * 更新进度，参数如下
     * @param entity
     * @param request
     * @return
     */
    @RequestMapping(value="/plan/updateProgress.action", method=RequestMethod.POST)
    @ResponseBody
    public MobileApiResponse planUpdateProgress(@RequestBody CmProjectPlan entity, HttpServletRequest request) {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        try {
            Integer completionRatio = entity.getCompletionRatio() == null ? 0 : entity.getCompletionRatio();
            if (completionRatio.intValue() > 100) {
                completionRatio = 100;
            }
            BigDecimal completionMoney = entity.getCompletionMoney() == null ? new BigDecimal(0) : entity.getCompletionMoney();
            Result result = this.cmProjectPlanService.modifyProgress(entity.getId(), completionRatio, completionMoney,
                    entity.getActualStartDate(), entity.getActualEndDate());
            //记录移动端所需的上报日志
            mobileReportLogService.saveLog(MobileReportLogEnum.progress, result.getData(), UserUtils.getUser(apiResponse.getUserId()));
            return MobileApiResponse.ok(result);
        } catch (CustomException e) {
            e.printStackTrace();
            return MobileApiResponse.error(e.getMessage());
        }
    }

    /**
     * 反馈，参数如下
     * @param entity
     * @param request
     * @return
     */
    @RequestMapping(value="/plan/feedback.action", method=RequestMethod.POST)
    @ResponseBody
    public MobileApiResponse planFeedback(@RequestBody CmProjectPlan entity, HttpServletRequest request) {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        try {
            CmProjectPlan param = this.cmProjectPlanService.getById(entity.getId());
            param.setFeedback(entity.getFeedback());
            param.setFeedbackUser(apiResponse.getUserId());
            return MobileApiResponse.ok(this.cmProjectPlanService.updateEntity(param));
        } catch (CustomException e) {
            e.printStackTrace();
            return MobileApiResponse.error(e.getMessage());
        }
    }

    /**
     * 编码生成器
     * @param key：前缀
     * @return
     */
    @RequestMapping(value="getNumberCode.action", method=RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse getNumberCode(@RequestParam("key") String key) {
        if (StringUtil.isEmpty(key)) {
            return MobileApiResponse.error(ResultCode.PARAM_IS_BLANK);
        }
        INumber number = new Number();
        String no = number.getNumber(key, 1, 2, null);
        return MobileApiResponse.ok(no.substring(1));
    }

    /**
     * 问题上报
     * @param entity
     * @param request
     * @return
     */
    @RequestMapping(value="/question/report.action", method=RequestMethod.POST)
    @ResponseBody
    public MobileApiResponse questionReport(@RequestBody CmProjectQuestion entity, HttpServletRequest request) {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        String questionType = entity.getQuestionType();
        if (StringUtil.isEmpty(questionType)) {
            return MobileApiResponse.error(ResultCode.PARAM_IS_BLANK);
        }

        Map<String, String> workflowAuditUser = new HashMap<>(2);
        workflowAuditUser.put("partaUnitLeaderId", entity.getPartaUnitLeaderId());
        workflowAuditUser.put("superviseLeaderId", entity.getSuperviseLeaderId());
        entity.setExtStr1(JsonUtil.java2Json(workflowAuditUser));
        entity.setPiId(entity.getWorkflowBean().getBusiness_Key_());
        try {
            entity.setAuditStatus(WorkflowAuditStatusEnum.ing.toString());
            Result res = this.cmProjectQuestionService.saveEntity(entity);
            if (res.getCode().intValue() == ResultCode.SUCCESS.code().intValue()) {
                //记录日志
                mobileReportLogService.saveLog(MobileReportLogEnum.question, entity, UserUtils.getUser(apiResponse.getUserId()));
                WorkflowBean wkBean = CustomWorkflowUtil.ssemblyStartBean(questionType + "Question", entity.getPiId(), apiResponse.getUserId(), entity.getPartaUnitLeaderId(), definitionService, runtimeInfoService);
                OperationAbstract operation = new CreateOperationAbstractImpl();
                operation.excute(wkBean);
                operation = new SubmitOperationAbstractImpl();
                operation.excute(wkBean);
                return MobileApiResponse.okMsg("提交成功");
            }
            return MobileApiResponse.error("提交失败");
        } catch (Exception ex) {
            return MobileApiResponse.error(ex.getMessage());
        }
    }

    /**
     * 问题列表
     * @param entity
     * @param page
     * @param request
     * @return
     */
    @RequestMapping(value="/question/list.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse questionList(CmProjectQuestion entity, PageManager page, HttpServletRequest request) {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        //是否分页表示
        String isPage = request.getParameter("isPage");
        entity.addOrderByFieldDesc(" t.create_date ");

        if (isPage != null && GlobalContext.TRUE.equals(isPage)) {
            //分页
            PageManager page_ = this.cmProjectQuestionService.query(entity, page);
            GlobalUtil.setTableRowNo(page_, page.getPageRows());
            return MobileApiResponse.ok(page_);
        } else {
            //不分页直接查询列表
            try {
                List<CmProjectQuestion> dataList = this.cmProjectQuestionService.queryAll(entity);
                return MobileApiResponse.ok(dataList);
            } catch (CustomException e) {
                e.printStackTrace();
                return MobileApiResponse.error(e.getMessage());
            }
        }
    }

    /**
     * 问题详细
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value="/question/detail.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse questionDetail(@RequestParam("id") String id, HttpServletRequest request) {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        return MobileApiResponse.ok(this.cmProjectQuestionService.getById(id));
    }

    /**
     * 根据项目获取周报列表
     * @param request
     * @param page
     * @return
     */
    @RequestMapping(value = "/weekly/list.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse weeklyList(HttpServletRequest request, PageManager page) {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        //项目id
        String projectId = request.getParameter("projectId");
        String projectName = request.getParameter("projectName");
        String region = request.getParameter("region");
        String projectType = request.getParameter("projectType");
        String buildType = request.getParameter("buildType");
        //是否分页表示
        String isPage = request.getParameter("isPage");
        CmProjectWeekly entity = new CmProjectWeekly();
        if (!StringUtil.isEmpty(projectName)) {
            entity.setProjectName(projectName);
        }
        if (!StringUtil.isEmpty(region)) {
            entity.setRegion(region);
        }
        if (!StringUtil.isEmpty(projectType)) {
            entity.setProjectType(projectType);
        }
        if (!StringUtil.isEmpty(buildType)) {
            entity.setBuildType(buildType);
        }
        entity.setProjectId(projectId);
        //entity.addOrderByFieldDesc(" t.if_release ");
        //entity.addOrderByFieldDesc(" t.release_date ");
        entity.addOrderByFieldDesc(" t.create_date ");
        if (isPage != null && GlobalContext.TRUE.equals(isPage)) {
            //分页
            PageManager page_ = this.cmProjectWeeklyService.query(entity, page);
            GlobalUtil.setTableRowNo(page_, page.getPageRows());
            return MobileApiResponse.ok(page_);
        } else {
            //不分页直接查询列表
            try {
                List<CmProjectWeekly> dataList = this.cmProjectWeeklyService.queryAll(entity);
                return MobileApiResponse.ok(dataList);
            } catch (CustomException e) {
                e.printStackTrace();
                return MobileApiResponse.error(e.getMessage());
            }
        }
    }

    /**
     * 周报上报
     * @param entity
     * @param request
     * @return
     */
    @RequestMapping(value = "/weekly/report.action", method = RequestMethod.POST)
    @ResponseBody
    public MobileApiResponse weeklyReport(@RequestBody CmProjectWeekly entity, HttpServletRequest request) {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        User user = UserUtils.getUser(apiResponse.getUserId());
        if (user == null) {
            return MobileApiResponse.error(ResultCode.DATA_IS_WRONG);
        }
        return this.cmProjectWeeklyService.mobileReport(entity, user);
    }

    /**
     * 周报详细信息
     * @param id
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/weekly/detail.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse weeklyDetail(@RequestParam("id") String id, HttpServletRequest request) throws Exception{
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        CmProjectWeekly weekly = this.cmProjectWeeklyService.getById(id);
        if (weekly == null) {
            return MobileApiResponse.error(ResultCode.DATA_IS_WRONG);
        }
        CmProjectWeeklyItem item = new CmProjectWeeklyItem();
        item.setWeeklyId(weekly.getId());
        List<CmProjectWeeklyItem> weeklyItemList = this.cmProjectWeeklyItemService.queryAll(item);
        weekly.setWeeklyItemList(weeklyItemList);
        return MobileApiResponse.ok(weekly);
    }

    /**
     * 保存周报事项
     * @param entity
     * @param request
     * @return
     */
    @RequestMapping(value = "/weekly/item/report.action", method = RequestMethod.POST)
    @ResponseBody
    public MobileApiResponse weeklyReport(@RequestBody CmProjectWeeklyItem entity, HttpServletRequest request) {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        try {
            if (!StringUtil.isEmpty(entity.getId())) {
                return MobileApiResponse.fromResult(this.cmProjectWeeklyItemService.updateEntity(entity));
            }
            CmProjectWeeklyItem db = this.cmProjectWeeklyItemService.getByPlanAndWeekly(entity.getWeeklyId(), entity.getPlanId());
            if (db != null) {
                entity.setId(db.getId());
                return MobileApiResponse.fromResult(this.cmProjectWeeklyItemService.updateEntity(entity));
            }
            return MobileApiResponse.fromResult(this.cmProjectWeeklyItemService.saveEntity(entity));
        } catch (Exception ex) {
            ex.printStackTrace();
            return MobileApiResponse.error(ex.getMessage());
        }
    }

    /**
     * 删除周报进度
     * @param id
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/weekly/item/delete.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse itemDelete(@RequestParam("id") String id, HttpServletRequest request) throws Exception{
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        CmProjectWeeklyItem entity = new CmProjectWeeklyItem();
        entity.setPrimaryKeys(new String[]{id});
        if (this.cmProjectWeeklyItemService.deleteByIds(entity) != 0) {
            return MobileApiResponse.ok();
        }
        return MobileApiResponse.error(500, "删除失败");
    }

    /**
     * 周报反馈
     * @param entity
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/weekly/feedback.action", method = RequestMethod.POST)
    @ResponseBody
    public MobileApiResponse weeklyFeedback(@RequestBody CmProjectWeekly entity, HttpServletRequest request) throws Exception{
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        CmProjectWeekly db = this.cmProjectWeeklyService.getById(entity.getId());
        if (db == null) {
            return MobileApiResponse.error(ResultCode.DATA_IS_WRONG);
        }
        db.setFeedback(entity.getFeedback());
        db.setStatus(1);
        db.setFeedbackUser(apiResponse.getUserId());
        return MobileApiResponse.fromResult(this.cmProjectWeeklyService.updateEntity(db));
    }

    /**
     * 进度上报
     * @param entity
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/plan/images/report.action", method = RequestMethod.POST)
    @ResponseBody
    public MobileApiResponse planImagesReport(@RequestBody CmProjectPlanImages entity, HttpServletRequest request) throws Exception{
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        if (StringUtil.isEmpty(entity.getId())) {
            //新增
            entity.setUserId(apiResponse.getUserId());
            Result result = this.cmProjectPlanImagesService.saveEntity(entity);
            if (result.isSuccess()) {
                this.mobileReportLogService.saveLog(MobileReportLogEnum.image, result.getData(), UserUtils.getUser(apiResponse.getUserId()));
            }
        } else {
            this.cmProjectPlanImagesService.updateEntity(entity);
        }
        return MobileApiResponse.ok();
    }

    @RequestMapping(value = "/plan/images/delete.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse planImagesList(@RequestParam("id") String id, HttpServletRequest request) {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        CmProjectPlanImages entity = this.cmProjectPlanImagesService.getById(id);
        if (entity != null && entity.getUserId().equals(apiResponse.getUserId())) {
            entity.setPrimaryKeys(new String[] { entity.getId() });
            try {
                this.cmProjectPlanImagesService.deleteByIds(entity);
            } catch (CustomException e) {
                e.printStackTrace();
            }
        }
        return MobileApiResponse.ok();
    }

    /**
     * 形象进度列表
     * @param request
     * @param page
     * @return
     */
    @RequestMapping(value = "/plan/images/list.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse planImagesList(HttpServletRequest request, PageManager page) {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        //项目id
        String projectId = request.getParameter("projectId");
        //计划id
        String planId = request.getParameter("planId");
        //阶段id
        String stageId = request.getParameter("stageId");

        //是否分页表示
        String isPage = request.getParameter("isPage");
        CmProjectPlanImages entity = new CmProjectPlanImages();
        entity.setProjectId(projectId);
        entity.setPlanId(planId);
        entity.setStageId(stageId);
        String isMy = request.getParameter("isMy");
        if (!StringUtil.isEmpty(isMy) && isMy.equals("1")) {
            entity.setUserId(apiResponse.getUserId());
        }
        entity.addOrderByFieldDesc(" t.create_date ");
        if (isPage != null && GlobalContext.TRUE.equals(isPage)) {
            //分页
            PageManager page_ = this.cmProjectPlanImagesService.query(entity, page);
            List<CmProjectPlanImages> dataList = (List<CmProjectPlanImages>) page_.getData();
            if (dataList != null) {
                for (CmProjectPlanImages images : dataList) {
                    images.setAttachList(getAttachList(images.getId(), "cm_project_plan_images"));
                }
            }
            GlobalUtil.setTableRowNo(page_, page.getPageRows());
            return MobileApiResponse.ok(page_);
        } else {
            //不分页直接查询列表
            try {
                List<CmProjectPlanImages> dataList = this.cmProjectPlanImagesService.queryAll(entity);
                if (dataList != null) {
                    for (CmProjectPlanImages images : dataList) {
                        images.setAttachList(getAttachList(images.getId(), "cm_project_plan_images"));
                    }
                }
                return MobileApiResponse.ok(dataList);
            } catch (CustomException e) {
                e.printStackTrace();
                return MobileApiResponse.error(e.getMessage());
            }
        }
    }
    /**
     * 形象进度列表
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "/plan/images/detail.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse planImagesDetail(HttpServletRequest request) {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        //计划id
        String id = request.getParameter("id");
        if (StringUtil.isEmpty(id)) {
            return MobileApiResponse.error(ResultCode.PARAM_IS_BLANK);
        }
        CmProjectPlanImages cmProjectPlanImages = cmProjectPlanImagesService.getById(id);
        if (cmProjectPlanImages != null) {
            Attach param = new Attach();
            param.setBusinessIdArray(new String[]{id});
            param.setBusinessTable("cm_project_plan_images");
            try {
                List<Attach> attachList = this.attachService.queryAttachByBusinessIds(param);
                String loadAttachUrl = "";
                if (attachList != null) {
                    for (Attach attach : attachList) {
                        loadAttachUrl += "," + "/mobile/system/preview.action?attachId=" + attach.getId();
                    }
                }
                if (!StringUtil.isEmpty(loadAttachUrl)) {
                    cmProjectPlanImages.setLoadAttachUrl(loadAttachUrl.substring(1));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return MobileApiResponse.ok(cmProjectPlanImages);
    }

    /**
     * 产值上报资源库项目列表列表
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "/ptProject/list.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse ptProjectList(HttpServletRequest request, PageManager page) {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        User user = commonService.getUserById(apiResponse.getUserId());
        if (user != null) {
            ProjectInfo entity = new ProjectInfo();
            String resourceId = "-1";
            try {
                Department dept = DeptCacheUtil.getDeptById(user.getDeptId());
                if (dept != null) {
                    resourceId = dept.getResourceId();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            entity.setBuilddeptid(resourceId);

            String projectName = request.getParameter("projectName");
            if (!StringUtil.isEmpty(projectName)) {
                entity.setProjectname(projectName);
            }
            entity.addOrderByFieldDesc(" t.dlh_data_modify_date_ ");
                //分页
                PageManager page_ = this.projectInfoService.query(entity, page);
                GlobalUtil.setTableRowNo(page_, page.getPageRows());
                return MobileApiResponse.ok(page_);
        }else{
            return MobileApiResponse.error();
        }
    }

    /**
     * 产值上报
     * @param entity
     * @param request
     * @return
     */
    @RequestMapping(value = "/production/item/report.action", method = RequestMethod.POST)
    @ResponseBody
    public MobileApiResponse productionReport(@RequestBody ProjectProductionReport entity, HttpServletRequest request) {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        try {
            ProjectProductionReport entityOld = new ProjectProductionReport();
            entityOld.setYearMon(entity.getYearMon());
            entityOld.setProjectNumber(entity.getProjectNumber());
            entityOld = this.projectProductionReportService.get(entityOld);
            if (entityOld!=null) {
                ProjectInfo projectInfo = new ProjectInfo();
                projectInfo.setProjectnumber(entityOld.getProjectNumber());
                projectInfo = projectInfoService.get(projectInfo);
                if(projectInfo.getProductionTotal()!=null){
                    projectInfo.setProductionTotal(projectInfo.getProductionTotal().subtract(entity.getCompletedInvestmentAmount()));
                }
                projectInfoService.update(projectInfo);
                projectProductionReportService.delete(entityOld);
            }
            Result result = this.projectProductionReportService.saveEntity(entity);
            if(result.isSuccess()){
                ProjectInfo projectInfo = new ProjectInfo();
                projectInfo.setProjectnumber(entity.getProjectNumber());
                projectInfo = projectInfoService.get(projectInfo);
                if(projectInfo.getProductionTotal()==null){
                    projectInfo.setProductionTotal(entity.getCompletedInvestmentAmount());
                }else{
                    projectInfo.setProductionTotal(entity.getCompletedInvestmentAmount().add(projectInfo.getProductionTotal()));
                }
                projectInfoService.update(projectInfo);
                //记录日志
                entity.setProjectName(projectInfo.getProjectname());
                this.mobileReportLogService.saveLog(MobileReportLogEnum.money, entity, UserUtils.getUser(apiResponse.getUserId()));
            }
            return MobileApiResponse.fromResult(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            return MobileApiResponse.error(ex.getMessage());
        }
    }
    /**
     * 产值上报列表
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "/production/item/list.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse productionList(HttpServletRequest request) throws CustomException {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        //产值项目编码
        String projectNumber = request.getParameter("projectNumber");
        if (StringUtil.isEmpty(projectNumber)) {
            return MobileApiResponse.error(ResultCode.PARAM_IS_BLANK);
        }
        ProjectProductionReport entityPam = new ProjectProductionReport();
        entityPam.setProjectNumber(projectNumber);
        List<ProjectProductionReport> entityList = projectProductionReportService.queryAll(entityPam);
        return MobileApiResponse.ok(entityList);
    }
    /**
     * 产值上报详细
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "/production/item/detail.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse productionDetail(HttpServletRequest request) throws CustomException {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        //产值项目编码
        String projectNumber = request.getParameter("projectNumber");
        String yearMon = request.getParameter("yearMon");
        if (StringUtil.isEmpty(projectNumber)||StringUtil.isEmpty(yearMon)) {
            return MobileApiResponse.error(ResultCode.PARAM_IS_BLANK);
        }
        ProjectProductionReport entityPam = new ProjectProductionReport();
        entityPam.setProjectNumber(projectNumber);
        List<ProjectProductionReport> list = projectProductionReportService.queryAll(entityPam);
        BigDecimal total = BigDecimal.ZERO;
        for(ProjectProductionReport item:list){
            total = total.add(item.getCompletedInvestmentAmount());
        }
        ProjectProductionReport entityPamMon = new ProjectProductionReport();
        entityPamMon.setProjectNumber(projectNumber);
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH )+1;
//        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");
//        String dateStr = f.format(new Date());
        entityPamMon.setYearMon(yearMon);
        entityPamMon = projectProductionReportService.get(entityPamMon);
        entityPamMon.setCompletedInvestmentTotal(total);
        return MobileApiResponse.ok(entityPamMon);
    }
    /**
     * 产值上报本月详细
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "/production/thisMon/detail.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse thisMonDetail(HttpServletRequest request) throws CustomException {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
//        产值id
        String id = request.getParameter("id");
        if (StringUtil.isEmpty(id)) {
            return MobileApiResponse.error(ResultCode.PARAM_IS_BLANK);
        }
        ProjectProductionReport entityPam = new ProjectProductionReport();
        entityPam.setId(id);
        entityPam = projectProductionReportService.get(entityPam);
        if(!StringUtil.isEmpty(entityPam.getProjectNumber())){
            ProjectInfo projectInfo = new ProjectInfo();
            projectInfo.setProjectnumber(entityPam.getProjectNumber());
            projectInfo = projectInfoService.get(projectInfo);
            if(projectInfo!=null){
                entityPam.setProjectName(projectInfo.getProjectname());
            }
        }
        return MobileApiResponse.ok(entityPam);
    }
    /**
     * 产值上报当前月是否上报
     * @param request
     * @param
     * @return
     */
    @RequestMapping(value = "/production/item/isReport.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse productionisReport(HttpServletRequest request) throws CustomException {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        //产值项目编码
        String projectNumber = request.getParameter("projectNumber");
        String yearMon = request.getParameter("yearMon");
        if (StringUtil.isEmpty(projectNumber)||StringUtil.isEmpty(yearMon)) {
            return MobileApiResponse.error(ResultCode.PARAM_IS_BLANK);
        }
//        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM");
//        String dateStr = f.format(new Date());
        ProjectProductionReport entityPam = new ProjectProductionReport();
        entityPam.setProjectNumber(projectNumber);
        entityPam.setYearMon(yearMon);
        List<ProjectProductionReport> list = projectProductionReportService.queryAll(entityPam);
        if(list.size()>0){
            return MobileApiResponse.ok(1);
        }else{
            return MobileApiResponse.ok(0);
        }
    }

    /**
     * 我的上报列表
     * @param request
     * @param page
     * @return
     */
    @RequestMapping(value = "/report/log/list.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse reportLogList(HttpServletRequest request, PageManager page) {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        //人员id
//        String userId = request.getParameter("userId");
        String userId = apiResponse.getUserId();
//        if (StringUtil.isEmpty(userId)) {
//            return MobileApiResponse.error(ResultCode.PARAM_IS_BLANK);
//        }
        //是否分页表示
        String isPage = request.getParameter("isPage");
        String businessType = request.getParameter("businessType");
        MobileReportLog entity = new MobileReportLog();
        entity.setUserId(userId);
        entity.setBusinessType(businessType);
        entity.addOrderByFieldDesc(" t.create_date ");
        if (isPage != null && GlobalContext.TRUE.equals(isPage)) {
            //分页
            PageManager page_ = this.mobileReportLogService.query(entity, page);
            GlobalUtil.setTableRowNo(page_, page.getPageRows());
            return MobileApiResponse.ok(page_);
        } else {
            //不分页直接查询列表
            try {
                List<MobileReportLog> dataList = this.mobileReportLogService.queryAll(entity);
                return MobileApiResponse.ok(dataList);
            } catch (CustomException e) {
                e.printStackTrace();
                return MobileApiResponse.error(e.getMessage());
            }
        }
    }

    /**
     * 返回计划阶段和计划的关系，stage中有child为计划
     * @param projectId
     * @param request
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/plan/Picker.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse planPicker(@RequestParam("projectId") String projectId, HttpServletRequest request, PageManager page) throws Exception{
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        CmProjectPlanStage stageParam = new CmProjectPlanStage();
        stageParam.setProjectId(projectId);
        stageParam.addOrderByField(" t.queue ");
        List<CmProjectPlanStage> stageList = this.cmProjectPlanStageService.queryAll(stageParam);
        CmProjectPlan planParam = new CmProjectPlan();
        planParam.setProjectId(projectId);
        planParam.addOrderByField(" t.queue ");
        List<CmProjectPlan> planList = this.cmProjectPlanService.queryAll(planParam);
        List<Map<String, Object>> resultList = new ArrayList<>();
        if (stageList != null) {
            for (CmProjectPlanStage stage : stageList) {
                Map<String, Object> resultMap = new HashMap<>(3);
                resultMap.put("id", stage.getId());
                resultMap.put("name", stage.getStageName());
                List<Map<String, Object>> childList = new ArrayList<>();
                if (planList != null) {
                    for (CmProjectPlan plan : planList) {
                        if (plan.getStageId().equals(stage.getId())) {
                            Map<String, Object> planMap = new HashMap<>(3);
                            planMap.put("id", plan.getId());
                            planMap.put("name", plan.getPlanName());
                            childList.add(planMap);
                        }
                    }
                }
                resultMap.put("child", childList);
                resultList.add(resultMap);
            }
        }
        return MobileApiResponse.ok(resultList);
    }

    /**
     * 根据项目获取处置负责人
     * @param projectId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "getSuperviseChildDeptByProjectId.action", method = RequestMethod.GET)
    @ResponseBody
    public List<Department> getSuperviseChildDeptByProjectId(String projectId) throws Exception {
        if (StringUtil.isEmpty(projectId)) {
            return Collections.emptyList();
        }
        CmProjectInfo project = this.cmProjectInfoService.getById(projectId);
        if (project == null || project.getSuperviseDeptId() == null) {
            return Collections.emptyList();
        }
        Department param = new Department();
        param.setLikeIdToCode(project.getSuperviseDeptId());
        return this.departmentService.query(param);
    }

    /**
     * 上报项目坐标
     * @param request
     * @return
     */
    @RequestMapping(value="reportPointByProjectNumber.action",method= RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse reportPointByProjectNumber(@RequestParam("projectNumber") String projectNumber, @RequestParam("lng") String lng, @RequestParam("lat") String lat, HttpServletRequest request){
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        CmProjectInfo project = this.cmProjectInfoService.getbyProjectNumber(projectNumber);
        if (project == null) {
            return MobileApiResponse.error(ResultCode.RESULE_DATA_NONE);
        }
        project.setLat(lat);
        project.setLng(lng);
        try {
            this.cmProjectInfoService.update(project);
            Map<String, Object> rsync = new HashMap<>(2);
            rsync.put("objUrl", ResourceEnums.pt_project_info.toString());
            Map<String, Object> info = new HashMap<>(10);
            info.put("projectNumber", project.getProjectNumber());
            info.put("longitude", project.getLng());
            info.put("latitude", project.getLat());
            rsync.put("info", info);
            HttpClientUtil.post(GlobalContext.getProperty("resourceSystemUrl") + IResourceService.REPORT_URL , new ObjectMapper().writeValueAsString(rsync));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MobileApiResponse.ok();
    }

    /**
     * 上报用户坐标
     * @param request
     * @return
     */
    @RequestMapping(value="reportUserPoint.action",method= RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse reportUserPoint(@RequestParam(value="projectNumber", required = false) String projectNumber,
                                             @RequestParam(value="juli",required = false) String juli, @RequestParam("lng") String lng, @RequestParam("lat") String lat, HttpServletRequest request) throws CustomException {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        CmProjectPersonLnglat entity = new CmProjectPersonLnglat();
        entity.setUserId(apiResponse.getUserId());
        entity.setLat(lat);
        entity.setLng(lng);
        entity.setProjectNumber(projectNumber);
        entity.setExtStr1(juli);
        return MobileApiResponse.ok(this.cmProjectPersonLnglatService.saveEntity(entity));
    }

    @RequestMapping(value="getWeeklyName.action",method= RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse getWeeklyName() {
        return MobileApiResponse.ok("【项目周报-第" + GlobalUtil.getCurrentWeek() + "周】(" + GlobalUtil.getMonday("MM.dd") + "-" + GlobalUtil.getSunday("MM.dd") + ")");
    }

    private List<Map<String, String>> getAttachList(String busId, String busTable) {
        try {
            Attach param = new Attach();
            param.setBusinessIdArray(new String[]{busId});
            param.setBusinessTable(busTable);
            List<Attach> attachList = this.attachService.queryAttachByBusinessIds(param);
            if (attachList == null) {
                attachList = Collections.EMPTY_LIST;
            }
            List<Map<String, String>> resultList = new ArrayList<>();
            for (Attach attach : attachList) {
                Map<String, String> resultMap = new HashMap<>(4);
                resultMap.put("ext", attach.getExt());
                resultMap.put("attachId", attach.getId());
                resultMap.put("name", attach.getName());
                resultMap.put("category", attach.getCategory());
                resultMap.put("url", "/mobile/system/download.action?attachId=" + attach.getId());
                resultMap.put("preview", "/mobile/system/preview.action?attachId=" + attach.getId());
                resultList.add(resultMap);
            }
            return resultList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
