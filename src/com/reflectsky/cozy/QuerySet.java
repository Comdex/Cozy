
package com.reflectsky.cozy;

import java.util.List;
import java.util.Map;


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
	 *�����ų�������ѯ���ʽ�Ͳ�ѯ��������ORM��ѯ����
	 * @author Comdex
	 * @param expression �ų�������ѯ���ʽ
	 * @param params �ų�������ѯ����
	 * @return QuerySet
	 */
	public QuerySet exclude(String expression,Object...params);
	
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
	
	/**
	 *������󷵻������������ڶ��������������� Offset
	 * @author Comdex
	 * @param int ���Ʒ�������
	 * @param long ƫ������
	 * @return QuerySet
	 */
	public QuerySet limit(int count, long... offset);
	
	/**
	 *����ƫ������
	 * @author Comdex
	 * @param long ƫ������
	 * @return QuerySet
	 */
	public QuerySet offset(long offset);
	
	/**
	 *���ؽ������ key => valueֵ(List<Map<String,String>>)
     *keyΪ Model ��� Field name��value ��ֵ �� string����
	 * @author Comdex
	 * @return List<Map<String,String>>
	 */
	public List<Map<String,String>> values();
}
