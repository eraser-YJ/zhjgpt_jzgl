package com.jc.csmp.ptProjectZtb.service;

import com.jc.csmp.ptProject.vo.EchartsVo;
import com.jc.csmp.ptProjectZtb.domain.CompanyProjectsZtb;
import com.jc.csmp.ptProjectZtb.vo.WinbiddingVo;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.foundation.util.Result;

import java.util.List;

/**
 * @Version 1.0
 */
public interface ICompanyProjectsZtbService extends IBaseService<CompanyProjectsZtb>{

	/**
	 * 根据主键删除多条记录方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(CompanyProjectsZtb entity) throws CustomException;

	/**
	 * 保存方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result saveEntity(CompanyProjectsZtb entity) throws CustomException;

	/**
	 * 修改方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result updateEntity(CompanyProjectsZtb entity) throws CustomException;

	List<EchartsVo> queryEchartsForZ();
	PageManager queryWinbiddingForPm(CompanyProjectsZtb entity, PageManager page);
	PageManager queryBiddingXzjd(CompanyProjectsZtb entity, PageManager page);
	PageManager queryProjectApprover(CompanyProjectsZtb entity, PageManager page);
	Long queryXzjdCount(WinbiddingVo entity);
	List<EchartsVo> queryEchartsForLjjn(CompanyProjectsZtb entity);
}
