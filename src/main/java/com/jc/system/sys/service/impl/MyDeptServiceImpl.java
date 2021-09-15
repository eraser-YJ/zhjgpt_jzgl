package com.jc.system.sys.service.impl;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.JsonUtil;
import com.jc.system.security.domain.Department;
import com.jc.system.security.service.IDepartmentService;
import com.jc.system.sys.domain.PinDepartment;
import com.jc.system.sys.service.IApiPinDepartmentService;
import com.jc.system.sys.service.IPinDepartmentService;
import com.jc.system.sys.util.PinDeptAndUserInit;
import com.jc.system.sys.util.Pinyin4jUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Pattern;

/***
 * @author Administrator
 * @date 2020-07-01
 */
@Service
public class MyDeptServiceImpl {
    protected transient final Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    private IDepartmentService departmentService;
    @Autowired
    private IPinDepartmentService pinDepartmentService;
    @Autowired
    private IApiPinDepartmentService apiPinDepartmentService;

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void afterMethod(Object[] o, Method method){
        String pathStr = method.toGenericString();
        if (pathStr.indexOf("DepartmentController.update") != -1){
            try {
                Department deptVo= (Department)JsonUtil.json2Java(JsonUtil.java2Json(o[0]), Department.class);
                deptVo = departmentService.getDeptById(deptVo);
                PinDepartment filterPinDept = filterPinDept(deptVo);
                PinDepartment pinDepartment = new PinDepartment();
                pinDepartment.setDeptId(deptVo.getId());
                pinDepartment = pinDepartmentService.get(pinDepartment);
                if (pinDepartment == null){
                    pinDepartment = filterPinDept(deptVo);
                    pinDepartmentService.save(pinDepartment);
                } else {
                    pinDepartment.setDeptName(deptVo.getName());
                    pinDepartment.setDeptAbbreviate(filterPinDept.getDeptAbbreviate());
                    pinDepartment.setDeptInitials(filterPinDept.getDeptInitials());
                    pinDepartment.setDeptFull(filterPinDept.getDeptFull());
                    pinDepartment.setDeleteFlag(deptVo.getDeleteFlag());
                    pinDepartmentService.update(pinDepartment);
                }
                PinDeptAndUserInit.refresh();
            } catch (CustomException e) {
                logger.error(e.getMessage());
            }
        } else if(pathStr.indexOf("DepartmentController.save") != -1) {
            try {
                Department deptVo = (Department)JsonUtil.json2Java(JsonUtil.java2Json(o[0]),Department.class);
                Department dept = new Department();
                dept.setName(deptVo.getName());
                dept.setParentId(deptVo.getParentId());
                dept = departmentService.get(dept);
                if (dept != null) {
                    PinDepartment pinDepartment = filterPinDept(dept);
                    pinDepartmentService.save(pinDepartment);
                    PinDeptAndUserInit.refresh();
                }
            } catch (CustomException e) {
                logger.error(e.getMessage());
            }
        } else if(pathStr.indexOf("DepartmentController.deleteByIds") != -1){
            String ids = o[1].toString();
            PinDepartment pinDepartment = new PinDepartment();
            pinDepartment.setPrimaryKeys(ids.split(","));
            try {
                pinDepartmentService.delete(pinDepartment);
                PinDeptAndUserInit.refresh();
            } catch (CustomException e) {
                logger.error(e.getMessage());
            }
        } else if(pathStr.indexOf("DepartmentController.logicDeleteById") != -1){
            try {
                Department deptVo= (Department)JsonUtil.json2Java(JsonUtil.java2Json(o[0]),Department.class);
                List<Department> deptList = apiPinDepartmentService.getDeptByDeptIdForDel(deptVo.getId());
                if(deptList != null && deptList.size() > 0){
                    String[] primaryKeys = new String[deptList.size()];
                    for(int i=0;i<deptList.size();i++){
                        primaryKeys[i] = deptList.get(i).getId().toString();
                    }
                    PinDepartment pinDepartment = new PinDepartment();
                    pinDepartment.setPrimaryKeys(primaryKeys);
                    pinDepartmentService.delete(pinDepartment);
                    PinDeptAndUserInit.refresh();
                }
            } catch (CustomException e) {
                logger.error(e.getMessage());
            }
        }
    }

    private PinDepartment filterPinDept(Department dept){
        PinDepartment pinDepartment = new PinDepartment();
        String deptName = dept.getName();

        if(deptName != null && !"".equals(deptName.trim())) {
            StringBuffer sb = new StringBuffer();
            char[] aa = deptName.toCharArray();
            for (int i = 0; i < aa.length; i++) {
                char c = aa[i];
                if (Pattern.matches("[\u4e00-\u9fa5]", String.valueOf(c))) {
                    sb.append(c);
                }
            }
            deptName = sb.toString();

            if (deptName != null && !"".equals(deptName)) {
                String abbreviate = Pinyin4jUtil.converterToFirstSpell(deptName);
                String userFull = Pinyin4jUtil.converterToSpell(deptName);
                pinDepartment.setDeptId(dept.getId());
                pinDepartment.setDeptName(dept.getName());
                pinDepartment.setDeptAbbreviate(abbreviate);
                pinDepartment.setDeptInitials(abbreviate.substring(0, 1));
                pinDepartment.setDeptFull(userFull);
            } else {
                String abbinit = dept.getName().substring(0,1);
                pinDepartment.setDeptId(dept.getId());
                pinDepartment.setDeptName(dept.getName());
                if (Pattern.matches("[a-zA-Z]+", String.valueOf(abbinit))){
                    pinDepartment.setDeptInitials(abbinit);
                    pinDepartment.setDeptAbbreviate(abbinit);
                    pinDepartment.setDeptFull(dept.getName());
                } else if(Pattern.matches("[\u4e00-\u9fa5]", String.valueOf(abbinit))){
                    deptName = sb.toString();
                    if (deptName != null && !"".equals(deptName)) {
                        String abbreviate = Pinyin4jUtil.converterToFirstSpell(deptName);
                        String userFull = Pinyin4jUtil.converterToSpell(deptName);
                        pinDepartment.setDeptFull(userFull);
                        pinDepartment.setDeptAbbreviate(abbreviate);
                        pinDepartment.setDeptInitials(abbreviate.substring(0, 1));

                    }
                } else {
                    pinDepartment.setDeptAbbreviate("#");
                    pinDepartment.setDeptInitials("#");
                    pinDepartment.setDeptFull(dept.getName());
                }
            }
        }
        return pinDepartment;
    }
}
