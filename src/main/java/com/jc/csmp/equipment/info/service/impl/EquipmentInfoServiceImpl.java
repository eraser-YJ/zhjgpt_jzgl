package com.jc.csmp.equipment.info.service.impl;

import com.jc.csmp.common.enums.EquiWorkStatusEnum;
import com.jc.csmp.doc.common.MechType;
import com.jc.csmp.equipment.info.domain.EquipmentInfo;
import com.jc.csmp.equipment.info.dao.IEquipmentInfoDao;
import com.jc.csmp.equipment.info.domain.EquipmentInfo;
import com.jc.csmp.equipment.info.service.IEquipmentInfoService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Version 1.0
 */
@Service
public class EquipmentInfoServiceImpl extends BaseServiceImpl<EquipmentInfo> implements IEquipmentInfoService {

    private IEquipmentInfoDao equipmentInfoDao;

    public EquipmentInfoServiceImpl() {
    }

    @Autowired
    public EquipmentInfoServiceImpl(IEquipmentInfoDao equipmentInfoDao) {
        super(equipmentInfoDao);
        this.equipmentInfoDao = equipmentInfoDao;
    }

    @Override
    public EquipmentInfo getById(String id) throws CustomException {
        if (id == null) {
            return null;
        }
        EquipmentInfo cond = new EquipmentInfo();
        cond.setId(id);
        return equipmentInfoDao.get(cond);
    }

    @Override
    public void updateEquiStatus(String id, EquiWorkStatusEnum statusEnum) throws CustomException {
        EquipmentInfo info = new EquipmentInfo();
        info.setModifyDateNew(new java.util.Date());
        info.setId(id);
        info.setWorkStatus(statusEnum.toString());
        equipmentInfoDao.update(info);
    }


    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Integer deleteByIds(EquipmentInfo entity) throws CustomException {
        Integer result = -1;
        try {
            propertyService.fillProperties(entity, true);
            result = equipmentInfoDao.delete(entity, false);
        } catch (Exception e) {
            CustomException ce = new CustomException(e);
            ce.setBean(entity);
            throw ce;
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Result saveEntity(EquipmentInfo entity) throws CustomException {
        if (!StringUtil.isEmpty(entity.getId())) {
            return Result.failure(ResultCode.PARAM_IS_INVALID);
        }
        try {
            if (entity.getEquipmentType() == null) {
                return Result.failure(1000, "设备类型不能为空");
            }
            if (entity.getEquipmentCode() == null) {
                return Result.failure(1000, "设备编码不能为空");
            }
            List<EquipmentInfo> dbList = queryByCode(MechType.getByCode(entity.getEquipmentType()), Arrays.asList(entity.getEquipmentCode()));
            if (dbList != null) {
                for (EquipmentInfo db : dbList) {
                    if (db.getEquipmentCode().equalsIgnoreCase(entity.getEquipmentCode()) && !db.getId().equalsIgnoreCase(entity.getId())) {
                        return Result.failure(1002, "该类型设备编码不能重复");
                    }
                }
            }
            entity.setDataStatus("1");
            this.save(entity);
            return Result.success(MessageUtils.getMessage("JC_SYS_001"));
        } catch (Exception e) {
            CustomException ce = new CustomException(e);
            ce.setBean(entity);
            throw ce;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Result updateEntity(EquipmentInfo entity) throws CustomException {
        if (StringUtil.isEmpty(entity.getId())) {
            return Result.failure(ResultCode.PARAM_IS_INVALID);
        }
        try {
            if (entity.getEquipmentType() == null) {
                return Result.failure(1000, "设备类型不能为空");
            }
            if (entity.getEquipmentCode() == null) {
                return Result.failure(1000, "设备编码不能为空");
            }
            List<EquipmentInfo> dbList = queryByCode(MechType.getByCode(entity.getEquipmentType()), Arrays.asList(entity.getEquipmentCode()));
            if (dbList != null) {
                for (EquipmentInfo db : dbList) {
                    if (db.getEquipmentCode().equalsIgnoreCase(entity.getEquipmentCode()) && !db.getId().equalsIgnoreCase(entity.getId())) {
                        return Result.failure(1002, "该类型设备编码不能重复");
                    }
                }
            }
            Integer flag = this.update(entity);
            if (flag == 1) {
                return Result.success(MessageUtils.getMessage("JC_SYS_003"));
            } else {
                return Result.failure(1, MessageUtils.getMessage("JC_SYS_004"));
            }
        } catch (Exception e) {
            CustomException ce = new CustomException(e);
            ce.setBean(entity);
            throw ce;
        }
    }

    @Override
    public List<EquipmentInfo> queryByPorjectAndType(String projectCode, String mechType) {
        EquipmentInfo cond = new EquipmentInfo();
        cond.setProjectCode(projectCode);
        if (mechType != null && mechType.length() > 0) {
            cond.setEquipmentType(mechType);
        }
        return this.equipmentInfoDao.queryAll(cond);
    }

    @Override
    public List<EquipmentInfo> queryByCode(MechType type, List<String> codes) {
        if (codes == null || codes.size() <= 0) {
            return new ArrayList<>();
        }
        EquipmentInfo cond = new EquipmentInfo();
        cond.setEquipmentCodes(codes.toArray(new String[codes.size()]));
        cond.setEquipmentType(type.toString());
        List<EquipmentInfo> dataList = equipmentInfoDao.queryAll(cond);
        return dataList;
    }

    @Override
    public List<EquipmentInfo> queryUsedByCode(MechType type, List<String> codes) {
        if (codes == null || codes.size() <= 0) {
            return new ArrayList<>();
        }
        EquipmentInfo cond = new EquipmentInfo();
        cond.setEquipmentCodes(codes.toArray(new String[codes.size()]));
        cond.setEquipmentType(type.toString());
        cond.setDataStatus("1");
        List<EquipmentInfo> dataList = equipmentInfoDao.queryAll(cond);
        return dataList;
    }

}

