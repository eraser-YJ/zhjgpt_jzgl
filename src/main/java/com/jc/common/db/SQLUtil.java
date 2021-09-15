package com.jc.common.db;

public class SQLUtil {

	/**
	 * @Document 是否SQL注入
	 * @param sql
	 * @return
	 */
	public static boolean isAccessSql(String sql) {
		if (sql == null || sql.trim().length() == 0) {
			return false;
		}
		String sqlStr = sql == null ? "" : sql.toUpperCase().trim();
		if (sqlStr.indexOf("DELETE ") >= 0) {
			return true;
		}
		if (sqlStr.indexOf("INSERT ") >= 0) {
			return true;
		}
		if (sqlStr.indexOf("UPDATE ") >= 0) {
			return true;
		}
		if (sqlStr.indexOf("DROP ") >= 0) {
			return true;
		}
		if (sqlStr.indexOf("TRANCATE ") >= 0) {
			return true;
		}
		if (sqlStr.indexOf("CREATE ") >= 0) {
			return true;
		}
		if (sqlStr.indexOf("ALERT ") >= 0) {
			return true;
		}
		if (sqlStr.indexOf("MODIFY ") >= 0) {
			return true;
		}
		if (sqlStr.indexOf("GRANT ") >= 0) {
			return true;
		}
		if (sqlStr.indexOf("ADD ") >= 0) {
			return true;
		}
		if (sqlStr.indexOf("REVOKE ") >= 0) {
			return true;
		}
		if (sqlStr.indexOf("USE ") >= 0) {
			return true;
		}
		return false;

	}
}
