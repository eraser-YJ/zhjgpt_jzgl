package com.jc.resource.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jc.csmp.project.domain.CmProjectInfo;
import com.jc.csmp.project.service.ICmProjectInfoService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.GlobalUtil;
import com.jc.foundation.util.StringUtil;
import com.jc.foundation.web.BaseController;
import com.jc.resource.bean.*;
import com.jc.resource.enums.ResourceEnums;
import com.jc.resource.util.ResourceUtil;
import com.jc.system.applog.ActionLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 对接资源系统控制器
 * @Author 常鹏
 * @Date 2020/7/22 17:02
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/resource/system")
public class ResourceController extends BaseController {
    @Autowired
    private ICmProjectInfoService cmProjectInfoService;

    /**
     * 资源企业列表
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/company/{pageName}.action", method = RequestMethod.GET)
    @ActionLog(operateModelNm="",operateFuncNm="manage",operateDescribe="资源企业列表")
    public String company(HttpServletRequest request, @PathVariable String pageName) throws Exception{
        return "resource/company/" + pageName;
    }

    @RequestMapping(value = "companyList.action", method = RequestMethod.GET)
    @ResponseBody
    public PageManagerMapData companyList(String companyName, PageManager page) {
        List<QueryDataParam> paramList = null;
        if (!StringUtil.isEmpty(companyName)) {
            paramList = new ArrayList<>();
            paramList.add(QueryDataParam.create(ResourceOperatorTypeEnum.varchar.getType(), ResourceOperatorActionEnum.like.toString(), "company_name", companyName));
        }
        return ResourceEnums.pt_company_info.getResourceService().query(page, paramList);
    }

    /**
     * 获取资源库列表数据
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
        List<QueryDataParam> paramList = new ObjectMapper().readValue(condJson, new TypeReference<List<QueryDataParam>>() {});
        PageManagerMapData pageMapData = null;
        if (sign.equals(ResourceEnums.pt_project_approval.toString()) || sign.equals(ResourceEnums.pt_project_finish.toString())) {
            pageMapData = ResourceEnums.getByCode(sign).getResourceService().query(page, paramList);
        } else {
            pageMapData = ResourceUtil.query(sign, page, paramList);
        }
        boolean isProject = false;
        if (sign.equals(ResourceEnums.pt_project_info.toString())) {
            isProject = true;
        }
        int no = page.getPage() * page.getPageRows() + 1;
        List<Map<String, String>> dataList = pageMapData.getData();
        if (dataList != null) {
            for (Map<String, String> map : dataList) {
                map.put("no", (no++) + "");
                if (isProject) {
                    String canChoose = "true";
                    CmProjectInfo projectInfo = this.cmProjectInfoService.getbyProjectNumber(map.get("projectNumber"));
                    if (projectInfo != null) {
                        canChoose = "false";
                    }
                    map.put("canChoose", canChoose);
                }
            }
        }
        return pageMapData;
    }

    /*列表显示页面跳转*/
    @RequestMapping(value = "/viewDataPage.action", method = RequestMethod.GET)
    @ActionLog(operateModelNm="",operateFuncNm="manage",operateDescribe="资源企业列表")
    public String viewDataPage(HttpServletRequest request, PageManager page) throws Exception{

/*        String sign = request.getParameter("objUrl");
        PageManagerMapData pageMapData = null;
        String condJson = GlobalUtil.replaceStr(request.getParameter("condJson"), "“", "\"");
        List<QueryDataParam> paramList = new ArrayList<>();
        if(!StringUtil.isEmpty(condJson)){
            paramList = new ObjectMapper().readValue(condJson, new TypeReference<List<QueryDataParam>>() {});
        }
        pageMapData = ResourceUtil.query(sign, page, paramList);
        List<Map<String, Object>> info = new ArrayList<>();
        request.setAttribute("pageCondArea", info);
        request.setAttribute("pageCondAreaSize", info.size());*/
//        request.setAttribute("pageListArea", buildListArea(fieldList));

        String sign = request.getParameter("objUrl");
        ReturnDataPageModel model = ResourceUtil.getCondAndHeader(sign);
        request.setAttribute("sign", sign);
        request.setAttribute("pageTitle", ResourceEnums.getByCode(sign).getSupervisePageTitle());
        if (model != null) {
            request.setAttribute("pageCondArea", model.getCondList());
            request.setAttribute("pageCondAreaSize", model.getCondList().size());
            request.setAttribute("pageListArea", model.getHeadBody());
//            request.setAttribute("pageCondAreaSize", info.size());
//            request.setAttribute("pageListArea", buildListArea(fieldList));
        }


        return "resource/view/resourceViewDataList.jsp";
    }
}
