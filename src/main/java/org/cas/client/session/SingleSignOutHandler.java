package org.cas.client.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.util.XmlUtils;
/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
public final class SingleSignOutHandler {
	private final Log log = LogFactory.getLog(getClass());

	private SessionMappingStorage sessionMappingStorage = new HashMapBackedSessionMappingStorage();

	private String artifactParameterName = "ticket";

	private String logoutParameterName = "logoutRequest";

	public void setSessionMappingStorage(SessionMappingStorage storage) {
		this.sessionMappingStorage = storage;
	}

	public SessionMappingStorage getSessionMappingStorage() {
		return this.sessionMappingStorage;
	}

	public void setArtifactParameterName(String name) {
		this.artifactParameterName = name;
	}

	public void setLogoutParameterName(String name) {
		this.logoutParameterName = name;
	}

	public void init() {
		CommonUtils.assertNotNull(this.artifactParameterName, "artifactParameterName cannot be null.");
		CommonUtils.assertNotNull(this.logoutParameterName, "logoutParameterName cannot be null.");
		CommonUtils.assertNotNull(this.sessionMappingStorage, "sessionMappingStorage cannote be null.");
	}

	public boolean isTokenRequest(HttpServletRequest request) {
		return CommonUtils.isNotBlank(CommonUtils.safeGetParameter(request, this.artifactParameterName));
	}

	public boolean isLogoutRequest(HttpServletRequest request) {
		return ("POST".equals(request.getMethod())) && (!isMultipartRequest(request))
				&& (CommonUtils.isNotBlank(CommonUtils.safeGetParameter(request, this.logoutParameterName)));
	}

	public void recordSession(HttpServletRequest request) {
		HttpSession session = request.getSession(true);

		String token = CommonUtils.safeGetParameter(request, this.artifactParameterName);
		if (this.log.isDebugEnabled()) {
			this.log.debug("Recording session for token " + token);
		}
		try {
			this.sessionMappingStorage.removeBySessionById(session.getId());
		} catch (Exception e) {
			log.error(e);
		}
		this.sessionMappingStorage.addSessionById(token, session);
	}

	public void destroySession(HttpServletRequest request) {
		String logoutMessage = CommonUtils.safeGetParameter(request, this.logoutParameterName);
		if (this.log.isTraceEnabled()) {
			this.log.trace("Logout request:\n" + logoutMessage);
		}

		String token = XmlUtils.getTextForElement(logoutMessage, "SessionIndex");
		if (CommonUtils.isNotBlank(token)) {
			HttpSession session = this.sessionMappingStorage.removeSessionByMappingId(token);

			if (session != null) {
				String sessionID = session.getId();

				if (this.log.isDebugEnabled()) {
					this.log.debug("Invalidating session [" + sessionID + "] for token [" + token + "]");
				}
				try {
					session.invalidate();
				} catch (IllegalStateException e) {
					this.log.debug("Error invalidating session.", e);
				}
			}
		}
	}

	private boolean isMultipartRequest(HttpServletRequest request) {
		return (request.getContentType() != null) && (request.getContentType().toLowerCase().startsWith("multipart"));
	}
}