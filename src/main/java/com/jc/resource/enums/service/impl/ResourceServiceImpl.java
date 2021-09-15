package com.jc.resource.enums.service.impl;

import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.*;
import com.jc.resource.bean.*;
import com.jc.resource.enums.ResourceEnums;
import com.jc.resource.util.HttpClientUtil;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;

/**
 * @Author 常鹏
 * @Date 2020/7/27 15:47
 * @Version 1.0
 */
public class ResourceServiceImpl {
    /**
     * 创建参数对象
     * @param pageParam
     * @return
     */
    public static PageManagerMapData createPage(PageManager pageParam) {
        PageManagerMapData page = new PageManagerMapData();
        if(pageParam.getPageRows() == -1) {
            pageParam.setPageRows(2147483647);
        }
        page.setPage(pageParam.getPage());
        page.setPageRows(pageParam.getPageRows());
        page.setsEcho(pageParam.getsEcho());
        return page;
    }

    /**
     * 基础分页查询，并返回结果
     * @param url: 请求的url
     * @param pageParam: 分页参数
     * @param paramList：查询条件
     * @param enums: 资源库
     * @return
     */
    public static PageManagerMapData query(String url, PageManager pageParam, List<QueryDataParam> paramList, ResourceEnums enums) {
        return select(url, pageParam, paramList, enums.toString());
    }

    /**
     * 基础分页查询，并返回结果
     * @param url: 请求的url
     * @param pageParam: 分页参数
     * @param paramList：查询条件
     * @param sign: 资源库标识
     * @return
     */
    public static PageManagerMapData query(String url, PageManager pageParam, List<QueryDataParam> paramList, String sign) {
        return select(url, pageParam, paramList, sign);
    }

    private static PageManagerMapData select(String url, PageManager pageParam, List<QueryDataParam> paramList, String sign) {
        QueryDataModel model = new QueryDataModel();
        if (paramList != null) {
            model.setCondJson(paramList);
        }
        model.setObjUrl(getRealObjUrl(sign));
        model.setPage(pageParam.getPage());
        model.setPageRows(pageParam.getPageRows());
        Result result = HttpClientUtil.post(GlobalContext.getProperty("resourceSystemUrl") + url, JsonUtil.java2Json(model));
        PageManagerMapData page = createPage(pageParam);
        if (result.getCode().intValue() == ResultCode.SUCCESS.code().intValue()) {
            ReturnDataPageModel pageModel = (ReturnDataPageModel) JsonUtil.json2Java((String) result.getData(), ReturnDataPageModel.class);
            page.setPage(pageModel.getPage());
            page.setTotalCount(pageModel.getTotalCount());
            page.setPageRows(pageModel.getPageRows());
            page.setData(pageModel.getData());
            int pageCount = pageModel.getTotalCount() / pageParam.getPageRows();
            if(pageModel.getTotalCount() % pageParam.getPageRows() > 0) {
                pageCount++;
            }
            page.setTotalPages(pageCount);
        }
        return page;
    }

    /**
     * 根据资源表配置的主键获取详细信息
     * @param enums
     * @param url
     * @param value
     * @return
     */
    public static List<DetailData> getByPk(ResourceEnums enums, String url, String value) {
        return getByPk(enums, url, enums.getPkColumn(), value);
    }

