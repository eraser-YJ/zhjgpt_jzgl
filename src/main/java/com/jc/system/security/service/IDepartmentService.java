package com.jc.system.security.service;

import java.util.List;
import java.util.Map;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.system.security.beans.DeptTree;
import com.jc.system.security.domain.Department;
import com.jc.system.security.domain.User;

/**
 * 组织机构部门service
 * @author Administrator
 * @date 2020-06-29
 */
public interface IDepartmentService extends IBaseService<Department> {
	
	/**
	 * 分页查询方法
	 * @param department 条件
	 * @param page 分页参数
	 * @return PagingBean 查询结果
	 */
	@Override
    PageManager query(Department department, PageManager page) ;

	/**
	 * 查询方法
	 * @param department: 条件
	 * @return 符合条件的列表
	 * @throws CustomException
	 */
	List<Department> query(Department department) throws CustomException;

	/**
	 * 根据主键删除多条记录方法
	 * @param department: 条件
	 * @return 成功或失败
	 * @throws CustomException
	 */
	Integer deleteByIds(Department department) throws CustomException;

	/**
	 * 保存方法
	 * @param department: 要保存的数据
	 * @return
	 * @throws CustomException
	 */
	@Override
    Integer save(Department department) throws CustomException;

	/**
	 * 修改方法
	 * @param department
	 * @return
	 * @throws CustomException
	 */
	@Override
    Integer update(Department department) throws CustomException;

	/**
	 * 分页查询方法
	 * @param department
	 * @param page
	 * @return
	 * @throws CustomException
	 */
	PageManager searchQuery(Department department, PageManager page) throws CustomException;

	/**
	 * 查询部门树(带部门负责人)
	 * @param department
	 * @return
	 * @throws CustomException
	 */
	List<Department> queryTree(Department department) throws CustomException;
	
	/**
	 * 查询机构树
	 * @param department
	 * @return
	 * @throws CustomException
	 */
	List<Department> queryOrgTree(Department department) throws CustomException;

	/**
	 * 查询管理员机构部门树
	 * @param userId
	 * @return
	 * @throws CustomException
	 */
	List<Department> queryManagerDeptTree(String userId) throws CustomException;

	/**
	 * 查询部门信息(带上级部门)
	 * @param department
	 * @return
	 * @throws CustomException
	 */
	Department queryOne(Department department) throws CustomException;

	/**
	 * 根据部门ID查询所有节点
	 * @param department
	 * @return
	 */
	List<Department> queryAllByDeptId(Department department) ;
	
	/**
	 * 根据部门Id集合删除
	 * @param department
	 * @return
	 * @throws CustomException
	 */
	Integer updateByDeptIds(Department department) throws CustomException;

	/**
	 * 根据部门ID获取本部门及所有子部门加人员信息
	 * @param id
	 * @return
	 */
	List<Department> getDeptAndUserByDeptId(String id) ;

	/**
	 * 逻辑删除部门-[删除关联关系adminSide]
	 * @param department
	 * @return
	 * @throws CustomException
	 */
	Map<String, Object> logicDeleteById(Department department) throws CustomException;
	
	/**
	 * 分页查询方法[用户查询]
	 * @param user
	 * @param page
	 * @return
	 */
	PageManager userManageList(User user, final PageManager page);

	/**
	 * 用户查询
	 * @return
	 */
	List<User> searchUserList();

	/**
	 * 获取全部部门及人员
	 * @return
	 * @throws Exception
	 */
	String getAllDeptAndUser() throws Exception ;

	/**
	 * 获取全部部门及人员,根据机构id
	 * @param orgId
	 * @return
	 * @throws Exception
	 */
	String getAllDeptAndUser(String orgId) throws Exception ;

	/**
	 * 获取在线用户信息
	 * @return
	 * @throws Exception
	 */
	String getDeptAndUserByOnLine() throws Exception ;

	/**
	 * 获取在线用户信息,根据机构id
	 * @param orgId
	 * @return
	 * @throws Exception
	 */
	String getDeptAndUserByOnLine(String orgId) throws Exception ;

	/**
	 * 获取职务人员
	 * @return
	 * @throws Exception
	 */
	String getPostAndUser() throws Exception ;

	/**
	 * 获取职务人员,根据机构id
	 * @param orgId
	 * @return
	 * @throws Exception
	 */
	String getPostAndUser(String orgId) throws Exception ;

