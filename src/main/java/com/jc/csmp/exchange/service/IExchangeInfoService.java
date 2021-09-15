package com.jc.csmp.exchange.service;

import com.jc.common.kit.vo.ResVO;
import com.jc.csmp.common.enums.EquiWorkStatusEnum;
import com.jc.csmp.doc.common.MechType;
import com.jc.csmp.equipment.info.domain.EquipmentInfo;
import com.jc.csmp.exchange.vo.IEquiInfoVO;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.foundation.util.Result;

import java.util.List;
import java.util.Map;

/**
 * @Version 1.0
 */
public interface IExchangeInfoService {
    ResVO addDeviceInfoEx(List<Map<String, String>> mapList, IEquiInfoVO attVO) throws Exception;

    ResVO updateDeviceOnline(List<Map<String, String>> mapList, IEquiInfoVO attVO) throws Exception;

    ResVO realDeviceDataEx(List<Map<String, String>> mapList, IEquiInfoVO attVO) throws Exception;

}
