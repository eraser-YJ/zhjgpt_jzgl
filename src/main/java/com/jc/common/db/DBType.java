package com.jc.common.db;

import com.jc.common.db.dialect.IDBDialect;
import com.jc.common.db.dialect.mongo.MongoDialect;
import com.jc.common.db.dialect.mysql.MysqlDialect;

public enum DBType {
	mysql(new MysqlDialect()), mongo(new MongoDialect());

	private IDBDialect service;

	DBType(IDBDialect dialectService) {
		service = dialectService;
	}

	public IDBDialect getService() {
		return service;
	}

	public static DBType getType(String type) {
		for (DBType ctype : DBType.values()) {
			if (ctype.toString().equalsIgnoreCase(type)) {
				return ctype;
			}
		}
		return null;
	}
}
