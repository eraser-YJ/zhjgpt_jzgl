package com.jc.pin.service.impl;

import com.jc.foundation.cache.CacheClient;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.JsonUtil;
import com.jc.pin.domain.PinReSubDepartment;
import com.jc.pin.domain.PinReSubUser;
import com.jc.pin.domain.PinSubDepartment;
import com.jc.pin.domain.PinSubUser;
import com.jc.pin.service.IApiPinSubDepartmentService;
import com.jc.pin.service.IPinSubDepartmentService;
import com.jc.pin.service.IPinSubUserService;
import com.jc.sys.domain.SubDepartment;
import com.jc.sys.service.ISubDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 拼音子部门service
 * @author Administrator
 * @date 2020-06-30
 */
@Service
public class ApiPinSubDepartmentServiceImpl implements IApiPinSubDepartmentService {

    @Autowired
    private ISubDepartmentService departmentService;
    @Autowired
    private IPinSubDepartmentService pinDepartmentService;
    @Autowired
    private IPinSubUserService pinUserService;

    @Override
    public String getAllDeptAndUser(String orgId,String dutyId,String tabType,String spellType,String search,String weight) throws Exception {
        String jsonArray = "";
        if (orgId == null || "0".equals(orgId)) {orgId = "";}
        if (search == null) {search = "";}
        if (spellType != null && "all".equals(spellType)) {
            spellType = "";
        }
        jsonArray = getAllDeptAndUserByOrgId(orgId,tabType,spellType,search,weight);

        return jsonArray;
    }