	/**
	 * 获取个人组别
	 * @return
	 */
	String getPersonGroupAndUser();

	/**
	 * 获取公共组别
	 * @return
	 * @throws Exception
	 */
	String getPublicGroupAndUser();

	/**
	 * 清除部门人员的缓存信息
	 */
	void clearDeptAndUserCache() ;

	/**
	 * 根据登录用户与角色获取组织
	 * @return
	 * @throws Exception
	 */
	List<Department> getDepartmentByUserAndRole() throws Exception;

	/**
	 * 根据部门ID查找所在
	 * @return
	 * @throws Exception
	 */
	String getDeptIdsByOrgId() throws Exception;

	/**
	 * 根据部门ID获取本部门及所有子部门
	 * @param id
	 * @return
	 */
	List<Department> getDeptByDeptId(String id);

	/**
	 * 获取所有组织并存入缓存
	 */
	void getAllDepts();

	/**
	 * 缓存查询单个组织
	 * @param dept
	 * @return
	 */
	Department getDeptById(Department dept);

	/**
	 * 根据登录人所在机构查询组织树
	 * @return
	 * @throws CustomException
	 */
	List<Department> getOrgTree() throws CustomException;

	/**
	 * 根据登录人所在机构查询组织树
	 * @param orgId
	 * @return
	 * @throws CustomException
	 */
	List<Department> getOrgTree(String orgId) throws CustomException;

	/**
	 * 查询整个机构组织树不包含部门
	 * @return
	 */
	List<Department> getAllOrgNoDeptTree();

	/**
	 * 根据登录人所在机构查询组织树与人员
	 * @return
	 * @throws CustomException
	 */
	List<Department> getOrgAndPersonTree() throws CustomException;

	/**
	 * 根据登录人所在机构查询组织树与人员
	 * @param orgId
	 * @return
	 * @throws CustomException
	 */
	List<Department> getOrgAndPersonTree(String orgId) throws CustomException;

	List<DeptTree> getOrgAndPersonDeptTree() throws CustomException;
	List<DeptTree> getOrgAndPersonDeptTree(String orgId) throws CustomException;

	/**
	 * 根据多人用户ID串获取所属部门
	 * @param dept
	 * @return
	 */
	List<Department> getDeptByUserIds(Department dept);

	/**
	 * 根据部门id获得部门领导
	 * @param deptId
	 * @return
	 */
	User getDeptLeader(String deptId);

	/**
	 * 根据用户id获取管理的机构列表
	 * @param userId
	 * @return
	 */
	List<Department> getManageOrgByUserId(String userId);

	/**
	 * 根据部门id查询本级及以上所有父级组织集合（包含机构和部门）
	 * @param deptId
	 * @return
	 */
	List<Department> getAllParentDeptAndBrotherDeptByDeptId(String deptId);

	/**
	 * 根据部门id查询本级及以下所有子级组织集合（包含机构和部门）
	 * @param deptId
	 * @return
	 */
	List<Department> getAllChildDeptAndBrohterDeptByDeptId(String deptId);

	/**
	 * 根据部门ID获取直属下级的部门数据
	 * @param id
	 * @return
	 */
	List<Department> getSubChildDeptAndUserByDeptId(String id);

	/**
	 * 根据部门ID查询本级部门所有人员集合（包含机构和部门）
	 * @param id
	 * @return
	 */
	List<Department> getSameLevelDeptAndUserByDeptId(String id);

	/**
	 * 根据部门id查询本级以下所有子级组织集合（包含机构和部门）
	 * @param deptId
	 * @return
	 * @throws NullPointerException
	 */
	List<Department> getAllChildDeptAndUserByDeptId(String deptId) throws NullPointerException;

	/**
	 * 根据部门id获取本部门及所有子部门下所有人员集合
	 * @param id
	 * @return
	 * @throws NullPointerException
	 */
	List<User> getAllUsersByDeptId(String id) throws NullPointerException;

	/**
	 * 根据部门id获取部门人员集合及直属下级的部门集合及部门所有人员个数
	 * @param id
	 * @return
	 * @throws NullPointerException
	 */
	Map<String, Object> getUserAndSubChildDeptByDeptId(String id) throws NullPointerException;

	/**
	 * 根据id获取对象
	 * @param id
	 * @return
	 */
	Department getById(String id);

	/**
	 * 根据code获取对象
	 * @param code
	 * @return
	 */
	Department getByCode(String code);
}