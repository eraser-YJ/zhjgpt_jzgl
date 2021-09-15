package com.jc.system.common.util;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.foundation.util.StringUtil;
import com.jc.system.common.domain.CascadeBusinessSetting;
import com.jc.system.common.domain.CascadeSetting;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @date 2020-06-30
 */public class DeleteCascadeUtils {
	private static final Logger logger = Logger.getLogger(DeleteCascadeUtils.class);
	private static JdbcTemplate jdbcTemplate;
	//private static Jaxb2Marshaller unmarshaller;
	private static CascadeBusinessSetting deleteCascade;

	/**
	 * 初始化JDBC和OXM
	 */
	static {
		// 初始化JDBC
		jdbcTemplate = new JdbcTemplate((DataSource) SpringContextHolder.getBean("dataSource"));
		// 加载配置文件
		InputStream fis = null;
		try {
            deleteCascade = new CascadeBusinessSetting();
			fis = DeleteCascadeUtils.class
					.getResourceAsStream("/delete-cascade.xml");
            if(fis == null){
                DeleteCascadeUtils.class.getClassLoader()
                        .getResourceAsStream("/delete-cascade.xml");
            }
			//因jaxb jar引用冲突用dom4j重写
			SAXReader reader = new SAXReader();
			Document document = reader.read(fis);
			Element rootElement = document.getRootElement();
			List<Element> functionElementList = rootElement.elements("function");
            CascadeSetting[] functionArray = new CascadeSetting[functionElementList.size()];
            for (int i = 0; i < functionElementList.size(); i++) {
                Element element  = functionElementList.get(i);
                CascadeSetting function = new CascadeSetting();
                function.setId(element.attributeValue("id"));
                function.setTableName(element.attributeValue("table-name"));
                function.setComment(element.attributeValue("comment"));
                function.setColumnName(element.attributeValue("column-name"));
                function.setPiIdColumn(element.attributeValue("piIdColumn"));
                function.setWorkflowVaildClass(element.attributeValue("workflowVaildClass"));
                List<Element> refTableElementList = element.elements("ref-table");
                int refTableSize  = refTableElementList.size();
                if(refTableSize>0){
                    CascadeSetting[] refTableArray = new CascadeSetting[refTableSize];
                    for (int j = 0; j < refTableSize; j++) {
                        Element refTableElement  = refTableElementList.get(j);
                        CascadeSetting refTable  = new CascadeSetting();
                        refTable.setId(refTableElement.attributeValue("id"));
                        refTable.setTableName(refTableElement.attributeValue("table-name"));
                        refTable.setComment(refTableElement.attributeValue("comment"));
                        refTable.setColumnName(refTableElement.attributeValue("column-name"));
                        refTable.setPiIdColumn(refTableElement.attributeValue("piIdColumn"));
                        refTable.setWorkflowVaildClass(refTableElement.attributeValue("workflowVaildClass"));
                        refTableArray[j] = refTable;
                    }
                    function.setRefTable(refTableArray);
                }
                functionArray[i] = function;
            }
            deleteCascade.setFunction(functionArray);
		} catch(Exception ex){
			logger.error("init delete cascade error",ex);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
	}

    public static void main(String[] args){
        new DeleteCascadeUtils();
    }


	public DeleteCascadeUtils() {
	}

	/**
	 * 判断指定表指定记录是否可删除
	 * @param id 待删除记录的id
	 * @param columnValue 待删除记录的值
	 * @return 为可删除，false 不可删除
	 */
	public static boolean canDelete(String id, String columnValue) {
		boolean inuse = false;
		StringBuffer sql = new StringBuffer();
		// 在级联删除配置中查找匹配的Function
		for (CascadeSetting setting : deleteCascade.getFunction()) {
			if (setting.getId().equals(id)) {
				// 在匹配的Function中，查询关联表是否有未删除的记录关联了主表这条记录
				for (CascadeSetting refTable : setting.getRefTable()) {
					if (sql.length() > 0) {
                        sql.delete(0, sql.length());
                    }
					sql.append("select count(id) from ")
							.append(refTable.getTableName())
							.append(" where delete_flag='0' and ")
							.append(refTable.getColumnName()).append("='")
							.append(columnValue).append("'");
					int count = jdbcTemplate.queryForObject(sql.toString(),Integer.class);
					if (count > 0) {
						inuse |= true;
						break;
					}
				}

				if (inuse == true) {
                    break;
                }
			}

		}
		return !inuse;
	}

	/**
	 * 判断指定表指定记录是否可删除
	 * @param id 待删除记录的id
	 * @param columnValue 待删除记录的值
	 * @return true 为可删除，false 不可删除
	 */
	public static boolean canBatchDelete(String id, String columnValue) {
		if (StringUtil.isEmpty((String) columnValue)) {
			return true;
		}
        String[] value = ((String) columnValue).split(",");

		boolean inuse = false;
		StringBuffer sql = new StringBuffer();
		// 在级联删除配置中查找匹配的Function
		for (CascadeSetting setting : deleteCascade.getFunction()) {
			if (setting.getId().equals(id)) {
				// 在匹配的Function中，查询关联表是否有未删除的记录关联了主表这条记录
				for (CascadeSetting refTable : setting.getRefTable()) {
					if (sql.length() > 0) {
                        sql.delete(0, sql.length());
                    }
					sql.append("select count(id) from ").append(refTable.getTableName()).append(" where delete_flag='0' and (1=2 ");
					for (String v : value) {
						sql.append(" or ").append(refTable.getColumnName()).append("='").append(v).append("'");
					}
					sql.append(")");
					int count = jdbcTemplate.queryForObject(sql.toString(),Integer.class);
					if (count > 0) {
						inuse |= true;
						break;
					}
				}

				if (inuse == true) {
                    break;
                }
			}

		}
		return !inuse;
	}

	/**
	 * 删除数据(工作流)
	 * @param tableName 待删除记录的表名
	 * @param columnValue 待删除记录的值
	 * @throws CustomException
	 */
	public static void deleteDataForWorkflow(String tableName, Object columnValue) throws CustomException{
		for (CascadeSetting setting : deleteCascade.getFunction()) {
			if (setting.getTableName().equals(tableName)) {
				// 查询对应的数据
				String queryTable = "select * from " + tableName + " where " + setting.getPiIdColumn() + " = '" + columnValue + "' and delete_flag='0'";
				List<Map<String, Object>> result = jdbcTemplate.queryForList(queryTable);
				for (Map<String, Object> item : result) {
					if (setting.getRefTable() != null) {
						// 对关联表进行递归删除
						for (CascadeSetting refTable : setting.getRefTable()) {
							deleteData(refTable.getTableName(), refTable.getColumnName(), item.get(setting.getColumnName()));
						}
					}
				}
				String deleteSql = "update " + setting.getTableName() + " set delete_flag = 1 where " + setting.getPiIdColumn() + " = '" + columnValue + "'";
				jdbcTemplate.update(deleteSql);
			}
		}
	}

	/**
	 * 方法描述：删除数据(工作流)
	 * 
	 * @param tableName
	 *            待删除记录的表名
	 * @param columnName
	 *            待删除记录的列名
	 * @param columnValue
	 *            待删除记录的值
	 * @return true 为可删除，false 不可删除
	 */
	public static void deleteData(String tableName, String columnName, Object columnValue) {
		for (CascadeSetting setting : deleteCascade.getFunction()) {
			if (setting.getTableName().equals(tableName)) {
				// 查询对应的数据
				String queryTable = "select * from " + tableName + " where " + columnName + " = '" + columnValue + "' and delete_flag='0'";
				List<Map<String, Object>> result = jdbcTemplate.queryForList(queryTable);
				for (Map<String, Object> item : result) {
					// 对关联表进行递归删除
					for (CascadeSetting refTable : setting.getRefTable()) {
						deleteData(refTable.getTableName(), refTable.getColumnName(), item.get(columnValue));
					}
				}
			}
		}
		String deleteSql = "update " + tableName + " set delete_flag = 1 where " + columnName + " = '" + columnValue + "'";
		jdbcTemplate.update(deleteSql);
	}

	/**
	 * 方法描述：检查指定值的记录是否存在
	 * @param functionId
	 * @param columnValue
	 * @return
	 */
	public static boolean checkExist(String functionId, String columnValue) {
		StringBuffer sql = new StringBuffer();
		// 在级联配置中查找匹配的Function
		for (CascadeSetting setting : deleteCascade.getFunction()) {
			if (setting.getId().equals(functionId)) {
				if (sql.length() > 0) {
                    sql.delete(0, sql.length() - 1);
                }
				sql.append("select count(id) from ")
						.append(setting.getTableName())
						.append(" where delete_flag='0' and ")
						.append(setting.getColumnName()).append("='")
						.append(columnValue).append("'");
				int count = jdbcTemplate.queryForObject(sql.toString(),Integer.class);
				if (count > 0) {
					return true;
				}
			}
		}
		return false;
	}
}
