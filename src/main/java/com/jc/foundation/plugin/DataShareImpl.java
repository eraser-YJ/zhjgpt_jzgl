package com.jc.foundation.plugin;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.jc.foundation.domain.Attach;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.DataShareInterface;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.content.domain.AttachBusiness;
import com.jc.system.content.service.IAttachBusinessService;
import com.jc.system.content.service.IAttachService;
import com.jc.system.dic.IDicManager;
import com.jc.system.dic.domain.Dic;
import com.jc.system.domain.Department;
import com.jc.system.domain.Role;
import com.jc.system.domain.User;
import com.jc.system.security.domain.Subsystem;
import com.jc.system.security.domain.SysUserRole;
import com.jc.system.security.service.ISubsystemService;
import com.jc.system.security.service.ISysUserRoleService;
import com.jc.system.service.ISysService;
import com.jc.system.util.menuUtil;

public class DataShareImpl implements DataShareInterface{
	
	private static Logger log = Logger.getLogger(DataShareImpl.class);
	
	@Override
	public String getDicValue(String string, String string2, String portalStatus) {
		IDicManager dicManager = SpringContextHolder.getBean(IDicManager.class);
		Dic dic = dicManager.getDic(string, string2, portalStatus);
		return dic==null?"":dic.getValue();
	}

	@Override
	public void saveMenuID(String url, HttpServletRequest request) {
		try {
			menuUtil.saveMenuID(url, request);
		} catch (CustomException e) {
			log.error(e.getLogMsg(), e);
		}
	}

	@Override
	public List<Subsystem> queryAllSubsystem(Subsystem subsystem) {
		ISubsystemService subsystemService = SpringContextHolder.getBean(ISubsystemService.class);
		try {
			return subsystemService.queryAll(subsystem);
		} catch (CustomException e) {
			log.error(e.getLogMsg(), e);
		}
		return new ArrayList<>();
	}

	@Override
	public String getRoleStr(User user) {
		ISysUserRoleService sysUserRoleService = SpringContextHolder.getBean(ISysUserRoleService.class);
		
		SysUserRole userRole = new SysUserRole();
        userRole.setUserId(user.getId());
		List<SysUserRole> userRoles;
		try {
			userRoles = sysUserRoleService.queryAll(userRole);
			String roleStr = "";
			if(userRoles != null){
				for(int i=0;i<userRoles.size();i++){
					if("".equals(roleStr)) {
	                    roleStr = userRoles.get(i).getRoleId().toString();
	                } else {
	                    roleStr = roleStr + "," + userRoles.get(i).getRoleId().toString();
	                }
				}
			}
			return roleStr;
		} catch (CustomException e) {
			log.error(e.getLogMsg(), e);
		}
		return "";
	}

	@Override
	public String getAttachId(Attach attach) {
		IAttachService attachService = SpringContextHolder.getBean(IAttachService.class);
		List<Attach> attachList;
		try {
			attachList = attachService.queryAttachByBusinessIds(attach);
			if (attachList != null && attachList.size() > 0) {
				return attachList.get(0).getId();
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return "";
	}

	@Override
	public void saveAttach(Attach attach) {
		IAttachService attachService = SpringContextHolder.getBean(IAttachService.class);
		try {
			attachService.save(attach);
		} catch (CustomException e) {
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public void saveAttachBusiness(AttachBusiness attachBusiness) {
		IAttachBusinessService attachService = SpringContextHolder.getBean(IAttachBusinessService.class);
		try {
			attachService.save(attachBusiness);
		} catch (CustomException e) {
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public List<AttachBusiness> queryAllAttachBusiness(AttachBusiness attachBusiness) {
		IAttachBusinessService attachService = SpringContextHolder.getBean(IAttachBusinessService.class);
		try {
			return attachService.queryAll(attachBusiness);
		} catch (CustomException e) {
			log.error(e.getMessage(), e);
		}
		return new ArrayList<>();
	}

	@Override
	public void deleteForAttachAndBusiness(AttachBusiness attachBusiness) {
		IAttachBusinessService attachService = SpringContextHolder.getBean(IAttachBusinessService.class);
		attachService.deleteForAttachAndBusiness(attachBusiness);
	}

	@Override
	public Attach getAttach(Attach attach) {
		IAttachService attachService = SpringContextHolder.getBean(IAttachService.class);
		try {
			return attachService.get(attach);
		} catch (CustomException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public void deleteAttach(Attach attach, boolean b) {
		IAttachService attachService = SpringContextHolder.getBean(IAttachService.class);
		try {
			attachService.delete(attach,b);
		} catch (CustomException e) {
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public List<Role> queryAllRole(Role role) {
		ISysService sysService = SpringContextHolder.getBean(ISysService.class);
		return sysService.queryAllRole(role);
	}

	@Override
	public List<Department> queryAllDepartment(Department department) {
		ISysService sysService = SpringContextHolder.getBean(ISysService.class);
		return sysService.queryAllDepartment(department);
	}

	@Override
	public List<User> queryAllUser(User user) {
		ISysService sysService = SpringContextHolder.getBean(ISysService.class);
		return sysService.queryAllUser(user);
	}

}
