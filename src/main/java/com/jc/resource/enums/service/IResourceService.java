package com.jc.resource.enums.service;

import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.Result;
import com.jc.resource.bean.DetailData;
import com.jc.resource.bean.PageManagerMapData;
import com.jc.resource.bean.QueryDataParam;

import java.util.List;
import java.util.Map;

/**
 * 资源接口
 * @Author 常鹏
 * @Date 2020/7/27 14:43
 * @Version 1.0
 */
public interface IResourceService {
    String PAGE_URL = "/webapi/queryTable/listPage.action";
    String DETAIL_URL = "/webapi/queryTable/detailPage.action";
    String HEADER_COND_URL = "/webapi/queryTable/getHeaderOrCond.action";
    String REPORT_URL = "/webapi/queryTable/savePhyData.action";
    String ATTACH_UTL = "/webapi/queryTable/syncFileMap.action";
    /**
     * 分页查询
     * @param pageParam
     * @param paramList
     * @return
     */
    PageManagerMapData query(PageManager pageParam, List<QueryDataParam> paramList);

    /**
     * 根据业务id查询详细数据
     * @param id
     * @return
     */
    List<DetailData> getByBusinessId(String id);

    /**
     * 根据dlh_data_id_获取详细数据
     * @param dataId
     * @return
     */
    List<DetailData> detailDataList(String dataId);

    /**
     * 根据dlh_data_id_获取详细数据
     * @param dataId
     * @return
     */
    Map<String, Object> detail(String dataId);

    /**
     * 根据和唯一编码获取数量，使用sql进行查询
     * @param projectNumber
     * @return
     */
    int getCountByProjectNumber(String projectNumber);

    /**
     * 同步数据
     * @param obj
     * @return
     * @throws Exception
     */
    Result rsyncData(Object obj) throws Exception;
}
