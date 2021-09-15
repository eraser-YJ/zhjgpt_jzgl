package com.jc.system.sys.service.impl;

import com.jc.foundation.cache.CacheClient;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.JsonUtil;
import com.jc.system.group.domain.Group;
import com.jc.system.group.service.IGroupService;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.dao.IDepartmentDao;
import com.jc.system.security.domain.*;
import com.jc.system.security.service.IAdminSideService;
import com.jc.system.security.service.IDepartmentService;
import com.jc.system.security.service.IRoleService;
import com.jc.system.sys.domain.PinDepartment;
import com.jc.system.sys.domain.PinReDepartment;
import com.jc.system.sys.domain.PinReUser;
import com.jc.system.sys.domain.PinUser;
import com.jc.system.sys.service.IApiPinDepartmentService;
import com.jc.system.sys.service.IPinDepartmentService;
import com.jc.system.sys.service.IPinUserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/***
 * @author Administrator
 * @date 2020-07-01
 */
@Service
public class ApiPinDepartmentServiceImpl implements IApiPinDepartmentService {
    protected transient final Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    private IDepartmentService departmentService;
    @Autowired
    private IAdminSideService adminSideService;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private IGroupService groupService;
    @Autowired
    private IPinDepartmentService pinDepartmentService;
    @Autowired
    private IPinUserService pinUserService;

    private IDepartmentDao departmentDao;
    @Autowired
    public ApiPinDepartmentServiceImpl(IDepartmentDao departmentDao) {
        this.departmentDao=departmentDao;
    }

    @Override
    public String getAllDeptAndUser(String orgId, String dutyId, String tabType, String spellType, String search, String weight) throws Exception {
        String jsonArray = "";
        if(orgId == null || "0".equals(orgId)) {
            orgId = "";
        }
        if(search == null) {
            search = "";
        }
        if(spellType != null && "all".equals(spellType)) {
            spellType = "";
        }

        if ("2".equals(tabType)){
            //根据职位
            jsonArray = getPostAndUser(orgId,dutyId,tabType,spellType,search,weight);
        } else if("3".equals(tabType)){
            //私人组别
            jsonArray = getPersonGroupAndUser(orgId,tabType,spellType,search,weight);
        } else if("4".equals(tabType)){
            //公共组别
            jsonArray = getPublicGroupAndUser(orgId,tabType,spellType,search,weight);
        } else if("5".equals(tabType)){
            //在线人员
            jsonArray = getDeptAndUserByOnLine(orgId,tabType,spellType,search,weight);
        } else {
            //全部人员
            jsonArray = getAllDeptAndUserByOrgId(orgId,tabType,spellType,search,weight);
        }
        return jsonArray;
    }

    @Override
    public String getSelDeptAndUser(String search, String weight) throws Exception {
        Map<String,Object> usersAndDepts = new HashMap<>();
        List<PinReDepartment> pinDepartmentList = this.getDeptByUserAndRoleForSelect("",search);
        List<PinReUser> uList = getFilterUser("",search,"",weight);
        if (pinDepartmentList != null && pinDepartmentList.size()>0){
            usersAndDepts.put("depts",pinDepartmentList);
        } else {
            usersAndDepts.put("depts",new ArrayList<>());
        }
        if (uList != null && uList.size()>0){
            usersAndDepts.put("users",uList);
        } else {
            usersAndDepts.put("users",new ArrayList<>());
        }
        return JsonUtil.java2Json(usersAndDepts);
    }

    @Override
    public String getSelUsers(String search, String weight) throws Exception {
        return getAllDeptAndUserByOrgId("","","",search,weight);
    }

    private String getAllDeptAndUserByOrgId(String orgId,String tabType,String spellType,String search,String weight) throws Exception {
        List<PinReDepartment> list = this.getDeptByUserAndRoleForSelect(orgId,"");
        List<PinReUser> uList = getFilterUser(spellType,search,"",weight);
        return JsonUtil.java2Json(pottingAllDeptAndUser(list,uList));
    }

