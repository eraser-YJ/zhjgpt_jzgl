package com.jc.csmp.statis.web;

import com.jc.csmp.common.enums.DicKeyEnum;
import com.jc.csmp.project.domain.CmProjectInfo;
import com.jc.csmp.project.service.ICmProjectInfoService;
import com.jc.csmp.statis.service.IJdbcTemplateStatisticsService;
import com.jc.foundation.util.GlobalUtil;
import com.jc.foundation.util.StringUtil;
import com.jc.foundation.web.BaseController;
import com.jc.system.applog.ActionLog;
import com.jc.system.dic.IDicManager;
import com.jc.system.dic.domain.Dic;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.Department;
import com.jc.system.security.util.DeptCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 统计控制器
 * @Author 常鹏
 * @Date 2020/8/3 15:18
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/statistics")
public class StatisticsController extends BaseController {

    @Autowired
    private ICmProjectInfoService cmProjectInfoService;
    @Autowired
    private IDicManager dicManager;
    @Autowired
    private IJdbcTemplateStatisticsService jdbcTemplateStatisService;

    /**
     * 项目驾驶舱
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="cockpit.action",method= RequestMethod.GET)
    @ActionLog(operateModelNm="",operateFuncNm="manage",operateDescribe="项目驾驶舱")
    public String cockpit(HttpServletRequest request) throws Exception {
        return "csmp/project/info/cmProjectCockpit";
    }

    /**
     * 项目驾驶舱左侧树
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "projectInfoTree.action")
    @ResponseBody
    public List<Department> projectInfoTree(HttpServletRequest request) throws Exception{
        List<Dic> dicList = this.dicManager.getDicsByTypeCode(DicKeyEnum.region.getTypeCode(), DicKeyEnum.region.getParentCode());
        List<Department> resultList = new ArrayList<>();
        resultList.add(Department.createTreeData("region_0", "0", "长春新区"));
        if (dicList != null) {
            for (Dic dic : dicList) {
                resultList.add(Department.createTreeData("region_" + dic.getCode(), "region_0", dic.getName()));
            }
        }
        CmProjectInfo param = new CmProjectInfo();
        param.setDeptCodeCondition(DeptCacheUtil.getCodeById(SystemSecurityUtils.getUser().getDeptId()));
        List<CmProjectInfo> projectList = this.cmProjectInfoService.queryAll(param);
        if (projectList != null) {
            for (CmProjectInfo project : projectList) {
                resultList.add(Department.createTreeData("project_" + project.getId() + "|" + project.getProjectNumber() + "|" + project.getApprovalNumber(), "region_" + project.getRegion(), project.getProjectName()));
            }
        }
        return resultList;
    }

    /**
     * 项目驾驶舱质量安全现场问题统计
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="questionByYear.action",method=RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> questionByYear(HttpServletRequest request) throws Exception{
        String projectId = request.getParameter("projectId");
        if (StringUtil.isEmpty(projectId)) {
            return Collections.emptyList();
        }
        Integer year = GlobalUtil.toInteger(request.getParameter("year"), GlobalUtil.getYear());
        return this.jdbcTemplateStatisService.projectQuestionByYear(new Long(year), projectId);
    }

    /**
     * 项目驾驶舱-项目超期计划
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="projectPassTimePlan.action",method=RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> projectPassTimePlan(HttpServletRequest request) throws Exception{
        String projectId = request.getParameter("projectId");
        if (StringUtil.isEmpty(projectId)) {
            return Collections.emptyList();
        }
        return this.jdbcTemplateStatisService.projectPassTimePlan(projectId);
    }
}
