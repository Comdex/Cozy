package com.reflectsky.cozy.core;

import java.util.Vector;

/**
 * 模型对应数据库表信息保存类
 * @author Comdex
 */
public class TableInfo {
	//表名
	private String tableName;
	//类名
	private String typeName;
	
	private Vector<FieldInfo> fields;
	
	public TableInfo(){
		fields = new Vector<FieldInfo>();
	}
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public void setTypeName(String typeName){
		this.typeName = typeName;
	}
	
	public String getTypeName(){
		return this.typeName;
	}
	

	public void setFields(Vector<FieldInfo> fields) {
		this.fields = fields;
	}
	
	public void addFieldInfo(FieldInfo fin){
		this.fields.add(fin);
	}
	
	public FieldInfo getFieldInfo(int index){
		return this.fields.get(index);
	}
	
	public Vector<FieldInfo> getAllFieldInfos(){
		return this.fields;
	}
}
