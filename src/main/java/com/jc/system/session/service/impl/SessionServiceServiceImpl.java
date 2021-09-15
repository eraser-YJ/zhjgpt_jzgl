package com.jc.system.session.service.impl;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.JsonUtil;
import com.jc.system.security.beans.UserBean;
import com.jc.system.security.domain.Department;
import com.jc.system.security.domain.Principal;
import com.jc.system.security.domain.User;
import com.jc.system.security.service.IDepartmentService;
import com.jc.system.security.service.IUserService;
import com.jc.system.session.dao.ISessionServiceDao;
import com.jc.system.session.domain.SessionService;
import com.jc.system.session.service.ISessionServiceService;
import org.apache.log4j.Logger;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.cas.client.session.HashMapBackedSessionMappingStorage;
import org.cas.client.session.SingleSignOutFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Service
public class SessionServiceServiceImpl extends BaseServiceImpl<SessionService> implements ISessionServiceService {
	protected final Logger logger = Logger.getLogger(this.getClass());

	private ISessionServiceDao sessionServiceDao;

	public SessionServiceServiceImpl(){}

	@Autowired
	public SessionServiceServiceImpl(ISessionServiceDao sessionServiceDao){
		super(sessionServiceDao);
		this.sessionServiceDao = sessionServiceDao;
	}

	@Autowired
	private SessionDAO sessionDAO;
	@Autowired
	private IDepartmentService departmentService;
	@Autowired
	private IUserService userService;

	@Override
	public int getLogoutUser(String userName){
		SessionService sessionVo = new SessionService();
		sessionVo.setUserName(userName);
		sessionVo.setDeleteFlag(0);
		List<SessionService> serviceList = sessionServiceDao.queryAll(sessionVo);
		if (serviceList != null){
			return serviceList.size();
		} else {
			return 0;
		}
	}

	/**
	 * 获取在线用户--返回SessionService（机构不隔离）
	 * @return
	 */
	@Override
	public String getDeptAndUserByOnLine(){
		List<Department> list = getDepartmentAll();
		Map<String,User> userMap = getUserAll();
		List<SessionService> uList = getOnlineAllUserBean();
		return JsonUtil.java2Json(pottingOnLineDeptAndUser(list,uList,userMap));
	}

	private ArrayNode pottingOnLineDeptAndUser(List<Department> list, List<SessionService> uList,Map<String,User> userMap){
		List<User> userList = new ArrayList<User>();
		if (uList != null && uList.size() > 0) {
			if (list != null && list.size() > 0) {
				for (int j = 0; j < uList.size(); j++) {
					User uTmp = userMap.get(uList.get(j).getUserId());
					for (int i = 0; i < list.size(); i++) {
						Department deptTmp = list.get(i);
						if (deptTmp.getId().equals(uTmp.getDeptId()) && uTmp.getIsAdmin() == 0) {
							if(!isExistUser(uTmp, userList)){
								UserBean uBean = new UserBean();
								uBean.setId(uTmp.getId());
								uBean.setDeptId(uTmp.getDeptId());
								uBean.setDisplayName(uTmp.getDisplayName());
								uBean.setOrderNo(uTmp.getOrderNo());
								deptTmp.addUserBean(uBean);
								userList.add(uTmp);
							}
						}
					}
				}
			}
		}
		ArrayNode array = JsonUtil.createArrayNode();
		Department d = this.getDeptRoot();
		for (Department department : list) {
			if (department.getId().equals(d.getId())) {
				ObjectNode obj = (ObjectNode) JsonUtil.createNode();
				obj.put("id", department.getId());
				obj.put("name", department.getName());
				obj.put("deptType", department.getDeptType());
				obj.put("parentId", department.getParentId());
				obj.putPOJO("user", department.getUserBeanList());
				recur(list, department.getId(), obj);
				array.add(obj);
			}
		}
		return array;
	}

	/**
	 * 获取根节点数据
	 * @return
	 */
	private Department getDeptRoot(){
		Department d = new Department();
		d.setParentId("0");
		try {
			return departmentService.get(d);
		} catch (CustomException e) {
			logger.error(e,e);
			return d;
		}
	}

	/**
	 * 有方法-递归添加下级菜单
	 * @param departMentList
	 * @param id
	 * @param obj
	 * @return
	 */
	private ObjectNode recur(List<Department> departMentList, String id, ObjectNode obj) {
		ArrayNode array = JsonUtil.createArrayNode();
		for (int i = 0; i < departMentList.size(); i++) {
			if (departMentList.get(i).getParentId().equals(id)) {
				ObjectNode sonobj = (ObjectNode) JsonUtil.createNode();
				sonobj.put("id", departMentList.get(i).getId());
				sonobj.put("name", departMentList.get(i).getName());
				sonobj.put("deptType", departMentList.get(i).getDeptType());
				sonobj.put("parentId", departMentList.get(i).getParentId());
				sonobj.putPOJO("user", departMentList.get(i).getUserBeanList());
				recur(departMentList, departMentList.get(i).getId(), sonobj);
				array.add(sonobj);
			}
		}
		if (!array.isNull()) {
			obj.put("subDept", array);
		}
		return obj;
	}

