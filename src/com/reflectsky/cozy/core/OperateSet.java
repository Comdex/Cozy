package com.reflectsky.cozy.core;

import java.util.Vector;

/**
 * @author Comdex
 *数据库查询实体
 */
public class OperateSet {
	//参数查询sql语句
	private String strSql = "";
	//参数值
	private Vector<Object> param ;
	//映射类
	private TableInfo tbinfo;
	
	//数据库中的自增键名
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
