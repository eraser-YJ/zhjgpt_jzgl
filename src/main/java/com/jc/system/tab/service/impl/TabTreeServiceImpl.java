package com.jc.system.tab.service.impl;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.system.tab.dao.ITabTreeDao;
import com.jc.system.tab.domain.TabTree;
import com.jc.system.tab.service.ITabTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Service
public class TabTreeServiceImpl extends BaseServiceImpl<TabTree> implements ITabTreeService{

	private ITabTreeDao tabTreeDao;
	
	public TabTreeServiceImpl(){}
	
	@Autowired
	public TabTreeServiceImpl(ITabTreeDao tabTreeDao){
		super(tabTreeDao);
		this.tabTreeDao = tabTreeDao;
	}

	@Override
    @Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(TabTree tabTree) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(tabTree,true);
			result = tabTreeDao.delete(tabTree);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(tabTree);
			throw ce;
		}
		return result;
	}

}