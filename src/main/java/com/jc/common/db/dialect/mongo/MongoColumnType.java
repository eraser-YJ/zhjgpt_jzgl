package com.jc.common.db.dialect.mongo;

import com.jc.common.db.dialect.mongo.column.IMongoColumnType;
import com.jc.common.db.dialect.mongo.column.type.TypeString;

/**
 * @description 所有支持的数据库类型
 * @author lc  Administrator
 */
public enum MongoColumnType {
	// 字符
	stringex("varchar", "字符", new TypeString());

	private String strCode;
	private String strName;
	private IMongoColumnType service;

	MongoColumnType(String inStrCode, String inStringName, IMongoColumnType typeService) {
		strCode = inStrCode;
		strName = inStringName;
		service = typeService;
	}

	public String getStrCode() {
		return strCode;
	}

	public IMongoColumnType getService() {
		return service;
	}

	public String getStrName() {
		return strName;
	}

	public void setStrName(String strName) {
		this.strName = strName;
	}

	public static MongoColumnType getType(String type) {
		for (MongoColumnType ctype : MongoColumnType.values()) {
			if (ctype.getStrCode().equalsIgnoreCase(type)) {
				return ctype;
			}
		}
		return null;
	}
}
