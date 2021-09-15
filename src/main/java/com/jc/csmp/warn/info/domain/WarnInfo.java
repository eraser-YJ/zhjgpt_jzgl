package com.jc.csmp.warn.info.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jc.foundation.domain.BaseBean;

import java.util.Date;

import com.jc.foundation.util.DateUtils;

/**
 * @author lc
 * @version 2020-03-10
 */
public class WarnInfo extends BaseBean {

    private static final long serialVersionUID = 1L;

    public WarnInfo() {
    }

    private String warnStatus;
    private String targetType;
    private String targetId;
    private String targetCode;
    private String targetName;
    private String targetProjectCode;
    private String targetProjectName;
    private String warnReason;
    private String processResult;
    private Date warnTime;
    private Date warnTimeBegin;
    private Date warnTimeEnd;
    private String warnReasonCode;
    private String warnReasonCodeValue;
    private Date processTime;
    private Date processTimeBegin;
    private Date processTimeEnd;
    private String processUser;
    private String processUserName;
    private String num01;
    private String num02;
    private String num03;
    private String num04;
    private String num05;
    private String num06;
    private String num07;
    private String num08;
    private String num09;
    private String num10;
    private String num11;
    private String num12;
    private String num13;
    private String num14;
    private String num15;
    private String num16;
    private String num17;
    private String num18;
    private String num19;
    private String num20;
    private String num21;
    private String num22;
    private String num23;
    private String num24;
    private String num25;
    private String str01;
    private String str02;
    private String str03;
    private String str04;
    private String str05;
    private String str06;
    private String str07;
    private String str08;
    private String str09;
    private String str10;
    private String str11;
    private String str12;
    private String str13;
    private String str14;
    private String str15;
    private String str16;
    private String str17;
    private String str18;
    private String str19;
    private String str20;

    public String getWarnStatus() {
        return warnStatus;
    }

