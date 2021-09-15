package com.jc.csmp.message.service;

import com.jc.csmp.message.domain.CmMessageInfo;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.foundation.util.Result;
import com.jc.workflow.external.WorkflowBean;

/**
 * 建设管理-消息管理service
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public interface ICmMessageInfoService extends IBaseService<CmMessageInfo>{

	/**
	 * 根据主键删除多条记录方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(CmMessageInfo entity) throws CustomException;

	/**
	 * 保存方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
    Result saveEntity(CmMessageInfo entity) throws CustomException;

	/**
	 * 修改方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result updateEntity(CmMessageInfo entity) throws CustomException;

	/**
	 * 根据id获取数据
	 * @param id
	 * @return
	 */
	CmMessageInfo getById(String id);

	/**
	 * 发送消息
	 * @param title: 标题
	 * @param content: 内容
	 * @param receiveId: 接收人
	 * @param receiveDeptId: 接收人所在部门
	 * @param receiveDeptCode: 接收人所在部门编码
	 * @return
	 */
	Result sendMessage(String title, String content, String receiveId, String receiveDeptId, String receiveDeptCode);

	/**
	 * 待办分页查询
	 * @param param
	 * @param page
	 * @return
	 */
	PageManager workflowTodoQuery(CmMessageInfo param, PageManager page);

	/**
	 * 已办理分页查询
	 * @param param
	 * @param page
	 * @return
	 */
	PageManager workflowDoneQuery(CmMessageInfo param, PageManager page);
}
