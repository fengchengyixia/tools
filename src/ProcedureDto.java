package com.sinosoft.undwrt.servlet;

public class ProcedureDto {
	private String procedureName;
	private String tableName;
	private String tableCName;
	private String insertSql;
	
	public String getProcedureName() {
		return procedureName;
	}
	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getTableCName() {
		return tableCName;
	}
	public void setTableCName(String tableCName) {
		this.tableCName = tableCName;
	}
	public String getInsertSql() {
		return insertSql;
	}
	public void setInsertSql(String insertSql) {
		this.insertSql = insertSql;
	}
	
}
