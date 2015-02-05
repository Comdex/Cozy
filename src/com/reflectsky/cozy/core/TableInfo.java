package com.reflectsky.cozy.core;

import java.util.Vector;

/**
 * ģ�Ͷ�Ӧ���ݿ����Ϣ������
 * @author Comdex
 */
public class TableInfo {
	//����
	private String tableName;
	//����
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
