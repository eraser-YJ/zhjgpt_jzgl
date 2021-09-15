package com.jc.csmp.doc.dept.domain;

import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.util.DateUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lc
 * @version 2020-03-10
 */
public class SysDepartment extends BaseBean {

    private static final long serialVersionUID = 1L;

    public SysDepartment() {
    }

    private String code;
    private String name;
    private String fullName;
    private String deptDesc;
    private String leaderId;
    private String chargeLeaderId;
    private String leaderId2;
    private String parentId;
    private Integer managerDept;
    private String organizationId;
    private String deptType;
    private Integer queue;
    private String shortName;
    private String userName;
    private String password;
    private String type;
    private String status;
    private Date openDay;
    private Date openDayBegin;
    private Date openDayEnd;
    private Date endDay;
    private Date endDayBegin;
    private Date endDayEnd;
    private BigDecimal fileSpace;
    private BigDecimal usedSpace;
    private BigDecimal balance;
    private BigDecimal smsBalance;
    private String logo;
    private String cont;
    private String telp;
    private String email;
    private String memo;
    private String deleteUser;
    private Date deleteDate;
    private Date deleteDateBegin;
    private Date deleteDateEnd;
    private String createUserDep;
    private String resourceId;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setDeptDesc(String deptDesc) {
        this.deptDesc = deptDesc;
    }

    public String getDeptDesc() {
        return deptDesc;
    }

    public void setLeaderId(String leaderId) {
        this.leaderId = leaderId;
    }

    public String getLeaderId() {
        return leaderId;
    }

    public void setChargeLeaderId(String chargeLeaderId) {
        this.chargeLeaderId = chargeLeaderId;
    }

    public String getChargeLeaderId() {
        return chargeLeaderId;
    }

    public void setLeaderId2(String leaderId2) {
        this.leaderId2 = leaderId2;
    }

    public String getLeaderId2() {
        return leaderId2;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setManagerDept(Integer managerDept) {
        this.managerDept = managerDept;
    }

    public Integer getManagerDept() {
        return managerDept;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setDeptType(String deptType) {
        this.deptType = deptType;
    }

    public String getDeptType() {
        return deptType;
    }

    public void setQueue(Integer queue) {
        this.queue = queue;
    }

    public Integer getQueue() {
        return queue;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setOpenDayBegin(Date openDayBegin) {
        this.openDayBegin = openDayBegin;
    }

    public Date getOpenDayBegin() {
        return openDayBegin;
    }

    public void setOpenDayEnd(Date openDayEnd) {
        if (openDayEnd == null) {
            return;
        }
        this.openDayEnd = DateUtils.fillTime(openDayEnd);
    }

    public Date getOpenDayEnd() {
        return openDayEnd;
    }

    public void setOpenDay(Date openDay) {
        this.openDay = openDay;
    }

    public Date getOpenDay() {
        return openDay;
    }

    public void setEndDayBegin(Date endDayBegin) {
        this.endDayBegin = endDayBegin;
    }

    public Date getEndDayBegin() {
        return endDayBegin;
    }

    public void setEndDayEnd(Date endDayEnd) {
        if (endDayEnd == null) {
            return;
        }
        this.endDayEnd = DateUtils.fillTime(endDayEnd);
    }

    public Date getEndDayEnd() {
        return endDayEnd;
    }

    public void setEndDay(Date endDay) {
        this.endDay = endDay;
    }

    public Date getEndDay() {
        return endDay;
    }

    public void setFileSpace(BigDecimal fileSpace) {
        this.fileSpace = fileSpace;
    }

    public BigDecimal getFileSpace() {
        return fileSpace;
    }

    public void setUsedSpace(BigDecimal usedSpace) {
        this.usedSpace = usedSpace;
    }

    public BigDecimal getUsedSpace() {
        return usedSpace;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setSmsBalance(BigDecimal smsBalance) {
        this.smsBalance = smsBalance;
    }

    public BigDecimal getSmsBalance() {
        return smsBalance;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLogo() {
        return logo;
    }

    public void setCont(String cont) {
        this.cont = cont;
    }

    public String getCont() {
        return cont;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }

    public String getTelp() {
        return telp;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getMemo() {
        return memo;
    }

    public void setDeleteUser(String deleteUser) {
        this.deleteUser = deleteUser;
    }

    public String getDeleteUser() {
        return deleteUser;
    }

    public void setDeleteDateBegin(Date deleteDateBegin) {
        this.deleteDateBegin = deleteDateBegin;
    }

    public Date getDeleteDateBegin() {
        return deleteDateBegin;
    }

    public void setDeleteDateEnd(Date deleteDateEnd) {
        if (deleteDateEnd == null) {
            return;
        }
        this.deleteDateEnd = DateUtils.fillTime(deleteDateEnd);
    }

    public Date getDeleteDateEnd() {
        return deleteDateEnd;
    }

    public void setDeleteDate(Date deleteDate) {
        this.deleteDate = deleteDate;
    }

    public Date getDeleteDate() {
        return deleteDate;
    }

    public void setCreateUserDep(String createUserDep) {
        this.createUserDep = createUserDep;
    }

    public String getCreateUserDep() {
        return createUserDep;
    }


    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceId() {
        return resourceId;
    }
}