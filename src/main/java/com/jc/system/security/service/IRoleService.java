package com.jc.system.security.service;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.system.security.domain.Role;
import com.jc.system.security.domain.RoleBlocks;
import com.jc.system.security.domain.RoleExts;
import com.jc.system.security.domain.RoleMenus;
import java.util.List;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface IRoleService extends IBaseService<Role>{

    /**
     * 保存角色-菜单关联数据
     * @param role
     * @throws CustomException
     */
    void saveRoleMenu(Role role) throws CustomException;

    /**
     * 根据角色获得已选中菜单
     * @param roleMenus
     * @return
     * @throws CustomException
     */
    List<RoleMenus> getMenusByRole(RoleMenus roleMenus) throws CustomException;

    /**
     * 根据角色获得权限信息
     * @param roleExts
     * @return
     * @throws CustomException
     */
    List<RoleExts> getExtsByRole(RoleExts roleExts) throws CustomException;

    /**
     * 根据角色获得已选中部门
     * @param roleBlocks
     * @return
     * @throws CustomException
     */
    List<RoleBlocks> getBlocksByRole(RoleBlocks roleBlocks) throws CustomException;

    /**
     * 获得部门及部门下所有角色集合
     * @return
     * @throws Exception
     */
    ArrayNode getAllDeptAndRole() throws Exception;

    /**
     * 根据角色获得配置部门集合
     * @param roles
     * @return
     * @throws CustomException
     */
    List<RoleBlocks> getAllDeptWithRoles(String[] roles) throws CustomException;

    /**
     * 根据角色获得配置部门集合(数据权限)
     * @param roles
     * @return
     * @throws CustomException
     */
    List<RoleBlocks> getAllDeptWithRolesPermission(String[] roles) throws CustomException;

    /**
     * 根据部门id获得与之相关的集合
     * @param id
     * @return
     * @throws CustomException
     */
    List<RoleBlocks> getDeptRelation(String id) throws CustomException;

    /**
     * 用户管理角色列表
     * @param role
     * @return
     * @throws CustomException
     */
    List<Role> getRolesForUser(Role role) throws CustomException;

    /**
     * 根据角色获得配置部门集合(人员选择树使用)
     * @param roles
     * @return
     * @throws CustomException
     */
    List<RoleBlocks> getAllDeptWithRolesSelect(String[] roles) throws CustomException;

    /**
     * @description缓存所有角色信息
     */
    void getAllRoles();

    /**
     * 获取所有角色集合
     * @return
     */
    List<Role> getAllRole();

    /**
     * 获取角色缓存信息
     * @param role
     * @return
     */
    Role getRoleById(Role role);

    /**
     * 根据用户信息获取角色Id信息
     * @param userId
     * @return
     */
    String getRolesByUserId(String userId);

    /**
     * 根据部门名称或角色名称模糊查询
     * @param role
     * @return
     */
    List<Role> getRolesByRoleOrDept(Role role);

    /**
     * 根据角色获得制度查询配置部门集合(数据权限)
     * @param roleId
     * @return
     * @throws CustomException
     */
    List<RoleBlocks> getAllDeptWithRolesForRegimeQuery(String[] roleId) throws CustomException;

    /**
     * 根据用户id及菜单id获取角色列表
     * @param role
     * @return
     */
    List<Role> getRolesByUserIdAndMenuId(Role role);

    List<Role> getWeight(Role role);
}