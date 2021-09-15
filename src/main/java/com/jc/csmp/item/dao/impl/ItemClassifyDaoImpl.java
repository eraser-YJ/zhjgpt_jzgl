package com.jc.csmp.item.dao.impl;

import org.springframework.stereotype.Repository;

import com.jc.cmsp.item.dao.IItemClassifyDao;
import com.jc.csmp.item.domain.ItemClassify;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.foundation.exception.ConcurrentException;
import com.jc.foundation.exception.DBException;

/**
 * @title   
 * @version
 */
@Repository
public class ItemClassifyDaoImpl extends BaseClientDaoImpl<ItemClassify> implements IItemClassifyDao{

	public ItemClassifyDaoImpl(){}

}