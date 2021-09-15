package com.jc.pin.service;

/**
 * 拼音子部门service
 * @author Administrator
 * @date 2020-06-30
 */
public interface IApiPinSubDepartmentService {

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
    String getAllDeptAndUser(String orgId, String dutyId, String tabType, String spellType, String search, String weight) throws Exception ;

    /**
     * 通用弹出树检索功能组装树
     * @param search
     * @param weight
     * @return
     * @throws Exception
     */
    String getSelDeptAndUser(String search, String weight) throws Exception;

    /**
     * 根据条件获取人员
     * @param search
     * @param weight
     * @return
     * @throws Exception
     */
    String getSelUsers(String search, String weight) throws Exception;

}
