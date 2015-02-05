package com.reflectsky.cozy;

import java.util.List;
import java.util.Map;

/**
 * 原生SQL查询接口
 * @author Comdex
 */
public interface RawSet {
	/**
	 *执行原生SQL命令
	 * @author Comdex
	 * @return 受影响记录条数，返回-1代表执行失败 
	 */
	public int exec();
	
	/**
	 *把符合条件的数据库记录赋值到对应对象
	 * @author Comdex
	 * @param obj
	 * @return boolean true为执行成功，false为失败
	 */
	public boolean queryRow(Object obj);
	
	/**
	 *把符合条件的数据库记录赋值到对应的List
	 * @author Comdex
	 * @param list 被赋值的List
	 * @param clazz List中的泛型种类
	 * @return int 受影响数据库记录条数，返回-1代表执行失败
	 */
	public int queryRows(List list,Class clazz);
	
	/**
	 *为原生SQL查询对象设置参数
	 * @author Comdex
	 * @param objects 要设置的参数，多个
	 * @return RawSet 原生SQL查询对象
	 */
	public RawSet setArgs(Object... objects);
	
	/**
	 *把一条数据库记录映射到Map中,如果有多条只取第一条
	 * @author Comdex
	 * @param map 被映射的Map 
	 * @param name 需要被映射的数据库记录的字段(可选，多个)
	 * @return int 受影响记录条数，返回-1代表失败
	 */
	public int rowsToMap(Map<String,Object> map,String...name);
	
	/**
	 *把一条数据库记录映射到对象obj中，如果有多条只取第一条
	 * @author Comdex
	 * @param obj 被映射的对象
	 * @param name 需要被映射的数据库记录的字段(可选，多个)
	 * @return int 收影响记录条数，返回-1代表失败 
	 */
	public int rowsToObject(Object obj,String...name);
	
	/**
	 *返回一个SQL预查询对象
	 * @author Comdex
	 * @return RawPreparer 
	 */
	public RawPreparer prepare();
}
