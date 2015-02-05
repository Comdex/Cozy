package com.reflectsky.cozy.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.reflectsky.cozy.OrmUtil;
import com.reflectsky.cozy.Ormer;
import com.reflectsky.cozy.apiimpl.MySQLOrmImpl;
import com.reflectsky.cozy.apiimpl.MySQLOrmUtilImpl;


public class OrmManager {
	private String DriverName="";
	private String DBUrl="";
	private String DBUser="";
	private String DBPassword="";
	//Ĭ��Ϊmysql
	private String DBtype = "mysql";
	//���õ�ORMʵ�ֲ���Map
	private Map<String, Class> ormMap = new HashMap<String,Class>();
	//���õ�ORMUtilʵ�ֲ���Map
	private Map<String, Class> ormUtilMap = new HashMap<String,Class>();
	//orm sql���ִ�й���
	private  boolean Debug = false;
	//orm �Զ�����ִ�й���
	private  boolean Verbose = true;
	//�Ƿ�drop table�󽨱�
	private  boolean Force = false;
	private TableCache tableCache;
	private static ConnectionPool connPool;
	
	//����OrmUtil
	private OrmUtil ormUtil = null;
	
	private static Map<String,Integer> supportTag ;
	
	static{
		supportTag = new HashMap<String, Integer>();
		supportTag.put("null", 1);
		supportTag.put("unique", 1);
		supportTag.put("index", 2);
	}
	
