package com.jc.csmp.warn.info.web;

import com.jc.csmp.equipment.info.domain.EquipmentInfo;
import com.jc.csmp.equipment.info.service.IEquipmentInfoService;
import com.jc.csmp.warn.info.dao.IRealtimeInfoDao;
import com.jc.csmp.warn.info.domain.RealtimeInfo;
import com.jc.csmp.warn.info.domain.WarnStatisInfo;
import com.jc.csmp.warn.info.domain.validator.WarnInfoValidator;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Version 1.0
 */
@Controller
@RequestMapping(value = "/warn/statis/")
public class WarnStatisController extends BaseController {

    @Autowired
    private IEquipmentInfoService equipmentInfoService;
    @Autowired
    private IRealtimeInfoDao dao;


    public WarnStatisController() {
    }


    /**
     * 分页查询方法
     *
     * @return
     */
    @RequestMapping(value = "manageList.action", method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    public List<WarnStatisInfo> manageList(WarnStatisInfo cond) {
        try {

            List<WarnStatisInfo> dataList = dao.queryWarnProcessStatis(cond);
            return dataList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @RequestMapping(value = "process.action", method = RequestMethod.GET)
    public String manage() throws Exception {
        return "warn/info/warnInfoStatis";
    }


}

