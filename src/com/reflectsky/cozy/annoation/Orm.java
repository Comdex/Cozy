package com.reflectsky.cozy.annoation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Cozy ORM ����ע��
 * @author Comdex
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Orm {
	/**
	 *���������Զ�Ӧ�����ݿ������
	 * @author Comdex
	 * @return String
	 */
	String column() default "";
	
	/**
	 *�����������Ƿ���Ϊ����,trueΪ����
	 * @author Comdex
	 * @return boolean
	 */
	boolean pk() default false;
	
	/**
	 *�����������Ƿ���Ϊ��������trueΪ������
	 * @author Comdex
	 * @return boolean
	 */
	boolean auto() default false;
	
	/**
	 *�����������Ƿ񱻺���,����ORM��¼
	 * @author Comdex
	 * @return boolean
	 */
	boolean ignore() default false;
	
	/**
	 *���������Զ�Ӧ�����ݿ�����
	 * @author Comdex
	 * @return 
	 */
	String type() default "";
	
	/**
	 *���������Զ�Ӧ�����ݿ����͵�digits����
	 * @author Comdex
	 * @return int
	 */
	int digits() default 0;
	
	/**
	 *���������Զ�Ӧ�����ݿ����͵�decimals����
	 * @author Comdex
	 * @return int
	 */
	int decimals() default 0;
	
	/**
	 *���������Զ�Ӧ�����ݿ����͵ĳ���
	 * @author Comdex
	 * @return int
	 */
	int size() default 0;
	
	/**
	 *���������Ե��������ݿ�����
	 * @author Comdex
	 * @return String
	 */
	String other() default "";
}
