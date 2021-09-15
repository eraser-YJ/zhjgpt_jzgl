package com.jc.resource.enums.service.impl;

import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.Result;
import com.jc.resource.bean.DetailData;
import com.jc.resource.bean.PageManagerMapData;
import com.jc.resource.bean.QueryDataParam;
import com.jc.resource.enums.ResourceEnums;
import com.jc.resource.enums.service.IResourceService;

import java.util.List;
import java.util.Map;

public class PersonInfoServiceImpl extends ResourceServiceImpl implements IResourceService {

    @Override
    public PageManagerMapData query(PageManager pageParam, List<QueryDataParam> paramList) {
        return query(PAGE_URL, pageParam, paramList, ResourceEnums.pt_person_info);
    }

    @Override
    public List<DetailData> getByBusinessId(String id) {
        return getByPk(ResourceEnums.pt_person_info, DETAIL_URL, "id", id);
    }

    @Override
    public int getCountByProjectNumber(String projectNumber) {
        return 0;
    }

    @Override
    public List<DetailData> detailDataList(String dataId) {
        return getByDataId(ResourceEnums.pt_person_info.toString(), IResourceService.DETAIL_URL, dataId);
    }

    @Override
    public Map<String, Object> detail(String dataId) {
        return getColumnContent(getByDataId(ResourceEnums.pt_person_info.toString(), IResourceService.DETAIL_URL, dataId), null);
    }

    @Override
    public Result rsyncData(Object obj) throws Exception { return Result.success();
    }

}
