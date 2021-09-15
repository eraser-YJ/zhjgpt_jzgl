package com.jc.pin.service.impl;

import com.jc.foundation.cache.CacheClient;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.util.MessageUtils;
import com.jc.pin.dao.IPinSubDepartmentDao;
import com.jc.pin.domain.PinReSubDepartment;
import com.jc.pin.domain.PinSubDepartment;
import com.jc.pin.service.IPinSubDepartmentService;
import com.jc.sys.domain.SubDepartment;
import com.jc.sys.service.ISubDepartmentService;
import com.jc.system.sys.util.Pinyin4jUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 部门拼音serviceImpl
 * @author Administrator
 * @date 2020-06-30
 */
@Service
public class PinSubDepartmentServiceImpl extends BaseServiceImpl<PinSubDepartment> implements IPinSubDepartmentService{

	@Autowired
	private ISubDepartmentService departmentService;
	
	private IPinSubDepartmentDao pinSubDepartmentDao;
	
	public PinSubDepartmentServiceImpl(){}
	
	@Autowired
	public PinSubDepartmentServiceImpl(IPinSubDepartmentDao pinSubDepartmentDao){
		super(pinSubDepartmentDao);
		this.pinSubDepartmentDao = pinSubDepartmentDao;
	}
	
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	@Override
	public Integer deleteByIds(PinSubDepartment pinSubDepartment) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(pinSubDepartment,true);
			result = pinSubDepartmentDao.delete(pinSubDepartment);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(pinSubDepartment);
			throw ce;
		}
		return result;
	}

	@Override
	public List<PinReSubDepartment> queryPinDepartment(PinSubDepartment pinDepartment) throws CustomException {
		return pinSubDepartmentDao.queryPinDepartment(pinDepartment);
	}

	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	@Override
	public Map<String, Object> infoLoading() throws CustomException {
		Map<String, Object> resultMap = new HashMap<>();
		List<SubDepartment> allDeptList = departmentService.queryAll(new SubDepartment());
		if (allDeptList != null && allDeptList.size() > 0){
			List<PinSubDepartment> pinDepartmentList = new ArrayList<>();
			for(SubDepartment dept:allDeptList){
				PinSubDepartment pinDepartment = new PinSubDepartment();
				String deptName = dept.getName();
				if(deptName != null && !"".equals(deptName.trim())) {
					StringBuffer sb = new StringBuffer();
					char[] aa = deptName.toCharArray();
					for (int i = 0; i < aa.length; i++) {
						char c = aa[i];
						if (Pattern.matches("[\u4e00-\u9fa5]", String.valueOf(c))) {
							sb.append(c);
						}
					}

					String abbinit = dept.getName().substring(0,1);
					pinDepartment.setDeptId(dept.getId());
					pinDepartment.setDeptName(dept.getName());
					if (Pattern.matches("[a-zA-Z]+", String.valueOf(abbinit))){
						pinDepartment.setDeptAbbreviate(abbinit);
						pinDepartment.setDeptInitials(abbinit);
						pinDepartment.setDeptFull(dept.getName());
					} else if(Pattern.matches("[\u4e00-\u9fa5]", String.valueOf(abbinit))){
						deptName = sb.toString();
						if (deptName != null && !"".equals(deptName)) {
							String abbreviate = Pinyin4jUtil.converterToFirstSpell(deptName);
							String userFull = Pinyin4jUtil.converterToSpell(deptName);
							pinDepartment.setDeptAbbreviate(abbreviate);
							pinDepartment.setDeptInitials(abbreviate.substring(0, 1));
							pinDepartment.setDeptFull(userFull);
						}
					} else {
						pinDepartment.setDeptAbbreviate("#");
						pinDepartment.setDeptInitials("#");
						pinDepartment.setDeptFull(dept.getName());
					}
					propertyService.fillProperties(pinDepartment,false);
				}
				pinDepartmentList.add(pinDepartment);
			}
			List<PinSubDepartment> delPinDepartments = pinSubDepartmentDao.queryAll(new PinSubDepartment());
			if(delPinDepartments != null && delPinDepartments.size() > 0){
				String delIds = "";
				for(PinSubDepartment delDepartment:delPinDepartments){
					if ("".equals(delIds)){
						delIds = String.valueOf(delDepartment.getId());
					}else {
						delIds = delIds + "," + String.valueOf(delDepartment.getId());
					}
				}
				PinSubDepartment delTemps = new PinSubDepartment();
				delTemps.setPrimaryKeys(delIds.split(","));
				pinSubDepartmentDao.delete(delTemps,false);
			}

			pinSubDepartmentDao.save(pinDepartmentList);
			CacheClient.removeCache("cache_sub_department_info_pins");
			CacheClient.putCache("cache_sub_department_info_pins", JsonUtil.java2Json(pinDepartmentList));
			resultMap.put("success", "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_001"));
		} else {
			resultMap.put("success", "false");
		}
		return resultMap;
	}

}