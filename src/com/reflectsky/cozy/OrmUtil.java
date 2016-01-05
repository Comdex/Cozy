package com.reflectsky.cozy;

import com.reflectsky.cozy.core.TableInfo;

/**
 * ORM实用接口
 * @author Comdex
 */
public interface OrmUtil {
	public TableInfo getTableInfo(Class clazz) throws Exception;
	public String generateTableCreateSql(TableInfo tbinfo);
}

