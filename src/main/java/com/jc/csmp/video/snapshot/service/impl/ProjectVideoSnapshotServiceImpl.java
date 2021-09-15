package com.jc.csmp.video.snapshot.service.impl;

import com.jc.csmp.video.snapshot.domain.ProjectVideoSnapshot;
import com.jc.csmp.video.snapshot.dao.IProjectVideoSnapshotDao;
import com.jc.csmp.video.snapshot.domain.ProjectVideoSnapshot;
import com.jc.csmp.video.snapshot.service.IProjectVideoSnapshotService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/** 
 * @Version 1.0
 */
@Service
public class ProjectVideoSnapshotServiceImpl extends BaseServiceImpl<ProjectVideoSnapshot> implements IProjectVideoSnapshotService {

	private IProjectVideoSnapshotDao projectVideoSnapshotDao;

	public ProjectVideoSnapshotServiceImpl(){}

	@Autowired
	public ProjectVideoSnapshotServiceImpl(IProjectVideoSnapshotDao projectVideoSnapshotDao){
		super(projectVideoSnapshotDao);
		this.projectVideoSnapshotDao = projectVideoSnapshotDao;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(ProjectVideoSnapshot entity) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(entity,true);
			result = projectVideoSnapshotDao.delete(entity,false);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result saveEntity(ProjectVideoSnapshot entity) throws CustomException {
		if (!StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
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
	public Result updateEntity(ProjectVideoSnapshot entity) throws CustomException {
		if (StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
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

}

