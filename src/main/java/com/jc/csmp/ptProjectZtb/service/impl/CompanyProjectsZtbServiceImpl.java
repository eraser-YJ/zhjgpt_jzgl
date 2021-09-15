package com.jc.csmp.ptProjectZtb.service.impl;

import com.jc.csmp.ptProject.vo.EchartsVo;
import com.jc.csmp.ptProjectZtb.domain.CompanyProjectsZtb;
import com.jc.csmp.ptProjectZtb.dao.ICompanyProjectsZtbDao;
import com.jc.csmp.ptProjectZtb.domain.CompanyProjectsZtb;
import com.jc.csmp.ptProjectZtb.service.ICompanyProjectsZtbService;
import com.jc.csmp.ptProjectZtb.vo.WinbiddingVo;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.*;
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
public class CompanyProjectsZtbServiceImpl extends BaseServiceImpl<CompanyProjectsZtb> implements ICompanyProjectsZtbService {

	private ICompanyProjectsZtbDao companyProjectsZtbDao;

	public CompanyProjectsZtbServiceImpl(){}

	@Autowired
	public CompanyProjectsZtbServiceImpl(ICompanyProjectsZtbDao companyProjectsZtbDao){
		super(companyProjectsZtbDao);
		this.companyProjectsZtbDao = companyProjectsZtbDao;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(CompanyProjectsZtb entity) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(entity,true);
			result = companyProjectsZtbDao.delete(entity);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result saveEntity(CompanyProjectsZtb entity) throws CustomException {
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
	public Result updateEntity(CompanyProjectsZtb entity) throws CustomException {
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
	public List<EchartsVo> queryEchartsForZ(){
		List<EchartsVo> list = new ArrayList<>();
		try {
			list = companyProjectsZtbDao.queryEchartsForZ();
			return list;
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public List<EchartsVo> queryEchartsForLjjn(CompanyProjectsZtb entity){
		List<EchartsVo> list = new ArrayList<>();
		try {
			list = companyProjectsZtbDao.queryEchartsForLjjn(entity);
			return list;
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public PageManager queryWinbiddingForPm(CompanyProjectsZtb entity, PageManager page){
		try {
			PageManager list = companyProjectsZtbDao.queryWinbiddingForPm(entity, page);
			return list;
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public PageManager queryBiddingXzjd(CompanyProjectsZtb entity, PageManager page){
		try {
			PageManager page_ = companyProjectsZtbDao.queryBiddingXzjd(entity, page);
			return page_;
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public PageManager queryProjectApprover(CompanyProjectsZtb entity, PageManager page){
		try {
			PageManager list = companyProjectsZtbDao.queryProjectApprover(entity,page);
			return list;
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Long queryXzjdCount(WinbiddingVo entity){
		Long a= 0L;
		try {
			a = companyProjectsZtbDao.queryXzjdCount(entity);
			return a;
		} catch (Exception e) {
			throw e;
		}
	}

}

