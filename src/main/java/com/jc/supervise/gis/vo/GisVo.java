package com.jc.supervise.gis.vo;

import java.math.BigDecimal;

public class GisVo {


    private String areaName;//区域名称
    private String pcpHtlb;//合同类别
    private Integer cc = 0;//数量
    private Long cz;//产值

    private Integer compType1 = 0;//勘察
    private Integer compType2 = 0;//设计
    private Integer compType3 = 0;//施工
    private Integer compType4 = 0;//监理

    private Integer areaTotal = 0;//区域企业总数
    private Integer companyTotal = 0;//企业总数
    private BigDecimal productionTotal;//产值
    private BigDecimal amountCompleted;//投资完成金额
    private String proportion1;//占比
    private String proportion2;//占比

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getPcpHtlb() {
        return pcpHtlb;
    }

    public void setPcpHtlb(String pcpHtlb) {
        this.pcpHtlb = pcpHtlb;
    }

    public Integer getCc() {
        return cc;
    }

    public void setCc(Integer cc) {
        this.cc = cc;
    }

    public Long getCz() {
        return cz;
    }

    public void setCz(Long cz) {
        this.cz = cz;
    }

    public Integer getAreaTotal() {
        return areaTotal;
    }

    public void setAreaTotal(Integer areaTotal) {
        this.areaTotal = areaTotal;
    }

    public Integer getCompType1() {
        return compType1;
    }

    public void setCompType1(Integer compType1) {
        this.compType1 = compType1;
    }

    public Integer getCompType2() {
        return compType2;
    }

    public void setCompType2(Integer compType2) {
        this.compType2 = compType2;
    }

    public Integer getCompType3() {
        return compType3;
    }

    public void setCompType3(Integer compType3) {
        this.compType3 = compType3;
    }

    public Integer getCompType4() {
        return compType4;
    }

    public void setCompType4(Integer compType4) {
        this.compType4 = compType4;
    }

    public Integer getCompanyTotal() {
        return companyTotal;
    }

    public void setCompanyTotal(Integer companyTotal) {
        this.companyTotal = companyTotal;
    }

    public BigDecimal getProductionTotal() {
        return productionTotal;
    }

    public void setProductionTotal(BigDecimal productionTotal) {
        this.productionTotal = productionTotal;
    }

    public BigDecimal getAmountCompleted() {
        return amountCompleted;
    }

    public void setAmountCompleted(BigDecimal amountCompleted) {
        this.amountCompleted = amountCompleted;
    }

    public String getProportion1() {
        return proportion1;
    }

    public void setProportion1(String proportion1) {
        this.proportion1 = proportion1;
    }

    public String getProportion2() {
        return proportion2;
    }

    public void setProportion2(String proportion2) {
        this.proportion2 = proportion2;
    }
}
