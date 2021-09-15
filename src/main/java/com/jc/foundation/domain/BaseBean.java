package com.jc.foundation.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jc.foundation.constants.OrderType;
import com.jc.foundation.util.StringUtil;
import com.jc.system.util.OperLogUtils;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 基础bean
 * @Author 常鹏
 * @Date 2020/8/4 12:29
 * @Version 1.0
 */
public class BaseBean implements IBaseBean {
    private static final long serialVersionUID = -1771748797466926767L;
    @ApiModelProperty( value = "创建时间", name = "createDate" )
    @JsonFormat( pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8" )
    protected Date createDate;
    @JsonFormat( pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8" )
    @ApiModelProperty( value = "创建时间开始条件", name = "createDateBegin" )
    protected Date createDateBegin;
    @JsonFormat( pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8" )
    @ApiModelProperty( value = "创建时间结束条件", name = "createDateEnd" )
    protected Date createDateEnd;
    @ApiModelProperty( value = "创建人员Id", name = "createUser" )
    protected String createUser;
    @ApiModelProperty( value = "创建用户部门Id", name = "createUserDept" )
    protected String createUserDept;
    @ApiModelProperty(value="创建用户组织", name="createUserOrg")
    protected String createUserOrg;
    @ApiModelProperty(value="删除标志位", name="deleteFlag")
    protected Integer deleteFlag = new Integer(0);
    @ApiModelProperty(value="预留时间字段1", name="extDate1")
    protected Date extDate1;
    @ApiModelProperty(value="预留时间字段1查询开始条件", name="extDate1Begin")
    protected Date extDate1Begin;
    @ApiModelProperty(value="预留时间字段1查询结束条件", name="extDate1End")
    protected Date extDate1End;
    @ApiModelProperty(value="预留时间字段2", name="extDate2")
    protected Date extDate2;
    @ApiModelProperty(value="预留时间字段2查询开始条件", name="extDate2Begin")
    protected Date extDate2Begin;
    @ApiModelProperty(value="预留时间字段2查询结束条件", name="extDate2End")
    protected Date extDate2End;
    @ApiModelProperty(value="预留数字字段1", name="extNum1")
    protected BigDecimal extNum1;
    @ApiModelProperty(value="预留数字字段2", name="extNum2")
    protected BigDecimal extNum2;
    @ApiModelProperty(value="预留数字字段3", name="extNum3")
    protected BigDecimal extNum3;
    protected String dataAccessDynamicSQL;
    @ApiModelProperty(value="预留字符字段1", name="extStr1")
    protected String extStr1;
    @ApiModelProperty(value="预留字符字段2", name="extStr2")
    protected String extStr2;
    @ApiModelProperty(value="预留字符字段3", name="extStr3")
    protected String extStr3;
    @ApiModelProperty(value="预留字符字段4", name="extStr4")
    protected String extStr4;
    @ApiModelProperty(value="预留字符字段5", name="extStr5")
    protected String extStr5;
    protected String id;
    private Map<String, OrderType> orderByMap = new LinkedHashMap();
    protected final transient Logger log = Logger.getLogger(this.getClass());
    @JsonFormat( pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value="修改时间", name="modifyDate")
    protected Date modifyDate;
    @ApiModelProperty(value="修改时间查询开始条件", name="modifyDateBegin")
    protected Date modifyDateBegin;
    @ApiModelProperty(value="修改时间查询结束条件", name="modifyDateEnd")
    protected Date modifyDateEnd;
    @ApiModelProperty(value="新修改时间", name="modifyDateNew")
    protected Date modifyDateNew;
    @ApiModelProperty(value="修改人员", name="modifyUser")
    protected String modifyUser;
    @ApiModelProperty(value="安全系数", name="weight")
    private Integer weight;
    @ApiModelProperty(value="用于批量删除时传入主健的ids用逗号分隔", name="primaryKeys")
    public String[] primaryKeys;
    @ApiModelProperty(value="表格行号", name="tableRowNo")
    private Integer tableRowNo;

    public Integer getTableRowNo() {
        return tableRowNo;
    }

    public void setTableRowNo(Integer tableRowNo) {
        this.tableRowNo = tableRowNo;
    }

    public BaseBean() {}

    public Date getExtDate1End() {
        return this.extDate1End;
    }

    public void setExtDate1End(Date extDate1End) {
        this.extDate1End = extDate1End;
    }

    public Date getExtDate2Begin() {
        return this.extDate2Begin;
    }

    public void setExtDate2End(Date extDate2End) {
        this.extDate2End = extDate2End;
    }

    @Override
    public Integer getWeight() {
        return this.weight;
    }
    @Override
    public void setWeight(Integer weight) {
        this.weight = weight;
    }
    @Override
    public String getModifyUser() {
        return this.modifyUser;
    }
    @Override
    public Date getCreateDate() {
        return this.createDate;
    }

    public Date getCreateDateBegin() {
        return this.createDateBegin;
    }

    public Date getCreateDateEnd() {
        return this.createDateEnd;
    }
    @Override
    public String getCreateUser() {
        return this.createUser;
    }
    @Override
    public String getCreateUserDept() {
        return this.createUserDept;
    }
    @Override
    public String getCreateUserOrg() {
        return this.createUserOrg;
    }
    @Override
    public String getDefaultValue(String name) {
        return this.getDefaultValue(name, "");
    }
    @Override
    public String getDefaultValue(String name, String def) {
        try {
            if (name != null && name.length() > 0) {
                Method method = this.getClass().getMethod("get" + name.substring(0, 1).toUpperCase() + name.substring(1));
                Object obj = method.invoke(this);
                if (obj == null) {
                    return def;
                }

                return String.valueOf(obj);
            }
        } catch (SecurityException var5) {
            this.log.error("获取属性错误", var5);
        } catch (Exception var6) {
            this.log.error(var6);
        }

        return name;
    }
    @Override
    public Integer getDeleteFlag() {
        return this.deleteFlag;
    }
    @Override
    public Date getExtDate1() {
        return this.extDate1;
    }
    @Override
    public Date getExtDate2() {
        return this.extDate2;
    }
    @Override
    public BigDecimal getExtNum1() {
        return this.extNum1;
    }
    @Override
    public BigDecimal getExtNum2() {
        return this.extNum2;
    }
    @Override
    public BigDecimal getExtNum3() {
        return this.extNum3;
    }
    @Override
    public String getExtStr1() {
        return this.extStr1;
    }
    @Override
    public String getExtStr2() {
        return this.extStr2;
    }
    @Override
    public String getExtStr3() {
        return this.extStr3;
    }
    @Override
    public String getExtStr4() {
        return this.extStr4;
    }
    @Override
    public String getExtStr5() {
        return this.extStr5;
    }
    @Override
    public String getId() {
        return this.id;
    }
    @Override
    public String toLogMsg() {
        return this.toString();
    }

    @Override
    public Date getModifyDate() {
        return this.modifyDate;
    }
    public Date getModifyDateBegin() {
        return this.modifyDateBegin;
    }

    public Date getModifyDateEnd() {
        return this.modifyDateEnd;
    }
    @Override
    public Date getModifyDateNew() {
        return this.modifyDateNew;
    }
    @Override
    public String[] getPrimaryKeys() {
        return this.primaryKeys;
    }
    @Override
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setCreateDateBegin(Date createDateBegin) {
        this.createDateBegin = createDateBegin;
    }

    public void setCreateDateEnd(Date createDateEnd) {
        this.createDateEnd = createDateEnd;
    }
    @Override
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
    @Override
    public void setCreateUserDept(String createUserDept) {
        this.createUserDept = createUserDept;
    }
    @Override
    public void setCreateUserOrg(String createUserOrg) {
        this.createUserOrg = createUserOrg;
    }
    @Override
    public void setDefaultValue(String name, String def) {
        try {
            if (name != null && name.length() > 0) {
                Class[] argsClass = new Class[]{String.class};
                Method method = this.getClass().getMethod("set" + name.substring(0, 1).toUpperCase() + name.substring(1), argsClass);
                method.invoke(this, def);
            }
        } catch (SecurityException var5) {
            this.log.error("获取属性错误", var5);
        } catch (Exception var6) {
            this.log.error(var6);
        }

    }
    @Override
    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
    @Override
    public void setExtDate1(Date extDate1) {
        this.extDate1 = extDate1;
    }

    public void setExtDate1Begin(Date extDate1Begin) {
        this.extDate1Begin = extDate1Begin;
    }

    public Date getExtDate1Begin() {
        return this.extDate1Begin;
    }

    public void setExtDate2Begin(Date extDate2Begin) {
        this.extDate2Begin = extDate2Begin;
    }

    public Date getExtDate2End() {
        return this.extDate2End;
    }
    @Override
    public void setExtDate2(Date extDate2) {
        this.extDate2 = extDate2;
    }
    @Override
    public void setExtNum1(BigDecimal extNum1) {
        this.extNum1 = extNum1;
    }
    @Override
    public void setExtNum2(BigDecimal extNum2) {
        this.extNum2 = extNum2;
    }
    @Override
    public void setExtNum3(BigDecimal extNum3) {
        this.extNum3 = extNum3;
    }
    @Override
    public void setExtStr1(String extStr1) {
        this.extStr1 = extStr1;
    }
    @Override
    public void setExtStr2(String extStr2) {
        this.extStr2 = extStr2;
    }
    @Override
    public void setExtStr3(String extStr3) {
        this.extStr3 = extStr3;
    }
    @Override
    public void setExtStr4(String extStr4) {
        this.extStr4 = extStr4;
    }
    @Override
    public void setExtStr5(String extStr5) {
        this.extStr5 = extStr5;
    }
    @Override
    public void setId(String id) {
        this.id = id;
        this.getIdForLogExt(id);
    }

    public void getIdForLogExt(String id) {
        String str = this.getClass().getCanonicalName();
        if (id != null) {
            OperLogUtils.setOperObjMap(str.substring(str.lastIndexOf(".") + 1), id.toString());
        } else {
            OperLogUtils.clearOperObjMap(str.substring(str.lastIndexOf(".") + 1));
        }

    }

    @Override
    public void setModifyDate(Date modifyDate) {
        if (modifyDate != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String mdate = format.format(modifyDate);

            try {
                this.modifyDate = format.parse(mdate);
            } catch (ParseException var5) {
                var5.printStackTrace();
            }
        } else {
            this.modifyDate = modifyDate;
        }

    }

    public void setModifyDateBegin(Date modifyDateBegin) {
        this.modifyDateBegin = modifyDateBegin;
    }

    public void setModifyDateEnd(Date modifyDateEnd) {
        this.modifyDateEnd = modifyDateEnd;
    }
    @Override
    public void setModifyDateNew(Date modifyDateNew) {
        if (modifyDateNew != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String mdate = format.format(modifyDateNew);

            try {
                this.modifyDateNew = format.parse(mdate);
            } catch (ParseException var5) {
                var5.printStackTrace();
            }
        } else {
            this.modifyDateNew = modifyDateNew;
        }

    }
    @Override
    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }
    @Override
    public void setPrimaryKeys(String[] primaryKeys) {
        this.primaryKeys = primaryKeys;
        this.getPrimaryKeysForLogExt(primaryKeys);
    }

    public void getPrimaryKeysForLogExt(String[] primaryKeys) {
        String str = this.getClass().getCanonicalName();
        if (primaryKeys != null && primaryKeys.length > 0) {
            OperLogUtils.clearOperObjMap(str.substring(str.lastIndexOf(".") + 1));
            OperLogUtils.setOperObjsMap(str.substring(str.lastIndexOf(".") + 1), StringUtil.array2Str(primaryKeys));
        } else {
            OperLogUtils.clearOperObjsMap(str.substring(str.lastIndexOf(".") + 1));
        }

    }
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
    @Override
    public void addOrderByField(String fieldName) {
        this.addOrderByField(fieldName, OrderType.ASC);
    }
    @Override
    public void addOrderByFieldDesc(String fieldName) {
        this.addOrderByField(fieldName, OrderType.DESC);
    }
    @Override
    public void addOrderByField(String fieldName, OrderType orderType) {
        this.orderByMap.put(fieldName, orderType);
    }
    @Override
    public String getOrderBy() {
        int size = this.orderByMap.size();
        if (size == 0) {
            return null;
        } else {
            StringBuffer buffer = new StringBuffer();
            Iterator iterator = this.orderByMap.entrySet().iterator();

            while(iterator.hasNext()) {
                Map.Entry<String, OrderType> entry = (Map.Entry)iterator.next();
                buffer.append((String)entry.getKey()).append(" ").append(entry.getValue()).append(",");
            }

            return buffer.substring(0, buffer.length() - 1);
        }
    }
    @Override
    public void setOrderBy(String orderBy) {
        this.orderByMap.clear();
        if (!StringUtil.isEmpty(orderBy)) {
            String[] orders = orderBy.split(",");

            for(int i = 0; i < orders.length; ++i) {
                String order = orders[i];
                String[] fieldAndOrderType = order.split("\\s+");
                String field = fieldAndOrderType[0];
                OrderType orderType = OrderType.ASC;
                if (fieldAndOrderType.length != 1) {
                    orderType = OrderType.valueOf(fieldAndOrderType[1].toUpperCase());
                }

                this.orderByMap.put(field, orderType);
            }

        }
    }
    @Override
    public String getDataAccessDynamicSQL() {
        return this.dataAccessDynamicSQL;
    }
    @Override
    public void setDataAccessDynamicSQL(String dataAccessDynamicSQL) {
        this.dataAccessDynamicSQL = dataAccessDynamicSQL;
    }
}
