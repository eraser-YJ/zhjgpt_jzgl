package com.jc.common.db.dialect.mysql;

import com.jc.common.db.dialect.mysql.column.IMysqlColumnType;
import com.jc.common.db.dialect.mysql.column.type.TypeBigint;
import com.jc.common.db.dialect.mysql.column.type.TypeDatetime;
import com.jc.common.db.dialect.mysql.column.type.TypeDecimal;
import com.jc.common.db.dialect.mysql.column.type.TypeDic;
import com.jc.common.db.dialect.mysql.column.type.TypeInt;
import com.jc.common.db.dialect.mysql.column.type.TypeLongText;
import com.jc.common.db.dialect.mysql.column.type.TypeVarchar;

/**
 * @description 所有支持的数据库类型
 * @author lc  Administrator
 */
public enum MysqlColumnType {
	// 字符
	varchar("varchar", "字符", new TypeVarchar()),
	// 字符
	longtext("longtext", "长字段", new TypeLongText()),
	// 时间
	datetime("datetime", "日期", new TypeDatetime()),
	// 整型
	intEx("int", "整型", new TypeInt()),
	// 主键
	bigint("bigint", "长整型", new TypeBigint()),
	// 浮动性
	decimal("decimal", "浮点型", new TypeDecimal()),
	// 字典
	dic("dic", "字典", new TypeDic());

	private String strCode;
	private String strName;
	private IMysqlColumnType service;

	MysqlColumnType(String inStrCode, String inStringName, IMysqlColumnType typeService) {
		strCode = inStrCode;
		strName = inStringName;
		service = typeService;
	}

	public String getStrCode() {
		return strCode;
	}

	public IMysqlColumnType getService() {
		return service;
	}

	public String getStrName() {
		return strName;
	}

	public void setStrName(String strName) {
		this.strName = strName;
	}

	public static MysqlColumnType getType(String type) {
		for (MysqlColumnType ctype : MysqlColumnType.values()) {
			if (ctype.getStrCode().equalsIgnoreCase(type)) {
				return ctype;
			}
		}
		return null;
	}
}
