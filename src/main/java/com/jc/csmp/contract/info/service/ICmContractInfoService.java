package com.jc.csmp.contract.info.service;

import com.jc.csmp.contract.info.domain.CmContractInfo;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.foundation.util.Result;
import org.apache.commons.beanutils.converters.IntegerArrayConverter;

import java.math.BigDecimal;

/**
 * 建设管理-合同管理service
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public interface ICmContractInfoService extends IBaseService<CmContractInfo>{

	/**
	 * @description 发起流程方法
	 * @param entity 实体类
	 * @return Integer 增加的记录数
	 * @author
	 * @version  2020-07-09
	 */
	Integer saveWorkflow(CmContractInfo entity)  throws CustomException ;

	/**
	 * @description 修改方法
	 * @param entity 实体类
	 * @return Integer 修改的记录数量
	 * @author
	 * @version  2020-07-09
	 */
	Integer updateWorkflow(CmContractInfo entity) throws  CustomException ;

	PageManager workFlowTodoQueryByPage(CmContractInfo entity, PageManager page) ;

	PageManager workFlowDoneQueryByPage(CmContractInfo entity, PageManager page) ;

	PageManager workFlowInstanceQueryByPage(CmContractInfo entity, PageManager page) ;

	/**
	 * 根据主键删除多条记录方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(CmContractInfo entity) throws CustomException;

	/**
	 * 修改方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result updateEntity(CmContractInfo entity) throws CustomException;

	/**
	 * 根据id获取对象
	 * @param contractId
	 * @return
	 */
    CmContractInfo getById(String contractId);

	/**
	 * 更新支付总额
	 * @param id: 要更新的id
	 * @param totalPayment: 金额
	 * @return
	 */
	Result updateTotalPayment(String id, BigDecimal totalPayment);

	/**
	 * 修改合同金额, 合同的金额+改变的金额
	 * @param id: 合同id
	 * @param changeAmount: 改变金额，可以为负数
	 * @return
	 */
	Result updateContractMoney(String id, BigDecimal changeAmount);
}
