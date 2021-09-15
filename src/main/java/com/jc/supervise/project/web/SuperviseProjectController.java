package com.jc.supervise.project.web;

import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.StringUtil;
import com.jc.foundation.web.BaseController;
import com.jc.resource.bean.PageManagerMapData;
import com.jc.resource.bean.QueryDataParam;
import com.jc.resource.bean.ResourceOperatorActionEnum;
import com.jc.resource.bean.ResourceOperatorTypeEnum;
import com.jc.resource.enums.ResourceEnums;
import com.jc.resource.vo.ResourceProjectInfo;
import com.jc.system.applog.ActionLog;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 项目动态监管
 * @Author 常鹏
 * @Date 2020/7/27 15:35
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/supervise/project")
public class SuperviseProjectController extends BaseController {
    /**
     * 项目信息动态监管
     * @return
     */
    @RequestMapping(value = "information.action", method = RequestMethod.GET)
    @ActionLog(operateModelNm="",operateFuncNm="manage",operateDescribe="项目信息动态监管")
    public String information() {
        return "supervise/project/superviseProjectInfoList";
    }

    @RequestMapping(value = "informationList.action", method = RequestMethod.GET)
    @ResponseBody
    public PageManagerMapData informationList(ResourceProjectInfo entity, PageManager page){
        if(StringUtil.isEmpty(entity.getOrderBy())) {
            entity.addOrderByField(" t.create_date ");
        }
        List<QueryDataParam> paramList = new ArrayList<>();
        if (!StringUtil.isEmpty(entity.getProjectNumber())) {
            paramList.add(QueryDataParam.create(ResourceOperatorTypeEnum.varchar.getType(), ResourceOperatorActionEnum.like.toString(), "projectNumber", entity.getProjectNumber()));
        }
        if (!StringUtil.isEmpty(entity.getProjectName())) {
            paramList.add(QueryDataParam.create(ResourceOperatorTypeEnum.varchar.getType(), ResourceOperatorActionEnum.like.toString(), "projectName", entity.getProjectName()));
        }
        PageManagerMapData pageMapData = ResourceEnums.pt_project_info.getResourceService().query(page, paramList);
        int no = page.getPage() * page.getPageRows() + 1;
        List<Map<String, String>> dataList = pageMapData.getData();
        if (dataList != null) {
            for (Map<String, String> map : dataList) {
                map.put("no", (no++) + "");
            }
        }
        return pageMapData;
    }

    @RequestMapping(value = "flowImages.action", method = RequestMethod.GET)
    @ActionLog(operateModelNm="",operateFuncNm="manage",operateDescribe="项目信息动态监管")
    public String flowImages() {
        return "supervise/project/superviseProjectInfoflowImages";
    }
}
