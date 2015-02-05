package com.reflectsky.cozy.annoation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Cozy ORM 配置注解
 * @author Comdex
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Orm {
	/**
	 *设置类属性对应的数据库表列名
	 * @author Comdex
	 * @return String
	 */
	String column() default "";
	
	/**
	 *设置类属性是否作为主键,true为主键
	 * @author Comdex
	 * @return boolean
	 */
	boolean pk() default false;
	
	/**
	 *设置类属性是否作为自增长，true为自增长
	 * @author Comdex
	 * @return boolean
	 */
	boolean auto() default false;
	
	/**
	 *设置类属性是否被忽略,不被ORM记录
	 * @author Comdex
	 * @return boolean
	 */
	boolean ignore() default false;
	
	/**
	 *设置类属性对应的数据库类型
	 * @author Comdex
	 * @return 
	 */
	String type() default "";
	
	/**
	 *设置类属性对应的数据库类型的digits精度
	 * @author Comdex
	 * @return int
	 */
	int digits() default 0;
	
	/**
	 *设置类属性对应的数据库类型的decimals精度
	 * @author Comdex
	 * @return int
	 */
	int decimals() default 0;
	
	/**
	 *设置类属性对应的数据库类型的长度
	 * @author Comdex
	 * @return int
	 */
	int size() default 0;
	
	/**
	 *设置类属性的其他数据库配置
	 * @author Comdex
	 * @return String
	 */
	String other() default "";
}
