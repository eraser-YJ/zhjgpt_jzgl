package com.jc.system.security.util;

import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.security.domain.Role;
import com.jc.system.security.service.IRoleService;

/**
 * 角色缓存
 * @author Administrator
 * @date 2020-06-30
 */
public class RoleCacheUtil {

    private RoleCacheUtil() {
        throw new IllegalStateException("RoleCacheUtil class");
    }

    private static IRoleService roleService = SpringContextHolder.getBean(IRoleService.class);

    public static Role getRole(String id) {
        Role role = new Role();
        if(id != null){
            role.setId(id);
            role.setDeleteFlag(null);
            return roleService.getRoleById(role);
        }
        return null;
    }

    public static String getRolesbyUserId(String userId){
        return roleService.getRolesByUserId(userId);
    }
}
