package com.jc.csmp.ptProject.dao;

import com.jc.csmp.ptProject.domain.ProjectInfo;
import com.jc.csmp.ptProject.vo.EchartsVo;
import com.jc.foundation.dao.IBaseDao;
import com.jc.supervise.gis.vo.GisVo;

import java.util.List;


/**
 * @title  
 * @version  
 */
 
public interface IProjectInfoDao extends IBaseDao<ProjectInfo>{

    List<EchartsVo> queryEchartsForArea();
    List<EchartsVo> queryProjectJd();
    List<EchartsVo> queryAverageDay();
    List<EchartsVo> queryEchartsForStructureType();
    List<EchartsVo> queryEchartsForBuildingTypes();
    List<EchartsVo> queryProjectPass();
    EchartsVo queryEchartsForSgxk();
    List<GisVo> queryCompanyForArea();//查询企业占比
    List<GisVo> queryMoneyForArea();//查询投资完成情况及产值
    EchartsVo queryAverageAccept();

    List<ProjectInfo> projectJwdList(ProjectInfo projectInfo);//查询项目坐标信息

    List<ProjectInfo> projectCompJwdList(ProjectInfo projectInfo);//查询项目企业信息坐标信息

    List<ProjectInfo> projectByStageList(ProjectInfo projectInfo);//查询各阶段项目企业信息坐标信息
}
