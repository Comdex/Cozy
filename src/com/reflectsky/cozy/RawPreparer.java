package com.reflectsky.cozy;

import java.sql.ResultSet;


/**
 * ԭ��SQLԤ��ѯ�ӿ�
 * @author Comdex
 */
public interface RawPreparer {
	
	/**
	 *���ݴ����SQLռλ������ִ��Ԥ����SQL���
	 * @author Comdex
	 * @param objs
	 * @return int ������Ӱ���¼����������-1����ִ��ʧ��
	 */
	public int exec(Object... objs);
	
	/**
	 *�ͷ�ԭ��SQLԤ��ѯ�������Դ
	 * @author Comdex
	 * @return boolean trueΪִ�гɹ�,falseΪִ��ʧ��
	 */
	public boolean close();
}