	public Map<String, Integer> getSupportTag(){
		return supportTag;
	}
	
	
	/**ORM���������췽��
	 * @param driverName JDBC������
	 * @param dBUrl ���ݿ�URL
	 * @param dBUser ���ݿ��û���
	 * @param dBPassword ���ݿ�����
	 * @param dBtype ���ݿ����ͣ�Ĭ��֧��mysql
	 */
	public OrmManager(String driverName, String dBUrl, String dBUser,
			String dBPassword, String dBtype) {
		super();
		DriverName = driverName;
		DBUrl = dBUrl;
		DBUser = dBUser;
		DBPassword = dBPassword;
		DBtype = dBtype;
		tableCache = new TableCache();
		
		registerOrmUtilImpl("mysql", MySQLOrmUtilImpl.class);
		registerOrmImpl("mysql", MySQLOrmImpl.class);
		connPool = new ConnectionPool(DriverName, DBUrl, DBUser, DBPassword,this);
		
		try {
			connPool.createPool();
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	}
	
	/**ORM���������췽��
	 * @param driverName JDBC������
	 * @param dBUrl ���ݿ�URL
	 * @param dBUser ���ݿ��û���
	 * @param dBPassword ���ݿ�����
	 * @param dBtype ���ݿ����ͣ�Ĭ��֧��mysql
	 * @param debug �Ƿ���debugģʽ
	 */
	public OrmManager(String driverName, String dBUrl, String dBUser,
			String dBPassword, String dBtype, boolean debug) {
		super();
		DriverName = driverName;
		DBUrl = dBUrl;
		DBUser = dBUser;
		DBPassword = dBPassword;
		DBtype = dBtype;
		Debug = debug;
		
		registerOrmUtilImpl("mysql", MySQLOrmImpl.class);
		registerOrmImpl("mysql", MySQLOrmImpl.class);
		connPool = new ConnectionPool(DriverName, DBUrl, DBUser, DBPassword,this);
		try {
			connPool.createPool();
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	}
	
	/**
	 *��ȡ��ӳ�����ݿ����Ϣ����
	 * @author Comdex
	 * @return TableCache
	 */
	public TableCache getTableCache() {
		return tableCache;
	}

	/**
	 *������󱣳ֵ����ݿ�������
	 * @author Comdex
	 * @param maxConnections 
	 */
	public void setMaxOpenConns(int maxConnections){
		if(connPool != null){
			connPool.setMaxConnections(maxConnections);
		}
	}
	
	/**
	 *����ÿ�����ӵ����ݿ�������
	 * @author Comdex
	 * @param incrementalConnections 
	 */
	public void setIncrementalConns(int incrementalConnections){
		if (connPool != null){
			connPool.setIncrementalConnections(incrementalConnections);
		}
	}
	
	/**
	 *�Ƿ�ǿ�����½���
	 * @author Comdex
	 * @param force trueΪ���½���
	 */
	public void Force(boolean force){
		this.Force = force;
	}
	
	/**
	 *�����Ƿ�ǿ�����½���ı�ʾ
	 * @author Comdex
	 * @return boolean
	 */
	public boolean isForce(){
		return this.Force;
	}
	

	/**
	 *Ϊ����ʵ����ע�Ტ����ӳ����Ϣ���л���
	 * @author Comdex
	 * @param models Ҫ����ע���ʵ���� 
	 */
	public void RegisterModel(Class... models){
		
		Class clazz = ormUtilMap.get(this.DBtype);
		if (clazz != null){
			Class[] paramTypes = {OrmManager.class};
			Object[] params = {this};
			try {
				Constructor con = clazz.getConstructor(paramTypes);
				Object obj = con.newInstance(params);
				ormUtil = (OrmUtil) obj;
			} catch (NoSuchMethodException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		}
		
		if(models.length > 0){
			for(Class model : models){
				TableInfo tbinfo = null;
				try {
					
					tbinfo = ormUtil.getTableInfo(model);
					
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					System.out.println(e.getMessage());
					System.exit(-1);
				}
				if(tbinfo != null){
					tableCache.set(tbinfo);
				}
					
			}
		}
	}
	
	
	/**
	 *ע��������ORMʵ��
	 * @author Comdex
	 * @param dbtype ��ʵ�ֵ�ORM���ݿ�����
	 * @param impl ��ʵ�ֵ�ORM���Class
	 */
	public void registerOrmImpl(String dbtype, Class impl){
		ormMap.put(dbtype, impl);
	}
	
	
	/**
	 *ע��������ORMUtilʵ��
	 * @author Comdex
	 * @param dbtype ��ʵ�ֵ�ORMUtil���ݿ�����
	 * @param impl ��ʵ�ֵ�ORMUtil���Class
	 */
	public void registerOrmUtilImpl(String dbtype, Class impl){
		ormUtilMap.put(dbtype, impl);
	}
	
	
	/**
	 *����һ�����õ�ormer
	 * @author Comdex 
	 * @return Ormer
	 */
	public Ormer NewOrm() {
		Class clazz = ormMap.get(this.DBtype);
		if(clazz == null){
			return null;
		}
		try {
			Class[] paramTypes = {Connection.class,OrmManager.class};
			Object[] params = {connPool.getConnection(),this};
			Constructor con = clazz.getConstructor(paramTypes);
			Object obj = con.newInstance(params);
			return (Ormer)obj;
		} catch (InstantiationException e1) {
			// TODO �Զ����ɵ� catch ��
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO �Զ����ɵ� catch ��
			e1.printStackTrace();
		} catch (SQLException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 *��ȡJDBC������
	 * @author Comdex
	 * @return String
	 */
	public String getDriverName() {
		return DriverName;
	}
	
	/**
	 *����JDBC������
	 * @author Comdex
	 * @param driverName 
	 */
	public void setDriverName(String driverName) {
		DriverName = driverName;
	}
	
	/**
	 *��ȡ���ݿ�URL
	 * @author Comdex
	 * @return String 
	 */
	public String getDBUrl() {
		return DBUrl;
	}
	
	/**
	 *�������ݿ�URL
	 * @author Comdex
	 * @param dBUrl 
	 */
	public void setDBUrl(String dBUrl) {
		DBUrl = dBUrl;
	}
	
	/**
	 *��ȡ���ݿ��û���
	 * @author Comdex
	 * @return String
	 */
	public String getDBUser() {
		return DBUser;
	}
	
	/**
	 *�������ݿ��û���
	 * @author Comdex
	 * @param dBUser 
	 */
	public void setDBUser(String dBUser) {
		DBUser = dBUser;
	}
	
	/**
	 *��ȡ���ݿ�����
	 * @author Comdex
	 * @return String 
	 */
	public String getDBPassword() {
		return DBPassword;
	}
	
	/**
	 *�������ݿ�����
	 * @author Comdex
	 * @param dBPassword 
	 */
	public void setDBPassword(String dBPassword) {
		DBPassword = dBPassword;
	}
	
	/**
	 *�����Ƿ���Debugģʽ������������ִ�е�SQL���
	 * @author Comdex
	 * @param debug 
	 */
	public void Debug(boolean debug) {
		Debug = debug;
	}
	
	/**
	 *�����Ƿ���ʾCozyִ�еĽ���SQL���
	 * @author Comdex
	 * @param verbose trueΪ��ʾ
	 */
	public void Verbose(boolean verbose){
		Verbose = verbose;
	}
	
	public ConnectionPool getConnectionPool(){
		return connPool;
	}

	
	/**
	 *ͬ������
	 * @author Comdex 
	 */
	public void runSyncDB(){
		Connection conn = null;
			try {
				conn = connPool.getConnection();
				Statement stmt = conn.createStatement();
				Map<String,TableInfo> cache = tableCache.All();
				Vector<String> tb = new Vector<String>();
				for(Object key : cache.keySet()){
					TableInfo tbinfo = cache.get(key);
					String sql = ormUtil.generateTableCreateSql(tbinfo);
					if(!sql.equals("")){
						//ɾ�����ٽ���
						if(Force){
							String strSql = "DROP TABLE IF EXISTS " + tbinfo.getTableName() + ";";
							stmt.addBatch(strSql);
							if(Verbose){
								deBugInfo(strSql);
							}
						}else{
							tb.add(tbinfo.getTableName());
						}
						stmt.addBatch(sql);
						if(Verbose){
							deBugInfo(sql);
						}
					}
				}
				int[] affect = stmt.executeBatch();
				if(!Force){
					for(int i=0;i<cache.size();i++){
						if(affect[i] == 0){
							deBugInfo("table <" + tb.get(i) + "> already exists!");
						}
					}
				}
			} catch (SQLException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			} finally{
				connPool.freeConnection(conn);
			}
	}
	
	/**
	 *��ȡ�Ƿ���Debugģʽ�ı�ʾ
	 * @author Comdex
	 * @return boolean 
	 */
	public boolean isDebug(){
		return Debug;
	}
	
	//�������DEBUG��Ϣ
    public void deBugInfo(String info){
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(sdf.format(now) + "  " + info);
	}
	
}
