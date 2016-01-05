package com.reflectsky.cozy.mongodriver;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import com.reflectsky.cozy.Ormer;
import com.reflectsky.cozy.QuerySet;
import com.reflectsky.cozy.RawSet;
import com.reflectsky.cozy.core.OrmManager;

public class MongodbOrmImpl implements Ormer {

	@Override
	public boolean read(Object obj, String... fieldnames) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public boolean insert(Object obj) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public int update(Object obj, String... fieldnames) {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public int delete(Object obj) {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public void addCallback(Object obj) {
		// TODO 自动生成的方法存根

	}

	@Override
	public RawSet raw(String sql, Object... params) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public QuerySet queryTable(Object obj) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public void begin() {
		// TODO 自动生成的方法存根

	}

	@Override
	public boolean commit() {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public void rollback() {
		// TODO 自动生成的方法存根

	}

	@Override
	public Statement createStatement() {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public PreparedStatement createPreparedStatement(String sql) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public void close() {
		// TODO 自动生成的方法存根

	}

	@Override
	public void setConnection(Object conn) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void setOrmManager(OrmManager omanager) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public int insertMulti(List objects) {
		// TODO 自动生成的方法存根
		return 0;
	}

}
