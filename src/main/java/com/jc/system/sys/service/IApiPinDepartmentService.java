package com.jc.system.sys.service;

import com.jc.foundation.exception.CustomException;
import com.jc.system.security.domain.Department;

import java.util.List;

/***
 * @author Administrator
 * @date 2020-07-01
 */
public interface IApiPinDepartmentService {

    /**
     * 获取全部部门及人员
     * @param orgId
     * @param dutyId
     * @param tabType
     * @param spellType
     * @param search
     * @param weight
     * @return
     * @throws Exception
     */
    String getAllDeptAndUser(String orgId,String dutyId,String tabType,String spellType,String search,String weight) throws Exception ;

    /**
     * 通用弹出树检索功能组装树
     * @param search
     * @param weight
     * @return
     * @throws Exception
     */
    String getSelDeptAndUser(String search,String weight) throws Exception;

    /**
     * 根据条件获取人员
     * @param search
     * @param weight
     * @return
     * @throws Exception
     */
    String getSelUsers(String search,String weight) throws Exception;

    /**
     * 根据条件获取已删除部门的列表信息
     * @param id
     * @return
     * @throws CustomException
     */
    List<Department> getDeptByDeptIdForDel(String id) throws CustomException;
}
