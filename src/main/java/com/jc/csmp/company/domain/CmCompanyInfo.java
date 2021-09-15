package com.jc.csmp.company.domain;

import com.jc.csmp.common.enums.DicKeyEnum;
import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.util.GlobalUtil;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.foundation.util.StringUtil;
import com.jc.system.dic.IDicManager;
import com.jc.system.dic.domain.Dic;

/**
 * 建设管理-单位管理
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public class CmCompanyInfo extends BaseBean {
    private static final long serialVersionUID = 1L;
    public CmCompanyInfo(){}
    /**单位名称*/
    private String companyName;
    /**统一社会信用代码*/
    private String creditCode;
    /**法定代表人*/
    private String legalPerson;
    /**法人联系方式*/
    private String legalPhone;
    /**项目联系人*/
    private String liaisonMan;
    /**联系人电话*/
    private String liaisonPhone;
    /**委托代理人*/
    private String proxyMan;
    /**委托代理人电话*/
    private String proxyPhone;
    /**企业类型（施工、设计等，多个用逗号分隔）*/
    private String companyType;
    /**邮政编码*/
    private String zipcode;
    /**单位地址*/
    private String companyAddress;

    /**企业类型查询条件，模糊查询*/
    private String companyTypeLike;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCreditCode() {
        return creditCode;
    }

    public void setCreditCode(String creditCode) {
        this.creditCode = creditCode;
    }

    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }

    public String getLegalPhone() {
        return legalPhone;
    }

    public void setLegalPhone(String legalPhone) {
        this.legalPhone = legalPhone;
    }

    public String getLiaisonMan() {
        return liaisonMan;
    }

    public void setLiaisonMan(String liaisonMan) {
        this.liaisonMan = liaisonMan;
    }

    public String getLiaisonPhone() {
        return liaisonPhone;
    }

    public void setLiaisonPhone(String liaisonPhone) {
        this.liaisonPhone = liaisonPhone;
    }

    public String getProxyMan() {
        return proxyMan;
    }

    public void setProxyMan(String proxyMan) {
        this.proxyMan = proxyMan;
    }

    public String getProxyPhone() {
        return proxyPhone;
    }

    public void setProxyPhone(String proxyPhone) {
        this.proxyPhone = proxyPhone;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyTypeLike() {
        return companyTypeLike;
    }

    public void setCompanyTypeLike(String companyTypeLike) {
        this.companyTypeLike = companyTypeLike;
    }

    public String getCompanyTypeValue() {
        IDicManager dicManager = SpringContextHolder.getBean(IDicManager.class);
        StringBuffer sbuf = new StringBuffer();
        if (!StringUtil.isEmpty(this.getCompanyType())) {
            String[] array = GlobalUtil.splitStr(this.getCompanyType(), ',');
            if (array != null && array.length > 0) {
                for (String dicItem : array) {
                    String value = GlobalUtil.getDicValue(DicKeyEnum.companyType, dicItem);
                    if (value.trim().length() > 0) {
                        sbuf.append("," + value);
                    }
                }
            }
        }
        String result = sbuf.toString();
        if (result.trim().length() > 0) {
            result = result.substring(1);
        }
        return result;
    }
}
