package com.reflectsky.cozy;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import com.reflectsky.cozy.core.OrmManager;


/**
 * ORM接口
 * @author Comdex
 */
public interface Ormer {
	
	/**
	 *根据传入的对象查询数据库并赋值给该对象
	 *可指定读入数据库指定的字段给该对象
	 * @author Comdex
	 * @param obj  传入的对象，使用前务必注册
	 * @param fieldnames 指定读入的数据库字段(不是对象的字段名)，可选
	 * @return boolean true为执行成功，false为执行失败
	 */
	public boolean read(Object obj,String... fieldnames);

	/**
	 *根据传入的对象向数据库插入对应的数据
	 * @author Comdex
	 * @param obj 传入的对象
	 * @return boolean 返回false为失败
	 */
	public boolean insert(Object obj);

	/**
	 *根据传入的对象及指定的数据库字段更新数据库的记录
	 * @author Comdex
	 * @param obj 传入的对象
	 * @param fieldnames  与对象字段对应的数据库记录字段，可选
	 * @return int  返回0代表执行失败
	 */
	public int update(Object obj,String... fieldnames);

	/**
	 *根据传入的对象删除数据库指定的记录
	 * @author Comdex
	 * @param obj 传入的对象
	 * @return int  返回0代表执行失败
	 */
	public int delete(Object obj);
	
	/**
	 *根据传入的回调方法所在的对象开启回调支持
	 * @author Comdex
	 * @param obj 传入的对象
	 */
	public void addCallback(Object obj);
	
	/**
	 *根据传入的sql语句和占位符的参数(如果有)返回原生SQL查询对象RawSet
	 * @author Comdex
	 * @param sql 普通sql语句或含有占位符的sql语句
	 * @param params 可选，如果sql语句中有占位符务必指定该参数
	 * @return RawSet  原生SQL查询对象
	 */
	public RawSet raw(String sql,Object... params);
	
	/**
	 *根据传入的数据对象或数据库表名返回ORM查询对象
	 * @author Comdex
	 * @param obj 数据对象或数据库表名
	 * @return QuerySet ORM查询对象
	 */
	public QuerySet queryTable(Object obj);
	
	/**
	 *开始事务
	 * @author Comdex 
	 */
	public void begin();
	
	/**
	 *提交事务
	 * @author Comdex
	 * @return boolean true为执行成功
	 */
	public boolean commit();
	
	/**
	 *回滚事务
	 * @author Comdex 
	 */
	public void rollback();
	
	/**
	 *创建原生的JDBC Statement对象
	 * @author Comdex
	 * @return Statement
	 */
	public Statement createStatement();
	
	/**
	 *创建原生的JDBC PreparedStatement对象
	 * @author Comdex
	 * @param sql 需要预编译的SQL语句
	 * @return PreparedStatement
	 */
	public PreparedStatement createPreparedStatement(String sql);
	
	/**
	 *释放ORM的资源
	 * @author Comdex 
	 */
	public void close();
	
	/**
	 *仅供ORM管理器注入用
	 * @author Comdex
	 * @param conn 需要注入的connection
	 */
	public void setConnection(Object conn);
	
	/**
	 *仅供Orm管理器注入用
	 * @author Comdex
	 * @param OrmManager 需要注入的OrmManager
	 */
	public void setOrmManager(OrmManager omanager);
	
	/**
	 *同时插入多个对象
	 * @author Comdex
	 * @param objects 需要插入的对象list
	 * @return 执行插入的条数 
	 */
	public int insertMulti(List objects);
	
}
