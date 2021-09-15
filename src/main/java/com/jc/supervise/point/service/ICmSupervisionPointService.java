package com.jc.supervise.point.service;

import com.jc.csmp.project.domain.CmProjectInfo;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.foundation.util.Result;
import com.jc.supervise.point.domain.CmSupervisionPoint;
import com.jc.supervise.point.domain.CmSupervisionPointColumn;
import com.jc.supervise.point.vo.SupervisionPointInformation;

import java.util.List;
import java.util.Map;

/**
 * 建设管理-单位管理service
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public interface ICmSupervisionPointService extends IBaseService<CmSupervisionPoint>{

	/**
	 * 根据主键删除多条记录方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(CmSupervisionPoint entity) throws CustomException;

	/**
	 * 保存方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
    Result saveEntity(CmSupervisionPoint entity) throws CustomException;

	/**
	 * 修改方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result updateEntity(CmSupervisionPoint entity) throws CustomException;

	/**
	 * 获取监管机构与配置的监察点关系
	 * @return
	 */
	Map<String, List<SupervisionPointInformation>> getDeptIdAndPoint();

	/**
	 * 验证脚本
	 * @param entity
	 * @return
	 */
	Result checkJs(CmSupervisionPoint entity, CmProjectInfo project, List<CmSupervisionPointColumn> columnList);
}
