package org.cas.client.session;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.jasig.cas.client.util.AbstractConfigurationFilter;
/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
public final class SingleSignOutFilter extends AbstractConfigurationFilter
{
  private static final SingleSignOutHandler HANDLER = new SingleSignOutHandler();

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    if (!isIgnoreInitConfiguration()) {
      HANDLER.setArtifactParameterName(getPropertyFromInitParams(filterConfig, "artifactParameterName", "ticket"));
      HANDLER.setLogoutParameterName(getPropertyFromInitParams(filterConfig, "logoutParameterName", "logoutRequest"));
    }
    HANDLER.init();
  }

  public void setArtifactParameterName(String name) {
    HANDLER.setArtifactParameterName(name);
  }

  public void setLogoutParameterName(String name) {
    HANDLER.setLogoutParameterName(name);
  }

  public void setSessionMappingStorage(SessionMappingStorage storage) {
    HANDLER.setSessionMappingStorage(storage);
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest)servletRequest;

    if (HANDLER.isTokenRequest(request)) {
      HANDLER.recordSession(request); } else {
      if (HANDLER.isLogoutRequest(request)) {
        HANDLER.destroySession(request);

        return;
      }
      this.log.trace("Ignoring URI " + request.getRequestURI());
    }

    filterChain.doFilter(servletRequest, servletResponse);
  }

  @Override
  public void destroy()
  {
  }

  public static SingleSignOutHandler getSingleSignOutHandler() {
    return HANDLER;
  }
}