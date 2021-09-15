package com.jc.csmp.common.web;

import com.jc.crypto.utils.SM2Utils;
import com.jc.csmp.common.enums.DepartRootCodeEnum;
import com.jc.csmp.project.domain.CmProjectInfo;
import com.jc.csmp.project.plan.domain.CmProjectPlan;
import com.jc.csmp.project.plan.domain.CmProjectPlanStage;
import com.jc.csmp.project.plan.service.ICmProjectPlanService;
import com.jc.csmp.project.plan.service.ICmProjectPlanStageService;
import com.jc.csmp.project.service.ICmProjectInfoService;
import com.jc.digitalchina.domain.CmUserRelation;
import com.jc.digitalchina.domain.DigitalBean;
import com.jc.digitalchina.service.ICmUserRelationService;
import com.jc.foundation.domain.Attach;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.*;
import com.jc.foundation.web.BaseController;
import com.jc.resource.enums.vo.ResourceAttachInfo;
import com.jc.supervise.warning.service.ICmSupervisionWarningService;
import com.jc.system.content.service.IUploadService;
import com.jc.system.dic.IDicManager;
import com.jc.system.dic.domain.Dic;
import com.jc.system.event.UserAddEvent;
import com.jc.system.event.UserDeleteEvent;
import com.jc.system.event.UserUpdateEvent;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.Department;
import com.jc.system.security.domain.Unique;
import com.jc.system.security.domain.User;
import com.jc.system.security.service.IDepartmentService;
import com.jc.system.security.service.IUniqueService;
import com.jc.system.security.service.IUserService;
import com.jc.system.security.util.DeptCacheUtil;
import com.jc.workflow.suggest.bean.Suggest;
import com.jc.workflow.suggest.service.IWorkflowSuggestService;
import com.jc.workflow.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 一些通用的api数据
 * @Author 常鹏
 * @Date 2020/7/9 10:37
 * @Version 1.0
 */
@Controller
@RequestMapping(value = "/common/api")
public class CsmpController extends BaseController {
    @Autowired
    private IDepartmentService departmentService;
    @Autowired
    private IDicManager dicManager;
    @Autowired
    private IUploadService uploadService;
    @Autowired
    private ICmProjectPlanStageService cmProjectPlanStageService;
    @Autowired
    private ICmProjectPlanService cmProjectPlanService;
    @Autowired
    private ICmProjectInfoService cmProjectInfoService;
    @Autowired
    private ICmSupervisionWarningService cmSupervisionWarningService;
    @Autowired
    private IWorkflowSuggestService suggestService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private IUserService userService;
    @Autowired
    private ICmUserRelationService cmUserRelationService;
    @Autowired
    private IUniqueService uniqueService;

    /**
     * 获取部门树，根据跟目录获取
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "deptTree.action", method = RequestMethod.GET)
    @ResponseBody
    public List<Department> deptTree(HttpServletRequest request) throws Exception {
        String rootCode = request.getParameter("rootCode");
        DepartRootCodeEnum codeEnum = DepartRootCodeEnum.getByCode(rootCode);
        Department param = new Department();
        param.addOrderByField(" t.create_date ");
        param.setLikeCode(codeEnum.getValue());
        List<Department> dbList = this.departmentService.query(param);
        List<Department> resultList = new ArrayList<>();
        if (dbList != null) {
            for (Department department : dbList) {
                if (!department.getCode().equals(codeEnum.getValue())) {
                    resultList.add(department);
                }
            }
        }
        return resultList;
    }

    @RequestMapping(value = "companyTree.action", method = RequestMethod.GET)
    @ResponseBody
    public PageManager companyTree(String code, String name, PageManager page) throws Exception {
        if (StringUtil.isEmpty(code)) {
            code = DepartRootCodeEnum.COMPANY.getValue();
        } else {
            code = DeptCacheUtil.getCodeById(code);
        }
        Department param = new Department();
        param.addOrderByField(" t.queue ");
        param.setLikeCode(code);
        if (!StringUtil.isEmpty(name)) {
            param.setLikeName(name);
        }
        param.setNoChangeCodes(GlobalUtil.splitStr(DepartRootCodeEnum.NOCHANGECOMPANY.getValue(), ','));
        return this.departmentService.query(param, page);
    }

    @RequestMapping(value = "companyLeftTree.action", method = RequestMethod.GET)
    @ResponseBody
    public List<Department> companyTree() throws Exception {
        List<Department> deptList = new ArrayList<>();
        String[] array = GlobalUtil.splitStr(DepartRootCodeEnum.NOCHANGECOMPANY.getValue(), ',');
        for (String deptCode : array) {
            Department param = new Department();
            param.setCode(deptCode);
            param = GlobalUtil.getFirstItem(this.departmentService.query(param));
            deptList.add(Department.createTreeData(param.getId(), param.getParentId(), param.getName()));
        }
        return deptList;
    }

    @RequestMapping(value = "planStage.action", method = RequestMethod.GET)
    @ResponseBody
    public List<Department> planStage(String projectId) throws Exception {
        List<Department> deptList = new ArrayList<>();
        deptList.add(Department.createTreeData("0", "-1", "所属阶段"));
        if (StringUtil.isEmpty(projectId)) {
            return deptList;
        }
        CmProjectPlanStage param = new CmProjectPlanStage();
        param.setProjectId(projectId);
        param.addOrderByField(" t.queue ");
        List<CmProjectPlanStage> stageList = this.cmProjectPlanStageService.queryAll(param);
        if (stageList != null) {
            for (CmProjectPlanStage stage : stageList) {
                deptList.add(Department.createTreeData(stage.getId(), stage.getParentId(), stage.getStageName()));
            }
        }
        return deptList;
    }

    @RequestMapping(value = "planList.action", method = RequestMethod.GET)
    @ResponseBody
    public PageManager planList(String projectId, String code, String name, PageManager page) throws Exception {
        CmProjectPlan entity = new CmProjectPlan();
        entity.setProjectId(projectId);
        if (!StringUtil.isEmpty(code)) {
            entity.setStageId(code);
            entity.setStageIds(cmProjectPlanStageService.getChildIdById(entity.getStageId(), entity.getProjectId()));
            entity.setStageId(null);
        }
        if (!StringUtil.isEmpty(name)) {
            entity.setPlanName(name);
        }
        entity.addOrderByField(" t.queue ");
        return this.cmProjectPlanService.query(entity, page);
    }

    /**
     * 获取字典树
     * @param request
     * @return
     */
    @RequestMapping(value = "dicTree.action", method = RequestMethod.GET)
    @ResponseBody
    public List<Department> dicTree(HttpServletRequest request) {
        String typeCode = request.getParameter("tc");
        String parentCode = request.getParameter("pc");
        List<Dic> dicList = this.dicManager.getDicsByTypeCode(typeCode, parentCode);
        List<Department> resultList = new ArrayList<>();
        Department root = new Department();
        root.setId("0");
        root.setParentId("-1");
        root.setName("所属区域");
        resultList.add(root);
        if (dicList != null && dicList.size() > 0) {
            for (Dic dic : dicList) {
                resultList.add(Department.createTreeData(dic.getCode(), "0", dic.getName()));
            }
        }
        return resultList;
    }

