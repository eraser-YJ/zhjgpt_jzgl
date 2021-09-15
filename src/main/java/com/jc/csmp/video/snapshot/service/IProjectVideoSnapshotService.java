package com.jc.csmp.video.snapshot.service;

import com.jc.csmp.video.snapshot.domain.ProjectVideoSnapshot;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.foundation.util.Result;

/**
 * @Version 1.0
 */
public interface IProjectVideoSnapshotService extends IBaseService<ProjectVideoSnapshot>{

	/**
	 * 根据主键删除多条记录方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(ProjectVideoSnapshot entity) throws CustomException;

	/**
	 * 保存方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result saveEntity(ProjectVideoSnapshot entity) throws CustomException;

	/**
	 * 修改方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result updateEntity(ProjectVideoSnapshot entity) throws CustomException;
}
