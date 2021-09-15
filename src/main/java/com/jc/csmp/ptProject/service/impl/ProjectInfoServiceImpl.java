package com.jc.csmp.ptProject.service.impl;

import com.jc.csmp.ptProject.domain.ProjectInfo;
import com.jc.csmp.ptProject.dao.IProjectInfoDao;
import com.jc.csmp.ptProject.service.IProjectInfoService;
import com.jc.csmp.ptProject.vo.EchartsVo;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.*;
import com.jc.supervise.gis.vo.GisVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/** 
 * @Version 1.0
 */
@Service
public class ProjectInfoServiceImpl extends BaseServiceImpl<ProjectInfo> implements IProjectInfoService {

	private IProjectInfoDao projectInfoDao;

	public ProjectInfoServiceImpl(){}

	@Autowired
	public ProjectInfoServiceImpl(IProjectInfoDao projectInfoDao){
		super(projectInfoDao);
		this.projectInfoDao = projectInfoDao;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(ProjectInfo entity) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(entity,true);
			result = projectInfoDao.delete(entity);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result saveEntity(ProjectInfo entity) throws CustomException {
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
	public Result updateEntity(ProjectInfo entity) throws CustomException {
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

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public List<EchartsVo> queryEchartsForArea(){
		List<EchartsVo> list = new ArrayList<>();
		try {
			list = projectInfoDao.queryEchartsForArea();
			return list;
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public List<EchartsVo> queryEchartsForBuildingTypes(){
		List<EchartsVo> list = new ArrayList<>();
		try {
			list = projectInfoDao.queryEchartsForBuildingTypes();
			return list;
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public List<EchartsVo> queryEchartsForStructureType(){
		List<EchartsVo> list = new ArrayList<>();
		try {
			list = projectInfoDao.queryEchartsForStructureType();
			return list;
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public List<EchartsVo> queryProjectJd(){
		List<EchartsVo> list = new ArrayList<>();
		try {
			list = projectInfoDao.queryProjectJd();
			return list;
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public List<EchartsVo> queryAverageDay(){
		List<EchartsVo> list = new ArrayList<>();
		try {
			list = projectInfoDao.queryAverageDay();
			return list;
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public List<EchartsVo> queryProjectPass(){
		List<EchartsVo> list = new ArrayList<>();
		try {
			list = projectInfoDao.queryProjectPass();
			return list;
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public EchartsVo queryEchartsForSgxk(){
		EchartsVo list = new EchartsVo();
		try {
			list = projectInfoDao.queryEchartsForSgxk();
			return list;
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public EchartsVo queryAverageAccept(){
		EchartsVo list = new EchartsVo();
		try {
			list = projectInfoDao.queryAverageAccept();
			return list;
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * //查询企业占比
	 * @return
	 */
	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public List<GisVo> queryCompanyForArea(){
		try {
			return projectInfoDao.queryCompanyForArea();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 查询投资完成情况及产值
	 * @return
	 */
	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public List<GisVo> queryMoneyForArea(){
		try {
			return projectInfoDao.queryMoneyForArea();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 查询项目坐标信息
	 * @param projectInfo
	 * @return
	 */
	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public List<ProjectInfo> projectJwdList(ProjectInfo projectInfo){
		try {
			return projectInfoDao.projectJwdList(projectInfo);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 查询项目企业信息坐标信息
	 * @param projectInfo
	 * @return
	 */
	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public List<ProjectInfo> projectCompJwdList(ProjectInfo projectInfo){
		try {
			return projectInfoDao.projectCompJwdList(projectInfo);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 查询各阶段项目企业信息坐标信息
	 * @param projectInfo
	 * @return
	 */
	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public List<ProjectInfo> projectByStageList(ProjectInfo projectInfo){
		try {
			return projectInfoDao.projectByStageList(projectInfo);
		} catch (Exception e) {
			throw e;
		}
	}

}

