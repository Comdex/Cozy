package com.reflectsky.cozy.core;

import java.util.Vector;

/**
 * @author Comdex
 *���ݿ��ѯʵ��
 */
public class OperateSet {
	//������ѯsql���
	private String strSql = "";
	//����ֵ
	private Vector<Object> param ;
	//ӳ����
	private TableInfo tbinfo;
	
	//���ݿ��е���������
	private String autoKeyName = "";
	
	public OperateSet(String strSql, Vector<Object> param, TableInfo tbinfo) {
		super();
		this.strSql = strSql;
		this.param = param;
		this.tbinfo = tbinfo;
	}
	
	public String getAutoKeyName() {
		return autoKeyName;
	}

	public void setAutoKeyName(String autoKeyName) {
		this.autoKeyName = autoKeyName;
	}

	public String getStrSql() {
		return strSql;
	}
	public void setStrSql(String strSql) {
		this.strSql = strSql;
	}
	public Vector<Object> getParam() {
		return param;
	}
	public void setParam(Vector<Object> param) {
		this.param = param;
	}
	public TableInfo getTbinfo() {
		return tbinfo;
	}
	public void setTbinfo(TableInfo tbinfo) {
		this.tbinfo = tbinfo;
	}
}
