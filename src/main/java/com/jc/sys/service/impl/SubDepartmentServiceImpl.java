package com.jc.sys.service.impl;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.util.MessageUtils;
import com.jc.sys.dao.ISubDepartmentDao;
import com.jc.sys.domain.*;
import com.jc.sys.service.ISubDepartmentRoleGroupService;
import com.jc.sys.service.ISubDepartmentService;
import com.jc.sys.service.ISubRoleService;
import com.jc.sys.service.ISubUserService;
import com.jc.system.security.domain.User;
import com.jc.system.security.util.UserUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
@Service
public class SubDepartmentServiceImpl extends BaseServiceImpl<SubDepartment> implements ISubDepartmentService{

	private static Logger logger = Logger.getLogger(SubDepartmentServiceImpl.class);

	private static final String LABELERRORMESSAGE = "labelErrorMessage";

	@Autowired
	private ISubUserService subUserService;

	@Autowired
	private ISubDepartmentRoleGroupService subDepartmentRoleGroupService;

	@Autowired
	private ISubRoleService subRoleService;
	
	private ISubDepartmentDao subDepartmentDao;
	
	public SubDepartmentServiceImpl(){}
	
	@Autowired
	public SubDepartmentServiceImpl(ISubDepartmentDao subDepartmentDao){
		super(subDepartmentDao);
		this.subDepartmentDao = subDepartmentDao;
	}

	/**
	* @description 根据主键删除多条记录方法
	* @param  subDepartment 实体类
	* @return Integer 处理结果
	* @author
	* @version  2018-04-04 
	*/
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	@Override
	public Map<String, Object> deleteByIds(SubDepartment subDepartment) throws CustomException{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try{
			List<SubDepartment> subDepartments = new ArrayList<>();
			String[] deptIds = subDepartment.getPrimaryKeys();
			String deptIdStr = "";
			for (String deptId:deptIds){
				List<SubDepartment> subDepartmentList = subDepartmentDao.getDeptsByDeptId(deptId);
				subDepartments.addAll(subDepartmentList);
			}

			String[] primaryKeys = new String[subDepartments.size()];
			int i=0;
			for (SubDepartment subDept:subDepartments){
				if ("".equals(deptIdStr)){
					deptIdStr = subDept.getId().toString();
				} else {
					deptIdStr = deptIdStr + ","+ subDept.getId().toString();
				}
				primaryKeys[i] = subDept.getId().toString();
				i++;
			}

			//判断是否有人员存在
			if (!"".equals(deptIdStr)){
				SubUser userInfo = new SubUser();
				userInfo.setDeptIds(deptIdStr);
				List<SubUser> userList =  subUserService.queryAll(userInfo);
				if(userList != null && userList.size() > 0){
					resultMap.put(LABELERRORMESSAGE, MessageUtils.getMessage("JC_SYS_115"));
					return resultMap;
				}
			}
			//判断是否有角色组存在
			if (!"".equals(deptIdStr)){
				SubDepartmentRoleGroup selSubDepartmentRoleGroup = new SubDepartmentRoleGroup();
				selSubDepartmentRoleGroup.setDeptIds(deptIdStr);
				List<SubDepartmentRoleGroup> roleList =  subDepartmentRoleGroupService.queryAll(selSubDepartmentRoleGroup);
				if(roleList != null && roleList.size() > 0){
					resultMap.put(LABELERRORMESSAGE, MessageUtils.getMessage("JC_SYS_114"));
					return resultMap;
				}
			}

			//判断是否有角色存在
			if (!"".equals(deptIdStr)){
				SubRole subRole = new SubRole();
				subRole.setDeptIds(deptIdStr);
				List<SubRole> subRoleList = subRoleService.queryAll(subRole);
				if(subRoleList != null && subRoleList.size() > 0){
					resultMap.put(LABELERRORMESSAGE, MessageUtils.getMessage("JC_SYS_114"));
					return resultMap;
				}
			}

			subDepartment.setPrimaryKeys(primaryKeys);
			propertyService.fillProperties(subDepartment,true);
			subDepartmentDao.delete(subDepartment,false);
			resultMap.put("success", "true");
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(subDepartment);
			throw ce;
		}
		return resultMap;
	}

	/**
	 * 查询部门信息(带上级部门)
	 * @param department
	 * @author
	 * @version 2014-04-15
	 */
	@Override
	public SubDepartment queryOne(SubDepartment department) {
		SubDepartment dept = subDepartmentDao.queryOne(department);
		if (dept.getLeaderId() != null){
			User user = UserUtils.getUser(dept.getLeaderId());
			if (user != null){
				dept.setDisplayName(user.getDisplayName());
			}
		}

		return dept;
	}

