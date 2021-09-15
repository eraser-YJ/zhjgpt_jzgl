package com.jc.csmp.ptProject.dao.impl;

import com.jc.csmp.ptProject.vo.EchartsVo;
import com.jc.supervise.gis.vo.GisVo;
import org.springframework.stereotype.Repository;

import com.jc.csmp.ptProject.dao.IProjectInfoDao;
import com.jc.csmp.ptProject.domain.ProjectInfo;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;

import java.util.List;

/**
 * @title   
 * @version
 */
@Repository
public class ProjectInfoDaoImpl extends BaseClientDaoImpl<ProjectInfo> implements IProjectInfoDao{

	public ProjectInfoDaoImpl(){}

	@Override
	public List<EchartsVo> queryEchartsForBuildingTypes(){
		return this.getTemplate().selectList(this.getNameSpace(new ProjectInfo()) + ".queryEchartsForBuildingTypes");
	}
	@Override
	public List<EchartsVo> queryEchartsForStructureType(){
		return this.getTemplate().selectList(this.getNameSpace(new ProjectInfo()) + ".queryEchartsForStructureType");
	}
	@Override
	public List<EchartsVo> queryEchartsForArea(){
		return this.getTemplate().selectList(this.getNameSpace(new ProjectInfo()) + ".queryEchartsForArea");
	}
	@Override
	public List<EchartsVo> queryProjectJd(){
		return this.getTemplate().selectList(this.getNameSpace(new ProjectInfo()) + ".queryProjectJd");
	}
	@Override
	public List<EchartsVo> queryAverageDay(){
		return this.getTemplate().selectList(this.getNameSpace(new ProjectInfo()) + ".queryAverageDay");
	}
	@Override
	public List<EchartsVo> queryProjectPass(){
		return this.getTemplate().selectList(this.getNameSpace(new ProjectInfo()) + ".queryProjectPass");
	}
	@Override
	public EchartsVo queryEchartsForSgxk(){
		return this.getTemplate().selectOne(this.getNameSpace(new ProjectInfo()) + ".queryEchartsForSgxk");
	}
	@Override
	public EchartsVo queryAverageAccept(){
		return this.getTemplate().selectOne(this.getNameSpace(new ProjectInfo()) + ".queryAverageAccept");
	}

	/**
	 * 查询企业占比
	 * @return
	 */
	@Override
	public List<GisVo> queryCompanyForArea(){
		return this.getTemplate().selectList(this.getNameSpace(new ProjectInfo()) + ".queryCompanyForArea");
	}

	/**
	 * 查询投资完成情况及产值
	 * @return
	 */
	@Override
	public List<GisVo> queryMoneyForArea(){
		return this.getTemplate().selectList(this.getNameSpace(new ProjectInfo()) + ".queryMoneyForArea");
	}

	/**
	 * 查询项目坐标信息
	 * @param projectInfo
	 * @return
	 */
	@Override
	public List<ProjectInfo> projectJwdList(ProjectInfo projectInfo){
		return this.getTemplate().selectList(this.getNameSpace(projectInfo) + ".queryJwd",projectInfo);
	}

	/**
	 * 查询项目企业信息坐标信息
	 * @param projectInfo
	 * @return
	 */
	@Override
	public List<ProjectInfo> projectCompJwdList(ProjectInfo projectInfo){
		return this.getTemplate().selectList(this.getNameSpace(projectInfo) + ".queryJwdByCompany",projectInfo);
	}

	/**
	 * 查询各阶段项目企业信息坐标信息
	 * @param projectInfo
	 * @return
	 */
	@Override
	public List<ProjectInfo> projectByStageList(ProjectInfo projectInfo){
		return this.getTemplate().selectList(this.getNameSpace(projectInfo) + ".queryByStage",projectInfo);
	}


}