    public String getDeptAndUserByOnLine(String orgId,String tabType,String spellType,String search,String weight) throws Exception {
        List<PinReDepartment> list = getDeptByUserAndRoleForSelect(orgId,"");
        List<Principal> uList = SystemSecurityUtils.getOnlineUsers(orgId);
        List<PinReUser> pinUserList = filterUser(uList,getFilterUser(spellType,search,"",weight));
        return JsonUtil.java2Json(pottingAllDeptAndUser(list,pinUserList));
    }

    public String getPersonGroupAndUser(String orgId,String tabType,String spellType,String search,String weight) throws Exception {
        Group group = new Group();
        group.setGroupType("1");
        group.setCreateUser(SystemSecurityUtils.getUser().getId());
        List<Group> groupList= groupService.getAllGroupUsers(group);
        List<PinReUser> uList = getFilterUser(spellType,search,"",weight);
        List<PinReUser> pinUserList = filterGroup(groupList,uList);
        return JsonUtil.java2Json(pinUserList);
    }

    public String getPublicGroupAndUser(String orgId,String tabType,String spellType,String search,String weight) throws Exception {
        Group group = new Group();
        group.setGroupType("2");
        group.setCreateUserOrg(SystemSecurityUtils.getUser().getOrgId());
        List<Group> groupList= groupService.getAllGroupUsers(group);
        List<PinReUser> uList = getFilterUser(spellType,search,"",weight);
        List<PinReUser> pinUserList = filterGroup(groupList,uList);
        return JsonUtil.java2Json(pinUserList);
    }

    private List<PinReUser> filterGroup(List<Group> groupList,List<PinReUser> uList){
        List<PinReUser> pinUserList = new ArrayList<>();
        if (groupList != null && groupList.size() > 0) {
            if (uList != null && uList.size() > 0) {
                for (int i = 0; i < groupList.size(); i++) {
                    Group groupVo = groupList.get(i);
                    for (int j = 0; j < uList.size(); j++) {
                        PinReUser uTmp = uList.get(j);
                        if (groupVo.getUserId().equals(uTmp.getUserId())) {
                            pinUserList.add(uTmp);
                        }
                    }
                }
            }
        }
        return pinUserList;
    }

    public String getPostAndUser(String orgId,String dutyId,String tabType,String spellType,String search,String weight) throws Exception {
        return JsonUtil.java2Json(pottingPostAndUser(orgId,dutyId,spellType,search,weight));
    }

    private List<PinReUser> pottingPostAndUser(String orgId,String dutyId,String spellType,String search,String weight) throws Exception {
        List<PinReUser> uList = getFilterUser(spellType,search,dutyId,weight);
        //根据部门获取人员
        List<PinReDepartment> deptList = getDepartmentByUserAndRole(orgId);
        List<PinReUser> pinUserList = pottingAllDeptAndUser(deptList,uList);
        return pinUserList;
    }

    private List<PinReUser> filterUser(List<Principal> uList,List<PinReUser> pinUserList){
        List<PinReUser> newList = new ArrayList<>();
        if (uList != null && uList.size() > 0) {
            if (pinUserList != null && pinUserList.size() > 0) {
                for (int i = 0; i < uList.size(); i++) {
                    Principal principal = uList.get(i);
                    for (int j = 0; j < pinUserList.size(); j++) {
                        PinReUser uTmp = pinUserList.get(j);
                        if (principal.getId().equals(uTmp.getUserId())) {
                            newList.add(uTmp);
                        }
                    }
                }
            }
        }
        return newList;
    }

