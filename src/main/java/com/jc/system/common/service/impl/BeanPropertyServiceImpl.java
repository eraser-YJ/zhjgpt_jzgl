package com.jc.system.common.service.impl;

import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.service.IBeanPropertyService;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.User;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 * @date 2020-06-30
 */
@Service
public class BeanPropertyServiceImpl implements IBeanPropertyService {
	Logger logger = Logger.getLogger(BeanPropertyServiceImpl.class);

	public BeanPropertyServiceImpl() {

	}
	
	@Override
	public void fillProperties(BaseBean bean, boolean modify) {
		Date now = new Date();
		if (bean.getCreateDate() == null && !modify) {
			bean.setCreateDate(now);
		}
		if (bean.getModifyDate() == null) {
			bean.setModifyDate(now);
		}
		if (modify) {
			bean.setModifyDateNew(now);
		}
		User user = getCurrentUser();
		if (!modify) {
			if (bean.getCreateUser() == null) {
				bean.setCreateUser(user.getId());
			}
			if (bean.getCreateUserDept() == null) {
				bean.setCreateUserDept(user.getDeptId());
			}
			if (bean.getCreateUserOrg() == null) {
				bean.setCreateUserOrg(user.getOrgId());
			}
		}
		if (bean.getModifyUser() == null) {
			bean.setModifyUser(user.getId());
		}
	}

	private User getCurrentUser() {
		User user = null;
		try {
			user = SystemSecurityUtils.getUser();
		} catch (Exception e) {
			user = createErrorUser();
		}
		if(user == null){
			user = createErrorUser();
		}
		return user;
	}
	
	private User createErrorUser() {
		User user = new User();
		user.setId("-1");
		user.setDeptId("-1");
		user.setOrgId("-1");
		return user;
	}

	@Override
	public <T extends BaseBean> List<T> fillProperties(List<T> list, boolean modify) {
		Date now = new Date();
		User user = getCurrentUser();
		List<T> clone = new ArrayList<>();
		String orgId = user.getOrgId();
		for (T bean : list) {
			if (bean.getCreateDate() == null && !modify) {
				bean.setCreateDate(now);
			}
			if (bean.getModifyDate() == null) {
				bean.setModifyDate(now);
			}
			if (modify) {
				bean.setModifyDateNew(now);
			}
			if (bean.getCreateUser() == null) {
				bean.setCreateUser(user.getId());
			}
			if (bean.getModifyUser() == null) {
				bean.setModifyUser(user.getId());
			}
			if (bean.getCreateUserDept() == null) {
				bean.setCreateUserDept(user.getDeptId());
			}
			if (bean.getCreateUserOrg() == null) {
				bean.setCreateUserOrg(orgId);
			}

			clone.add(bean);
		}
		return clone;
	}

}
