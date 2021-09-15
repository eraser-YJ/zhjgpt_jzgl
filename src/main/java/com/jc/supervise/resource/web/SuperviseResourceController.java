package com.jc.supervise.resource.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.GlobalUtil;
import com.jc.foundation.util.Result;
import com.jc.foundation.util.ResultCode;
import com.jc.foundation.util.StringUtil;
import com.jc.foundation.web.BaseController;
import com.jc.resource.bean.*;
import com.jc.resource.enums.ResourceEnums;
import com.jc.resource.util.ResourceUtil;
import com.jc.system.applog.ActionLog;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 监管资源控制器
 * @Author 常鹏
 * @Date 2020/8/18 13:39
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/supervise/resource")
public class SuperviseResourceController extends BaseController {
    public SuperviseResourceController() {
    }

    /**
     * 监管资源列表页
     * @param request
     * @return
     */
    @RequestMapping(value = "manage.action", method = RequestMethod.GET)
    @ActionLog(operateModelNm="",operateFuncNm="manage",operateDescribe="进入资源列表信息")
    public String manage(HttpServletRequest request) {
        String sign = request.getParameter("sign");
        ReturnDataPageModel model = ResourceUtil.getCondAndHeader(sign);
        request.setAttribute("sign", sign);
        request.setAttribute("pageTitle", ResourceEnums.getByCode(sign).getSupervisePageTitle());
        if (model != null) {
            request.setAttribute("pageCondArea", model.getCondList());
            request.setAttribute("pageColumnArea", model.getHeadBody());
        }
        return "supervise/resource/superviseResourceList";
    }

    /**
     * 监管资源列表数据
     * @param request
     * @param page
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "manageList.action", method = RequestMethod.GET)
    @ResponseBody
    public PageManagerMapData manageList(HttpServletRequest request, PageManager page) throws Exception {
        String sign = request.getParameter("sign");
        String condJson = GlobalUtil.replaceStr(request.getParameter("condJson"), "“", "\"");
        List<QueryDataParam> paramList = null;
        if (!StringUtil.isEmpty(condJson)) {
            paramList = new ObjectMapper().readValue(condJson, new TypeReference<List<QueryDataParam>>() {});
        }
        // 根据项目编号查询，一般这个参数与上面的condJson不会同时出现
        String projectNumber = request.getParameter("projectNumber");
        if (!StringUtil.isEmpty(projectNumber)) {
            if (paramList == null) {
                paramList = new ArrayList<>();
            }
            paramList.add(QueryDataParam.create(ResourceOperatorTypeEnum.varchar.getType(), ResourceOperatorActionEnum.like.toString(), ResourceEnums.getByCode(sign).getProject(), projectNumber));
        }
        PageManagerMapData pageMapData = ResourceEnums.getByCode(sign).getResourceService().query(page, paramList);
        int no = page.getPage() * page.getPageRows() + 1;
        List<Map<String, String>> dataList = pageMapData.getData();
        if (dataList != null) {
            for (Map<String, String> map : dataList) {
                map.put("no", (no++) + "");
            }
        }
        return pageMapData;
    }

    /**
     * 展示详细信息
     * @param request
     * @return
     */
    @RequestMapping(value = "detail.action")
    @ActionLog(operateModelNm="",operateFuncNm="manage",operateDescribe="进入资源详细信息")
    @ResponseBody
    public List<DetailData> detail(HttpServletRequest request) {
        String sign = request.getParameter("sign");
        String dataId = request.getParameter("dataId");
        if (StringUtil.isEmpty(sign) || StringUtil.isEmpty(dataId)) {
            return Collections.emptyList();
        }
        return ResourceEnums.getByCode(sign).getResourceService().detailDataList(dataId);
    }

    /**
     * 获取资源表格表头头部信息
     * @param request
     * @return
     */
    @RequestMapping(value = "resourceHeader.action", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> resourceHeader(HttpServletRequest request) {
        String sign = request.getParameter("sign");
        ReturnDataPageModel model = ResourceUtil.getCondAndHeader(sign);
        return model.getHeadBody();
    }

    /**
     * 加载流程图显示隐藏
     * @param projectNumber: 项目编号
     * @return
     */
    @RequestMapping(value = "flowDataCount.action", method = RequestMethod.GET)
    @ResponseBody
    public Result flowDataCount(String projectNumber){
        if (StringUtil.isEmpty(projectNumber)) {
            return Result.failure(ResultCode.PARAM_IS_BLANK);
        }
        Map<String, Integer> resultMap = new HashMap<>(8);
        resultMap.put("pt_project_approval", ResourceEnums.pt_project_approval.getResourceService().getCountByProjectNumber(projectNumber));
        resultMap.put("pt_project_design", ResourceEnums.pt_project_design.getResourceService().getCountByProjectNumber(projectNumber));
        resultMap.put("pt_company_projects_ztb", ResourceEnums.pt_company_projects_ztb.getResourceService().getCountByProjectNumber(projectNumber));
        resultMap.put("pt_company_projects_htba", ResourceEnums.pt_company_projects_htba.getResourceService().getCountByProjectNumber(projectNumber));
        resultMap.put("pt_project_finish", ResourceEnums.pt_project_finish.getResourceService().getCountByProjectNumber(projectNumber));
        resultMap.put("pt_company_projects_sgxk", ResourceEnums.pt_company_projects_sgxk.getResourceService().getCountByProjectNumber(projectNumber));
        resultMap.put("pt_project_quality", ResourceEnums.pt_project_quality.getResourceService().getCountByProjectNumber(projectNumber));
        resultMap.put("pt_project_safe", ResourceEnums.pt_project_safe.getResourceService().getCountByProjectNumber(projectNumber));
        return Result.success(resultMap);
    }
}
