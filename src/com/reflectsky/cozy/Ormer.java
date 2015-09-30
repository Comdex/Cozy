package com.reflectsky.cozy;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import com.reflectsky.cozy.core.OrmManager;


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
	 * @return boolean ����falseΪʧ��
	 */
	public boolean insert(Object obj);

	/**
	 *���ݴ���Ķ���ָ�������ݿ��ֶθ������ݿ�ļ�¼
	 * @author Comdex
	 * @param obj ����Ķ���
	 * @param fieldnames  ������ֶζ�Ӧ�����ݿ��¼�ֶΣ���ѡ
	 * @return int  ����0����ִ��ʧ��
	 */
	public int update(Object obj,String... fieldnames);

	/**
	 *���ݴ���Ķ���ɾ�����ݿ�ָ���ļ�¼
	 * @author Comdex
	 * @param obj ����Ķ���
	 * @return int  ����0����ִ��ʧ��
	 */
	public int delete(Object obj);
	
	/**
	 *���ݴ���Ļص��������ڵĶ������ص�֧��
	 * @author Comdex
	 * @param obj ����Ķ���
	 */
	public void addCallback(Object obj);
	
	/**
	 *���ݴ����sql����ռλ���Ĳ���(�����)����ԭ��SQL��ѯ����RawSet
	 * @author Comdex
	 * @param sql ��ͨsql������ռλ����sql���
	 * @param params ��ѡ�����sql�������ռλ�����ָ���ò���
	 * @return RawSet  ԭ��SQL��ѯ����
	 */
	public RawSet raw(String sql,Object... params);
	
	/**
	 *���ݴ�������ݶ�������ݿ��������ORM��ѯ����
	 * @author Comdex
	 * @param obj ���ݶ�������ݿ����
	 * @return QuerySet ORM��ѯ����
	 */
	public QuerySet queryTable(Object obj);
	
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
	
	/**
	 *����ORM������ע����
	 * @author Comdex
	 * @param conn ��Ҫע���connection
	 */
	public void setConnection(Object conn);
	
	/**
	 *����Orm������ע����
	 * @author Comdex
	 * @param OrmManager ��Ҫע���OrmManager
	 */
	public void setOrmManager(OrmManager omanager);
	
	/**
	 *ͬʱ����������
	 * @author Comdex
	 * @param objects ��Ҫ����Ķ���list
	 * @return ִ�в�������� 
	 */
	public int insertMulti(List objects);
	
}
