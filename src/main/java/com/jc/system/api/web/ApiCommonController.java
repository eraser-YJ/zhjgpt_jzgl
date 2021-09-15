package com.jc.system.api.web;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.GlobalContext;
import com.jc.system.common.service.ICommonService;
import com.jc.system.security.domain.AdminSide;
import com.jc.system.security.domain.Role;
import com.jc.system.security.domain.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Controller
@RequestMapping(value = "/api/system")
public class ApiCommonController {

    protected transient final Logger logger = Logger.getLogger(this.getClass());

    private static final String SYS_ORG_ID = "sys.orgid";
    private static final String SYS_ORG_VAL = "[0-9]+";

    @Autowired
    ICommonService commonService;

    /**
     * @description 显示管理员机构树
     */
    @RequestMapping(value = "managerDeptTree.action",method=RequestMethod.GET)
    @ResponseBody
    public List<AdminSide> managerDeptTree(HttpServletRequest request) {
        Object orgId = request.getSession().getAttribute("roleOrgId");
        String rootId = request.getParameter("rootId");
        rootId = rootId == null ? "" : rootId;
        if (orgId != null && !"".equals(orgId.toString())) {
            boolean result = orgId.toString().matches(SYS_ORG_VAL);
            if (result == true) {
                return commonService.queryManagerDeptRree(orgId.toString());
            } else {
                return commonService.queryManagerDeptRree();
            }
        } else if(GlobalContext.getProperty(SYS_ORG_ID) != null && !"".equals(GlobalContext.getProperty(SYS_ORG_ID))) {
            boolean result = GlobalContext.getProperty(SYS_ORG_ID).matches(SYS_ORG_VAL);
            if (result == true) {
                return commonService.queryManagerDeptRree(GlobalContext.getProperty(SYS_ORG_ID));
            } else {
                return commonService.queryManagerDeptRree(rootId);
            }
        } else {
            return commonService.queryManagerDeptRree(rootId);
        }
    }

    /**
     * 根据用户ID获取用户信息
     * @param id
     * @return
     */
    @RequestMapping(value = "getUserById.action",method=RequestMethod.GET)
    @ResponseBody
    public User getUserById(String id)	{
        User user = commonService.getUserById(id);
        return user;
    }

    /**
     * @description 获取在线用户数
     */
    @RequestMapping(value = "getOnlineUserCount.action",method=RequestMethod.GET)
    @ResponseBody
    public int getOnlineUserCount() {
        return commonService.getOnlineUserCount();
    }


    /**
     * @description 用户管理角色列表
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "getRolesForUser.action",method=RequestMethod.GET)
    @ResponseBody
    public  List<Role> getRolesForUser(HttpServletRequest request)  {
        try{
            Object orgId = request.getSession().getAttribute("roleOrgId");
            if (orgId != null && !"".equals(orgId.toString())) {
                boolean result = orgId.toString().matches(SYS_ORG_VAL);
                if (result == true) {
                    return commonService.getRolesForUser(orgId.toString());
                } else {
                    return commonService.getRolesForUser();
                }
            } else if(GlobalContext.getProperty(SYS_ORG_ID) != null && !"".equals(GlobalContext.getProperty(SYS_ORG_ID))) {
                boolean result = GlobalContext.getProperty(SYS_ORG_ID).matches(SYS_ORG_VAL);
                if (result == true) {
                    return commonService.getRolesForUser(GlobalContext.getProperty(SYS_ORG_ID));
                } else {
                    return commonService.getRolesForUser();
                }
            } else {
                return commonService.getRolesForUser();
            }
        } catch (CustomException e) {
            logger.error(e);
        }
        return Collections.emptyList();
    }

}
