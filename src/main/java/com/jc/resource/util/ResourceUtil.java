package com.jc.resource.util;

import com.jc.csmp.project.domain.CmProjectInfo;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.Result;
import com.jc.foundation.util.ResultCode;
import com.jc.resource.bean.*;
import com.jc.resource.enums.ResourceEnums;
import com.jc.resource.enums.service.IResourceService;
import com.jc.resource.enums.service.impl.ResourceServiceImpl;
import com.jc.workflow.util.JsonUtil;
import com.jc.workflow.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 常鹏
 * @Date 2020/7/28 12:55
 * @Version 1.0
 */
public class ResourceUtil {
    /**
     * 获取条件和表格头
     * @param objUrl
     * @return
     */
    public static ReturnDataPageModel getCondAndHeader(String objUrl) {
        if (StringUtil.isEmpty(objUrl)) {
            return null;
        }
        Result res = HttpClientUtil.get(GlobalContext.getProperty("resourceSystemUrl") + IResourceService.HEADER_COND_URL + "?modelSign=" + ResourceServiceImpl.getRealObjUrl(objUrl));
        if (res.getCode().intValue() == ResultCode.SUCCESS.code().intValue()) {
            return (ReturnDataPageModel) JsonUtil.json2Java((String) res.getData(), ReturnDataPageModel.class);
        }
        return null;
    }

    /**
     * 列表数据
     * @param sign
     * @param pageParam
     * @param paramList
     * @return
     */
    public static PageManagerMapData query(String sign, PageManager pageParam, List<QueryDataParam> paramList) {
        return ResourceServiceImpl.query(IResourceService.PAGE_URL, pageParam, paramList, sign);
    }

    /**
     * 详细数据
     * @param enums
     * @param column
     * @param value
     * @return
     */
    public static List<DetailData> detailDataList(ResourceEnums enums, String column, String value) {
        return ResourceServiceImpl.getByPk(enums, IResourceService.DETAIL_URL, column, value);
    }

    public static List<DetailData> detailDataList(String sign, String dataId) {
        return ResourceServiceImpl.getByDataId(sign, IResourceService.DETAIL_URL, dataId);
    }

    /**
     * 根据dlh_data_id_获取详细数据
     * @param sign: 资源表示
     * @param dataId: dlh_data_id_
     * @return
     */
    public static Map<String, Object> detail(String sign, String dataId) {
        return ResourceServiceImpl.getColumnContent(ResourceServiceImpl.getByDataId(sign, IResourceService.DETAIL_URL, dataId), null);
    }

    /**
     * 获取未竣工的项目，监管使用
     * @return
     */
    public static List<CmProjectInfo> getNoFinishProject() {
        List<QueryDataParam> paramList = new ArrayList<>();
        paramList.add(QueryDataParam.create(ResourceOperatorTypeEnum.datetime.getType(), ResourceOperatorActionEnum.isnull.toString(), "realFinishDate", null));
        PageManager pageParam = new PageManager();
        pageParam.setPageRows(100000);
        pageParam.setPage(0);
        PageManagerMapData page = ResourceUtil.query(ResourceEnums.pt_project_info.toString(), pageParam, paramList);
        List<CmProjectInfo> projectList = new ArrayList<>();
        if (page.getData() != null) {
            List<Map<String, Object>> mapList = page.getData();
            for (Map<String, Object> map : mapList) {
                CmProjectInfo project = new CmProjectInfo();
                project.setProjectNumber((String) map.get("projectNumber"));
                project.setProjectName((String) map.get("projectName"));
                projectList.add(project);
            }
        }
        return projectList;
    }
}
