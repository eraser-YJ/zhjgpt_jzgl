package com.jc.resource.enums.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jc.csmp.project.domain.CmProjectInfo;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.Result;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.resource.bean.DetailData;
import com.jc.resource.bean.PageManagerMapData;
import com.jc.resource.bean.QueryDataParam;
import com.jc.resource.enums.ResourceEnums;
import com.jc.resource.enums.service.IResourceService;
import com.jc.resource.enums.vo.ResourceAttachInfo;
import com.jc.resource.util.HttpClientUtil;
import com.jc.system.content.service.IAttachService;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.Department;
import com.jc.system.security.util.DeptCacheUtil;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目基本信息对接资源库信息
 * @Author 常鹏
 * @Date 2020/7/27 14:44
 * @Version 1.0
 */
public class ProjectInfoResourceServiceImpl extends ResourceServiceImpl implements IResourceService {

    private JdbcTemplate jdbcTemplate;

    public ProjectInfoResourceServiceImpl() {
        jdbcTemplate = SpringContextHolder.getBean(JdbcTemplate.class);
    }

    @Override
    public PageManagerMapData query(PageManager pageParam, List<QueryDataParam> paramList) {
        return query(PAGE_URL, pageParam, paramList, ResourceEnums.pt_project_info);
    }

    /**
     * 根据项目的唯一编码查询全部信息
     * @param id
     * @return
     */
    @Override
    public List<DetailData> getByBusinessId(String id) {
        return getByPk(ResourceEnums.pt_project_info, DETAIL_URL, id);
    }

    @Override
    public List<DetailData> detailDataList(String dataId) {
        return getByDataId(ResourceEnums.pt_project_info.toString(), IResourceService.DETAIL_URL, dataId);
    }

    @Override
    public Map<String, Object> detail(String dataId) {
        return getColumnContent(getByDataId(ResourceEnums.pt_project_info.toString(), IResourceService.DETAIL_URL, dataId), null);
    }

    @Override
    public int getCountByProjectNumber(String projectNumber) {
        return selectCountByProject(jdbcTemplate, ResourceEnums.pt_project_info, projectNumber);
    }

    @Override
    public Result rsyncData(Object obj) throws Exception {
        CmProjectInfo entity = (CmProjectInfo) obj;
        Map<String, Object> param = new HashMap<>(2);
        param.put("objUrl", ResourceEnums.pt_project_info.toString());
        Map<String, Object> info = new HashMap<>(10);
        info.put("approvalNumber", entity.getApprovalNumber());
        info.put("projectNumber", entity.getProjectNumber());
        info.put("projectName", entity.getProjectName());
        info.put("projectAddress", entity.getProjectAddress());
        Department buildDept = DeptCacheUtil.getDeptById(entity.getBuildDeptId());
        if (buildDept != null) {
            info.put("buildDeptId", buildDept.getResourceId());
            info.put("buildDept", buildDept.getName());
        }
        info.put("buildArea", entity.getBuildArea());
        info.put("projectType", entity.getProjectType());
        info.put("projectMoney", entity.getInvestmentAmount());
        info.put("project_area", entity.getRegion());
        info.put("approvalDate", entity.getProjectApprovalDate());
        info.put("projectCate", "政府");
        param.put("info", info);
        Result result = HttpClientUtil.post(GlobalContext.getProperty("resourceSystemUrl") + REPORT_URL , new ObjectMapper().writeValueAsString(param));
        if (result.isSuccess()) {
            //同步附件
            ResourceEnums.pt_attach_info.getResourceService().rsyncData(ResourceAttachInfo.create(SpringContextHolder.getBean(IAttachService.class),
                    entity.getAttachFile(), entity.getDeleteAttachFile(), SystemSecurityUtils.getUser().getDisplayName(),
                    entity.getProjectNumber(), ResourceEnums.pt_project_info.toString()));
        }
        return result;
    }
}