	private boolean isExistUser(User user, List<User> list){
		for(User u : list){
			if(u.getLoginName().equals(user.getLoginName())){
				return true;
			}
		}
		return false;
	}

	private Map<String,User> getUserAll(){
		Map<String,User> userMap = new HashMap<>();
		User user = new User();
		user.setDeleteFlag(0);
		try {
			List<User> userList = userService.queryAll(user);
			for (User user1:userList){
				userMap.put(user1.getId(),user1);
			}
			return userMap;
		} catch (CustomException e) {
			logger.error(e,e);
			return userMap;
		}
	}

	private List<Department> getDepartmentAll(){
		Department dept = new Department();
		dept.setDeleteFlag(0);
		try {
			return departmentService.queryAll(dept);
		} catch (CustomException e) {
			logger.error(e,e);
			return new ArrayList<>();
		}
	}

	private List<SessionService> getOnlineAllUserBean(){
		List<SessionService> sessionServiceList = new ArrayList<>();
		String casStart = GlobalContext.getProperty("cas.start");
		if(casStart==null || !"true".equals(casStart)){
			Collection<Session> sessions = sessionDAO.getActiveSessions();
			for(Session session : sessions){
				PrincipalCollection principalCollection = (PrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
				if(principalCollection != null){
					Principal principal = (Principal) principalCollection.getPrimaryPrincipal();
					SessionService sessionService = new SessionService();
					sessionService.setUserName(principal.getDisplayName());
					sessionService.setUserId(principal.getId());
					sessionService.setLoginName(principal.getLoginName());
					sessionService.setUserIp(principal.getIp());
					sessionServiceList.add(sessionService);
				}
			}

		}else{
			HashMapBackedSessionMappingStorage sessionStorage = (HashMapBackedSessionMappingStorage) SingleSignOutFilter.getSingleSignOutHandler().getSessionMappingStorage();
			Map<String, HttpSession> sessionMap =sessionStorage.getManagedSessions();
			for (Map.Entry<String, HttpSession> entry : sessionMap.entrySet()) {
				PrincipalCollection principalCollection = (PrincipalCollection) entry.getValue().getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
				if(principalCollection != null){
					SessionService sessionService = new SessionService();
					sessionService = copyTo(principalCollection,sessionService);
					sessionServiceList.add(sessionService);
				}
			}
		}

		removeDuplicateWithOrder(sessionServiceList);
		return sessionServiceList;
	}

	private SessionService copyTo(PrincipalCollection principalCollection,SessionService sessionService){
		Principal principal = (Principal) principalCollection.getPrimaryPrincipal();
		sessionService.setUserName(principal.getDisplayName());
		sessionService.setUserId(principal.getId());
		sessionService.setLoginName(principal.getLoginName());
		sessionService.setUserIp(principal.getIp());
		return sessionService;
	}

	/**
	 * 去除集合中的重复数据
	 * @param list
	 */
	private void removeDuplicateWithOrder(List<SessionService> list) {
		Set<String> set = new HashSet<>();
		List<SessionService> newList = new ArrayList<>();
		for (Iterator<SessionService> iter = list.iterator(); iter.hasNext();) {
			SessionService element = iter.next();
			if (set.add(element.getUserId())){
				newList.add(element);
			}
		}
		list.clear();
		list.addAll(newList);
	}

	public List<Session> getSessionByUsername(String username){
		Collection<Session> sessions = sessionDAO.getActiveSessions();
		List<Session> sessionList = new ArrayList<>();
		for(Session session : sessions){
			PrincipalCollection principalCollection = (PrincipalCollection) session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
			if(principalCollection != null){
				Principal principal = (Principal) principalCollection.getPrimaryPrincipal();
				if(username.equals(principal.getLoginName())){
					sessionList.add(session);
				}
			}
		}
		return sessionList;
	}

	public void kickOutUser(String username){
		List<Session> sessionList = getSessionByUsername(username);
		for(Session session : sessionList){
			session.setAttribute("kickout", true);
		}
	}

	@Override
	public Integer deleteByIds(SessionService sessionService) throws CustomException{
		Integer result = -1;
		User user = new User();
		user.setDeleteFlag(0);
		user.setId(sessionService.getUserId());
		user = userService.get(user);

		String casStart = GlobalContext.getProperty("cas.start");
		if(casStart==null || !"true".equals(casStart)){
			kickOutUser(user.getLoginName());
		} else {
			SessionService sessionVo = new SessionService();
			sessionVo.setUserName(user.getLoginName());
			sessionVo.setDeptName(user.getDeptName());
			sessionServiceDao.save(sessionVo);
		}

		return result;
	}

}