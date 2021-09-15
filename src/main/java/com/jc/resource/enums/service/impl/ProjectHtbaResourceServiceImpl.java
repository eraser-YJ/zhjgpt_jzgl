package com.jc.resource.enums.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jc.csmp.contract.info.domain.CmContractInfo;
import com.jc.csmp.project.domain.CmProjectInfo;
import com.jc.csmp.project.service.ICmProjectInfoService;
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

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 合同备案对接资源库信息
 * @Author 常鹏
 * @Date 2020/7/27 14:44
 * @Version 1.0
 */
public class ProjectHtbaResourceServiceImpl extends ResourceServiceImpl implements IResourceService {
    private JdbcTemplate jdbcTemplate;

    public ProjectHtbaResourceServiceImpl() {
        jdbcTemplate = SpringContextHolder.getBean(JdbcTemplate.class);
    }

    @Override
    public PageManagerMapData query(PageManager pageParam, List<QueryDataParam> paramList) {
        return query(PAGE_URL, pageParam, paramList, ResourceEnums.pt_company_projects_htba);
    }

    /**
     * 根据项目的唯一编码查询全部信息
     * @param id
     * @return
     */
    @Override
    public List<DetailData> getByBusinessId(String id) {
        return getByPk(ResourceEnums.pt_company_projects_htba, DETAIL_URL, id);
    }

    @Override
    public int getCountByProjectNumber(String projectNumber) {
        return selectCountByProject(jdbcTemplate, ResourceEnums.pt_company_projects_htba, projectNumber);
    }

    @Override
    public List<DetailData> detailDataList(String dataId) {
        return getByDataId(ResourceEnums.pt_company_projects_htba.toString(), IResourceService.DETAIL_URL, dataId);
    }

    @Override
    public Map<String, Object> detail(String dataId) {
        return getColumnContent(getByDataId(ResourceEnums.pt_company_projects_htba.toString(), IResourceService.DETAIL_URL, dataId), null);
    }

    @Override
    public Result rsyncData(Object obj) throws Exception {
        CmContractInfo entity = (CmContractInfo) obj;
        Map<String, Object> param = new HashMap<>(2);
        param.put("objUrl", ResourceEnums.pt_company_projects_htba.toString());
        Map<String, Object> info = new HashMap<>(10);
        info.put("id", entity.getId());
        info.put("pcp_htlb", entity.getContractType());
        info.put("pcp_htbh", entity.getContractCode());
        info.put("pcp_htje", entity.getContractMoney());
        info.put("pcp_qdrq", entity.getSignDate());
        info.put("pcp_xq", entity.getContractContent());
        try {
            Department partyB = DeptCacheUtil.getDeptById(entity.getPartybUnit());
            if (partyB != null) {
                info.put("partyBid", partyB.getResourceId());
                info.put("partyBname", partyB.getName());
            }
            CmProjectInfo project = SpringContextHolder.getBean(ICmProjectInfoService.class).getById(entity.getProjectId());
            if (project != null) {
                info.put("pcp_project_num", project.getProjectNumber());
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
                    entity.getId(), ResourceEnums.pt_company_projects_htba.toString()));
        }
        return result;
    }
}
