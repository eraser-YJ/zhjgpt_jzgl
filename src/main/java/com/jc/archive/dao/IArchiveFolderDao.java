package com.jc.archive.dao;

import com.jc.foundation.dao.IBaseDao;
import com.jc.archive.domain.Folder;

import java.util.List;


/**
 * @title  GOA2.0源代码
 * @description  dao接口类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 
 * @version  2014-06-05
 */
 
public interface IArchiveFolderDao extends IBaseDao<Folder>{
	/**
	 * 方法描述：删除文件夹到垃圾箱
	 * @param folder Folder对象，将删除primaryKeys指定的文件夹
	 * @return 受影响记录行数
	 * @author zhangligang
	 * @version  2014年6月20日上午8:31:13
	 * @see
	 */
	public int deleteDirToRecycle(Folder folder);

	/**
	 * @description 权限过滤文件夹
	 * @param Folder folder 实体类
	 * @return List<Folder>
	 * @throws Exception 
	 * @author weny
	 * @version  2014-07-09
	 */
	public List<Folder> getFolderPermission(Folder folder);

	/**
	 * 方法描述：更新所有子文件夹的Path
	 * @param folder
	 * @author zhangligang
	 * @version  2014年8月5日上午10:44:25
	 * @see
	 */
	public int updateAllChildPath(Folder folder);
}
