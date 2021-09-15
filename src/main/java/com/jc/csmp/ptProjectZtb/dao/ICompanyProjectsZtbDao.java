package com.jc.csmp.ptProjectZtb.dao;

import com.jc.csmp.ptProject.vo.EchartsVo;
import com.jc.csmp.ptProjectZtb.domain.CompanyProjectsZtb;
import com.jc.csmp.ptProjectZtb.vo.WinbiddingVo;
import com.jc.foundation.dao.IBaseDao;
import com.jc.foundation.domain.PageManager;

import java.util.List;


/**
 * @title  
 * @version  
 */
 
public interface ICompanyProjectsZtbDao extends IBaseDao<CompanyProjectsZtb>{

    List<EchartsVo> queryEchartsForZ();
    List<EchartsVo> queryEchartsForLjjn(CompanyProjectsZtb entity);
    PageManager queryWinbiddingForPm(CompanyProjectsZtb entity, PageManager page);
    PageManager queryBiddingXzjd(CompanyProjectsZtb entity, PageManager page);
    PageManager queryProjectApprover(CompanyProjectsZtb entity, PageManager page);
    Long queryXzjdCount(WinbiddingVo entity);
}
