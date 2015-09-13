package com.reflectsky.cozy.apiimpl;

import com.reflectsky.cozy.OrmUtil;
import com.reflectsky.cozy.core.OrmManager;
import com.reflectsky.cozy.core.TableInfo;

public class MongodbOrmUtilImpl implements OrmUtil {
	private OrmManager oManager = null;
	
	public MongodbOrmUtilImpl(OrmManager oManager){
		this.oManager = oManager;
	}
	
	@Override
	public TableInfo getTableInfo(Class clazz) throws Exception {
		return null;
	}

	@Override
	public String generateTableCreateSql(TableInfo tbinfo) {
		return null;
	}

}
