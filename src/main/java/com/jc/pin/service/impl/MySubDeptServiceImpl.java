package com.jc.pin.service.impl;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.JsonUtil;
import com.jc.pin.domain.PinSubDepartment;
import com.jc.pin.service.IApiPinSubDepartmentService;
import com.jc.pin.service.IPinSubDepartmentService;
import com.jc.sys.domain.SubDepartment;
import com.jc.sys.service.ISubDepartmentService;
import com.jc.system.sys.util.PinDeptAndUserInit;
import com.jc.system.sys.util.Pinyin4jUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @author Administrator
 * @date 2020-06-30
 */
@Service
public class MySubDeptServiceImpl {

    private static Logger logger = Logger.getLogger(MySubDeptServiceImpl.class);

    @Autowired
    private ISubDepartmentService departmentService;
    @Autowired
    private IPinSubDepartmentService pinDepartmentService;

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void afterMethod(Object[] o, Method method){
        String pathstr = method.toGenericString();
        if (pathstr.indexOf("SubDepartmentController.update") != -1) {
            try {
                SubDepartment deptVo= (SubDepartment)JsonUtil.json2Java(JsonUtil.java2Json(o[0]),SubDepartment.class);
                deptVo = departmentService.get(deptVo);
                PinSubDepartment filterPinDept = filterPinDept(deptVo);

                PinSubDepartment pinDepartment = new PinSubDepartment();
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
        } else if(pathstr.indexOf("SubDepartmentController.save") != -1) {
            try {
                SubDepartment deptVo= (SubDepartment)JsonUtil.json2Java(JsonUtil.java2Json(o[0]),SubDepartment.class);
                SubDepartment dept = new SubDepartment();
                dept.setName(deptVo.getName());
                dept.setParentId(deptVo.getParentId());
                dept = departmentService.get(dept);
                if(dept != null){
                    PinSubDepartment pinDepartmentVal = new PinSubDepartment();
                    pinDepartmentVal.setDeptId(dept.getId());
                    pinDepartmentVal = pinDepartmentService.get(pinDepartmentVal);
                    if (pinDepartmentVal == null){
                        PinSubDepartment pinDepartment = filterPinDept(dept);
                        pinDepartmentService.save(pinDepartment);
                        PinDeptAndUserInit.refresh();
                    }

                }
            } catch (CustomException e) {
                logger.error(e.getMessage());
            }
        } else if(pathstr.indexOf("SubDepartmentController.deleteByIds") != -1) {
            String ids = o[1].toString();
            PinSubDepartment pinDepartment = new PinSubDepartment();
            pinDepartment.setPrimaryKeys(ids.split(","));
            try {
                pinDepartmentService.delete(pinDepartment);
                PinDeptAndUserInit.refresh();
            } catch (CustomException e) {
                logger.error(e.getMessage());
            }
        }
    }

    private PinSubDepartment filterPinDept(SubDepartment dept) {
        PinSubDepartment pinDepartment = new PinSubDepartment();
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
