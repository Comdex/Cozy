package com.reflectsky.cozy.core;

import java.util.HashMap;
import java.util.Map;

/**
 * 模型注册信息缓存
 * @author Comdex
 */
public class TableCache {
	private  Map<String, TableInfo> cache;
	private  Map<String, TableInfo> cacheByTN;
	
	public TableCache(){
		cache = new HashMap<String,TableInfo>();
		cacheByTN = new HashMap<String,TableInfo>();
	}
	
	public Map<String, TableInfo> All(){
		return cache;
	}
	
	public TableInfo get(String tableName){
		return cache.get(tableName);
	}
	//根据类型名得到tableinfo
	public TableInfo getByTN(String typename){
		return cacheByTN.get(typename);
	}
	
	public void set(TableInfo ti){
		cache.put(ti.getTableName(), ti);
		cacheByTN.put(ti.getTypeName(), ti);
	}
	
	public void clean(){
		cache = new HashMap<String,TableInfo>();
		cacheByTN = new HashMap<String,TableInfo>();
	}
}
