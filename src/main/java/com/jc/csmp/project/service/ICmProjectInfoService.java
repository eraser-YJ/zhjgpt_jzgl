package com.jc.csmp.project.service;

import com.jc.csmp.project.domain.CmProjectInfo;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.foundation.util.Result;

import java.util.List;

/**
 * 建设管理-项目管理service
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public interface ICmProjectInfoService extends IBaseService<CmProjectInfo>{

	/**
	 * 根据主键删除多条记录方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(CmProjectInfo entity) throws CustomException;

	/**
	 * 保存方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
    Result saveEntity(CmProjectInfo entity) throws CustomException;

	/**
	 * 修改方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result updateEntity(CmProjectInfo entity) throws CustomException;

	/**
	 * 验证项目编号
	 * @param id: 主键id
	 * @param projectNumber：项目编号
	 * @return
	 */
	Result checkProjectNumber(String id, String projectNumber);

	/**
	 * 根据id获取数据
	 * @param id
	 * @return
	 */
	CmProjectInfo getById(String id);

	/**
	 * 根据项目编号获取项目
	 * @param projectNumber
	 * @return
	 */
    CmProjectInfo getbyProjectNumber(String projectNumber);

	/**
	 * 设置项目完结
	 * @param projectNumbers:项目编号，多个逗号分隔
	 * @return
	 * @throws Exception
	 */
	Result finishProject(String projectNumbers) throws Exception;

	/**
	 * 根据监管机构获取监管项目列表
	 * @param deptId
	 * @return
	 */
    List<CmProjectInfo> getListBySuperviseDeptId(String deptId);
	/**
	 * 根据工程报监查询未申报的项目
	 * @param CmProjectInfo
	 * * @Author limin  li
	 *  * @Date 2020/7/6 14:29
	 * @return
	 */
	PageManager queryProjectSafety(CmProjectInfo o, PageManager page) ;
}