    private List<PinReUser> getFilterUser(String spellType,String search,String dutyId,String weight) throws Exception {
        List<PinReUser> uList = null;
        PinUser pinUser = new PinUser();

        if (!"".equals(spellType) && "".equals(search) && "".equals(dutyId)){
            pinUser.setUserInitials(spellType);
            pinUser.setSearchType("0");//按照拼音首字母检索
            uList = getAllPinUser(pinUser,weight);
        } else if("".equals(spellType) && !"".equals(search) && "".equals(dutyId)){
            pinUser.setUserInitials(search);
            pinUser.setUserAbbreviate(search);
            pinUser.setUserFull(search);
            pinUser.setUserName(search);
            pinUser.setSearchType("1");//按照拼音首字母或拼音缩写或拼音全拼或用户姓名检索
            uList = getAllPinUser(pinUser,weight);
        } else if(!"".equals(spellType) && !"".equals(search) && "".equals(dutyId)){
            pinUser.setUserName(search);
            pinUser.setUserFull(search);
            pinUser.setUserAbbreviate(search);
            pinUser.setUserInitials(spellType);
            pinUser.setSearchType("2");//按照拼音首字母和拼音缩写或拼音全拼或用户姓名检索
            uList = getAllPinUser(pinUser,weight);
        } else if("".equals(spellType) && !"".equals(search) && !"".equals(dutyId)){
            pinUser.setUserInitials(search);
            pinUser.setUserName(search);
            pinUser.setUserFull(search);
            pinUser.setUserAbbreviate(search);
            pinUser.setDutyId(dutyId);//按照职位检索
            pinUser.setSearchType("1");//按照拼音首字母或拼音缩写或拼音全拼或用户姓名检索
            uList = getAllPinUser(pinUser,weight);
        } else if("".equals(spellType) && "".equals(search) && !"".equals(dutyId)){
            pinUser.setDutyId(dutyId);//按照职位检索
            uList = getAllPinUser(pinUser,weight);
        } else if(!"".equals(spellType) && "".equals(search) && !"".equals(dutyId)){
            pinUser.setUserInitials(spellType);
            pinUser.setSearchType("0");//按照拼音首字母检索
            pinUser.setDutyId(dutyId);//按照职位检索
            uList = getAllPinUser(pinUser,weight);
        } else if(!"".equals(spellType) && !"".equals(search) && !"".equals(dutyId)){
            pinUser.setUserName(search);
            pinUser.setUserFull(search);
            pinUser.setUserAbbreviate(search);
            pinUser.setUserInitials(spellType);
            pinUser.setSearchType("2");//按照拼音首字母和拼音缩写或拼音全拼或用户姓名检索
            pinUser.setDutyId(dutyId);//按照职位检索
            uList = getAllPinUser(pinUser,weight);
        } else if ("".equals(spellType) && "".equals(search) && "".equals(dutyId)){
            uList = getAllPinUser(null,weight);
        } else {
            uList = getAllPinUser(null,weight);
        }
        return uList;
    }

    private String getWeight(String weight){
        String weights = null;
        if(weight != null && !"".equals(weight)){
            if("manager".equals(weight)){
                weights = "1";
            } else if("security".equals(weight)) {
                weights = "2";
            } else if("audit".equals(weight)) {
                weights = "3";
            } else {
                weights = "0";
            }
        } else {
            weights = "0";
        }
        return weights;
    }

    private List<PinReDepartment> getAllPinDepartment(PinDepartment pinDepartment) throws CustomException{
        List<PinReDepartment> u = null;
        if (pinDepartment == null) {
            u = JsonUtil.json2Array(CacheClient.getCache("cache_department_info_pins"),PinReDepartment.class);
            if(u == null||u.isEmpty()){
                u = pinDepartmentService.queryPinDepartment(new PinDepartment());
                CacheClient.putCache("cache_department_info_pins", JsonUtil.java2Json(u));
            }
        } else {
            u = pinDepartmentService.queryPinDepartment(pinDepartment);
        }
        return u;
    }

