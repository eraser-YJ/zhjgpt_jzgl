package com.jc.csmp.ptProjectZtb.dao.impl;

import com.jc.csmp.ptProject.vo.EchartsVo;
import com.jc.csmp.ptProjectZtb.vo.WinbiddingVo;
import com.jc.foundation.domain.PageManager;
import org.springframework.stereotype.Repository;

import com.jc.csmp.ptProjectZtb.dao.ICompanyProjectsZtbDao;
import com.jc.csmp.ptProjectZtb.domain.CompanyProjectsZtb;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.foundation.exception.ConcurrentException;
import com.jc.foundation.exception.DBException;

import java.util.List;

/**
 * @title   
 * @version
 */
@Repository
public class CompanyProjectsZtbDaoImpl extends BaseClientDaoImpl<CompanyProjectsZtb> implements ICompanyProjectsZtbDao{

	public CompanyProjectsZtbDaoImpl(){}
	@Override
	public List<EchartsVo> queryEchartsForZ(){
		return this.getTemplate().selectList(this.getNameSpace(new CompanyProjectsZtb()) + ".queryEchartsForZ");
	}
	@Override
	public List<EchartsVo> queryEchartsForLjjn(CompanyProjectsZtb entity){
		return this.getTemplate().selectList(this.getNameSpace(new CompanyProjectsZtb()) + ".queryEchartsForLjjn",entity);
	}
	@Override
	public PageManager queryWinbiddingForPm(CompanyProjectsZtb entity, PageManager page){
		return this.queryByPage(entity, page, "queryWinbiddingForPmCount", "queryWinbiddingForPm");
	}
	@Override
	public PageManager queryBiddingXzjd(CompanyProjectsZtb entity, PageManager page){
		return this.queryByPage(entity, page, "queryXzjdCount", "queryBiddingXzjd");
	}
	@Override
	public PageManager queryProjectApprover(CompanyProjectsZtb entity, PageManager page){
		return this.queryByPage(entity, page, "queryProjectApproverCount", "queryProjectApprover");
	}
	@Override
	public Long queryXzjdCount(WinbiddingVo entity){
		return this.getTemplate().selectOne(this.getNameSpace(new CompanyProjectsZtb()) + ".queryXzjdCount",entity);
	}
}