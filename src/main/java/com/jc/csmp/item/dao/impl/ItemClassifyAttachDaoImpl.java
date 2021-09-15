package com.jc.csmp.item.dao.impl;

import org.springframework.stereotype.Repository;

import com.jc.csmp.item.dao.IItemClassifyAttachDao;
import com.jc.csmp.item.domain.ItemClassifyAttach;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.foundation.exception.ConcurrentException;
import com.jc.foundation.exception.DBException;

/**
 * @title   
 * @version
 */
@Repository
public class ItemClassifyAttachDaoImpl extends BaseClientDaoImpl<ItemClassifyAttach> implements IItemClassifyAttachDao{

	public ItemClassifyAttachDaoImpl(){}

}