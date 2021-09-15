package com.jc.resource.enums.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jc.csmp.contract.info.domain.CmContractInfo;
import com.jc.csmp.contract.info.service.ICmContractInfoService;
import com.jc.csmp.project.domain.CmProjectInfo;
import com.jc.csmp.project.domain.CmProjectQuestion;
import com.jc.csmp.project.service.ICmProjectInfoService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.*;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 质量问题信息为了统一接口。模拟资源的操作
 * @Author 常鹏
 * @Date 2020/7/27 14:44
 * @Version 1.0
 */
public class QuestionQualityResourceServiceImpl extends ResourceServiceImpl implements IResourceService {

    private JdbcTemplate jdbcTemplate;

    public QuestionQualityResourceServiceImpl() {
        jdbcTemplate = SpringContextHolder.getBean(JdbcTemplate.class);
    }

    @Override
    public PageManagerMapData query(PageManager pageParam, List<QueryDataParam> paramList) {
        return query(PAGE_URL, pageParam, paramList, ResourceEnums.pt_project_quality);
    }

    /**
     * 根据项目的唯一编码查询全部信息
     * @param id
     * @return
     */
    @Override
    public List<DetailData> getByBusinessId(String id) {
        return getByPk(ResourceEnums.pt_project_quality, DETAIL_URL, id);
    }

    @Override
    public int getCountByProjectNumber(String projectNumber) {
        return selectCountByProject(jdbcTemplate, ResourceEnums.pt_project_quality, projectNumber);
    }

    @Override
    public List<DetailData> detailDataList(String dataId) {
        return getByDataId(ResourceEnums.pt_project_quality.toString(), IResourceService.DETAIL_URL, dataId);
    }

    @Override
    public Map<String, Object> detail(String dataId) {
        return getColumnContent(getByDataId(ResourceEnums.pt_project_quality.toString(), IResourceService.DETAIL_URL, dataId), null);
    }

    @Override
    public Result rsyncData(Object obj) throws Exception {
        CmProjectQuestion entity = (CmProjectQuestion) obj;
        Map<String, Object> param = new HashMap<>(2);
        param.put("objUrl", ResourceEnums.pt_project_quality.toString());
        Map<String, Object> info = new HashMap<>(10);
        info.put("id", entity.getId());
        info.put("quality_num", entity.getCode());
        info.put("quality_title", entity.getTitle());
        info.put("quality_ms", entity.getQuestionMeta());
        info.put("quality_yq", entity.getRectificationAsk());
        info.put("quality_jg", entity.getRectificationResult());
        info.put("quality_hyjg", entity.getCheckResult());
        info.put("quality_remark", entity.getRemark());
        info.put("EXT_DATE1", entity.getCreateDate());
        try {
            Department department = DeptCacheUtil.getDeptById(entity.getRectificationCompany());
            if (department != null) {
                info.put("company_zznum", department.getResourceId());
                info.put("quality_zgdw", department.getName());
            }
            CmContractInfo contract = SpringContextHolder.getBean(ICmContractInfoService.class).getById(entity.getContractId());
            if (contract != null) {
                info.put("contract_num", contract.getContractCode());
                info.put("quality_ht", contract.getContractName());
            }
            CmProjectInfo project = SpringContextHolder.getBean(ICmProjectInfoService.class).getById(entity.getProjectId());
            if (project != null) {
                info.put("quality_project", project.getProjectName());
                info.put("project_num", project.getProjectNumber());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        param.put("info", info);
        Result result = HttpClientUtil.post(GlobalContext.getProperty("resourceSystemUrl") + REPORT_URL , new ObjectMapper().writeValueAsString(param));
        if (result.isSuccess()) {
            //同步附件
            ResourceEnums.pt_attach_info.getResourceService().rsyncData(ResourceAttachInfo.create(SpringContextHolder.getBean(IAttachService.class),
                    entity.getAttachFile(), entity.getDeleteAttachFile(), SystemSecurityUtils.getUser().getDisplayName(),
                    entity.getId(), ResourceEnums.pt_project_quality.toString()));
        }
        return result;
    }
}
