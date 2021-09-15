package com.jc.mobile.supervise.web;

import com.jc.csmp.project.domain.CmProjectInfo;
import com.jc.csmp.project.service.ICmProjectInfoService;
import com.jc.mobile.basic.web.MobileController;
import com.jc.mobile.util.MobileApiResponse;
import com.jc.mobile.vo.MobileProjectStatisBean;
import com.jc.resource.util.ResourceDbServer;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.Department;
import com.jc.system.security.domain.User;
import com.jc.system.security.util.DeptCacheUtil;
import com.jc.system.security.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 监管app
 * @Author 常鹏
 * @Date 2020/8/3 9:40
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/mobile/supervise")
public class MobileSuperviseController extends MobileController {

    @Autowired
    public ICmProjectInfoService cmProjectInfoService;

    /**
     * 监管首页统计数据
     * @param request
     * @return
     */
    @RequestMapping(value = "indexStatis.action",method= RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse indexStatis(HttpServletRequest request) {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        MobileProjectStatisBean result = new MobileProjectStatisBean();
        result.setPersonCount(ResourceDbServer.getInstance().getPersonCountByPtPersonInfo());
        result.setCompanyCount(ResourceDbServer.getInstance().getCountByPtCompanyInfo());
        result.setProjectCount(ResourceDbServer.getInstance().getCountByPtProjectInfo());
        Map<String, Double> data = ResourceDbServer.getInstance().getProductionTotalByProjectCate();
        result.setProjectOutputMoney(new BigDecimal(data.get("qiye")));
        result.setProjectTotalInvestment(new BigDecimal(data.get("zhengfu")));
        result.setWarningCount(ResourceDbServer.getInstance().getWarningCount());
        return MobileApiResponse.ok(result);
    }

    /**
     * 企业首页统计数据
     * @param request
     * @return
     */
    @RequestMapping(value = "companyStatis.action",method= RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse companyStatis(HttpServletRequest request) throws Exception{
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        User user = UserUtils.getUser(apiResponse.getUserId());
        Department department = DeptCacheUtil.getDeptById(user.getDeptId());
        String resourceId = department.getResourceId();
        MobileProjectStatisBean result = new MobileProjectStatisBean();
        CmProjectInfo entity = new CmProjectInfo();
        entity.setDeptCodeCondition(DeptCacheUtil.getCodeById(user.getDeptId()));
        List<CmProjectInfo> rsList = this.cmProjectInfoService.queryAll(entity);
        result.setPersonCount(ResourceDbServer.getInstance().getPersonCountByPtPersonInfo(resourceId));
        result.setProjectCount(rsList == null ? 0L : new Long(rsList.size()));
        result.setWarningCount(ResourceDbServer.getInstance().getWarningCountByBuildDeptId(department.getId()));
        return MobileApiResponse.ok(result);
    }
}
