package com.jc.csmp.project.service;

import com.jc.csmp.project.domain.CmProjectPerson;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.foundation.util.Result;

import java.util.List;

/**
 * 建设管理-项目人员分配service
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public interface ICmProjectPersonService extends IBaseService<CmProjectPerson>{

	/**
	 * 根据主键删除多条记录方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(CmProjectPerson entity) throws CustomException;

	/**
	 * 保存方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result saveEntity(CmProjectPerson entity) throws CustomException;

	/**
	 * 修改方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result updateEntity(CmProjectPerson entity) throws CustomException;

	/**
	 * 保存监管单位和建设单位
	 * @param projectId
	 * @param superviseDeptId
	 * @param build_dept_id
	 * @return
	 */
	Result saveAutoData(String projectId, String superviseDeptId, String build_dept_id);

	/**
	 * 保存参与者
	 * @param projectId
	 * @param deptId
	 * @return
	 * @throws CustomException
	 */
	Result saveOtherData(String projectId, String deptId) throws CustomException;

	/**
	 * 根据项目获取监理单位
	 * @param projectId
	 * @return
	 */
	CmProjectPerson getSupervisorByProjectId(String projectId);

	/**
	 * 根据项目id获取
	 * @param projectId
	 * @return
	 */
	List<CmProjectPerson> getByProjectId(String projectId);
}
