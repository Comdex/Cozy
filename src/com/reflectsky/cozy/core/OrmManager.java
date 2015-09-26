package com.reflectsky.cozy.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
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
	//默认为mysql
	private String DBtype = "mysql";
	//可用的ORM实现查找Map
	private Map<String, Class> ormMap = new HashMap<String,Class>();
	//可用的ORMUtil实现查找Map
	private Map<String, Class> ormUtilMap = new HashMap<String,Class>();
	//orm sql语句执行过程
	private  boolean Debug = false;
	//orm 自动建表执行过程
	private  boolean Verbose = true;
	//是否drop table后建表
	private  boolean Force = false;
	private TableCache tableCache;
	private static ConnectionPool connPool;
	private int defaultRowsLimit = 1000;
	
	//缓存OrmUtil
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
	
	
	/**ORM管理器构造方法
	 * @param driverName JDBC驱动名
	 * @param dBUrl 数据库URL
	 * @param dBUser 数据库用户名
	 * @param dBPassword 数据库密码
	 * @param dBtype 数据库类型，默认支持mysql
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
			// TODO 自动生成的 catch 块
			deBugInfo(e.getMessage());
		}
	}
	
	/**ORM管理器构造方法
	 * @param driverName JDBC驱动名
	 * @param dBUrl 数据库URL
	 * @param dBUser 数据库用户名
	 * @param dBPassword 数据库密码
	 * @param dBtype 数据库类型，默认支持mysql
	 * @param debug 是否开启debug模式
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
			// TODO 自动生成的 catch 块
			deBugInfo(e.getMessage());
		}
	}
	
	/**
	 *获取类映射数据库表信息缓存
	 * @author Comdex
	 * @return TableCache
	 */
	public TableCache getTableCache() {
		return tableCache;
	}

	/**
	 *设置最大保持的数据库连接数
	 * @author Comdex
	 * @param maxConnections 
	 */
	public void setMaxOpenConns(int maxConnections){
		if(connPool != null){
			connPool.setMaxConnections(maxConnections);
		}
	}
	
	/**
	 *设置每次增加的数据库连接数
	 * @author Comdex
	 * @param incrementalConnections 
	 */
	public void setIncrementalConns(int incrementalConnections){
		if (connPool != null){
			connPool.setIncrementalConnections(incrementalConnections);
		}
	}
	
	/**
	 *是否强制重新建表
	 * @author Comdex
	 * @param force true为重新建表
	 */
	public void Force(boolean force){
		this.Force = force;
	}
	
	/**
	 *返回是否强制重新建表的表示
	 * @author Comdex
	 * @return boolean
	 */
	public boolean isForce(){
		return this.Force;
	}
	

	/**
	 *为各个实体类注册并解析映射信息进行缓存
	 * @author Comdex
	 * @param models 要进行注册的实体类 
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
				// TODO 自动生成的 catch 块
				deBugInfo(e.getMessage());
			} catch (SecurityException e) {
				// TODO 自动生成的 catch 块
				deBugInfo(e.getMessage());
			} catch (InstantiationException e) {
				// TODO 自动生成的 catch 块
				deBugInfo(e.getMessage());
			} catch (IllegalAccessException e) {
				// TODO 自动生成的 catch 块
				deBugInfo(e.getMessage());
			} catch (IllegalArgumentException e) {
				// TODO 自动生成的 catch 块
				deBugInfo(e.getMessage());
			} catch (InvocationTargetException e) {
				// TODO 自动生成的 catch 块
				deBugInfo(e.getMessage());
			}
		}
		
		if(models.length > 0){
			for(Class model : models){
				TableInfo tbinfo = null;
				try {
					
					tbinfo = ormUtil.getTableInfo(model);
					
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					deBugInfo(e.getMessage());
					System.exit(-1);
				}
				if(tbinfo != null){
					tableCache.set(tbinfo);
				}
					
			}
		}
	}
	
	
	/**
	 *注册自主的ORM实现
	 * @author Comdex
	 * @param dbtype 所实现的ORM数据库类型
	 * @param impl 所实现的ORM类的Class
	 */
	public void registerOrmImpl(String dbtype, Class impl){
		ormMap.put(dbtype, impl);
	}
	
	
	/**
	 *注册自主的ORMUtil实现
	 * @author Comdex
	 * @param dbtype 所实现的ORMUtil数据库类型
	 * @param impl 所实现的ORMUtil类的Class
	 */
	public void registerOrmUtilImpl(String dbtype, Class impl){
		ormUtilMap.put(dbtype, impl);
	}
	
	
	/**
	 *返回一个可用的ormer
	 * @author Comdex 
	 * @return Ormer
	 */
	public Ormer NewOrm() {
		Class clazz = ormMap.get(this.DBtype);
		if(clazz == null){
			return null;
		}
		try {
			if(this.DBtype == "mongodb"){
				/*Constructor con = clazz.getConstructor();
				Object obj = con.newInstance();
				java.lang.reflect.Method conMethod = clazz.getDeclaredMethod("setConnection", Object.class);
				conMethod.invoke(obj, connPool.getConnection());
				java.lang.reflect.Method ormMethod = clazz.getDeclaredMethod("setOrmManager", OrmManager.class);
				ormMethod.invoke(obj, this);
				return (Ormer)obj;*/
			}
			Constructor con = clazz.getConstructor();
			Object obj = con.newInstance();
			java.lang.reflect.Method conMethod = clazz.getDeclaredMethod("setConnection", Object.class);
			conMethod.invoke(obj, connPool.getConnection());
			java.lang.reflect.Method ormMethod = clazz.getDeclaredMethod("setOrmManager", OrmManager.class);
			ormMethod.invoke(obj, this);
			java.lang.reflect.Method defaultLimitMethod = clazz.getDeclaredMethod("setDefaultRowsLimit", int.class);
			defaultLimitMethod.invoke(obj, this.defaultRowsLimit);
			return (Ormer)obj;
		} catch (InstantiationException e1) {
			// TODO 自动生成的 catch 块
			deBugInfo(e1.getMessage());
		} catch (IllegalAccessException e1) {
			// TODO 自动生成的 catch 块
			deBugInfo(e1.getMessage());
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			deBugInfo(e.getMessage());
		} catch (NoSuchMethodException e) {
			// TODO 自动生成的 catch 块
			deBugInfo(e.getMessage());
		} catch (SecurityException e) {
			// TODO 自动生成的 catch 块
			deBugInfo(e.getMessage());
		} catch (IllegalArgumentException e) {
			// TODO 自动生成的 catch 块
			deBugInfo(e.getMessage());
		} catch (InvocationTargetException e) {
			// TODO 自动生成的 catch 块
			deBugInfo(e.getMessage());
		}
		return null;
	}
	
	/**
	 *获取JDBC驱动名
	 * @author Comdex
	 * @return String
	 */
	public String getDriverName() {
		return DriverName;
	}
	
	/**
	 *设置JDBC驱动名
	 * @author Comdex
	 * @param driverName 
	 */
	public void setDriverName(String driverName) {
		DriverName = driverName;
	}
	
	/**
	 *获取数据库URL
	 * @author Comdex
	 * @return String 
	 */
	public String getDBUrl() {
		return DBUrl;
	}
	
	/**
	 *设置数据库URL
	 * @author Comdex
	 * @param dBUrl 
	 */
	public void setDBUrl(String dBUrl) {
		DBUrl = dBUrl;
	}
	
	/**
	 *获取数据库用户名
	 * @author Comdex
	 * @return String
	 */
	public String getDBUser() {
		return DBUser;
	}
	
	/**
	 *设置数据库用户名
	 * @author Comdex
	 * @param dBUser 
	 */
	public void setDBUser(String dBUser) {
		DBUser = dBUser;
	}
	
	/**
	 *获取数据库密码
	 * @author Comdex
	 * @return String 
	 */
	public String getDBPassword() {
		return DBPassword;
	}
	
	/**
	 *设置数据库密码
	 * @author Comdex
	 * @param dBPassword 
	 */
	public void setDBPassword(String dBPassword) {
		DBPassword = dBPassword;
	}
	
	/**
	 *设置是否开启Debug模式，开启后会输出执行的SQL语句
	 * @author Comdex
	 * @param debug 
	 */
	public void Debug(boolean debug) {
		Debug = debug;
	}
	
	/**
	 *设置是否显示Cozy执行的建表SQL语句
	 * @author Comdex
	 * @param verbose true为显示
	 */
	public void Verbose(boolean verbose){
		Verbose = verbose;
	}
	
	public ConnectionPool getConnectionPool(){
		return connPool;
	}

	
	/**
	 *同步建表
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
						//删除表再建表
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
				// TODO 自动生成的 catch 块
				deBugInfo(e.getMessage());
			} finally{
				connPool.freeConnection(conn);
			}
	}
	
	/**
	 *获取是否开启Debug模式的表示
	 * @author Comdex
	 * @return boolean 
	 */
	public boolean isDebug(){
		return Debug;
	}
	
	//包内输出DEBUG信息
    public void deBugInfo(String info){
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(sdf.format(now) + "  " + info);
	}
    
    //关闭ResultSet
    public void closeRs(ResultSet rs){
    	try {
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			deBugInfo(e.getMessage());
		}
    }
    
    //关闭Connection
    public void closeStmt(Statement stmt){
    	try {
			stmt.close();
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			deBugInfo(e.getMessage());
		}
    }
	
    public String getDBType(){
    	return this.DBtype;
    }
    
    public void setDefaultRowsLimit(int limit){
    	this.defaultRowsLimit = limit;
    }
    
    public int getDefaultRowsLimit(){
    	return this.defaultRowsLimit;
    }
}
