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
		// TODO �Զ����ɵķ������
		return false;
	}

	@Override
	public boolean insert(Object obj) {
		// TODO �Զ����ɵķ������
		return false;
	}

	@Override
	public int update(Object obj, String... fieldnames) {
		// TODO �Զ����ɵķ������
		return 0;
	}

	@Override
	public int delete(Object obj) {
		// TODO �Զ����ɵķ������
		return 0;
	}

	@Override
	public void addCallback(Object obj) {
		// TODO �Զ����ɵķ������

	}

	@Override
	public RawSet raw(String sql, Object... params) {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public QuerySet queryTable(Object obj) {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public void begin() {
		// TODO �Զ����ɵķ������

	}

	@Override
	public boolean commit() {
		// TODO �Զ����ɵķ������
		return false;
	}

	@Override
	public void rollback() {
		// TODO �Զ����ɵķ������

	}

	@Override
	public Statement createStatement() {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public PreparedStatement createPreparedStatement(String sql) {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public void close() {
		// TODO �Զ����ɵķ������

	}

	@Override
	public void setConnection(Object conn) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public void setOrmManager(OrmManager omanager) {
		// TODO �Զ����ɵķ������
		
	}

	@Override
	public int insertMulti(List objects) {
		// TODO �Զ����ɵķ������
		return 0;
	}

}
