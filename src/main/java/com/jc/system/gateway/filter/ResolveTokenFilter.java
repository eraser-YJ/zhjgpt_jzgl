package com.jc.system.gateway.filter;

import com.jc.system.gateway.utils.TokenGenerator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public class ResolveTokenFilter extends OncePerRequestFilter {
    TokenGenerator tokenGenerator;

    public ResolveTokenFilter() {
        super();
        this.tokenGenerator = TokenGenerator.getInstance();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token=httpServletRequest.getHeader("token");
        if(StringUtils.isNotEmpty(token)) {
            String[] tokenInfo=tokenGenerator.resolveToken(token);
            Map<String, Object> extraParams=new HashMap<>();
            if(tokenInfo!=null && tokenInfo.length>2){
                extraParams.put("userId",tokenInfo[0]);
            }
            RequestParameterWrapper requestParameterWrapper = new RequestParameterWrapper(httpServletRequest);
            requestParameterWrapper.addParameters(extraParams);
            filterChain.doFilter(requestParameterWrapper, httpServletResponse);
        }
        else{
            filterChain.doFilter(httpServletRequest,httpServletResponse);
        }
    }
}
