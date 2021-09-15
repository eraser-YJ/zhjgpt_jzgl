package com.jc.csmp.project.service.impl;

import com.jc.csmp.project.dao.ICmProjectRelationOrderDao;
import com.jc.csmp.project.domain.CmProjectRelationOrder;
import com.jc.csmp.project.service.ICmProjectRelationOrderService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.*;
import com.jc.system.content.service.IUploadService;
import com.jc.system.security.domain.Department;
import com.jc.system.security.util.DeptCacheUtil;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 建设管理-项目工程联系单service实现
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Service
public class CmProjectRelationOrderServiceImpl extends BaseServiceImpl<CmProjectRelationOrder> implements ICmProjectRelationOrderService {

	private ICmProjectRelationOrderDao projectRelationOrderDao;
	@Autowired
	private IUploadService uploadService;

	public CmProjectRelationOrderServiceImpl(){}

	@Autowired
	public CmProjectRelationOrderServiceImpl(ICmProjectRelationOrderDao projectRelationOrderDao){
		super(projectRelationOrderDao);
		this.projectRelationOrderDao = projectRelationOrderDao;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(CmProjectRelationOrder entity) throws CustomException{
		Integer result = -1;
		try {
			List<CmProjectRelationOrder> orderList = this.projectRelationOrderDao.queryAll(entity);
			int signCount = 0;
			if (orderList != null) {
				for (CmProjectRelationOrder data : orderList) {
					if (data.getSignStatus() != null && GlobalContext.SIGN_1.equals(data.getSignStatus())) {
						signCount++;
					}
				}
			}
			if (signCount > 0) {
				return GlobalContext.CUSTOM_SIGN_ERROR;
			}
			propertyService.fillProperties(entity,true);
			result = projectRelationOrderDao.delete(entity);
		} catch(Exception e) {
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result saveEntity(CmProjectRelationOrder entity) throws CustomException {
		if (!StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
			Department department = DeptCacheUtil.getDeptById(entity.getSignerDept());
			if (department == null || department.getLeaderId() == null) {
				return Result.failure(1, "签收部门未配置负责人，工程单无法被签收，请先配置部门负责人");
			}
			entity.setSignStatus("0");
			this.save(entity);
			//保存签收组织
			this.projectRelationOrderDao.insertRelationDept(entity.getId(), "1", entity.getSignerDept());
			//保存抄送组织
			if (!StringUtil.isEmpty(entity.getReceiverDept())) {
				String[] array = GlobalUtil.splitStr(entity.getReceiverDept(), ',');
				if (array != null) {
					for (String deptId : array) {
						this.projectRelationOrderDao.insertRelationDept(entity.getId(), "2", deptId);
					}
				}
			}
			uploadService.managerForAttach(entity.getId(), "cm_project_relation_order", entity.getAttachFile(), entity.getDeleteAttachFile(), 1);
			return Result.success(MessageUtils.getMessage("JC_SYS_001"));
		} catch (Exception e) {
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result updateEntity(CmProjectRelationOrder entity) throws CustomException {
		if (StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
			if (entity.getSignStatus() != null && entity.getSignStatus().equals(GlobalContext.SIGN_1)) {
				return Result.failure(1, "工程单已被签收，无法修改");
			}
			Integer flag = this.update(entity);
			if (flag == 1) {
				uploadService.managerForAttach(entity.getId(), "cm_project_relation_order", entity.getAttachFile(), entity.getDeleteAttachFile(), 1);
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
	public PageManager myQuery(CmProjectRelationOrder entity, PageManager page){
		return this.projectRelationOrderDao.queryByPage(entity, page, "myQueryCount", "myQuery");
	}

	@Override
	public CmProjectRelationOrder getById(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		CmProjectRelationOrder entity = new CmProjectRelationOrder();
		entity.setId(id);
		return GlobalUtil.getFirstItem(this.projectRelationOrderDao.queryAll(entity));
	}
}