	@Override
	public List<SubDepartment> getDeptsByDeptId(String id){
		return subDepartmentDao.getDeptsByDeptId(id);
	}

	@Override
	public void deleteAll(SubDepartment department) {
		subDepartmentDao.deleteAll(department);
	}

	@Override
	public List queryManagerDeptRree(User user) {
		return filterManagerDeptRree(user);
	}

	private List filterManagerDeptRree(User userInfo){
		try {
			if(userInfo != null){
				//查询所有部门
				return pottingIsUser(userInfo);
			}
		} catch (CustomException e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public String getAllDeptAndUser(User user) throws Exception {
		List<SubDepartment> list = filterManagerDeptRree(user);
		SubUser subUser = new SubUser();
		subUser.setDeleteFlag(0);
		subUser.addOrderByField("t.ORDER_NO");
		List<SubUser> uList = subUserService.queryAll(subUser);
		return JsonUtil.java2Json(pottingAllDeptAndUser(list,uList));
	}

	private ArrayNode pottingAllDeptAndUser(List<SubDepartment> list, List<SubUser> uList){
		List<SubDepartment> newList = new ArrayList<SubDepartment>();
		if (list != null && list.size() > 0) {
			if (uList != null && uList.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					SubDepartment deptTmp = list.get(i);
					for (int j = 0; j < uList.size(); j++) {
						SubUser uTmp = uList.get(j);
						if (deptTmp.getId().equals(uTmp.getDeptId())) {
							SubUserBean uBean = new SubUserBean();
							uBean.setId(uTmp.getId());
							uBean.setDeptId(uTmp.getDeptId());
							uBean.setDisplayName(uTmp.getDisplayName());
							uBean.setOrderNo(uTmp.getOrderNo());
							uBean.setSubSystem(true);
							deptTmp.addUserBean(uBean);
							//deptTmp.addUser(uTmp);
						}
					}
					newList.add(deptTmp);
				}
			}
		}
		ArrayNode array = JsonUtil.createArrayNode();
		SubDepartment d = this.getSubDeptRoot();
		for (SubDepartment department : newList) {
			if (department.getParentId().equals(d.getParentId())) {
				ObjectNode obj = (ObjectNode) JsonUtil.createNode();
				obj.put("id", department.getId());
				obj.put("name", department.getName());
				obj.put("deptType", department.getDeptType());
				obj.put("parentId", department.getParentId());
				obj.putPOJO("user", department.getUserBeanList());
				recur(newList, department.getId(), obj);
				array.add(obj);
			}
		}
		return array;
	}

	private List pottingIsUser(User userInfo) throws CustomException{
		SubDepartment deptTemp = new SubDepartment();
		deptTemp.setDeleteFlag(0);
		List<SubDepartment> allDeptList = subDepartmentDao.queryAll(deptTemp);
		//返回结果LIST
		List<SubDepartment> deptList = new ArrayList<SubDepartment>();
		for(SubDepartment dept : allDeptList){
			dept.setIsChecked(dept.getIsChecked());
			dept.setUserType(2);
			deptList.add(dept);
		}
		return deptList;
	}

	/**
	 * 获取根节点数据
	 * @return
	 * @author 张继伟
	 * @version 1.0 2014年10月30日 上午11:39:17
	 */
	private SubDepartment getSubDeptRoot(){
		SubDepartment d = new SubDepartment();
		d.setParentId("0");
		return subDepartmentDao.get(d);
	}

	/**
	 * 私有方法-递归添加下级菜单
	 * @param departMentList 部门集合
	 * @param id 本身ID
	 * @param obj json对象
	 * @return
	 * @author 张继伟
	 * @version 1.0 2014年5月19日 上午10:27:22
	 */
	private ObjectNode recur(List<SubDepartment> departMentList, String id, ObjectNode obj) {
		ArrayNode array = JsonUtil.createArrayNode();
		for (int i = 0; i < departMentList.size(); i++) {
			if (departMentList.get(i).getParentId().equals(id)) {
				ObjectNode sonobj = (ObjectNode) JsonUtil.createNode();
				sonobj.put("id", departMentList.get(i).getId());
				sonobj.put("name", departMentList.get(i).getName());
				sonobj.put("deptType", departMentList.get(i).getDeptType());
				sonobj.put("parentId", departMentList.get(i).getParentId());
				sonobj.putPOJO("user", departMentList.get(i).getUserBeanList());
				recur(departMentList, departMentList.get(i).getId(), sonobj);
				array.add(sonobj);
			}
		}
		if (!array.isNull()) {
			obj.put("subDept", array);
		}
		return obj;
	}

}