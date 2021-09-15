package com.jc.mobile.web;

import com.jc.csmp.contract.info.domain.CmContractInfo;
import com.jc.csmp.contract.info.service.ICmContractInfoService;
import com.jc.csmp.project.domain.CmProjectInfo;
import com.jc.csmp.project.plan.domain.CmProjectPlan;
import com.jc.csmp.project.plan.service.ICmProjectPlanService;
import com.jc.csmp.project.service.ICmProjectInfoService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.StringUtil;
import com.jc.mobile.basic.web.MobileController;
import com.jc.mobile.util.MobileApiResponse;
import com.jc.system.security.domain.User;
import com.jc.system.security.util.DeptCacheUtil;
import com.jc.system.security.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author 常鹏
 * @Date 2020/8/3 16:13
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/mobile/contract")
public class MobileContractController extends MobileController {
    @Autowired
    private ICmContractInfoService cmContractInfoService;

    /**
     * 根据项目过滤合同
     * @param projectId
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="dataListByProjectId.action", method=RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse dataListByProjectId(@RequestParam("projectId") String projectId, HttpServletRequest request) throws Exception{
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        CmContractInfo param = new CmContractInfo();
        param.setProjectId(projectId);
        List<CmContractInfo> dataList = this.cmContractInfoService.queryAll(param);
        if (dataList == null) {
            dataList = Collections.emptyList();
        }
        return MobileApiResponse.ok(dataList);
    }
}
