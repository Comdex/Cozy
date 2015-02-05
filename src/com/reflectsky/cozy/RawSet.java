package com.reflectsky.cozy;

import java.util.List;
import java.util.Map;

/**
 * ԭ��SQL��ѯ�ӿ�
 * @author Comdex
 */
public interface RawSet {
	/**
	 *ִ��ԭ��SQL����
	 * @author Comdex
	 * @return ��Ӱ���¼����������-1����ִ��ʧ�� 
	 */
	public int exec();
	
	/**
	 *�ѷ������������ݿ��¼��ֵ����Ӧ����
	 * @author Comdex
	 * @param obj
	 * @return boolean trueΪִ�гɹ���falseΪʧ��
	 */
	public boolean queryRow(Object obj);
	
	/**
	 *�ѷ������������ݿ��¼��ֵ����Ӧ��List
	 * @author Comdex
	 * @param list ����ֵ��List
	 * @param clazz List�еķ�������
	 * @return int ��Ӱ�����ݿ��¼����������-1����ִ��ʧ��
	 */
	public int queryRows(List list,Class clazz);
	
	/**
	 *Ϊԭ��SQL��ѯ�������ò���
	 * @author Comdex
	 * @param objects Ҫ���õĲ��������
	 * @return RawSet ԭ��SQL��ѯ����
	 */
	public RawSet setArgs(Object... objects);
	
	/**
	 *��һ�����ݿ��¼ӳ�䵽Map��,����ж���ֻȡ��һ��
	 * @author Comdex
	 * @param map ��ӳ���Map 
	 * @param name ��Ҫ��ӳ������ݿ��¼���ֶ�(��ѡ�����)
	 * @return int ��Ӱ���¼����������-1����ʧ��
	 */
	public int rowsToMap(Map<String,Object> map,String...name);
	
	/**
	 *��һ�����ݿ��¼ӳ�䵽����obj�У�����ж���ֻȡ��һ��
	 * @author Comdex
	 * @param obj ��ӳ��Ķ���
	 * @param name ��Ҫ��ӳ������ݿ��¼���ֶ�(��ѡ�����)
	 * @return int ��Ӱ���¼����������-1����ʧ�� 
	 */
	public int rowsToObject(Object obj,String...name);
	
	/**
	 *����һ��SQLԤ��ѯ����
	 * @author Comdex
	 * @return RawPreparer 
	 */
	public RawPreparer prepare();
}
