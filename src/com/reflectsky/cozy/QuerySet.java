
package com.reflectsky.cozy;

import java.util.List;


/**
 * ORM��ѯ�ӿ�
 * @author Comdex
 */
public interface QuerySet {
	/**
	 *���ݹ��˲�ѯ���ʽ�Ͳ�ѯ��������ORM��ѯ����
	 * @author Comdex
	 * @param expression ���˲�ѯ���ʽ
	 * @param params ��ѯ����
	 * @return QuerySet
	 */
	public QuerySet filter(String expression,Object...params);
	
	/**
	 *���������ѯ���ʽ����ORM��ѯ����
	 * @author Comdex
	 * @param expressions �����ѯ���ʽ
	 * @return QuerySet
	 */
	public QuerySet orderBy(String...expressions);
	
	/**
	 *����������������ݿ��¼����
	 * @author Comdex
	 * @return long ���ݿ��¼����,-1Ϊִ��ʧ��
	 */
	public long count();
	
	/**
	 *��ѯ�������������ݿ��¼�Ƿ����
	 * @author Comdex
	 * @return boolean trueΪ����,falseΪ������
	 */
	public boolean exist();
	
	/**
	 *ɾ���������������ݿ��¼
	 * @author Comdex
	 * @return long ������Ӱ��ļ�¼����
	 */
	public long delete();
	
	/**
	 *�ѷ������������ݿ��¼���ݸ�ֵ��һ��ָ������
	 * @author Comdex
	 * @param bean ����ֵ�Ķ���
	 * @param params ָ������ֵ�����ݿ��ֶ�
	 * @return boolean trueΪִ�гɹ���falseΪʧ�� 
	 */
	public boolean one(Object bean,String...params);
	
	/**
	 *�ѷ������������ݿ��¼���ݸ�ֵ��ָ����List����
	 * @author Comdex
	 * @param list ����ֵ��list����
	 * @param clazz list������ķ��������class����
	 * @param params ָ��ȡ���ݵ����ݿ��¼���ֶ�
	 * @return long ��Ӱ��ļ�¼����
	 */
	public long all(List list,Class clazz,String...params);
}
