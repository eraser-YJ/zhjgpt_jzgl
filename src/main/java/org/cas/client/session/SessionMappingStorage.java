package org.cas.client.session;

import javax.servlet.http.HttpSession;
/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
public abstract interface SessionMappingStorage
{
  public abstract HttpSession removeSessionByMappingId(String paramString);

  public abstract void removeBySessionById(String paramString);

  public abstract void addSessionById(String paramString, HttpSession paramHttpSession);
}
