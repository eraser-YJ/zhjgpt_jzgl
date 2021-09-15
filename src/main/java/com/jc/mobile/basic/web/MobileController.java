package com.jc.mobile.basic.web;

import com.jc.foundation.util.ResultCode;
import com.jc.foundation.util.StringUtil;
import com.jc.mobile.util.MobileApiResponse;
import com.jc.mobile.util.MobileContext;
import com.jc.system.gateway.utils.TokenGenerator;
import com.jc.workflow.system.web.BaseController;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author 常鹏
 * @Date 2020/8/3 8:52
 * @Version 1.0
 */
public class MobileController extends BaseController {
    public static final String ACCESS_TOKEN = "access_token";

    /**
     * 验证token，并返回用户id
     * @param request
     * @return
     */
    public MobileApiResponse validateToken(HttpServletRequest request) {
        String accessToken = request.getHeader("accessToken");
        if (StringUtil.isEmpty(accessToken)) {
            accessToken = request.getParameter("accessToken");
            if(StringUtils.isEmpty(accessToken)){
                return MobileApiResponse.error(ResultCode.MOBILE_NOT_LOGIN);
            }
        }
        String[] tokenArray = TokenGenerator.getInstance().resolveToken(accessToken);

        if (tokenArray.length > 2) {
            String userId = tokenArray[0];
            String clientId = tokenArray[1];
            //TODO 这里可以考虑验证超时时间，我们先不验证了 tokenArray[2]: 过期时间
            if (clientId == null || !clientId.equals(MobileContext.CLIENT_ID)) {
                return MobileApiResponse.error(ResultCode.MOBILE_NOT_LOGIN);
            }
            return MobileApiResponse.ok(userId);
        }
        return MobileApiResponse.error(ResultCode.MOBILE_NOT_LOGIN);
    }
}
