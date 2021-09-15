package com.jc.system.sys.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.jc.foundation.cache.CacheClient;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.util.MessageUtils;
import com.jc.system.security.domain.Department;
import com.jc.system.security.service.IDepartmentService;
import com.jc.system.sys.domain.PinReDepartment;
import com.jc.system.sys.util.Pinyin4jUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jc.foundation.exception.CustomException;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.foundation.service.impl.BaseServiceImpl;

import com.jc.system.sys.dao.IPinDepartmentDao;
import com.jc.system.sys.domain.PinDepartment;
import com.jc.system.sys.service.IPinDepartmentService;

/***
 * @author Administrator
 * @date 2020-07-01
 */
@Service
public class PinDepartmentServiceImpl extends BaseServiceImpl<PinDepartment> implements IPinDepartmentService{

	@Autowired
	private IDepartmentService departmentService;

	private IPinDepartmentDao pinDepartmentDao;

	public PinDepartmentServiceImpl(){}

	@Autowired
	public PinDepartmentServiceImpl(IPinDepartmentDao pinDepartmentDao){
		super(pinDepartmentDao);
		this.pinDepartmentDao = pinDepartmentDao;
	}

	@Override
    @Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(PinDepartment pinDepartment) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(pinDepartment,true);
			result = pinDepartmentDao.delete(pinDepartment);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(pinDepartment);
			throw ce;
		}
		return result;
	}

	@Override
	public List<PinReDepartment> queryPinDepartment(PinDepartment pinDepartment) throws CustomException {
		return pinDepartmentDao.queryPinDepartment(pinDepartment);
	}

	@Override
    @Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Map<String, Object> infoLoading() throws CustomException {
		Map<String, Object> resultMap = new HashMap<>();
		List<Department> allDeptList = departmentService.queryAll(new Department());
		if (allDeptList != null && allDeptList.size() > 0){
			List<PinDepartment> pinDepartmentList = new ArrayList<>();
			for(Department dept:allDeptList){
				PinDepartment pinDepartment = new PinDepartment();
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
			List<PinDepartment> delPinDepartments = pinDepartmentDao.queryAll(new PinDepartment());
			if(delPinDepartments != null && delPinDepartments.size() > 0){
				String delIds = "";
				for(PinDepartment delDepartment:delPinDepartments){
					if ("".equals(delIds)){
						delIds = String.valueOf(delDepartment.getId());
					}else {
						delIds = delIds + "," + String.valueOf(delDepartment.getId());
					}
				}
				PinDepartment delTemps = new PinDepartment();
				delTemps.setPrimaryKeys(delIds.split(","));
				pinDepartmentDao.delete(delTemps,false);
			}

			pinDepartmentDao.save(pinDepartmentList);
			CacheClient.removeCache("cache_department_info_pins");
			CacheClient.putCache("cache_department_info_pins", JsonUtil.java2Json(pinDepartmentList));
			resultMap.put("success", "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_001"));
		}
		return resultMap;
	}

}