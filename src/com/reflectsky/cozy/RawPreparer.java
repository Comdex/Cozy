package com.reflectsky.cozy;

import java.sql.ResultSet;


/**
 * 原生SQL预查询接口
 * @author Comdex
 */
public interface RawPreparer {
	
	/**
	 *根据传入的SQL占位符参数执行预编译SQL语句
	 * @author Comdex
	 * @param objs
	 * @return int 返回收影响记录条数，返回-1代表执行失败
	 */
	public int exec(Object... objs);
	
	/**
	 *释放原生SQL预查询对象的资源
	 * @author Comdex
	 * @return boolean true为执行成功,false为执行失败
	 */
	public boolean close();
}
