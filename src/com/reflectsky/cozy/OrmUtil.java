package com.reflectsky.cozy;

import com.reflectsky.cozy.core.TableInfo;

/**
 * ORMʵ�ýӿ�
 * @author Comdex
 */
public interface OrmUtil {
	public TableInfo getTableInfo(Class clazz) throws Exception;
	public String generateTableCreateSql(TableInfo tbinfo);
}

