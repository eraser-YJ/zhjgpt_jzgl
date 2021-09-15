package com.jc.resource.enums.service.impl;

import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.JsonUtil;
import com.jc.resource.util.HttpClientUtil;
import com.jc.foundation.util.Result;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.resource.bean.DetailData;
import com.jc.resource.bean.PageManagerMapData;
import com.jc.resource.bean.QueryDataParam;
import com.jc.resource.enums.ResourceEnums;
import com.jc.resource.enums.service.IResourceService;
import com.jc.resource.enums.vo.ResourceAttachInfo;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 附件信息同步
 * @Author 常鹏
 * @Date 2020/8/18 10:10
 * @Version 1.0
 */
public class AttachResourceServiceImpl extends ResourceServiceImpl implements IResourceService {

    private JdbcTemplate jdbcTemplate;

    public AttachResourceServiceImpl() {
        jdbcTemplate = SpringContextHolder.getBean(JdbcTemplate.class);
    }

    @Override
    public PageManagerMapData query(PageManager pageParam, List<QueryDataParam> paramList) {
        return null;
    }

    @Override
    public List<DetailData> getByBusinessId(String id) {
        return null;
    }

    @Override
    public List<DetailData> detailDataList(String dataId) {
        return getByDataId(ResourceEnums.pt_attach_info.toString(), IResourceService.DETAIL_URL, dataId);
    }

    @Override
    public Map<String, Object> detail(String dataId) {
        return getColumnContent(getByDataId(ResourceEnums.pt_attach_info.toString(), IResourceService.DETAIL_URL, dataId), null);
    }

    @Override
    public int getCountByProjectNumber(String projectNumber) {
        return 0;
    }

    @Override
    public Result rsyncData(Object obj) throws Exception {
        if (obj == null) {
            return Result.success();
        }
        ResourceAttachInfo entity = (ResourceAttachInfo) obj;
        entity.setObjUrl(ResourceEnums.pt_attach_info.toString());
        System.out.println(JsonUtil.java2Json(entity));
        return HttpClientUtil.post(GlobalContext.getProperty("resourceSystemUrl") + ATTACH_UTL, JsonUtil.java2Json(entity));
    }
}