    public void setWarnStatus(String warnStatus) {
        this.warnStatus = warnStatus;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetCode(String targetCode) {
        this.targetCode = targetCode;
    }

    public String getTargetCode() {
        return targetCode;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public void setWarnTime(Date warnTime) {
        this.warnTime = warnTime;
    }

    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    public Date getWarnTime() {
        return warnTime;
    }

    public void setWarnReasonCode(String warnReasonCode) {
        this.warnReasonCode = warnReasonCode;
    }

    public String getWarnReasonCode() {
        return warnReasonCode;
    }

    public void setWarnReason(String warnReason) {
        this.warnReason = warnReason;
    }

    public String getWarnReason() {
        return warnReason;
    }

    public void setNum01(String num01) {
        this.num01 = num01;
    }

    public String getNum01() {
        return num01;
    }

    public void setNum02(String num02) {
        this.num02 = num02;
    }

    public String getNum02() {
        return num02;
    }

    public void setNum03(String num03) {
        this.num03 = num03;
    }

    public String getNum03() {
        return num03;
    }

    public void setNum04(String num04) {
        this.num04 = num04;
    }

    public String getNum04() {
        return num04;
    }

    public void setNum05(String num05) {
        this.num05 = num05;
    }

    public String getNum05() {
        return num05;
    }

    public void setNum06(String num06) {
        this.num06 = num06;
    }

    public String getNum06() {
        return num06;
    }

    public void setNum07(String num07) {
        this.num07 = num07;
    }

    public String getNum07() {
        return num07;
    }

    public void setNum08(String num08) {
        this.num08 = num08;
    }

    public String getNum08() {
        return num08;
    }

    public void setNum09(String num09) {
        this.num09 = num09;
    }

    public String getNum09() {
        return num09;
    }

    public void setNum10(String num10) {
        this.num10 = num10;
    }

    public String getNum10() {
        return num10;
    }

    public void setNum11(String num11) {
        this.num11 = num11;
    }

    public String getNum11() {
        return num11;
    }

    public void setNum12(String num12) {
        this.num12 = num12;
    }

    public String getNum12() {
        return num12;
    }

    public void setNum13(String num13) {
        this.num13 = num13;
    }

    public String getNum13() {
        return num13;
    }

    public void setNum14(String num14) {
        this.num14 = num14;
    }

    public String getNum14() {
        return num14;
    }

    public void setNum15(String num15) {
        this.num15 = num15;
    }

    public String getNum15() {
        return num15;
    }

    public void setNum16(String num16) {
        this.num16 = num16;
    }

    public String getNum16() {
        return num16;
    }

    public String getProcessResult() {
        return processResult;
    }

    public void setProcessResult(String processResult) {
        this.processResult = processResult;
    }

    public void setProcessTimeBegin(Date processTimeBegin) {
        this.processTimeBegin = processTimeBegin;
    }

    public Date getProcessTimeBegin() {
        return processTimeBegin;
    }

    public void setProcessTimeEnd(Date processTimeEnd) {
        if (processTimeEnd == null) {
            return;
        }
        this.processTimeEnd = DateUtils.fillTime(processTimeEnd);
    }

    public Date getProcessTimeEnd() {
        return processTimeEnd;
    }

    @JsonFormat(
            pattern = "yyyy-MM-dd",
            timezone = "GMT+8"
    )
    public Date getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Date processTime) {
        this.processTime = processTime;
    }

    public String getProcessUser() {
        return processUser;
    }

    public void setProcessUser(String processUser) {
        this.processUser = processUser;
    }

    public String getProcessUserName() {
        return processUserName;
    }

    public void setProcessUserName(String processUserName) {
        this.processUserName = processUserName;
    }

    public Date getWarnTimeBegin() {
        return warnTimeBegin;
    }

    public void setWarnTimeBegin(Date warnTimeBegin) {
        this.warnTimeBegin = warnTimeBegin;
    }

    public Date getWarnTimeEnd() {
        return warnTimeEnd;
    }

    public void setWarnTimeEnd(Date warnTimeEnd) {
        if (warnTimeEnd == null) {
            return;
        }
        this.warnTimeEnd = DateUtils.fillTime(warnTimeEnd);
    }

    public String getTargetProjectCode() {
        return targetProjectCode;
    }

    public void setTargetProjectCode(String targetProjectCode) {
        this.targetProjectCode = targetProjectCode;
    }

    public String getTargetProjectName() {
        return targetProjectName;
    }

    public void setTargetProjectName(String targetProjectName) {
        this.targetProjectName = targetProjectName;
    }

    public void setNum17(String num17) {
        this.num17 = num17;
    }

    public String getNum17() {
        return num17;
    }

    public void setNum18(String num18) {
        this.num18 = num18;
    }

    public String getNum18() {
        return num18;
    }

    public void setNum19(String num19) {
        this.num19 = num19;
    }

    public String getNum19() {
        return num19;
    }

    public void setNum20(String num20) {
        this.num20 = num20;
    }

    public String getNum20() {
        return num20;
    }

    public void setNum21(String num21) {
        this.num21 = num21;
    }

    public String getNum21() {
        return num21;
    }

    public void setNum22(String num22) {
        this.num22 = num22;
    }

    public String getNum22() {
        return num22;
    }

    public void setNum23(String num23) {
        this.num23 = num23;
    }

    public String getNum23() {
        return num23;
    }

    public void setNum24(String num24) {
        this.num24 = num24;
    }

    public String getNum24() {
        return num24;
    }

    public void setNum25(String num25) {
        this.num25 = num25;
    }

    public String getNum25() {
        return num25;
    }

    public void setStr01(String str01) {
        this.str01 = str01;
    }

    public String getStr01() {
        return str01;
    }

    public void setStr02(String str02) {
        this.str02 = str02;
    }

    public String getStr02() {
        return str02;
    }

    public void setStr03(String str03) {
        this.str03 = str03;
    }

    public String getStr03() {
        return str03;
    }

    public void setStr04(String str04) {
        this.str04 = str04;
    }

    public String getStr04() {
        return str04;
    }

    public void setStr05(String str05) {
        this.str05 = str05;
    }

    public String getStr05() {
        return str05;
    }

    public void setStr06(String str06) {
        this.str06 = str06;
    }

    public String getStr06() {
        return str06;
    }

    public void setStr07(String str07) {
        this.str07 = str07;
    }

    public String getStr07() {
        return str07;
    }

    public void setStr08(String str08) {
        this.str08 = str08;
    }

    public String getStr08() {
        return str08;
    }

    public void setStr09(String str09) {
        this.str09 = str09;
    }

    public String getStr09() {
        return str09;
    }

    public void setStr10(String str10) {
        this.str10 = str10;
    }

    public String getStr10() {
        return str10;
    }

    public String getStr11() {
        return str11;
    }

    public void setStr11(String str11) {
        this.str11 = str11;
    }

    public String getStr12() {
        return str12;
    }

    public void setStr12(String str12) {
        this.str12 = str12;
    }

    public String getStr13() {
        return str13;
    }

    public void setStr13(String str13) {
        this.str13 = str13;
    }

    public String getStr14() {
        return str14;
    }

    public void setStr14(String str14) {
        this.str14 = str14;
    }

    public String getStr15() {
        return str15;
    }

    public void setStr15(String str15) {
        this.str15 = str15;
    }

    public String getStr16() {
        return str16;
    }

    public void setStr16(String str16) {
        this.str16 = str16;
    }

    public String getStr17() {
        return str17;
    }

    public void setStr17(String str17) {
        this.str17 = str17;
    }

    public String getStr18() {
        return str18;
    }

    public void setStr18(String str18) {
        this.str18 = str18;
    }

    public String getStr19() {
        return str19;
    }

    public void setStr19(String str19) {
        this.str19 = str19;
    }

    public String getStr20() {
        return str20;
    }

    public void setStr20(String str20) {
        this.str20 = str20;
    }

    public String getWarnReasonCodeValue() {
        return warnReasonCodeValue;
    }

    public void setWarnReasonCodeValue(String warnReasonCodeValue) {
        this.warnReasonCodeValue = warnReasonCodeValue;
    }


}