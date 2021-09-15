package com.jc.csmp.equipment.info.service;

import com.jc.csmp.common.enums.EquiWorkStatusEnum;
import com.jc.csmp.doc.common.MechType;
import com.jc.csmp.equipment.info.domain.EquipmentInfo;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.foundation.util.Result;

import java.util.List;

/**
 * @Version 1.0
 */
public interface IEquipmentInfoService extends IBaseService<EquipmentInfo>{

	EquipmentInfo getById(String id) throws CustomException;

	void updateEquiStatus(String id, EquiWorkStatusEnum statusEnum) throws CustomException;
	/**
	 * 根据主键删除多条记录方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Integer deleteByIds(EquipmentInfo entity) throws CustomException;

	/**
	 * 保存方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result saveEntity(EquipmentInfo entity) throws CustomException;

	/**
	 * 修改方法
	 * @param entity
	 * @return
	 * @throws CustomException
	 */
	Result updateEntity(EquipmentInfo entity) throws CustomException;


	/**
	 * 查询
	 * @param projectCode
	 * @param mechType
	 * @return
	 */
	List<EquipmentInfo> queryByPorjectAndType(String projectCode, String mechType);


	/**
	 * 查询
	 * @param codes
	 * @return
	 */
	List<EquipmentInfo> queryByCode(MechType type, List<String> codes);

	/**
	 * 查询
	 * @param codes
	 * @return
	 */
	List<EquipmentInfo> queryUsedByCode(MechType type, List<String> codes);
}
