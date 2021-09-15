package com.jc.supervise.basic.web;

import com.jc.csmp.project.domain.CmProjectQuestion;
import com.jc.csmp.project.service.ICmProjectQuestionService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.StringUtil;
import com.jc.foundation.web.BaseController;
import com.jc.system.applog.ActionLog;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.util.DeptCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 基础监管控制器
 * @Author 常鹏
 * @Date 2020/7/21 15:20
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/supervise/basic")
public class SuperviseBasicController extends BaseController {
    @Autowired
    private ICmProjectQuestionService projectQuestionService;

    public SuperviseBasicController() {
    }

    /**
     * 质量监督监管
     * @return
     */
    @RequestMapping(value = "quality.action", method = RequestMethod.GET)
    @ActionLog(operateModelNm="",operateFuncNm="manage",operateDescribe="进入合同备案监管列表")
    public String quality() {
        return "supervise/basic/superviseQualityList";
    }

    /**
     * 质量监督监管数据
     * @param entity
     * @param page
     * @return
     */
    @RequestMapping(value = "qualityList.action", method = RequestMethod.GET)
    @ResponseBody
    public PageManager qualityList(CmProjectQuestion entity, PageManager page){
        if(StringUtil.isEmpty(entity.getOrderBy())) {
            entity.addOrderByField(" t.modify_date ");
        }
        entity.setDeptCodeCondition(DeptCacheUtil.getCodeById(SystemSecurityUtils.getUser().getDeptId()));
        entity.setQuestionType("quality");
        PageManager page_ = projectQuestionService.query(entity, page);
        int no = page.getPage() * page.getPageRows() + 1;
        List<CmProjectQuestion> dataList = (List<CmProjectQuestion>) page_.getData();
        if (dataList != null) {
            for (CmProjectQuestion info : dataList) {
                info.setNo(no++);
            }
        }
        return page_;
    }

    /**
     * 安全监督监管
     * @return
     */
    @RequestMapping(value = "safe.action", method = RequestMethod.GET)
    @ActionLog(operateModelNm="",operateFuncNm="manage",operateDescribe="进入合同备案监管列表")
    public String safe() {
        return "supervise/basic/superviseSafeList";
    }

    /**
     * 安全监督监管数据
     * @param entity
     * @param page
     * @return
     */
    @RequestMapping(value = "safeList.action", method = RequestMethod.GET)
    @ResponseBody
    public PageManager safeList(CmProjectQuestion entity, PageManager page){
        if(StringUtil.isEmpty(entity.getOrderBy())) {
            entity.addOrderByField(" t.modify_date ");
        }
        entity.setDeptCodeCondition(DeptCacheUtil.getCodeById(SystemSecurityUtils.getUser().getDeptId()));
        entity.setQuestionType("safe");
        PageManager page_ = projectQuestionService.query(entity, page);
        int no = page.getPage() * page.getPageRows() + 1;
        List<CmProjectQuestion> dataList = (List<CmProjectQuestion>) page_.getData();
        if (dataList != null) {
            for (CmProjectQuestion info : dataList) {
                info.setNo(no++);
            }
        }
        return page_;
    }
}
