package com.jc.csmp.ptProject.vo;

import com.jc.foundation.domain.BaseBean;

public class EchartsVo extends BaseBean {
    private String value;
    private String name;
    private String namey;
    private String data;
    private String cc;
    private Long cc1;
    private Long cc2;
    private Long cc3;
    private String allCc;
    private Long[] arr;


    public Long getCc1() {
        return cc1;
    }

    public void setCc1(Long cc1) {
        this.cc1 = cc1;
    }

    public Long getCc2() {
        return cc2;
    }

    public void setCc2(Long cc2) {
        this.cc2 = cc2;
    }

    public Long getCc3() {
        return cc3;
    }

    public void setCc3(Long cc3) {
        this.cc3 = cc3;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getAllCc() {
        return allCc;
    }

    public void setAllCc(String allCc) {
        this.allCc = allCc;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getNamey() {
        return namey;
    }

    public void setNamey(String namey) {
        this.namey = namey;
    }

    public Long[] getArr() {
        return arr;
    }

    public void setArr(Long[] arr) {
        this.arr = arr;
    }
}
