package com.jc.system.security.service.impl;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jc.foundation.cache.CacheClient;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.exception.DBException;
import com.jc.foundation.exception.SystemException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.JsonUtil;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.dao.IRoleDao;
import com.jc.system.security.domain.*;
import com.jc.system.security.service.*;
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
 * @author Administrator
 * @date 2020-07-01
 */
@Service
public class RoleServiceImpl extends BaseServiceImpl<Role> implements IRoleService {

    protected transient final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    public RoleServiceImpl(IRoleDao dao) {
        super(dao);
        this.roleDao = dao;
    }

    public RoleServiceImpl(){
        
    }
    
    private IRoleDao roleDao;
    @Autowired
    private IDepartmentService departmentService;
    @Autowired
    private IRoleMenusService roleMenuService;
    @Autowired
    private IRoleBlocksService roleBlocksService;
    @Autowired
    private IRoleExtsService roleExtService;
    @Autowired
    private ISysUserRoleService sysUserRoleService;
    @Autowired
    private IAdminSideService adminSideService;
    public static final String CACHE_ROLE_INFO = "role_info_";

    @Override
    public PageManager query(Role role, PageManager page) {
        return super.query(role, page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED,timeout = 300)
    public void saveRoleMenu(Role role) throws CustomException {
        RoleMenus rMenu = new RoleMenus();
        RoleBlocks rBlock = new RoleBlocks();
        SysUserRole rUser = new SysUserRole();
        RoleExts rExt = new RoleExts();
        if (role.getPrimaryKeys()!=null && role.getPrimaryKeys().length > 0 ) {
            rMenu.setPrimaryKeys(role.getPrimaryKeys());
            rBlock.setPrimaryKeys(role.getPrimaryKeys());
            rUser.setRoleIds(role.getPrimaryKeys());
            rExt.setPrimaryKeys(role.getPrimaryKeys());
        } else {
            rMenu.setPrimaryKeys(role.getId().split("&"));
            rBlock.setPrimaryKeys(role.getId().split("&"));
            rUser.setRoleIds(role.getId().split("&"));
            rExt.setPrimaryKeys(role.getId().split("&"));
        }
        try {
            roleMenuService.delete(rMenu, false);
            roleBlocksService.delete(rBlock,false);
            sysUserRoleService.delete(rUser, false);
            roleExtService.delete(rExt,false);
            List<RoleMenus> roleMenus = role.getRoleMenus();
            List<RoleBlocks> roleBlocks = role.getRoleBlocks();
            List<SysUserRole> roleUsers = role.getSysUserRoles();
            List<RoleExts> roleExts = role.getRoleExts();
            roleMenuService.saveList(roleMenus);
            roleBlocksService.saveList(roleBlocks);
            sysUserRoleService.saveList(roleUsers);
            roleExtService.saveList(roleExts);
        } catch(Exception e) {
            SystemException ce = new SystemException(e);
            throw ce;
        }
    }

    /**
     * 根据角色获得已选中菜单
     * @param roleMenus
     * @return
     * @throws CustomException
     */
    @Override
    public List<RoleMenus> getMenusByRole(RoleMenus roleMenus) throws CustomException  {
        return roleMenuService.queryAll(roleMenus);
    }

    /**
     * 根据角色获得已选中权限信息
     * @param roleExts
     * @return
     * @throws CustomException
     */
    @Override
    public List<RoleExts> getExtsByRole(RoleExts roleExts) throws CustomException  {
        return roleExtService.queryAll(roleExts);
    }

    /**
     * 根据角色获得已选中部门
     * @param roleBlcoks
     * @return
     * @throws CustomException
     */
    @Override
    public List<RoleBlocks> getBlocksByRole(RoleBlocks roleBlcoks) throws CustomException {
        return roleBlocksService.queryAll(roleBlcoks);
    }

    /**
     * 删除角色及相关关联表信息
     * @param role
     * @return
     * @throws CustomException
     */
    @Override
    public Integer delete(Role role) throws CustomException {
        CacheClient.removeCache(CACHE_ROLE_INFO + role.getId());
        propertyService.fillProperties(role,false);
        try {
            if (role.getPrimaryKeys() != null && role.getPrimaryKeys().length > 0 ) {
                RoleMenus rMenu = new RoleMenus();
                RoleBlocks rBlock = new RoleBlocks();
                SysUserRole rUser = new SysUserRole();
                RoleExts rExts = new RoleExts();
                rMenu.setPrimaryKeys(role.getPrimaryKeys());
                rBlock.setPrimaryKeys(role.getPrimaryKeys());
                rUser.setRoleIds(role.getPrimaryKeys());
                rExts.setPrimaryKeys(role.getPrimaryKeys());
                roleMenuService.delete(rMenu, false);
                roleBlocksService.delete(rBlock,false);
                sysUserRoleService.delete(rUser, false);
                roleExtService.delete(rExts,false);
            }
            return roleDao.delete(role);
        } catch (DBException e) {
            SystemException ce = new SystemException(e);
            throw ce;
        }
    }

    /**
     * 获得部门及部门下所有角色集合
     * @return
     * @throws Exception
     */
    @Override
    public ArrayNode getAllDeptAndRole() throws Exception {
        List<Department> newList = new ArrayList<>();
        Department dept = new Department();
        dept.setDeleteFlag(0);
        List<Department> list = departmentService.getDepartmentByUserAndRole();
        String deptIds = "";
        List<AdminSide> asList = new ArrayList<>();
        User userInfo = SystemSecurityUtils.getUser();
        if (userInfo != null) {
            //管理员
            if (userInfo.getIsAdmin() == 1) {
                AdminSide adminSide = new AdminSide();
                adminSide.setUserId(userInfo.getId());
                asList = adminSideService.queryManagerDeptRree(adminSide);
            }
        }
        for (AdminSide adminSide : asList) {
            if (adminSide.getDeptType() == 1 && "1".equals(adminSide.getIsChecked())) {
                List<Department> deptsList = departmentService.getDeptAndUserByDeptId(adminSide.getId());
                for (Department depart : deptsList) {
                    deptIds += depart.getId() + ",";
                }
            }
        }
        deptIds = deptIds.length() > 0 ? deptIds.substring(0, deptIds.length() - 1) : null;
        Role role = new Role();
        role.setDeleteFlag(0);
        role.setDeptIds(deptIds);
        List<Role> uList = roleDao.queryAll(role);
        if (list != null && list.size() > 0) {
            if (uList != null && uList.size() > 0) {
                for (int i = 0; i < list.size(); i++){
                    Department deptTmp = list.get(i);
                    for (int j = 0; j < uList.size(); j++) {
                        Role uTmp = uList.get(j);
                        if (deptTmp.getId().equals(uTmp.getDeptId())) {
                            deptTmp.addRole(uTmp);
                        }
                    }
                    newList.add(deptTmp);
                }
            }
        }
        ArrayNode array = JsonUtil.createArrayNode();
        for (Department department : newList) {
            if (department.getParentId().equals("0")) {
                ObjectNode obj = (ObjectNode) JsonUtil.createNode();
                obj.put("id", department.getId());
                obj.put("name",department.getName());
                obj.put("deptType",department.getDeptType());
                obj.put("parentId",department.getParentId());
                obj.putPOJO("role",department.getRoles());
                recur(newList, department.getId(), obj);
                array.add(obj);
            }
        }
        return array;
    }

    /**
     * 递归部门层次关系
     * @param departMentList
     * @param id
     * @param obj
     * @return
     */
    private static ObjectNode recur(List<Department> departMentList, String id, ObjectNode obj){
        ArrayNode array = JsonUtil.createArrayNode();
        for(int i = 0 ; i < departMentList.size(); i++){
            if(departMentList.get(i).getParentId().equals(id)){
                ObjectNode sonobj = (ObjectNode) JsonUtil.createNode();
                sonobj.put("id", departMentList.get(i).getId());
                sonobj.put("name", departMentList.get(i).getName());
                sonobj.put("deptType", departMentList.get(i).getDeptType());
                sonobj.put("parentId", departMentList.get(i).getParentId());
                sonobj.putPOJO("role", departMentList.get(i).getRoles());
                recur(departMentList, departMentList.get(i).getId(), sonobj);
                array.add(sonobj);
            }
        }
        if(!array.isNull()){
            obj.put("subDept", array);
        }
        return obj;
    }

    /**
     * 根据角色获得配置部门集合
     * @param roles
     * @return
     * @throws CustomException
     */
    @Override
    public List<RoleBlocks> getAllDeptWithRoles(String[] roles) throws CustomException {
        List<RoleBlocks> deptList = new ArrayList<>();
        List<Department> departList = departmentService.queryAll(new Department());
        for(String roleId : roles){
            RoleExts rExts = new RoleExts();
            rExts.setRoleId(roleId);
            List<RoleExts> extsList = roleExtService.queryAll(rExts);
            for(RoleExts ext : extsList){
                switch(ext.getPermissionType()){
                    case 1:{
                        deptList.addAll(this.getDeptRelation(SystemSecurityUtils.getUser().getOrgId(),departList));
                        break;
                    }
                    case 2:{
                        deptList.addAll(this.getDeptRelation(SystemSecurityUtils.getUser().getDeptId(),departList));
                        break;
                    }
                    case 3:{
                        deptList.addAll(this.getDeptRelation(SystemSecurityUtils.getUser().getOrgId(),departList));
                        break;
                    }
                    case 4:{
                        RoleBlocks rBlock = new RoleBlocks();
                        rBlock.setRoleId(roleId);
                        List<RoleBlocks> roleBlockList = roleBlocksService.queryAll(rBlock);
                        deptList.addAll(roleBlockList);
                        break;
                    }
                    default:{
                        break;
                    }
                }
            }
        }
        return removalDept(deptList);
    }

    /**
     * 根据角色获得配置部门集合(数据权限使用)
     * @param roles
     * @return
     * @throws CustomException
     */
    @Override
    public List<RoleBlocks> getAllDeptWithRolesPermission(String[] roles) throws CustomException {
        List<RoleBlocks> deptList = new ArrayList<>();
        for(String roleId : roles){
            RoleExts rExts = new RoleExts();
            rExts.setRoleId(roleId);
            List<RoleExts> extsList = roleExtService.queryAll(rExts);
            for(RoleExts ext : extsList){
                switch(ext.getPermissionType()){
                    case 1:{
                        RoleBlocks rBlock = new RoleBlocks();
                        rBlock.setDeptId(SystemSecurityUtils.getUser().getId());
                        rBlock.setFlag(false);
                        rBlock.setIsChecked("0");
                        deptList.add(rBlock);
                        break;
                    }
                    case 2:{
                        RoleBlocks rBlock = new RoleBlocks();
                        rBlock.setDeptId(SystemSecurityUtils.getUser().getDeptId());
                        rBlock.setIsChecked("1");
                        deptList.add(rBlock);
                        break;
                    }
                    case 3:{
                        RoleBlocks rBlock = new RoleBlocks();
                        rBlock.setDeptId(SystemSecurityUtils.getUser().getOrgId());
                        rBlock.setIsChecked("1");
                        deptList.add(rBlock);
                        break;
                    }
                    case 4:{
                        RoleBlocks rBlock = new RoleBlocks();
                        rBlock.setRoleId(roleId);
                        rBlock.setIsChecked("1");
                        List<RoleBlocks> roleBlockList = roleBlocksService.queryAll(rBlock);
                        deptList.addAll(roleBlockList);
                        break;
                    }
                    default:{
                        break;
                    }
                }
            }
        }
        return removalDept(deptList);
    }

    /**
     * 去掉部门集合中的重复信息
     * @param deptList
     * @return
     */
    public List<RoleBlocks> removalDept(List<RoleBlocks> deptList){
        List<RoleBlocks> blocksList = new ArrayList<>();
        for(RoleBlocks block : deptList){
            if(!blocksList.contains(block)){
                blocksList.add(block);
            }else{
                for(RoleBlocks changeBlock : blocksList){
                    if(changeBlock.getDeptId().equals(block.getDeptId())&& "0".equals(changeBlock.getIsChecked()) && "1".equals(block.getIsChecked())){
                        changeBlock.setIsChecked("1");
                    }
                }
            }
        }
        return blocksList;
    }

    private List<RoleBlocks> getDeptRelation(String id,List<Department> departList) throws CustomException{
        Map<String,String> departS = new HashMap<>();
        List<RoleBlocks> rBlocksList = new ArrayList<>();
        for(Department depart : departList){
            departS.put(depart.getId(),depart.getParentId());
        }
        RoleBlocks rBlock = new RoleBlocks();
        rBlock.setIsChecked("1");
        rBlock.setDeptId(id);
        rBlocksList.add(rBlock);
        this.recurDeptRelation(id,rBlocksList,departS);
        return rBlocksList;
    }

    private void recurDeptRelation(String id,List<RoleBlocks> rBlocksList,Map<String,String> departS){
    	String pId = departS.get(id);
        if("0".equals(pId)){return;}
        RoleBlocks rBlock = new RoleBlocks();
        rBlock.setIsChecked("0");
        rBlock.setDeptId(pId);
        rBlocksList.add(rBlock);
        this.recurDeptRelation(pId, rBlocksList,departS);
    }

    /**
     * 根据部门id获得与之相关的集合
     */
    Map<String,String> departMap = new HashMap<>();
    
    @Override
    public List<RoleBlocks> getDeptRelation(String id) throws CustomException{
        departMap = new HashMap<>();
        List<RoleBlocks> rBlocksList = new ArrayList<>();
        List<Department> departList = departmentService.queryAll(new Department());
        for(Department depart : departList){
            departMap.put(depart.getId(),depart.getParentId());
        }
        RoleBlocks rBlock = new RoleBlocks();
        rBlock.setIsChecked("1");
        rBlock.setDeptId(id);
        rBlocksList.add(rBlock);
        this.recurDeptRelation(id,rBlocksList);
        return rBlocksList;
    }

    public void recurDeptRelation(String id,List<RoleBlocks> rBlocksList){
    	String pId = departMap.get(id);
        if("0".equals(pId)) {
            return;
        }
        RoleBlocks rBlock = new RoleBlocks();
        rBlock.setIsChecked("0");
        rBlock.setDeptId(pId);
        rBlocksList.add(rBlock);
        this.recurDeptRelation(pId, rBlocksList);
    }

    /**
     * 根据部门id获得与之相关的集合(数据权限使用)
     */
    Map<String,String> departMapForPermission = new HashMap<>();
    public List<RoleBlocks> getDeptRelationForPermisssion(String id,String deptType) throws CustomException{
        departMapForPermission = new HashMap<>();
        List<RoleBlocks> rBlocksList = new ArrayList<>();
        List<Department> departList = departmentService.queryAll(new Department());
        for (Department depart : departList) {
            departMapForPermission.put(depart.getId(), depart.getParentId() + "&&" + depart.getDeptType());
        }
        RoleBlocks rBlock = new RoleBlocks();
        rBlock.setIsChecked("1");
        rBlock.setDeptId(id);
        rBlocksList.add(rBlock);
        this.recurDeptRelationForPermisssion(id,rBlocksList,deptType);
        return rBlocksList;
    }

    public void recurDeptRelationForPermisssion(String id,List<RoleBlocks> rBlocksList,String deptType){
        String pIdAndDeptType = departMapForPermission.get(id);
        String pId = pIdAndDeptType.split("&&")[0];
        String deptTypeT = pIdAndDeptType.split("&&")[1];
        if("0".equals(pId) || ("1".equals(deptTypeT) && "1".equals(deptType)) ) {
            return;
        }
        RoleBlocks rBlock = new RoleBlocks();
        rBlock.setIsChecked("0");
        rBlock.setDeptId(pId);
        rBlocksList.add(rBlock);
        this.recurDeptRelationForPermisssion(pId, rBlocksList,deptTypeT);
    }

    @Override
    public List<RoleBlocks> getAllDeptWithRolesSelect(String[] roles) throws CustomException {
        List<RoleBlocks> deptList = new ArrayList<>();
        for(String roleId : roles){
            RoleExts rExts = new RoleExts();
            rExts.setRoleId(roleId);
            List<RoleExts> extsList = roleExtService.queryAll(rExts);
            for(RoleExts ext : extsList){
                switch(ext.getPermissionType()){
                    case 1:{ break; }
                    case 2:{ break; }
                    case 3:{ break; }
                    case 4:{
                        RoleBlocks rBlock = new RoleBlocks();
                        rBlock.setRoleId(roleId);
                        rBlock.setIsChecked("1");
                        List<RoleBlocks> roleBlockList = roleBlocksService.queryAll(rBlock);
                        deptList.addAll(roleBlockList);
                        break;
                    }
                    default:{ break; }
                }
            }
        }
        return removalDept(deptList);
    }

    @Override
    public void getAllRoles() {
        try {
            List<Role> roleList = roleDao.getRolesForUser(new Role());
            for (Role role : roleList) {
                CacheClient.putCache(CACHE_ROLE_INFO + role.getId(), JsonUtil.java2Json(role));
            }
        } catch (CustomException e) {
            logger.error(e,e);
        }
    }

    @Override
    public Role getRoleById(Role role) {
        Role u = (Role) JsonUtil.json2Java(CacheClient.getCache(CACHE_ROLE_INFO + role.getId()), Role.class);
        if(u == null){
            try {
                u = roleDao.getRoleById(role);
            } catch (CustomException e) {
                logger.error(e);
            }
            CacheClient.putCache(CACHE_ROLE_INFO + role.getId(), JsonUtil.java2Json(u));
        }
        return u;
    }

    @Override
    public String getRolesByUserId(String userId) {
        Role inRole = new Role();
        inRole.setUserId(userId);
        List<Role> roleList = roleDao.getRolesByUserId(inRole);
        String roles = "";
        if (roleList != null && roleList.size()>0){
            for (Role role:roleList){
                roles = role.getId() + "," + roles;
            }
            roles = roles.substring(0,roles.length()-1);
        } else {
            roles = "-1";
        }
        return roles;
    }

    @Override
    public List<Role> getRolesByRoleOrDept(Role role) {
        return roleDao.getRolesByRoleOrDept(role);
    }

    /**
     * 用户管理角色列表
     * @param role
     * @return
     * @throws CustomException
     */
    @Override
    public List<Role> getRolesForUser(Role role) throws CustomException {
        return roleDao.getRolesForUser(role);
    }

    /**
     * 根据角色获得制度查询配置部门集合
     * @param roles
     * @return
     * @throws CustomException
     */
    @Override
    public List<RoleBlocks>	getAllDeptWithRolesForRegimeQuery(String[] roles) throws CustomException {
        List<RoleBlocks> deptList = new ArrayList<>();
        //根据当前访问菜单id获取关联角色id
        String roleids = "";
        if(SystemSecurityUtils.getSession().getAttribute("powerDivisionSign") != null){
            String menuId = SystemSecurityUtils.getSession().getAttribute("powerDivisionSign").toString();
            if(!"".equals(menuId)){
                RoleMenus roleMenus = new RoleMenus();
                roleMenus.setMenuId(menuId);
                List<RoleMenus> roleMenulist =getMenusByRole(roleMenus);
                if(roleMenulist != null && roleMenulist.size() > 0) {
                    for(RoleMenus rolemenu : roleMenulist){
                        roleids = roleids + ","+rolemenu.getRoleId();
                    }
                    roleids = roleids+",";
                }
            }
        }
        for(String roleId : roles){
            RoleExts rExts = new RoleExts();
            rExts.setRoleId(roleId);
            List<RoleExts> extsList = roleExtService.queryAll(rExts);
            for(RoleExts ext : extsList){
                switch(ext.getPermissionType()){
                    case 1:{
                        deptList.addAll(this.getDeptRelation(SystemSecurityUtils.getUser().getOrgId()));
                        break;
                    }
                    case 2:{
                        deptList.addAll(this.getDeptRelation(SystemSecurityUtils.getUser().getOrgId()));
                        break;
                    }
                    case 3:{
                        deptList.addAll(this.getDeptRelation(SystemSecurityUtils.getUser().getOrgId()));
                        break;
                    }
                    case 4:{
                        if(roleids.indexOf(","+roleId+",") != -1){
                            RoleBlocks rBlock = new RoleBlocks();
                            rBlock.setRoleId(roleId);
                            List<RoleBlocks> roleBlockList = roleBlocksService.queryAll(rBlock);
                            deptList.addAll(roleBlockList);
                            break;
                        } else {
                            break;
                        }
                    }
                    default:{
                        break;
                    }
                }
            }
        }
        return removalDept(deptList);
    }

    @Override
    public List<Role> getRolesByUserIdAndMenuId(Role role) {
        return roleDao.getRolesByUserIdAndMenuId(role);
    }

    @Override
    public List<Role> getWeight(Role role) {
        List<Role> returnRoles = new ArrayList<>();
        List<Role> roles = getRolesByUserIdAndMenuId(role);
        String filterType = GlobalContext.getProperty("weight.filter.type");
        getReturnWeight(roles,filterType,returnRoles);
        return returnRoles;
    }

    private void getReturnWeight(List<Role> roles,String filterType,List<Role> returnRoles){
        int userWeight = 0;
        int deptWeight = 0;
        int organWeight = 0;
        int weight = 0;
        String deptId = null;
        String rdeptId = null;
        String rorganId = null;
        for(Role roleVo:roles){
            if (roleVo.getWeightType().equals(1)){
                userWeight = SystemSecurityUtils.getUser().getWeight();
            } else if (roleVo.getWeightType().equals(2)){
                Department dept = new Department();
                dept.setId(SystemSecurityUtils.getUser().getDeptId());
                dept = departmentService.getDeptById(dept);
                deptWeight = dept.getWeight();
                rdeptId = dept.getId();
            } else if (roleVo.getWeightType().equals(3)){
                Department organ = new Department();
                organ.setId(SystemSecurityUtils.getUser().getOrgId());
                organ = departmentService.getDeptById(organ);
                organWeight = organ.getWeight();
                rorganId = organ.getId();
            } else if (roleVo.getWeightType().equals(4)){
                returnRoles.add(roleVo);
            }
        }
        if ("max".equals(filterType)){
            if (userWeight > deptWeight){
                weight = userWeight;
            } else {
                weight = deptWeight;
            }
            if (weight < organWeight){
                weight = organWeight;
            }
        } else if ("min".equals(filterType)){
            if (userWeight < deptWeight){
                weight = userWeight;
            } else {
                weight = deptWeight;
            }
            if (weight > organWeight){
                weight = organWeight;
            }
        } else if ("user".equals(filterType)){
            weight = SystemSecurityUtils.getUser().getWeight();
        } else if ("dept".equals(filterType)){
            Department dept = new Department();
            dept.setId(SystemSecurityUtils.getUser().getDeptId());
            dept = departmentService.getDeptById(dept);
            weight = dept.getDeptType();
        } else if ("organ".equals(filterType)){
            Department organ = new Department();
            organ.setId(SystemSecurityUtils.getUser().getOrgId());
            organ = departmentService.getDeptById(organ);
            weight = organ.getWeight();
        }

        if (rorganId != null) {
            deptId = rorganId;
        } else {
            deptId = rdeptId;
        }

        Role rRole = new Role();
        rRole.setWeight(weight);
        rRole.setDeptId(deptId);
        returnRoles.add(rRole);
    }

    /**
     * 获取所有角色集合
     * @return
     */
    @Override
    public List<Role> getAllRole() {
        return roleDao.queryAll(new Role());
    }
}