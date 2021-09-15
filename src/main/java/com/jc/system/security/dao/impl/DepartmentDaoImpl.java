package com.jc.system.security.dao.impl;

import java.util.List;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;
import com.jc.foundation.dao.impl.BaseServerDaoImpl;
import com.jc.foundation.domain.PageManager;
import com.jc.system.security.dao.IDepartmentDao;
import com.jc.system.security.domain.Department;

/**
 * 组织dao
 * @author Administrator
 * @date 2020-06-29
 */
@Repository
public class DepartmentDaoImpl extends BaseServerDaoImpl<Department> implements IDepartmentDao {
	//查询记录总数
	public static final String SQL_SEARCH_QUERY_COUNT= "searchQueryCount";
	//查询
	public static final String SQL_SEARCH_QUERY = "searchQuery";
	//查询部门树
	public static final String SQL_QUERY_TREE = "queryTree";
	//获取部门信息
	public static final String SQL_QUERY_GET = "queryOne";
	//获取部门节点
	public static final String SQL_QUERY_ALL_BY_DEPTID = "queryAllByDeptId";
	//根据部门Id集合删除
	public static final String SQL_UPDATE_BY_DEPTIDS = "updateByDeptIds";
	//根据部门ID获取部门全部信息
	public static final String SQL_QUERY_BY_DEPTID = "queryByDeptId";
	//根据部门ID获取部门第一层的信息
	public static final String SQL_QUERY_BY_ID = "queryById";
	//根据组织ID获取部门第一层的信息
	public static final String SQL_QUERY_BY_ORG_ID = "queryByOrgId";
	//根据组织ID获取部门第一层的信息
	public static final String SQL_QUERY_DEPTPID_BY_ORG_ID = "queryDeptPidByOrgId";
	//查询管理员机构树
	public static final String SQL_QUERY_MANAGER_DEPTTREE = "queryManagerDeptTree";
	//查询管理员机构树
	public static final String SQL_QUERY_ALL_ORG_TREE = "queryAllOrgTree";
	//通过多人的用户ID串查询所在部门
	public static final String SQL_QUERY_DEPT_BY_USER_IDS = "queryDeptByUserIds";
	//通过父级ID查询同级所有部门
	public static final String SQL_QUERY_SAME_LEVEL_DEPT_BY_PARTMENT_ID = "querySameLevelDeptByParentId";
	
	@Override
    public PageManager searchQuery(Department department, PageManager page){
		PageManager rePage = new PageManager();
		Integer rowsCount = getTemplate().selectOne(getNameSpace(department) + "." + SQL_SEARCH_QUERY_COUNT, department);
		rePage.setTotalCount(rowsCount);
		Integer pageCount = rowsCount / page.getPageRows();
		if (rowsCount % page.getPageRows() > 0) {
			pageCount++;
		}
		if (page.getPage() + 1 > pageCount && pageCount != 0) {
			page.setPage(pageCount);
		}
		rePage.setPage(page.getPage() + 1);
		List<Department> list = getTemplate().selectList(getNameSpace(department) + "." + SQL_SEARCH_QUERY, department, new RowBounds((page.getPage()) * page.getPageRows(), page .getPageRows()));
		rePage.setTotalPages(pageCount);
		rePage.setData(list);
		rePage.setsEcho(page.getsEcho());
		return rePage;
	}

	@Override
    public List<Department> queryTree(Department department) {
		return getTemplate().selectList(getNameSpace(department) + "." + SQL_QUERY_TREE, department);
	}

	@Override
    public Department queryOne(Department department){
		return getTemplate().selectOne(getNameSpace(department) + "." + SQL_QUERY_GET, department);
	}

	@Override
    public List<Department> queryAllByDeptId(Department department) {
		return getTemplate().selectList(getNameSpace(department) + "." + SQL_QUERY_ALL_BY_DEPTID, department);
	}

	@Override
    public Integer updateByDeptIds(Department department) {
		return getTemplate().update(getNameSpace(department) + "." + SQL_UPDATE_BY_DEPTIDS, department);
	}

	@Override
    public List<Department> getDeptAndUserByDeptId(String id) {
		return getTemplate().selectList(getNameSpace(new Department()) + "." + SQL_QUERY_BY_DEPTID, id);
	}

	@Override
    public List<Department> getDeptByDeptId(String id) {
		return getTemplate().selectList(getNameSpace(new Department()) + "." + SQL_QUERY_BY_ID, id);
	}

	@Override
    public List<Department> getDeptByOrgId(String id) {
		return getTemplate().selectList(getNameSpace(new Department()) + "." + SQL_QUERY_BY_ORG_ID, id);
	}

	@Override
    public List<Department> getQueryDeptPidByOrgId(String id) {
		return getTemplate().selectList(getNameSpace(new Department()) + "." + SQL_QUERY_DEPTPID_BY_ORG_ID, id);
	}


	@Override
    public List<Department> queryManagerDeptTree(String userId) {
		return getTemplate().selectList(getNameSpace(new Department()) + "." + SQL_QUERY_MANAGER_DEPTTREE, userId);
	}

	@Override
    public List<Department> getAllOrgNoDeptTree() {
		return getTemplate().selectList(getNameSpace(new Department()) + "." + SQL_QUERY_ALL_ORG_TREE);
	}

	@Override
    public List<Department> getDeptByUserIds(Department dept) {
		return getTemplate().selectList(getNameSpace(new Department()) + "." + SQL_QUERY_DEPT_BY_USER_IDS, dept);
	}

	@Override
    public List<Department> getDeptByDeptIdForDel(String id) {
		return getTemplate().selectList(getNameSpace(new Department()) + ".queryTreebyId", id);
	}

	@Override
    public List<Department> getSameLevelDeptByParentId(String parentId) {
		return getTemplate().selectList(getNameSpace(new Department()) + "." + SQL_QUERY_SAME_LEVEL_DEPT_BY_PARTMENT_ID, parentId);
	}

	@Override
	public List<Department> getUserCountForAll() {
		return getTemplate().selectList(getNameSpace(new Department()) + ".queryUserCountForAll");
	}
}