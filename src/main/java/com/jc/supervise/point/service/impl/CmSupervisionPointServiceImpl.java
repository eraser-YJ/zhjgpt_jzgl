package com.jc.supervise.point.service.impl;

import com.jc.csmp.project.domain.CmProjectInfo;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.*;
import com.jc.supervise.point.dao.ICmSupervisionPointColumnDao;
import com.jc.supervise.point.dao.ICmSupervisionPointDao;
import com.jc.supervise.point.domain.CmSupervisionPoint;
import com.jc.supervise.point.domain.CmSupervisionPointColumn;
import com.jc.supervise.point.service.ICmSupervisionPointColumnService;
import com.jc.supervise.point.service.ICmSupervisionPointService;
import com.jc.supervise.point.vo.SupervisionPointInformation;
import com.jc.supervise.warning.service.ICmSupervisionWarningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 建设管理-监察点管理serviceImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Service
public class CmSupervisionPointServiceImpl extends BaseServiceImpl<CmSupervisionPoint> implements ICmSupervisionPointService {

	private ICmSupervisionPointDao cmSupervisionPointDao;
	@Autowired
	private ICmSupervisionWarningService cmSupervisionWarningService;

	@Autowired
	private ICmSupervisionPointColumnService cmSupervisionPointColumnService;

	public CmSupervisionPointServiceImpl(){}

	@Autowired
	public CmSupervisionPointServiceImpl(ICmSupervisionPointDao cmSupervisionPointDao){
		super(cmSupervisionPointDao);
		this.cmSupervisionPointDao = cmSupervisionPointDao;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(CmSupervisionPoint entity) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(entity,true);
			result = cmSupervisionPointDao.delete(entity);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result saveEntity(CmSupervisionPoint entity) throws CustomException {
		if (!StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
			this.save(entity);
			cmSupervisionPointColumnService.updateSupervisionId(entity.getId(), entity.getTempId());
			GlobalUtil.writeFile(entity.getId(), entity.getCreateDate(), entity.getJsContent());
			return Result.success(MessageUtils.getMessage("JC_SYS_001"));
		} catch (Exception e) {
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result updateEntity(CmSupervisionPoint entity) throws CustomException {
		if (StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
			Integer flag = this.update(entity);
			if (flag == 1) {
				GlobalUtil.writeFile(entity.getId(), entity.getCreateDate(), entity.getJsContent());
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
	public Map<String, List<SupervisionPointInformation>> getDeptIdAndPoint() {
		Map<String, List<SupervisionPointInformation>> resultMap = new HashMap<>(1);
		List<CmSupervisionPoint> pointList = this.cmSupervisionPointDao.queryAll(new CmSupervisionPoint());
		if (pointList != null && pointList.size() > 0) {
			for (CmSupervisionPoint point : pointList) {
				List<SupervisionPointInformation> list = resultMap.get(point.getDeptId());
				if (list == null) {
					list = new ArrayList<>();
				}
				CmSupervisionPointColumn columnParam = new CmSupervisionPointColumn();
				columnParam.setSupervisionId(point.getId());
				columnParam.addOrderByField(" t.queue ");
				try {
					if (StringUtil.isEmpty(point.getJsContent()) || StringUtil.isEmpty(point.getFunctionName())) {
						continue;
					}
					List<CmSupervisionPointColumn> columnList = this.cmSupervisionPointColumnService.queryAll(columnParam);
					if (columnList == null || columnList.size() == 0) {
						continue;
					}
					SupervisionPointInformation information = SupervisionPointInformation.create(point.getId(), point.getPointName(), point.getDeptId(),
							point.getPointDesc(), point.getFunctionName(), point.getErrorText(), point.getJsContent());
					information.setColumnList(columnList);
					list.add(information);
					resultMap.put(point.getDeptId(), list);
				} catch (CustomException e) {
					e.printStackTrace();
					continue;
				}
			}
		}
		return resultMap;
	}

	@Override
	public Result checkJs(CmSupervisionPoint entity, CmProjectInfo project, List<CmSupervisionPointColumn> columnList) {
		SupervisionPointInformation information = SupervisionPointInformation.create(entity.getId(), entity.getPointName(), entity.getDeptId(),
				entity.getPointDesc(), entity.getFunctionName(), entity.getErrorText(), entity.getJsContent());
		information.setColumnList(columnList);
		return this.cmSupervisionWarningService.checkScan(information, project.getProjectNumber());
	}
}
