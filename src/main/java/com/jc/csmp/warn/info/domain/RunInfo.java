package com.jc.csmp.warn.info.domain;

import com.jc.csmp.doc.common.MechType;
import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.util.DateUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lc
 * @version 2020-03-10
 */
public class RunInfo extends BaseBean {

    private static final long serialVersionUID = 1L;

    public RunInfo() {
    }

    public static final String TABLE_NAME = "equi_sensor_info";

    public static final String EQUI_KEY = "equiId";

    public static final String RUN_TIME = "runTime";
    private String equiId;

    private String runTimeStr;

    private Date runTimeBegin;

    private Date runTimeEnd;

    private String dataJson;

    public String getEquiId() {
        return equiId;
    }

    public void setEquiId(String equiId) {
        this.equiId = equiId;
    }


    public Date getRunTimeBegin() {
        return runTimeBegin;
    }

    public void setRunTimeBegin(Date runTimeBegin) {
        this.runTimeBegin = runTimeBegin;
    }

    public Date getRunTimeEnd() {
        return runTimeEnd;
    }

    public String getRunTimeStr() {
        return runTimeStr;
    }

    public void setRunTimeStr(String runTimeStr) {
        this.runTimeStr = runTimeStr;
    }

    public void setRunTimeEnd(Date runTimeEnd) {
        this.runTimeEnd =runTimeEnd;
    }

    public String getDataJson() {
        return dataJson;
    }

    public void setDataJson(String dataJson) {
        this.dataJson = dataJson;
    }
}