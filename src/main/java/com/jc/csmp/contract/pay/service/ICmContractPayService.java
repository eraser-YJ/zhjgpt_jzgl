package com.jc.csmp.contract.pay.service;

import com.jc.csmp.contract.pay.domain.CmContractPay;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;

/**
 * 建设管理-合同支付管理service
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public interface ICmContractPayService extends IBaseService<CmContractPay>{

	/**
	 * @description 发起流程方法
	 * @param entity 实体类
	 * @return Integer 增加的记录数
	 * @author
	 * @version  2020-07-09
	 */
	Integer saveWorkflow(CmContractPay entity)  throws CustomException ;

	/**
	 * @description 修改方法
	 * @param entity 实体类
	 * @return Integer 修改的记录数量
	 * @author
	 * @version  2020-07-09
	 */
	Integer updateWorkflow(CmContractPay entity) throws  CustomException ;

	PageManager workFlowTodoQueryByPage(CmContractPay entity, PageManager page) ;

	PageManager workFlowDoneQueryByPage(CmContractPay entity, PageManager page) ;

	PageManager workFlowInstanceQueryByPage(CmContractPay entity, PageManager page) ;

	/**
	 * 根据主键删除多条记录方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(CmContractPay entity) throws CustomException;

	/**
	 * 根据id获取
	 * @param id
	 * @return
	 */
	CmContractPay getById(String id);

	/**
	 * 根据登录用户权限查询能看的合同支付记录
	 * @param entity
	 * @param page
	 * @return
	 */
    PageManager selectAuthQuery(CmContractPay entity, PageManager page);
}
