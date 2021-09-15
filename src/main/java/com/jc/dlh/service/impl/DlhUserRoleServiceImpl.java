package com.jc.dlh.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jc.dlh.dao.IDlhUserRoleDao;
import com.jc.dlh.domain.DlhUserRole;
import com.jc.dlh.service.IDlhUserRoleService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.StringUtil;

/**
 * @title 统一数据资源中心
 * @description 业务服务类
 * @author lc 
 * @version 2020-03-18
 */
@Service
public class DlhUserRoleServiceImpl extends BaseServiceImpl<DlhUserRole> implements IDlhUserRoleService {

	private IDlhUserRoleDao dlhUserRoleDao;

	public DlhUserRoleServiceImpl() {
	}

	@Autowired
	public DlhUserRoleServiceImpl(IDlhUserRoleDao dlhUserRoleDao) {
		super(dlhUserRoleDao);
		this.dlhUserRoleDao = dlhUserRoleDao;
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer save(DlhUserRole dlhUserRole) throws CustomException {
		Integer result = Integer.valueOf(-1);
		try {
			try {
				// 删除该用户原有权限
				this.dlhUserRoleDao.deleteByUserId(dlhUserRole);
			} catch (Exception var5) {
			}
			// 新增
			if (!StringUtil.isEmpty(dlhUserRole.getDataIds())) {
				List<DlhUserRole> addList = new ArrayList<>();
				String[] arr = dlhUserRole.getDataIds().split(",");
				String[] arrs = dlhUserRole.getDataNames().split(",");
				for (int i = 0; i < arr.length; i++) {
					DlhUserRole dlhUserRoleAdd = new DlhUserRole();
					dlhUserRoleAdd.setDataId(arr[i]);
					dlhUserRoleAdd.setObjName(arrs[i]);
					dlhUserRoleAdd.setUserId(dlhUserRole.getUserId());
					addList.add(dlhUserRoleAdd);
				}
				result = this.saveList(addList);
			}
			return result;
		} catch (Exception var5) {
			CustomException ce = new CustomException(var5);
			throw ce;
		}
	}

	/**
	 * @description 根据主键删除多条记录方法
	 * @param DlhUserRole dlhUserRole 实体类
	 * @return Integer 处理结果
	 * @author lc 
	 * @version 2020-03-18
	 */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer deleteByIds(DlhUserRole dlhUserRole) throws CustomException {
		Integer result = -1;
		try {
			propertyService.fillProperties(dlhUserRole, true);
			result = dlhUserRoleDao.delete(dlhUserRole);
		} catch (Exception e) {
			CustomException ce = new CustomException(e);
			ce.setBean(dlhUserRole);
			throw ce;
		}
		return result;
	}

}