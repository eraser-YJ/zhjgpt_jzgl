package com.jc.csmp.ptProject.service;

import com.jc.csmp.ptProject.domain.ProjectInfo;
import com.jc.csmp.ptProject.vo.EchartsVo;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.foundation.util.Result;
import com.jc.supervise.gis.vo.GisVo;

import java.util.List;

/**
 * @Version 1.0
 */
public interface IProjectInfoService extends IBaseService<ProjectInfo>{

	/**
	 * 根据主键删除多条记录方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(ProjectInfo entity) throws CustomException;

	/**
	 * 保存方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result saveEntity(ProjectInfo entity) throws CustomException;

	/**
	 * 修改方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result updateEntity(ProjectInfo entity) throws CustomException;

	List<EchartsVo> queryEchartsForArea();
	List<EchartsVo> queryEchartsForBuildingTypes();
	List<EchartsVo> queryEchartsForStructureType();
	List<EchartsVo> queryProjectJd();
	List<EchartsVo> queryAverageDay();
	List<EchartsVo> queryProjectPass();
	EchartsVo queryEchartsForSgxk();
	EchartsVo queryAverageAccept();

	List<GisVo> queryCompanyForArea();//查询企业占比
	List<GisVo> queryMoneyForArea();//查询投资完成情况及产值

	List<ProjectInfo> projectJwdList(ProjectInfo projectInfo);//查询项目坐标信息
	List<ProjectInfo> projectCompJwdList(ProjectInfo projectInfo);//
	List<ProjectInfo> projectByStageList(ProjectInfo projectInfo);//查询各阶段项目企业信息坐标信息
}
