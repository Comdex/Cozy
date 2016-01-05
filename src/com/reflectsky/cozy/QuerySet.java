
package com.reflectsky.cozy;

import java.util.List;
import java.util.Map;


/**
 * ORM查询接口
 * @author Comdex
 */
public interface QuerySet {
	/**
	 *根据过滤查询表达式和查询参数返回ORM查询对象
	 * @author Comdex
	 * @param expression 过滤查询表达式
	 * @param params 查询参数
	 * @return QuerySet
	 */
	public QuerySet filter(String expression,Object...params);
	
	/**
	 *根据排序查询表达式返回ORM查询对象
	 * @author Comdex
	 * @param expressions 排序查询表达式
	 * @return QuerySet
	 */
	public QuerySet orderBy(String...expressions);
	
	/**
	 *根据排除条件查询表达式和查询参数返回ORM查询对象
	 * @author Comdex
	 * @param expression 排除条件查询表达式
	 * @param params 排除条件查询参数
	 * @return QuerySet
	 */
	public QuerySet exclude(String expression,Object...params);
	
	/**
	 *计算符合条件的数据库记录条数
	 * @author Comdex
	 * @return long 数据库记录条数,-1为执行失败
	 */
	public long count();
	
	/**
	 *查询符合条件的数据库记录是否存在
	 * @author Comdex
	 * @return boolean true为存在,false为不存在
	 */
	public boolean exist();
	
	/**
	 *删除符合条件的数据库记录
	 * @author Comdex
	 * @return long 返回受影响的记录条数
	 */
	public long delete();
	
	/**
	 *把符合条件的数据库记录数据赋值给一个指定对象
	 * @author Comdex
	 * @param bean 被赋值的对象
	 * @param params 指定被赋值的数据库字段
	 * @return boolean true为执行成功，false为失败 
	 */
	public boolean one(Object bean,String...params);
	
	/**
	 *把符合条件的数据库记录数据赋值给指定的List对象
	 * @author Comdex
	 * @param list 被赋值的list对象
	 * @param clazz list所保存的泛型种类的class对象
	 * @param params 指定取数据的数据库记录的字段
	 * @return long 受影响的记录条数
	 */
	public long all(List list,Class clazz,String...params);
	
	/**
	 *限制最大返回数据行数，第二个参数可以设置 Offset
	 * @author Comdex
	 * @param int 限制返回行数
	 * @param long 偏移行数
	 * @return QuerySet
	 */
	public QuerySet limit(int count, long... offset);
	
	/**
	 *设置偏移行数
	 * @author Comdex
	 * @param long 偏移行数
	 * @return QuerySet
	 */
	public QuerySet offset(long offset);
	
	/**
	 * 返回结果集的 key => value值(List<Map<String,String>>)
     * key为 Model 里的 Field name，value 的值 以 string保存
	 * @author Comdex
	 * @return List<Map<String,String>>
	 */
	public List<Map<String,String>> values();
	
	/**
	 * 只返回特定的Field值，将结果集以String类型读取展开到单个list里
	 * @author Comdex
	 * @return List<String>
	 */
	public List<String> valuesFlat(String field);
}
