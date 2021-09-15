package com.jc.common.db.dialect.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.jc.common.db.dialect.mysql.column.MysqlColumn;
import com.jc.dlh.cache.CacheDlhDbsource;
import com.jc.dlh.domain.DlhDbsource;
import com.jc.foundation.exception.CustomException;

/**
 * @description MYSQL 操作
 * @author lc Administrator
 */
public class MysqlDao {
	// 数据源
	private DlhDbsource source;
	// 连接
	private Connection conn;

	/**
	 * @description 实例化
	 * @param dbCode
	 * @return
	 * @throws CustomException
	 */
	public static MysqlDao getInstance(String dbCode) throws CustomException {
		// 创建查询连接
		DlhDbsource db = CacheDlhDbsource.queryByCode(dbCode);
		return new MysqlDao(db);
	}

	/**
	 * @description 初始化
	 * @param inSource
	 * @throws Exception
	 */
	private MysqlDao(DlhDbsource inSource) throws CustomException {
		try {
			source = inSource;
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			Properties jdbcProperties = new Properties();
			jdbcProperties.put("user", source.getDbUsername());
			jdbcProperties.put("password", source.getDbPassword());
			conn = DriverManager.getConnection(source.getDbAddress(), jdbcProperties);
		} catch (Exception e) {
			throw new CustomException(e.getMessage(), e);
		}
	}

	/**
	 * @description 是否自动提交
	 * @param autoCommit
	 * @throws Exception
	 */
	public void setAutoCommit(boolean autoCommit) throws CustomException {
		try {
			conn.setAutoCommit(autoCommit);
		} catch (Exception e) {
			throw new CustomException(e.getMessage(), e);
		}
	}

	/**
	 * @description 提交
	 * @throws Exception
	 */
	public void commit() throws CustomException {
		try {
			conn.commit();
		} catch (Exception e) {
			throw new CustomException(e.getMessage(), e);
		}
	}

	/**
	 * @description 回滚
	 * @throws Exception
	 */
	public void rollback() throws CustomException {
		try {
			conn.rollback();
		} catch (Exception e) {
			throw new CustomException(e.getMessage(), e);
		}
	}

	/**
	 * @description 创建
	 * @return
	 * @throws Exception
	 */
	public Statement createStatement() throws CustomException {
		try {
			return conn.createStatement();
		} catch (Exception e) {
			throw new CustomException(e.getMessage(), e);
		}
	}

	/**
	 * @description
	 * @param sql
	 * @return
	 * @throws Exception
	 */
	public PreparedStatement prepareStatement(String sql) throws CustomException {
		try {
			return conn.prepareStatement(sql);
		} catch (Exception e) {
			throw new CustomException(e.getMessage(), e);
		}
	}

	/**
	 * @description 关闭
	 */
	public void close() {
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 查询所有列
	 * 
	 * 
	 * @param conn
	 * @param table
	 * @return
	 * @throws Exception
	 */
	public <T> T queryForObject(String sql, Class<T> type) throws CustomException {
		// 取得连接
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				return rs.getObject(1, type);
			}
			return null;
		} catch (Exception e) {
			throw new CustomException(e.getMessage(), e);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 查询所有列
	 * 
	 * 
	 * @param conn
	 * @param table
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryForList(String sql) throws CustomException {
		// 取得连接
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			ResultSetMetaData data = rs.getMetaData();

			while (rs.next()) {
				Map<String, Object> itemMap = new HashMap<String, Object>();
				for (int i = 1; i <= data.getColumnCount(); i++) {
					itemMap.put(data.getColumnName(i), rs.getObject(i));
				}
				list.add(itemMap);
			}
			return list;
		} catch (Exception e) {
			throw new CustomException(e.getMessage(), e);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 查询所有列
	 * 
	 * 
	 * @param conn
	 * @param table
	 * @return
	 * @throws Exception
	 */
	public void execute(String sql) throws CustomException {
		// 取得连接
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (Exception e) {
			throw new CustomException(e.getMessage(), e);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 查询所有列
	 * 
	 * 
	 * @param conn
	 * @param table
	 * @return
	 * @throws Exception
	 */
	public MysqlTableVO queryTableMetaData(String table) throws CustomException {
		// 取得连接
		MysqlTableVO vo = null;
		PreparedStatement stmt = null;
		try {
			vo = new MysqlTableVO();
			vo.setTable(table);
			String sql = "select * from " + table + " where 1=2";
			stmt = conn.prepareStatement(sql);
			ResultSet rs = null;
			try {
				List<MysqlColumn> list = new ArrayList<MysqlColumn>();
				rs = stmt.executeQuery(sql);
				ResultSetMetaData data = rs.getMetaData();
				for (int i = 1; i <= data.getColumnCount(); i++) {
					MysqlColumn c = new MysqlColumn();
					c.setName(data.getColumnName(i));
					c.setDataType(data.getColumnTypeName(i).toUpperCase());
					list.add(c);
				}
				vo.setCols(list);
			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (Exception e) {
					}
				}
			}

			ResultSet pkRs = null;
			try {
				// 适用mysql
				pkRs = conn.getMetaData().getPrimaryKeys(conn.getCatalog().toUpperCase(), null, table.toUpperCase());
				// 适用oracle,mysql
				List<String> pkList = new ArrayList<String>();
				while (pkRs.next()) {
					pkList.add(pkRs.getString("COLUMN_NAME"));
				}
				vo.setPks(pkList.toArray(new String[0]));
			} finally {
				if (pkRs != null) {
					try {
						pkRs.close();
					} catch (Exception e) {
					}
				}
			}

		} catch (Exception e) {
			throw new CustomException(e.getMessage(), e);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return vo;
	}
}
