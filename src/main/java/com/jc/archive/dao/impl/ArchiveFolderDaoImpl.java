package com.jc.archive.dao.impl;

import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.archive.dao.IArchiveFolderDao;
import com.jc.archive.domain.Folder;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @title  GOA2.0源代码
 * @description  dao实现类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 
 * @version  2014-06-05
 */
@Repository
public class ArchiveFolderDaoImpl extends BaseClientDaoImpl<Folder> implements IArchiveFolderDao{

	public ArchiveFolderDaoImpl(){}

	@Override
	public int deleteDirToRecycle(Folder folder) {
		return getTemplate().update(getNameSpace(folder)+".deleteDirToRecycle", folder);
	}

	/**
	 * @description 权限过滤文件夹
	 * @param
	 * @return List<Folder>
	 * @throws Exception 
	 * @author weny
	 * @version  2014-07-09
	 */
	public List<Folder> getFolderPermission(Folder folder) {
		return getTemplate().selectList(getNameSpace(folder) + ".getFolderPermission", folder);
	}

	@Override
	public int updateAllChildPath(Folder folder) {
		return getTemplate().update(getNameSpace(folder) + ".updateAllChildPath", folder);
	}

}