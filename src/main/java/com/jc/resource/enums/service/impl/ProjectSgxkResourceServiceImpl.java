package com.jc.resource.enums.service.impl;

import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.Result;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.resource.bean.DetailData;
import com.jc.resource.bean.PageManagerMapData;
import com.jc.resource.bean.QueryDataParam;
import com.jc.resource.enums.ResourceEnums;
import com.jc.resource.enums.service.IResourceService;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

/**
 * 施工许可对接资源库信息
 * @Author 常鹏
 * @Date 2020/7/27 14:44
 * @Version 1.0
 */
public class ProjectSgxkResourceServiceImpl extends ResourceServiceImpl implements IResourceService {
    private JdbcTemplate jdbcTemplate;

    public ProjectSgxkResourceServiceImpl() {
        jdbcTemplate = SpringContextHolder.getBean(JdbcTemplate.class);
    }

    @Override
    public PageManagerMapData query(PageManager pageParam, List<QueryDataParam> paramList) {
        return query(PAGE_URL, pageParam, paramList, ResourceEnums.pt_company_projects_sgxk);
    }

    /**
     * 根据项目的唯一编码查询全部信息
     * @param id
     * @return
     */
    @Override
    public List<DetailData> getByBusinessId(String id) {
        return getByPk(ResourceEnums.pt_company_projects_sgxk, DETAIL_URL, id);
    }

    @Override
    public int getCountByProjectNumber(String projectNumber) {
        return selectCountByProject(jdbcTemplate, ResourceEnums.pt_company_projects_sgxk, projectNumber);
    }

    @Override
    public List<DetailData> detailDataList(String dataId) {
        return getByDataId(ResourceEnums.pt_company_projects_sgxk.toString(), IResourceService.DETAIL_URL, dataId);
    }

    @Override
    public Map<String, Object> detail(String dataId) {
        return getColumnContent(getByDataId(ResourceEnums.pt_company_projects_sgxk.toString(), IResourceService.DETAIL_URL, dataId), null);
    }

    @Override
    public Result rsyncData(Object obj) throws Exception { return Result.success();
    }
}
