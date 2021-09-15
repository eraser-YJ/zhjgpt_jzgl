package com.jc.mobile.web;

import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.ResultCode;
import com.jc.foundation.util.StringUtil;
import com.jc.mobile.basic.web.MobileController;
import com.jc.mobile.util.MobileApiResponse;
import com.jc.resource.bean.*;
import com.jc.resource.enums.ResourceEnums;
import com.jc.resource.util.ResourceUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 移动端资源库接口
 * @Author 常鹏
 * @Date 2020/8/3 16:13
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/mobile/resource")
public class MobileResourceController extends MobileController {

    /**
     * 企业信息列表
     * @param page
     * @param request
     * @return
     */
    @RequestMapping(value="/company/list.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse companyList(PageManager page, HttpServletRequest request){
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        String companyName = request.getParameter("companyName");
        List<QueryDataParam> paramList = new ArrayList<>();
        if (!StringUtil.isEmpty(companyName)) {
            paramList.add(QueryDataParam.create(ResourceOperatorTypeEnum.varchar.getType(), ResourceOperatorActionEnum.like.toString(), "company_name", companyName));
        }
        return selectRecord(page, ResourceEnums.pt_company_info.toString(), paramList);
    }

    /**
     * 人员信息库
     * @param page
     * @param request
     * @return
     */
    @RequestMapping(value="/person/list.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse personList(PageManager page, HttpServletRequest request){
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        String personName = request.getParameter("personName");
        List<QueryDataParam> paramList = new ArrayList<>();
        if (!StringUtil.isEmpty(personName)) {
            paramList.add(QueryDataParam.create(ResourceOperatorTypeEnum.varchar.getType(), ResourceOperatorActionEnum.like.toString(), "person_name", personName));
        }
        return selectRecord(page, ResourceEnums.pt_person_info.toString(), paramList);
    }

    private MobileApiResponse selectRecord(PageManager page, String sign, List<QueryDataParam> paramList) {
        PageManagerMapData pageMapData = ResourceUtil.query(sign, page, paramList);
        int no = page.getPage() * page.getPageRows() + 1;
        List<Map<String, String>> dataList = pageMapData.getData();
        if (dataList != null) {
            for (Map<String, String> map : dataList) {
                map.put("no", (no++) + "");
            }
        }
        return MobileApiResponse.ok(pageMapData);
    }

    /**
     * 企业信息库
     * @param
     * @param request
     * @return
     */
    @RequestMapping(value="/company/detail.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse companyDetail(HttpServletRequest request){
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        String companyId = request.getParameter("companyId");
        if (StringUtil.isEmpty(companyId)) {
            return MobileApiResponse.error(ResultCode.PARAM_IS_BLANK);
        }
        List<DetailData> pageMapData = ResourceUtil.detailDataList(ResourceEnums.pt_company_info.toString(), companyId);
        Map<String, String> data = new HashMap<>();
        if (pageMapData != null) {
            for (DetailData item : pageMapData) {
                data.put(item.getItemName(), item.getValue());
            }
        }
        return MobileApiResponse.ok(data);
    }

    /**
     * 人员信息库
     * @param
     * @param request
     * @return
     */
    @RequestMapping(value="/person/detail.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse detail(HttpServletRequest request){
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        String personId = request.getParameter("personId");
        if (StringUtil.isEmpty(personId)) {
            return MobileApiResponse.error(ResultCode.PARAM_IS_BLANK);
        }
        List<DetailData> pageMapData = ResourceUtil.detailDataList(ResourceEnums.pt_person_info.toString(), personId);
        Map<String, String> data = new HashMap<>();
        if (pageMapData != null) {
            for (DetailData item : pageMapData) {
                data.put(item.getItemName(), item.getValue());
            }
        }
        return MobileApiResponse.ok(data);
    }

}
