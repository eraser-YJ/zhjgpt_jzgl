package org.cas.client.session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
public final class HashMapBackedSessionMappingStorage implements SessionMappingStorage {
	
    /**
     * Maps the ID from the CAS server to the Session.
     */
    private final Map<String,HttpSession> MANAGED_SESSIONS = new HashMap<>();

    /**
     * Maps the Session ID to the key from the CAS Server.
     */
    private final Map<String,String> ID_TO_SESSION_KEY_MAPPING = new HashMap<>();

    private final Log log = LogFactory.getLog(getClass());

	@Override
    public synchronized void addSessionById(String mappingId, HttpSession session) {
        ID_TO_SESSION_KEY_MAPPING.put(session.getId(), mappingId);
        MANAGED_SESSIONS.put(mappingId, session);

	}                               

	@Override
    public synchronized void removeBySessionById(String sessionId) {
        if (log.isDebugEnabled()) {
            log.debug("Attempting to remove Session=[" + sessionId + "]");
        }

        final String key = ID_TO_SESSION_KEY_MAPPING.get(sessionId);

        if (log.isDebugEnabled()) {
            if (key != null) {
                log.debug("Found mapping for session.  Session Removed.");
            } else {
                log.debug("No mapping for session found.  Ignoring.");
            }
        }
        MANAGED_SESSIONS.remove(key);
        ID_TO_SESSION_KEY_MAPPING.remove(sessionId);
	}

	@Override
    public synchronized HttpSession removeSessionByMappingId(String mappingId) {
		final HttpSession session = MANAGED_SESSIONS.get(mappingId);

        if (session != null) {
        	removeBySessionById(session.getId());
        }

        return session;
	}
	
	public Map<String,HttpSession> getManagedSessions(){
		return this.MANAGED_SESSIONS;
	}
}
