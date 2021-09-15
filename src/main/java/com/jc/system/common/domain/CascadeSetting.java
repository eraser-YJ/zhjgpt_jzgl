package com.jc.system.common.domain;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class CascadeSetting {

	public CascadeSetting() {
		
	}
	/**自定义业务ID*/
	private String id;
	/**表名*/
	private String tableName;
	/**列名*/
	private String columnName;
	/**描述*/
	private String comment;
	/**工作流程关联字段*/
	private String piIdColumn;
	/**流程删除验证类*/
	private String workflowVaildClass;
	/**关联表*/
	private CascadeSetting[] refTable;
	
	//@XmlAttribute (name="table-name")
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	//@XmlAttribute (name="column-name")
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	//@XmlAttribute (name="comment")
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	//@XmlElement(name="ref-table")
	public CascadeSetting[] getRefTable() {
		return refTable;
	}
	public void setRefTable(CascadeSetting[] refTable) {
		this.refTable = refTable;
	}
	
	//@XmlAttribute(name="id")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	//@XmlAttribute (name="piIdColumn")
	public void setPiIdColumn(String piIdColumn) {
		this.piIdColumn = piIdColumn;
	}
	
	public String getPiIdColumn() {
		return piIdColumn;
	}
	
	//@XmlAttribute (name="workflowVaildClass")
	public void setWorkflowVaildClass(String workflowVaildClass) {
		this.workflowVaildClass = workflowVaildClass;
	}
	
	public String getWorkflowVaildClass() {
		return workflowVaildClass;
	}
	
}
