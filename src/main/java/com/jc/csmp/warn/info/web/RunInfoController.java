package com.jc.csmp.warn.info.web;

import com.jc.common.kit.vo.PageManagerEx;
import com.jc.csmp.common.mongo.MongoDialect;
import com.jc.csmp.equipment.info.domain.EquipmentInfo;
import com.jc.csmp.equipment.info.service.IEquipmentInfoService;
import com.jc.csmp.warn.info.domain.RunInfo;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.GlobalUtil;
import com.jc.foundation.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Version 1.0
 */
@Controller
@RequestMapping(value = "/run/info/")
public class RunInfoController extends BaseController {
    @Autowired
    private IEquipmentInfoService equipmentInfoService;

    /**
     * 分页查询方法
     *
     * @param page
     * @return
     */
    @RequestMapping(value = "manageList.action", method = RequestMethod.GET)
    @ResponseBody
    public PageManagerEx<Map<String, Object>> manageList(RunInfo info, PageManager page) throws CustomException {

        //保存
        MongoDialect dialect = new MongoDialect();
        PageManagerEx page_ = dialect.query(info, page);
        GlobalUtil.setTableRowNoEx(page_,page_.getPageRows());
        return page_;
    }



    @RequestMapping(value = "manage.action", method = RequestMethod.GET)
    public String message(HttpServletRequest request) throws CustomException {
        String equiId = request.getParameter("equiId");
        //检查
        EquipmentInfo entityCond = new EquipmentInfo();
        entityCond.setId(equiId);
        EquipmentInfo info = equipmentInfoService.get(entityCond);
        request.setAttribute("data", info);
        request.setAttribute("pageTitle", "智慧工地");
        return "warn/info/runInfoList";


    }
}

