package org.cas.client.session;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
public final class SingleSignOutHttpSessionListener
  implements HttpSessionListener
{
  private SessionMappingStorage sessionMappingStorage;

  @Override
  public void sessionCreated(HttpSessionEvent event)
  {
  }

  @Override
  public void sessionDestroyed(HttpSessionEvent event)
  {
    if (this.sessionMappingStorage == null) {
      this.sessionMappingStorage = getSessionMappingStorage();
    }
    HttpSession session = event.getSession();
    this.sessionMappingStorage.removeBySessionById(session.getId());
  }

  protected static SessionMappingStorage getSessionMappingStorage()
  {
    return SingleSignOutFilter.getSingleSignOutHandler().getSessionMappingStorage();
  }
}