    @RequestMapping(value = "suggestList.action", method = RequestMethod.GET)
    @ResponseBody
    public List<Suggest> suggestList(String pid, String itemId) throws Exception {
        List<Map<String, Object>> dbList = jdbcTemplate.queryForList("select proc_inst_id from workflow_instance where business_key_ = ?", new Object[]{pid});
        if (dbList != null && dbList.size() > 0) {
            String instanceId = (String) dbList.get(0).get("proc_inst_id");
            if (!GlobalUtil.isEmpty(instanceId)) {
                return this.suggestService.querySuggestList(instanceId, itemId, "false", "createTime");
            }
        }
        return new ArrayList<>();
    }

    /**
     * 根据项目id获取监管机构的子机构
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


    @RequestMapping(value = "demo.action", method = RequestMethod.GET)
    @ResponseBody
    public Result demo(HttpServletRequest request) throws Exception{
        DigitalBean digitalBean = new DigitalBean();
        digitalBean.setUserGuid("cp");
        digitalBean.setUserName("changpeng");
        digitalBean.setOptType(1);
        digitalBean.setRealName("常鹏1");
        digitalBean.setPhone("13756093823");
        if (digitalBean != null) {
            log.error("userName: " + digitalBean.getUserName());
            CmUserRelation relation = new CmUserRelation();
            relation.setUuid(digitalBean.getUserGuid());
            relation.setDeleteFlag(null);
            relation = this.cmUserRelationService.get(relation);
            User user = new User();
            user.setLoginName(digitalBean.getUserName());
            user.setDeleteFlag(null);
            user = this.userService.get(user);
            if (digitalBean.getOptType().intValue() == 3) {
                //删除
                log.error("userName: " + digitalBean.getUserName() + "删除");
                if (user != null) {
                    user.setPrimaryKeys(new String[]{ user.getId() });
                    SpringContextHolder.getApplicationContext().publishEvent(new UserDeleteEvent(user));
                    this.userService.delete(user);
                }
                if (relation != null) {
                    //删除关系
                    relation.setPrimaryKeys(new String[]{relation.getId()});
                    this.cmUserRelationService.deleteByIds(relation);
                }
            } else {
                //新增或修改
                if (user == null) {
                    user = new User(); user.setDeptId("100001"); user.setLoginName(digitalBean.getUserName()); user.setDisplayName(digitalBean.getRealName());
                    user.setMobile(digitalBean.getPhone()); user.setWeight(70); user.setExtStr1("secret_type_0");
                    user.setOrderNo(100);
                    Unique unique = uniqueService.getOne(new Unique());
                    if (unique != null) {
                        user.setCode(unique.getUuid());
                    }
                    user.setStatus("status_0"); user.setSex("99"); user.setKind("KIND"); user.setEthnic("ethnic_01");
                    user.setIsDriver(0); user.setIsLeader(0); user.setIsCheck("1"); user.setLockType(0); user.setIsAdmin(0);
                    log.error("userName: " + digitalBean.getUserName() + "新增用户");
                    SM2Utils.generateKeyPair();
                    if(!com.jc.foundation.util.StringUtil.isEmpty(GlobalContext.getProperty("password.default.value"))){
                        user.setPassword(SystemSecurityUtils.entryptPassword(GlobalContext.getProperty("password.default.value"), SM2Utils.getPubKey()));
                    } else {
                        user.setPassword(SystemSecurityUtils.entryptPassword(GlobalContext.PASSWORD_DEFAULT_VALUE, SM2Utils.getPubKey()));
                    }
                    user.setModifyPwdFlag(0);
                    user.setKeyCode(SM2Utils.getPriKey());
                    if (userService.save(user) == 1) {
                        unique.setState("1");
                        uniqueService.update(unique);
                        if (relation == null) {
                            relation = new CmUserRelation();
                            relation.setUuid(digitalBean.getUserGuid());
                            relation.setUserId(user.getId());
                            this.cmUserRelationService.saveEntity(relation);
                        } else {
                            relation.setUserId(user.getId());
                            relation.setDeleteFlag(0);
                            this.cmUserRelationService.updateEntity(relation);
                        }
                        SpringContextHolder.getApplicationContext().publishEvent(new UserAddEvent(user));
                    }
                } else {
                    //用户存在得情况 修改信息
                    log.error("userName: " + digitalBean.getUserName() + "修改用户");
                    user.setLoginName(digitalBean.getUserName());
                    user.setDisplayName(digitalBean.getRealName());
                    user.setMobile(digitalBean.getPhone());
                    if (user.getDeleteFlag().intValue() == 0) {
                        this.userService.update2(user);
                    } else {
                        user.setPrimaryKeys(new String[] { user.getId() });
                        this.userService.deleteBack(user);
                    }
                    SpringContextHolder.getApplicationContext().publishEvent(new UserUpdateEvent(user));
                    if (relation == null) {
                        relation = new CmUserRelation();
                        relation.setUuid(digitalBean.getUserGuid());
                        relation.setUserId(user.getId());
                        this.cmUserRelationService.saveEntity(relation);
                    } else {
                        relation.setUserId(user.getId());
                        relation.setDeleteFlag(0);
                        this.cmUserRelationService.updateEntity(relation);
                    }
                }
            }
        }
        return Result.success();
    }

    @RequestMapping(value = "upload.action")
    public void importExcel(@RequestParam(value = "fileField") MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Content-Type", "text/html; charset=utf-8");
        Attach attach = this.uploadService.upload(file, "", request);
        Writer writer = response.getWriter();
        System.out.println(file.getOriginalFilename());
        writer.write("{\"success\":\"true\",\"path\":\"/csmp/content/attach/originalRead/" + attach.getId() + "\", \"name\": \"" + attach.getResourcesName() + "\"}");
        writer.flush();
        writer.close();
    }

    @RequestMapping(value = "attach.action", method = RequestMethod.POST)
    @ResponseBody
    public Result attach(@RequestBody ResourceAttachInfo info) {
        return Result.success();
    }

    /**
     * 工作流历史
     * @param pid
     * @return
     */
    @RequestMapping(value = "workflowHistory.action", method = RequestMethod.GET)
    @ResponseBody
    public Result workflowHistory(@RequestParam("pid") String pid) {
        String sql = "select definitionId_ as definitionId, proc_inst_id as instanceId from workflow_instance where business_key_ = ?";
        List<Map<String, Object>> resultList = this.jdbcTemplate.queryForList(sql, new Object[]{pid});
        if (resultList != null && resultList.size() > 0) {
            return Result.success(resultList.get(0));
        }
        return Result.failure(ResultCode.RESULE_DATA_NONE);
    }

    /**
     * 工作流历史
     * @param pid
     * @return
     */
    @RequestMapping(value = "workflowTodo.action", method = RequestMethod.GET)
    @ResponseBody
    public Result workflowTodo(@RequestParam("pid") String pid) {
        StringBuffer buffer = new StringBuffer("select ");
        buffer.append("definitionId_ as definitionId, business_Key_ as businessKey, act_id as curNodeId, ");
        buffer.append("proc_inst_id as instanceId, task_id as taskId ");
        buffer.append("FROM workflow_todo d where business_key_ = ?");
        List<Map<String, Object>> resultList = this.jdbcTemplate.queryForList(buffer.toString(), new Object[]{pid});
        if (resultList != null && resultList.size() > 0) {
            return Result.success(resultList.get(0));
        }
        return Result.failure(ResultCode.RESULE_DATA_NONE);
    }

    /*@Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping(value = "sendMessage.action", method = RequestMethod.GET)
    @ResponseBody
    public Result sendMessage(HttpServletRequest request) {
        Map<String, String> map = new HashMap();
        map.put("id", "1");
        map.put("name", "zhangsan");
        //根据key发送到对应的队列
        rabbitTemplate.convertAndSend("queuekey", map);
        return Result.success();
    }*/
}
