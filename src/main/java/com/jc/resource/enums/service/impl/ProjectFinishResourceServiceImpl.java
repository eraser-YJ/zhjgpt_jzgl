package com.jc.resource.enums.service.impl;

import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.Result;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.resource.bean.*;
import com.jc.resource.enums.ResourceEnums;
import com.jc.resource.enums.service.IResourceService;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 竣工对接资源库信息
 * @Author 常鹏
 * @Date 2020/7/27 14:44
 * @Version 1.0
 */
public class ProjectFinishResourceServiceImpl extends ResourceServiceImpl implements IResourceService {
    private JdbcTemplate jdbcTemplate;

    public ProjectFinishResourceServiceImpl() {
        jdbcTemplate = SpringContextHolder.getBean(JdbcTemplate.class);
    }

    @Override
    public PageManagerMapData query(PageManager pageParam, List<QueryDataParam> paramList) {
        if (paramList == null) {
            paramList = new ArrayList<>();
        }
        paramList.add(QueryDataParam.create(ResourceOperatorTypeEnum.datetime.getType(), ResourceOperatorActionEnum.notnull.toString(), "realFinishDate", null));
        return query(PAGE_URL, pageParam, paramList, ResourceEnums.pt_project_finish);
    }

    /**
     * 根据项目的唯一编码查询全部信息
     * @param id
     * @return
     */
    @Override
    public List<DetailData> getByBusinessId(String id) {
        return getByPk(ResourceEnums.pt_project_finish, DETAIL_URL, id);
    }

    @Override
    public int getCountByProjectNumber(String projectNumber) {
        String sql = "select count(*) as cnum from " + getRealObjUrl(ResourceEnums.pt_project_finish) + " where " + ResourceEnums.pt_project_finish.getProject() + " = ? and realFinishDate is not null";
        return selectCountBySql(jdbcTemplate, sql, new Object[]{projectNumber});
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
    public Result rsyncData(Object obj) throws Exception { return Result.success();
    }
}
