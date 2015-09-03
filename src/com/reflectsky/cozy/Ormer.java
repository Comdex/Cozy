package com.reflectsky.cozy;

import java.sql.PreparedStatement;
import java.sql.Statement;


/**
 * ORM�ӿ�
 * @author Comdex
 */
public interface Ormer {
	/**
	 *���ݴ���Ķ����ѯ���ݿⲢ��ֵ���ö���
	 *��ָ���������ݿ�ָ�����ֶθ��ö���
	 * @author Comdex
	 * @param obj  ����Ķ���ʹ��ǰ���ע��
	 * @param fieldnames ָ����������ݿ��ֶ�(���Ƕ�����ֶ���)����ѡ
	 * @return boolean trueΪִ�гɹ���falseΪִ��ʧ��
	 */
	public boolean read(Object obj,String... fieldnames);

	/**
	 *���ݴ���Ķ��������ݿ�����Ӧ������
	 * @author Comdex
	 * @param obj ����Ķ���
	 * @return long �������ݼ�¼��������ID,ִ��ʧ�ܻ��Ӧ���ݱ�����������ID���Ƿ���-1
	 */
	public long insert(Object obj);

	/**
	 *���ݴ���Ķ���ָ�������ݿ��ֶθ������ݿ�ļ�¼
	 * @author Comdex
	 * @param obj ����Ķ���
	 * @param fieldnames  ������ֶζ�Ӧ�����ݿ��¼�ֶΣ���ѡ
	 * @return int  ����-1����ִ��ʧ��
	 */
	public int update(Object obj,String... fieldnames);

	/**
	 *���ݴ���Ķ���ɾ�����ݿ�ָ���ļ�¼
	 * @author Comdex
	 * @param obj ����Ķ���
	 * @return int  ����-1����ִ��ʧ��
	 */
	public int delete(Object obj);
	
	/**
	 *�Ƿ����ص�����֧��
	 * @author Comdex
	 * @param isOpen ����ֵ,trueΪ����
	 */
	public void openCallback(boolean isOpen);
	
	/**
	 *���ݴ����sql����ռλ���Ĳ���(�����)����ԭ��SQL��ѯ����RawSet
	 * @author Comdex
	 * @param sql ��ͨsql������ռλ����sql���
	 * @param params ��ѡ�����sql�������ռλ�����ָ���ò���
	 * @return RawSet  ԭ��SQL��ѯ����
	 */
	public RawSet raw(String sql,Object... params);
	
	/**
	 *���ݴ�������ݿ��������ORM��ѯ����
	 * @author Comdex
	 * @param tableName ���ݿ����
	 * @return QuerySet ORM��ѯ����
	 */
	public QuerySet queryTable(String tableName);
	
	/**
	 *��ʼ����
	 * @author Comdex 
	 */
	public void begin();
	
	/**
	 *�ύ����
	 * @author Comdex
	 * @return boolean trueΪִ�гɹ�
	 */
	public boolean commit();
	
	/**
	 *�ع�����
	 * @author Comdex 
	 */
	public void rollback();
	
	/**
	 *����ԭ����JDBC Statement����
	 * @author Comdex
	 * @return Statement
	 */
	public Statement createStatement();
	
	/**
	 *����ԭ����JDBC PreparedStatement����
	 * @author Comdex
	 * @param sql ��ҪԤ�����SQL���
	 * @return PreparedStatement
	 */
	public PreparedStatement createPreparedStatement(String sql);
	
	/**
	 *�ͷ�ORM����Դ
	 * @author Comdex 
	 */
	public void close();
}