    public static List<DetailData> getByPk(ResourceEnums enums, String url, String column, String value) {
        try {
            StringBuffer conn = new StringBuffer();
            conn.append(GlobalContext.getProperty("resourceSystemUrl") + url);
            conn.append("?objUrl=" + getRealObjUrl(enums));
            conn.append("&dlhDataId=" + value);
            conn.append("&yewuKey=" + column);
            Result result = HttpClientUtil.get(conn.toString());
            if (result.getCode().intValue() == ResultCode.SUCCESS.code().intValue()) {
                ReturnDetailModel mode = (ReturnDetailModel) JsonUtil.json2Java((String) result.getData(), ReturnDetailModel.class);
                if (mode.check()) {
                    return mode.getData();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * 根据资源表的dlhDataId获取详细信息
     * @param sign
     * @param url
     * @param value
     * @return
     */
    public static List<DetailData> getByDataId(String sign, String url, String value) {
        try {
            StringBuffer conn = new StringBuffer();
            conn.append(GlobalContext.getProperty("resourceSystemUrl") + url);
            conn.append("?objUrl=" + sign);
            conn.append("&dlhDataId=" + value);
            Result result = HttpClientUtil.get(conn.toString());
            if (result.getCode().intValue() == ResultCode.SUCCESS.code().intValue()) {
                ReturnDetailModel mode = (ReturnDetailModel) JsonUtil.json2Java((String) result.getData(), ReturnDetailModel.class);
                if (mode.check()) {
                    return mode.getData();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * 过滤要显示的详细信息字段
     * @param dataList
     * @param column
     * @return
     */
    public static Map<String, Object> getColumnContent(List<DetailData> dataList, String[] column) {
        if (dataList == null || dataList.size() == 0) {
            return null;
        }
        Map<String, Object> resultMap = new HashMap<>(dataList.size());
        for (DetailData detailData : dataList) {
            if (detailData.getItemType().equals(ResourceOperatorTypeEnum.dic.toString())) {
                resultMap.put(detailData.getItemName(), detailData.getDicCode());
                resultMap.put(detailData.getItemName() + "Value", detailData.getValue());
            } else {
                resultMap.put(detailData.getItemName(), detailData.getValue());
            }
        }
        if (column == null || column.length == 0) {
            return resultMap;
        } else {
            Map<String, Object> map = new HashMap<>(column.length);
            for (String s : column) {
                map.put(s, resultMap.get(s));
            }
            return map;
        }
    }

    /**
     * 根据项目编号获取想用数据的条数
     * @param jdbcTemplate: jdbc模板
     * @param enums: 资源枚举
     * @param code: 项目编号
     * @return
     */
    protected int selectCountByProject(JdbcTemplate jdbcTemplate, ResourceEnums enums, String code) {
        return selectCountByProject(jdbcTemplate, enums, code, null);
    }

    protected int selectCountByProject(JdbcTemplate jdbcTemplate, ResourceEnums enums, String code, List<Map<String, Object>> paramList) {
        if (StringUtil.isEmpty(code)) {
            return 0;
        }
        List<Object> paramObjectList = new ArrayList<>();
        paramObjectList.add(code);
        String sql = "select count(*) as cnum from " + getRealObjUrl(enums) + " where " + enums.getProject() + " = ?";
        if (paramList != null) {
            for (Map<String, Object> param : paramList) {
                sql += " and " + param.get("key") + " = ?";
                paramObjectList.add(param.get("value"));
            }
        }
        List<Map<String, Object>> dataList = jdbcTemplate.queryForList(sql, paramObjectList.toArray());
        if (dataList == null || dataList.size() == 0) {
            return 0;
        }
        Map<String, Object> map = dataList.get(0);
        if (map == null) {
            return 0;
        }
        return GlobalUtil.toInteger(map.get("cnum") + "", 0);
    }

    protected int selectCountBySql(JdbcTemplate jdbcTemplate, String sql, Object[] param) {
        List<Map<String, Object>> dataList = jdbcTemplate.queryForList(sql, param);
        if (dataList == null || dataList.size() == 0) {
            return 0;
        }
        Map<String, Object> map = dataList.get(0);
        if (map == null) {
            return 0;
        }
        return GlobalUtil.toInteger(map.get("cnum") + "", 0);
    }

    public static String getRealObjUrl(ResourceEnums enums) {
        return getRealObjUrl(enums.toString());
    }

    public static String getRealObjUrl(String sign) {
        if (sign.equals(ResourceEnums.pt_project_approval.toString())) {
            sign = ResourceEnums.pt_project_info.toString();
        } else if (sign.equals(ResourceEnums.pt_project_finish.toString())) {
            sign = ResourceEnums.pt_project_info.toString();
        }
        return sign;
    }
}