    private List<PinReUser> getAllPinUser(PinUser pinUser,String weight) throws CustomException{
        List<PinReUser> u = null;
        if (pinUser == null) {
            u = JsonUtil.json2Array(CacheClient.getCache("cache_user_info_pins"),PinReUser.class);
            String roleWeight = getWeight(weight);
            if(u == null || (u.isEmpty())){
                PinUser tempPinUser = new PinUser();
                tempPinUser.setExtStr1(roleWeight);
                u = pinUserService.queryPinUser(tempPinUser);
                CacheClient.putCache("cache_user_info_pins", JsonUtil.java2Json(u));
            } else {
                List<PinReUser> tempU = new ArrayList<>();
                for (PinReUser reUser : u){
                    if (reUser != null && roleWeight != null && roleWeight.equals(reUser.getWeightRole())){
                        tempU.add(reUser);
                    }
                }
                u.clear();
                u.addAll(tempU);
            }
        } else {
            pinUser.setExtStr1(getWeight(weight));
            u = pinUserService.queryPinUser(pinUser);
        }
        return u;
    }

    private List<PinReUser> pottingAllDeptAndUser(List<PinReDepartment> list, List<PinReUser> uList) throws Exception{
        List<PinReUser> newList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            if (uList != null && uList.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    PinReDepartment deptTmp = list.get(i);
                    for (int j = 0; j < uList.size(); j++) {
                        PinReUser uTmp = uList.get(j);
                        if (deptTmp.getDeptId().equals(uTmp.getDeptId())) {
                            newList.add(uTmp);
                        }
                    }
                }
            }
        }
        return newList;
    }

    private List<PinReDepartment> filterDept(List<Department> deptList,List<PinReDepartment> pinDepartmentList) {
        List<PinReDepartment> pinDepartments = new ArrayList<>();
        //查询所有部门
        for (Department department:deptList){
            for (PinReDepartment pinDepartment:pinDepartmentList){
                if (department.getId().equals(pinDepartment.getDeptId())){
                    pinDepartments.add(pinDepartment);
                    break;
                }
            }
        }
        return pinDepartments;
    }

    public List<PinReDepartment> getDeptByUserAndRoleForSelect(String orgId,String search) throws CustomException {
        User userInfo = SystemSecurityUtils.getUser();
        List<PinReDepartment> pinDepartmentList = null;
        if(userInfo != null){
            //超级管理员
            if(userInfo.getIsSystemAdmin() || userInfo.getIsAdmin() == 2){
                if(orgId != null && !"".equals(orgId)){
                    return filterDept(filterSelDept(orgId),filterAllPinDept(search));
                } else {
                    return filterAllPinDept(search);
                }
            }//管理员
            else if(userInfo.getIsAdmin() == 1 ) {
                if(orgId != null && !"".equals(orgId)){
                    return filterDept(filterSelDept(orgId),filterAllPinDept(search));
                } else {
                    //查询所有部门
                    List<Department> deptList = getDeptListForIsAdmin(userInfo,orgId);
                    pinDepartmentList = filterDept(deptList,filterAllPinDept(search));
                    return pinDepartmentList;
                }
            } else {
                if(orgId != null && !"".equals(orgId)){
                    return filterDept(filterSelDept(orgId),filterAllPinDept(search));
                } else {
                    List<Department> deptList = getDeptListForCommonUser(userInfo,orgId);
                    pinDepartmentList = filterDept(deptList,filterAllPinDept(search));
                    return pinDepartmentList;
                }
            }
        }
        return pinDepartmentList;
    }

    private List<Department> filterSelDept(String orgId){
        Department dept = new Department();
        List<Department> deptList = new ArrayList<>();
        dept.setId(orgId);
        try {
            dept = departmentService.get(dept);
        } catch (CustomException e) {
            logger.error(e.getMessage());
        }
        if (dept.getDeptType() == 1){
            deptList.addAll(departmentDao.getDeptByOrgId(dept.getId()));
        } else {
            deptList.add(dept);
        }
        return deptList;
    }

    private List<PinReDepartment> filterAllPinDept(String search) throws CustomException{
        List<PinReDepartment> pinDepartmentList = null;
        if (search != null && !"".equals(search)){
            PinDepartment pinDepartment = new PinDepartment();
            pinDepartment.setSearchType("1");
            pinDepartment.setDeptName(search);
            pinDepartment.setDeptInitials(search);
            pinDepartment.setDeptAbbreviate(search);
            pinDepartment.setDeptFull(search);
            pinDepartmentList = getAllPinDepartment(pinDepartment);
        } else {
            pinDepartmentList = getAllPinDepartment(null);
        }
        return pinDepartmentList;
    }

    private List<Department> getDeptListForIsAdmin(User userInfo,String orgId) throws CustomException{
        Department dept = new Department();
        dept.setDeleteFlag(0);
        if(orgId != null && !"".equals(orgId)) {
            dept.setOrgId(orgId);
        }
        List<Department> allDeptList = departmentService.queryAll(dept);

        AdminSide adminSide = new AdminSide();
        adminSide.setUserId(userInfo.getId());
        List<AdminSide> asList = adminSideService.queryAll(adminSide);
        List<Department> deptList = new ArrayList<Department>();
        //循环添加机构
        for(AdminSide as : asList){
            //添加自己
            Department d = new Department();
            d.setId(as.getDeptId());
            if(!isExist(d, deptList)){
                d = departmentService.get(d);
                d.setIsChecked(as.getIsChecked());
                d.setUserType(2);
                deptList.add(d);
                //递归添加部门	判断有权限的
                if("1".equals(as.getIsChecked())){
                    getDepts(as.getDeptId(), deptList, 2, allDeptList);
                }
            }
        }

        String userorgId = userInfo.getOrgId();
        List<Department> defaultDeptList = departmentDao.getDeptByOrgId(userorgId);//机构下只显示部门的
        //获取上级组织
        List<Department> deptParent = departmentDao.getQueryDeptPidByOrgId(userorgId);
        if(deptParent != null && deptParent.size() > 0){
            for(Department d : deptParent){
                defaultDeptList.add(d);
            }
        }
        //获取上级组织
        defaultDeptList.add(this.getDeptRoot());
        for(Department d : defaultDeptList){
            deptList.add(d);
        }
        removeDuplicateWithOrder(deptList);
        return deptList;
    }

    private List<Department> getDeptListForCommonUser(User userInfo,String orgid)  throws CustomException{

        String orgId = null;
        if (orgid != null && !"".equals(orgid)) {
            orgId = orgid;
        } else {
            orgId = userInfo.getOrgId();
        }
        List<Department> defaultDeptList = departmentDao.getDeptByOrgId(orgId);//机构下只显示部门的
        //获取上级组织
        List<Department> deptParent = departmentDao.getQueryDeptPidByOrgId(orgId);
        if(deptParent != null && deptParent.size() > 0){
            for(Department d : deptParent){
                defaultDeptList.add(d);
            }
        }
        defaultDeptList.add(this.getDeptRoot());

        String[] roleId = new String[userInfo.getSysUserRole().size()];
        for(int i=0;i<userInfo.getSysUserRole().size();i++){
            SysUserRole userRole = userInfo.getSysUserRole().get(i);
            roleId[i] = userRole.getRoleId();
        }
        if(roleId.length > 0){
            //查询角色关联的机构
            List<RoleBlocks> orgList = roleService.getAllDeptWithRolesSelect(roleId);
            if(orgList != null && orgList.size() > 0){
                //查询所有部门
                Department dept = new Department();
                dept.setDeleteFlag(0);

                //返回结果LIST
                for(RoleBlocks roleBlock : orgList){
                    List<Department> roleRelationDeptList = getAllParentDeptByDeptId(roleBlock.getDeptId());
                    defaultDeptList.removeAll(roleRelationDeptList);
                    defaultDeptList.addAll(roleRelationDeptList);
                }
            }
            removeDuplicateWithOrder(defaultDeptList);
            return defaultDeptList;
        }
        return defaultDeptList;
    }

    public List<PinReDepartment> getDepartmentByUserAndRole(String orgId) throws Exception {
        User userInfo = SystemSecurityUtils.getUser();
        if(userInfo != null){
            //超级管理员
            if(userInfo.getIsSystemAdmin()){
                return getAllPinDepartment(null);
            }
            //管理员
            else if(userInfo.getIsAdmin() == 1){
                //查询所有部门
                List<Department> deptList = getDeptListForIsAdmin(userInfo,orgId);
                List<PinReDepartment> pinDepartmentList = getAllPinDepartment(null);
                List<PinReDepartment> pinDepartments = filterDept(deptList,pinDepartmentList);
                return pinDepartments;
            }
            //普通用户
            else{
            	String[] roleId = new String[userInfo.getSysUserRole().size()];
                for(int i=0;i<userInfo.getSysUserRole().size();i++){
                    SysUserRole userRole = userInfo.getSysUserRole().get(i);
                    roleId[i] = userRole.getRoleId();
                }
                if(roleId.length > 0){
                    //查询角色关联的机构
                    List<RoleBlocks> orgList = roleService.getAllDeptWithRoles(roleId);
                    //角色没有关联机构 默认显示本部门
                    if(orgList == null || orgList.size() == 0){
                        orgList = roleService.getDeptRelation(SystemSecurityUtils.getUser().getDeptId());
                    }
                    //查询所有部门
                    Department dept = new Department();
                    dept.setDeleteFlag(0);
                    List<Department> allDeptList = departmentDao.queryAll(dept);
                    //返回结果LIST
                    List<Department> deptList = new ArrayList<Department>();
                    for(RoleBlocks roleBlock : orgList){
                        //添加自己
                        Department d = new Department();
                        d.setId(roleBlock.getDeptId());
                        d = departmentDao.get(d);
                        if(!isExist(d, deptList)){
                            d.setIsChecked(roleBlock.getIsChecked());
                            d.setUserType(3);
                            deptList.add(d);
                            //判断选择的机构
                            if("1".equals(roleBlock.getIsChecked())){
                                //递归添加部门
                                getDepts(roleBlock.getDeptId(), deptList, 3, allDeptList);
                            }
                        }
                    }
                    List<PinReDepartment> pinDepartmentList = getAllPinDepartment(null);
                    List<PinReDepartment> pinDepartments = filterDept(deptList,pinDepartmentList);
                    return pinDepartments;
                }
            }
        }
        return null;
    }

    private Department getDeptRoot() throws CustomException {
        Department d = new Department();
        d.setParentId("0");
        return departmentService.get(d);
    }
    private void removeDuplicateWithOrder(List<Department> list) {
        Set<String> set = new HashSet<>();
        List<Department> newList = new ArrayList<>();
        for (Iterator<Department> iter = list.iterator(); iter.hasNext();) {
            Department element = iter.next();
            if (set.add(element.getId())){
                newList.add(element);
            }
        }
        list.clear();
        list.addAll(newList);
    }

    private List<Department> getDepts(String id, List<Department> deptList, int userType, List<Department> allDeptList){
        try {
            Department dept = new Department();
            dept.setParentId(id);
            dept.setDeptType(0);
            List<Department> list = queryDept(dept, allDeptList);
            for(Department d : list){
                if(!isExist(d, deptList)){
                    d.setIsChecked("1");
                    d.setUserType(userType);
                    deptList.add(d);
                    getDepts(d.getId(), deptList, userType, allDeptList);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return deptList;
    }

    public List<Department> queryDept(Department dept, List<Department> allDeptList){
        List<Department> result = new ArrayList<>();
        for(Department d : allDeptList){
            if(d.getParentId().equals(dept.getParentId()) && d.getDeptType().equals(dept.getDeptType())){
                result.add(d);
            }
        }
        return result;
    }

    private boolean isExist(Department d, List<Department> list){
        for(Department department : list){
            if(department.getId().equals(d.getId())){
                return true;
            }
        }
        return false;
    }

    /**
     * 根据组织ID获取除根以外的父类
     * @param deptId
     * @return
     */
    private List<Department> getAllParentDeptByDeptId(String deptId) {
        return departmentDao.getQueryDeptPidByOrgId(deptId);
    }

    @Override
    public List<Department> getDeptByDeptIdForDel(String id) throws CustomException {
        return departmentDao.getDeptByDeptIdForDel(id);
    }

}
