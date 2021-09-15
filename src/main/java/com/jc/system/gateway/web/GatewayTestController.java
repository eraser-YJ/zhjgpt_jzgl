package com.jc.system.gateway.web;

import com.jc.system.gateway.utils.ApiResponse;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class GatewayTestController {

    /**
     * 移动接口测试方法
     * 客户端发送来一个GET请求
     *
     * @param request
     * @return 当前用户信息
     */
    @GetMapping("/mobile/test/echo")
    public ApiResponse testList(HttpServletRequest request) {
        User user = SystemSecurityUtils.getUser();
        Map<String, String> map = new HashMap<>();
        if (user != null) {
            map.put("id", user.getId());
            map.put("displayName", user.getDisplayName());
            map.put("loginName", user.getLoginName());
            map.put("orgId", user.getOrgId());
            map.put("deptId", user.getDeptId());
            map.put("dutyId", user.getDutyId());
        }
        return ApiResponse.ok().put("data", map).put("userId", request.getParameter("userId"));
    }
}
