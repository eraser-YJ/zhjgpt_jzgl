package com.jc.archive.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jc.archive.ArchiveException;
import com.jc.archive.dao.IVersionDao;
import com.jc.archive.domain.Document;
import com.jc.archive.domain.Version;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.archive.service.IVersionService;

/**
 * @title GOA2.0源代码
 * @description 业务服务类 Copyright (c) 2014 yixunnet.com Inc. All Rights
 *              Reserved Company 长春嘉诚网络工程有限公司
 * @author
 * @version 2014-07-01
 */
@Service
public class VersionServiceImpl extends BaseServiceImpl<Version> implements
		IVersionService {

	private IVersionDao versionDao;

	public VersionServiceImpl() {
	}

	@Autowired
	public VersionServiceImpl(IVersionDao versionDao) {
		super(versionDao);
		this.versionDao = versionDao;
	}

	@Override
	public Version createVersion(Document document) throws ArchiveException {
		Version version = new Version();

		Integer max = versionDao.getMaxVersion(document.getId());
		if (max != null) {
			version.setCurrentVersion(max + 1);
		} else {
			version.setCurrentVersion(1);
		}

		version.setFolderId(document.getFolderId()); /* 文件夹ID */
		version.setDocState(document.getDocState()); /* 0 暂存 1 已发布 2 审核中 */
		version.setFileType(document.getFileType()); /*
													 * 文件存放类型：0文档 1 知识 2 链接 3 收藏
													 */
		version.setContentType(document.getContentType()); /*
															 * 文档内容类型 0 未知 1
															 * word 2 excel 3
															 * ppt
															 */
		version.setModel(document.getModel()); /* 0 公共文档 1 我的文档 */
		version.setDmLink(document.getDmLink()); /* 链接路径:存放归档信息的URL */
		version.setDmName(document.getDmName()); /* 文档管理_文档名称 */
		version.setDmAlias(document.getDmAlias()); /* 文档在服务器中的名称已当前时间的long值名称 */
		version.setDmLockStatus(document.getDmLockStatus()); /*
															 * 文档管理_文档锁定状态(0-
															 * 未锁定;1-锁定)
															 */
		version.setPhysicalPath(document.getPhysicalPath()); /* 文件物理地址 */
		version.setDmDir(document.getDmDir()); /* 文档所在目录位置 */
		version.setDmType(document.getDmType()); /* 文档管理_文档类型(字典项) */
		version.setDmSuffix(document.getDmSuffix()); /* 文档管理_文档历史后缀名 */
		version.setWeight(document.getWeight()); /**/
		version.setKmTitle(document.getKmTitle()); /* 知识标题 */
		version.setDmInRecycle(document.getDmInRecycle()); /*
															 * 文档管理_回收站状态(0-否 ;
															 * 1-是级联;2-不是级联)
															 */
		version.setKeyWords(document.getKeyWords()); /* 关键字(文档/知识) */
		version.setPermissionValue(document.getPermissionValue()); /*
																	 * 针对知识管理共三位
																	 * 111 复制 打印
																	 * 下载
																	 */
		version.setAuthor(document.getAuthor()); /* 作者 */
		version.setDmSize(document.getDmSize()); /* 文档大小 */
		version.setSeq(document.getSeq()); /* 编号 */
		version.setIsValid(document.getIsValid()); /* 是否过期 0未过期 1 过期 */
		version.setPiId(document.getPiId()); /* 流程ID */
		if(document.getOpenDate() != null) {
			version.setOpenDate(document.getOpenDate()); /* 知识发布时间 */
		}
		if(document.getOpenDateBegin() != null) {
			version.setOpenDateBegin(document.getOpenDateBegin()); /* 知识发布时间开始 */
		}
		if(document.getOpenDateEnd() != null) {
			version.setOpenDateEnd(document.getOpenDateEnd()); /* 知识发布时间结束 */
		}
		version.setKmEndTime(document.getKmEndTime()); /* 知识管理_结束日期 */
		version.setKmKeepTime(document.getKmKeepTime()); /* 知识管理_知识时限天数(字典) */
		version.setKmRemind(document.getKmRemind()); /*
													 * 知识管理_通知提醒(0-不提醒;1-邮件;2
													 * -短信)
													 */
		version.setKmContent(document.getKmContent()); /* 知识管理_知识内容 */
		version.setKmClickNum(document.getKmClickNum()); /* 知识管理_点击次数 */

		version.setId(null);
		version.setBackUpId(document.getId());
		return version;

	}

	@Override
	public Integer getMaxVersion(String id) {
		return versionDao.getMaxVersion(id);
	}
}