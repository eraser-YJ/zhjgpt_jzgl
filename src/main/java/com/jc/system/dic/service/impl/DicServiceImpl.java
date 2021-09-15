package com.jc.system.dic.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.system.dic.cache.IDicCacheService;
import com.jc.system.dic.cache.impl.DicCacheServiceImpl;
import com.jc.system.dic.dao.IDicDao;
import com.jc.system.dic.domain.Dic;
import com.jc.system.dic.service.IDicService;

/**
 * @author Administrator
 * @date 2020-06-30
 */
@Service
public class DicServiceImpl extends BaseServiceImpl<Dic> implements IDicService {
	@Autowired
	public DicServiceImpl(IDicDao dicDao) {
		super(dicDao);
		this.dicDao = dicDao;
	}
	public DicServiceImpl(){
		
	}
	private IDicDao dicDao;

	@Override
    @Transactional(rollbackFor = Exception.class)
	public Dic saveDic(Dic dic) throws CustomException {
		dic.setDefaultValue(0);
		dicDao.save(dic);
		IDicCacheService cacheService = DicCacheServiceImpl.getInstance();
		cacheService.refreshDicItem(dic, null);
		return dic;
	}

	@Override
    @Transactional(rollbackFor = Exception.class)
	public Integer save(Dic dic) throws CustomException {
		propertyService.fillProperties(dic,false);
		//dic.setDicType("1");
		Integer flag = dicDao.save(dic);
		IDicCacheService cacheService = DicCacheServiceImpl.getInstance();
		cacheService.refreshDicItem(dic, null);
		return flag;
	}

	@Override
    @Transactional(rollbackFor = Exception.class)
	public Integer update(Dic dic) throws CustomException {
		String parentType = dic.getParentType();
		String parentCode = parentType.split("=")[1];
		IDicCacheService cacheService = DicCacheServiceImpl.getInstance();
		Dic oldDic = cacheService.getDic(dic.getParentId(),parentCode, dic.getCode());
		// 如果为某认选中的话需要现移出同类型的其他类型
		if (dic.getDefaultValue() != null && dic.getDefaultValue() == 1) {
			Dic dicTemp = new Dic();
			dicTemp.setParentId(dic.getParentId());
			dicTemp.setDicFlag(1);
			dicTemp.setDefaultValue(1);
			dicTemp = dicDao.get(dicTemp);
			if (dicTemp != null && !dic.getCode().equals(dicTemp.getCode())) {
				Dic oldDicTemp = dicTemp.clone();
				dicTemp.setDefaultValue(0);
				dicDao.update(dicTemp);
				cacheService.refreshDicItem(dicTemp, oldDicTemp);
			}
		}
		propertyService.fillProperties(dic,true);
		Integer ret = dicDao.update(dic);

		cacheService.refreshDicItem(dic, oldDic);
		return ret;
	}

	@Override
    public Dic get(Dic dic) {
		return dicDao.get(dic);
	}

	/**
	 * @description 分页查询方法
	 * @param dic 实体类,PageManager page 分页实体类
	 * @return PagingBean 查询结果
	 * @author
	 */
	@Override
    public PageManager query(Dic dic, PageManager page) {
		return dicDao.queryByPage(dic, page);
	}

	@Override
    public List<Dic> query(Dic dic){
		return dicDao.queryAll(dic);
	}

	@Override
    @Transactional(rollbackFor = Exception.class)
	public Integer delete(Dic dic) throws CustomException {
		dic = dicDao.get(dic);
		String parentType = dic.getParentType();
		String parentCode = parentType.split("=")[1];
		IDicCacheService cacheService = DicCacheServiceImpl.getInstance();
		Dic oldDic = cacheService.getDic(dic.getParentId(),parentCode, dic.getCode());
		Integer result = dicDao.delete(dic,false);
		cacheService.refreshDicItem(dic, oldDic);
		return result;
	}
	@Override
	public List<Dic> getDicByDuty(Dic dic) {
		return dicDao.getDicByDuty(dic);
	}
	
	@Override
    @Transactional(rollbackFor = Exception.class)
	public Integer delForDicList(Dic dic) throws CustomException {
		
		return dicDao.delForDicList(dic);
	}

}