package com.jc.common.db;

import com.jc.common.db.parse.SqlParseTool;
import com.jc.common.db.parse.SqlProcess;
import com.jc.common.db.parse.domain.SqlVO;
import com.jc.common.kit.vo.PageManagerEx;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.GlobalContext;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;

/**
 * @author lc Administrator
 * @description MYSQL 操作
 */
public class BusiDao {
    //当前线程缓存
    private ThreadLocal<Connection> connCache = new ThreadLocal<>();
    // 数据源
    private String url = GlobalContext.getProperty("jdbc.server.url");

    private String username = GlobalContext.getProperty("jdbc.server.username");

    private String password = GlobalContext.getProperty("jdbc.server.password");
    //连接
    private Connection conn;

    /**
     * DB服务
     *
     * @return
     * @throws CustomException
     * @description 实例化
     */
    public static BusiDao getInstance() throws CustomException {
        return new BusiDao();
    }

    /**
     * @throws Exception
     * @description 初始化
     */
    private BusiDao() throws CustomException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            Properties jdbcProperties = new Properties();
            jdbcProperties.put("user", username);
            jdbcProperties.put("password", password);
            conn = connCache.get();
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(url, jdbcProperties);
                connCache.set(conn);
            }
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), e);
        }
    }


    /**
     * 分页查询
     *
     * @param sql
     * @param paraMap
     * @param inPage
     * @return
     * @throws CustomException
     */
    public PageManagerEx<Map<String, Object>> queryByPage(String sql, Map<String, Object> paraMap, PageManager inPage) throws CustomException {
        // 取得连接
        PreparedStatement stmtCount = null;
        PreparedStatement stmtQuery = null;
        ResultSet rsCount = null;
        ResultSet rsQuery = null;
        try {
            PageManagerEx<Map<String, Object>> resPage = new PageManagerEx<Map<String, Object>>();
            resPage.setPageRows(inPage.getPageRows());
            if (resPage.getPageRows() < 0) {
                resPage.setPageRows(10);
            }
            resPage.setPage(inPage.getPage());
            if (resPage.getPage() < 0) {
                resPage.setPage(0);
            }
            // 解析SQL
            SqlVO vo = SqlProcess.action(sql, paraMap);
            // 分析出数量和列表查询语句
            String countSql = SqlParseTool.pageCountSql(vo.getSql());
            String querySql = SqlParseTool.pageQuerySql(vo.getSql());
            System.out.println(countSql);
            stmtCount = conn.prepareStatement(countSql);
            Object value;
            StringBuilder sqlPara = new StringBuilder();
            for (int i = 0; i < vo.getVoList().size(); i++) {
                value = vo.getVoList().get(i);
                sqlPara.append(value).append(" ");
                stmtCount.setObject(i + 1, value);
            }
            System.out.println(sqlPara.toString());
            rsCount = stmtCount.executeQuery();
            Integer count = null;
            if (rsCount.next()) {
                count = rsCount.getInt(1);
            }
            resPage.setTotalCount(count == null ? 0 : count);

            int totalPages = count / resPage.getPageRows();
            if (count % resPage.getPageRows() > 0) {
                totalPages++;
            }
            resPage.setTotalPages(totalPages);

            if (resPage.getPage() >= totalPages) {
                resPage.setPage(totalPages - 1);
            }
            if (resPage.getPage() < 0) {
                resPage.setPage(0);
            }
            StringBuilder limit = new StringBuilder();
            limit.append(" limit ").append(resPage.getPage() * resPage.getPageRows()).append(",").append(resPage.getPageRows()).append(" ");
            System.out.println(querySql + limit.toString());
            stmtQuery = conn.prepareStatement(querySql + limit.toString());
            StringBuilder sqlParaQuery = new StringBuilder();
            for (int i = 0; i < vo.getVoList().size(); i++) {
                value = vo.getVoList().get(i);
                sqlParaQuery.append(value).append(" ");
                stmtQuery.setObject(i + 1, value);
            }
            System.out.println(sqlParaQuery.toString());
            rsQuery = stmtQuery.executeQuery();
            ResultSetMetaData data = rsQuery.getMetaData();
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            while (rsQuery.next()) {
                Map<String, Object> itemMap = new HashMap<String, Object>();
                for (int i = 1; i <= data.getColumnCount(); i++) {
                    itemMap.put(data.getColumnLabel(i), rsQuery.getObject(i));
                }
                list.add(itemMap);
            }
            resPage.setData(list);
            return resPage;
        } catch (Exception e) {
            throw new CustomException(e.getMessage(), e);
        } finally {
            if (rsCount != null) {
                try {
                    rsCount.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (rsQuery != null) {
                try {
                    rsQuery.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (stmtCount != null) {
                try {
                    stmtCount.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (stmtQuery != null) {
                try {
                    stmtQuery.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 查询所有列
     *
     * @param sql
     * @param paraMap
     * @return
     * @throws CustomException
     */
    public List<Map<String, Object>> queryForList(String sql, Map<String, Object> paraMap) throws CustomException {
        // 取得连接
        PreparedStatement stmt = null;
        try {
            // 解析SQL
            SqlVO vo = SqlProcess.action(sql, paraMap);
            System.out.println(vo.getSql());
            stmt = conn.prepareStatement(vo.getSql());
            Object value;
            SimpleDateFormat f1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            StringBuilder sqlPara = new StringBuilder();
            for (int i = 0; i < vo.getVoList().size(); i++) {
                value = vo.getVoList().get(i);
                if (value != null && value instanceof Date) {
                    sqlPara.append(f1.format((Date) value)).append(" ");
                } else {
                    sqlPara.append(value).append(" ");
                }

                stmt.setObject(i + 1, value);
            }
            System.out.println(sqlPara.toString());
            ResultSet rs = stmt.executeQuery();
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            ResultSetMetaData data = rs.getMetaData();

            while (rs.next()) {
                Map<String, Object> itemMap = new HashMap<String, Object>();
                for (int i = 1; i <= data.getColumnCount(); i++) {
                    itemMap.put(data.getColumnLabel(i), rs.getObject(i));
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

}
