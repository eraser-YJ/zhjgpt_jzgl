package com.jc.csmp.project.service.impl;

import com.jc.csmp.common.enums.CompanyTypeConvertDepartmentEnums;
import com.jc.csmp.project.dao.ICmProjectPersonDao;
import com.jc.csmp.project.domain.CmProjectPerson;
import com.jc.csmp.project.service.ICmProjectPersonService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.*;
import com.jc.system.security.domain.Department;
import com.jc.system.security.domain.User;
import com.jc.system.security.util.DeptCacheUtil;
import com.jc.system.security.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 建设管理-项目人员分配serviceImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Service
public class CmProjectPersonServiceImpl extends BaseServiceImpl<CmProjectPerson> implements ICmProjectPersonService {

	private ICmProjectPersonDao cmProjectPersonDao;

	public CmProjectPersonServiceImpl(){}

	@Autowired
	public CmProjectPersonServiceImpl(ICmProjectPersonDao cmProjectPersonDao){
		super(cmProjectPersonDao);
		this.cmProjectPersonDao = cmProjectPersonDao;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(CmProjectPerson entity) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(entity,true);
			result = cmProjectPersonDao.delete(entity);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result saveEntity(CmProjectPerson entity) throws CustomException {
		if (!StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
			entity.setDeptCode(DeptCacheUtil.getCodeById(entity.getCompanyId()));
			this.save(entity);
			return Result.success(MessageUtils.getMessage("JC_SYS_001"));
		} catch (Exception e) {
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result updateEntity(CmProjectPerson entity) throws CustomException {
		if (StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
			entity.setDeptCode(DeptCacheUtil.getCodeById(entity.getCompanyId()));
			Integer flag = this.update(entity);
			if (flag == 1) {
				return Result.success(MessageUtils.getMessage("JC_SYS_003"));
			} else {
				return Result.failure(1, MessageUtils.getMessage("JC_SYS_004"));
			}
		} catch (Exception e) {
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
	}

	@Override
	public Result saveAutoData(String projectId, String superviseDeptId, String build_dept_id) {
		CmProjectPerson delete = new CmProjectPerson();
		//这个deleteDefault不是空的话，就会根据下面的两个参数进行删除
		delete.setDeleteDefault("1");
		delete.setProjectId(projectId);
		delete.setCanShow("0");
		try {
			this.cmProjectPersonDao.delete(delete, false);
			List<CmProjectPerson> saveList = new ArrayList<>();
			if (!StringUtil.isEmpty(superviseDeptId)) {
				CmProjectPerson supervise = new CmProjectPerson();
				supervise.setProjectId(projectId);
				supervise.setCompanyId(superviseDeptId);
				supervise.setCanShow("0");
				supervise.setDeptCode(DeptCacheUtil.getCodeById(supervise.getCompanyId()));
				saveList.add(supervise);
			}
			if (!StringUtil.isEmpty(build_dept_id)) {
				CmProjectPerson build = new CmProjectPerson();
				build.setProjectId(projectId);
				build.setCompanyId(build_dept_id);
				build.setCanShow("0");
				build.setDeptCode(DeptCacheUtil.getCodeById(build.getCompanyId()));
				saveList.add(build);
			}
			if (saveList.size() > 0) {
				this.saveList(saveList);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return Result.success();
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result saveOtherData(String projectId, String deptId) throws CustomException {
		CmProjectPerson entity = new CmProjectPerson();
		entity.setProjectId(projectId);
		entity.setCompanyId(deptId);
		List<CmProjectPerson> entityList = this.cmProjectPersonDao.queryAll(entity);
		if (entityList == null || entityList.size() == 0) {
			entity.setCanShow("1");
			Department department = DeptCacheUtil.getDeptById(deptId);
			if (department != null) {
				entity.setDeptCode(department.getCode());
				entity.setPartakeType(CompanyTypeConvertDepartmentEnums.getByDeptCode(department.getCode()).toString());
				String leaderId = department.getLeaderId();
				if (!StringUtil.isEmpty(leaderId)) {
					User user = UserUtils.getUser(leaderId);
					if (user != null) {
						entity.setLeader(user.getDisplayName());
						entity.setPhone(user.getMobile());
					}
				}
			}
			this.save(entity);
			return Result.success();
		}
		return Result.success();
	}

	@Override
	public CmProjectPerson getSupervisorByProjectId(String projectId) {
		CmProjectPerson param = new CmProjectPerson();
		param.setProjectId(projectId);
		param.setPartakeType("supervisor");
		return GlobalUtil.getFirstItem(this.cmProjectPersonDao.queryAll(param));
	}

	@Override
	public List<CmProjectPerson> getByProjectId(String projectId) {
		CmProjectPerson param = new CmProjectPerson();
		param.setProjectId(projectId);
		return this.cmProjectPersonDao.queryAll(param);
	}
}