    @Override
    public String getSelDeptAndUser(String search,String weight) throws Exception {
        Map<String,Object> usersAndDepts = new HashMap<>(2);
        List<PinReSubDepartment> pinDepartmentList = this.getDeptByUserAndRoleForSelect("",search);
        List<PinReSubUser> uList = getFilterUser("",search,"",weight);
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
    public String getSelUsers(String search,String weight) throws Exception {
        return getAllDeptAndUserByOrgId("","","",search,weight);
    }

    private String getAllDeptAndUserByOrgId(String orgId,String tabType,String spellType,String search,String weight) throws Exception {
        List<PinReSubDepartment> list = this.getDeptByUserAndRoleForSelect(orgId,"");
        List<PinReSubUser> uList = getFilterUser(spellType,search,"",weight);
        return JsonUtil.java2Json(pottingAllDeptAndUser(list,uList));
    }

    private List<PinReSubUser> getFilterUser(String spellType,String search,String dutyId,String weight) throws Exception {
        List<PinReSubUser> uList = null;
        PinSubUser pinUser = new PinSubUser();

        if (!"".equals(spellType) && "".equals(search) && "".equals(dutyId)) {
            pinUser.setUserInitials(spellType);
            //按照拼音首字母检索
            pinUser.setSearchType("0");
            uList = getAllPinUser(pinUser,weight);
        } else if("".equals(spellType) && !"".equals(search) && "".equals(dutyId)) {
            pinUser.setUserInitials(search);
            pinUser.setUserAbbreviate(search);
            pinUser.setUserFull(search);
            pinUser.setUserName(search);
            //按照拼音首字母或拼音缩写或拼音全拼或用户姓名检索
            pinUser.setSearchType("1");
            uList = getAllPinUser(pinUser,weight);
        } else if(!"".equals(spellType) && !"".equals(search) && "".equals(dutyId)) {
            pinUser.setUserName(search);
            pinUser.setUserFull(search);
            pinUser.setUserAbbreviate(search);
            pinUser.setUserInitials(spellType);
            //按照拼音首字母和拼音缩写或拼音全拼或用户姓名检索
            pinUser.setSearchType("2");
            uList = getAllPinUser(pinUser,weight);
        } else if("".equals(spellType) && !"".equals(search) && !"".equals(dutyId)) {
            pinUser.setUserInitials(search);
            pinUser.setUserName(search);
            pinUser.setUserFull(search);
            pinUser.setUserAbbreviate(search);
            //按照职位检索
            pinUser.setDutyId(dutyId);
            //按照拼音首字母或拼音缩写或拼音全拼或用户姓名检索
            pinUser.setSearchType("1");
            uList = getAllPinUser(pinUser,weight);
        } else if("".equals(spellType) && "".equals(search) && !"".equals(dutyId)) {
            //按照职位检索
            pinUser.setDutyId(dutyId);
            uList = getAllPinUser(pinUser,weight);
        } else if(!"".equals(spellType) && "".equals(search) && !"".equals(dutyId)) {
            pinUser.setUserInitials(spellType);
            //按照拼音首字母检索
            pinUser.setSearchType("0");
            //按照职位检索
            pinUser.setDutyId(dutyId);
            uList = getAllPinUser(pinUser,weight);
        } else if(!"".equals(spellType) && !"".equals(search) && !"".equals(dutyId)) {
            pinUser.setUserName(search);
            pinUser.setUserFull(search);
            pinUser.setUserAbbreviate(search);
            pinUser.setUserInitials(spellType);
            //按照拼音首字母和拼音缩写或拼音全拼或用户姓名检索
            pinUser.setSearchType("2");
            //按照职位检索
            pinUser.setDutyId(dutyId);
            uList = getAllPinUser(pinUser,weight);
        } else if ("".equals(spellType) && "".equals(search) && "".equals(dutyId)) {
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

    private List<PinReSubDepartment> getAllPinDepartment(PinSubDepartment pinDepartment) throws CustomException{
        List<PinReSubDepartment> u = null;
        if (pinDepartment == null) {
            u = JsonUtil.json2Array(CacheClient.getCache("cache_department_info_pins"),PinReSubDepartment.class);
            if(u == null || u.isEmpty()){
                u = pinDepartmentService.queryPinDepartment(new PinSubDepartment());
                CacheClient.putCache("cache_department_info_pins", JsonUtil.java2Json(u));
            }
        } else {
            u = pinDepartmentService.queryPinDepartment(pinDepartment);
        }
        return u;
    }

    private List<PinReSubUser> getAllPinUser(PinSubUser pinUser,String weight) throws CustomException{
        List<PinReSubUser> u = null;
        if (pinUser == null) {
            Object obj = CacheClient.getCache("cache_sub_user_info_pins");
            if (obj != null) {
                u = JsonUtil.json2Array(obj.toString(), PinReSubUser.class);
            }
            String roleWeight = getWeight(weight);
            if(u == null || u.isEmpty()){
                PinSubUser tempPinUser = new PinSubUser();
                tempPinUser.setExtStr1(roleWeight);
                u = pinUserService.queryPinUser(tempPinUser);
                CacheClient.putCache("cache_sub_user_info_pins", JsonUtil.java2Json(u));
            } else {
                List<PinReSubUser> tempU = new ArrayList<>();
                for (PinReSubUser reUser : u){
                    tempU.add(reUser);
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

    private List<PinReSubUser> pottingAllDeptAndUser(List<PinReSubDepartment> list, List<PinReSubUser> uList) throws Exception{
        List<PinReSubUser> newList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            if (uList != null && uList.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    PinReSubDepartment deptTmp = list.get(i);
                    for (int j = 0; j < uList.size(); j++) {
                        PinReSubUser uTmp = uList.get(j);
                        if (deptTmp.getDeptId().equals(uTmp.getDeptId())) {
                            newList.add(uTmp);
                        }
                    }
                }
            }
        }
        return newList;
    }

    public List<PinReSubDepartment> getDeptByUserAndRoleForSelect(String orgId,String search) throws CustomException {
        List<SubDepartment> deptList  = null;
        if(orgId != null && !"".equals(orgId)){
            deptList = filterSelDept(orgId);
        } else {
            deptList = departmentService.queryAll(new SubDepartment());
        }
        List<PinReSubDepartment> pinDepartmentList = filterDept(deptList,filterAllPinDept(search));
        return pinDepartmentList;
    }

    private List<SubDepartment> filterSelDept(String orgId){
        List<SubDepartment> deptList = new ArrayList<>();
        deptList.addAll(departmentService.getDeptsByDeptId(orgId));
        return deptList;
    }

    private List<PinReSubDepartment> filterDept(List<SubDepartment> deptList,List<PinReSubDepartment> pinDepartmentList) {
        List<PinReSubDepartment> pinDepartments = new ArrayList<>();
        for (SubDepartment department:deptList){
            for (PinReSubDepartment pinDepartment:pinDepartmentList){
                if (department.getId().equals(pinDepartment.getDeptId())){
                    pinDepartments.add(pinDepartment);
                    break;
                }
            }
        }
        return pinDepartments;
    }

    private List<PinReSubDepartment> filterAllPinDept(String search) throws CustomException{
        if (search != null && !"".equals(search)){
            PinSubDepartment pinDepartment = new PinSubDepartment();
            pinDepartment.setSearchType("1");
            pinDepartment.setDeptName(search);
            pinDepartment.setDeptInitials(search);
            pinDepartment.setDeptAbbreviate(search);
            pinDepartment.setDeptFull(search);
            return getAllPinDepartment(pinDepartment);
        } else {
            return getAllPinDepartment(null);
        }
    }

}
