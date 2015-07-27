package com.reflectsky.cozy.apiimpl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import sun.org.mozilla.javascript.internal.ast.ContinueStatement;

import com.reflectsky.cozy.Ormer;
import com.reflectsky.cozy.QuerySet;
import com.reflectsky.cozy.RawSet;
import com.reflectsky.cozy.core.FieldInfo;
import com.reflectsky.cozy.core.OperateSet;
import com.reflectsky.cozy.core.OrmManager;
import com.reflectsky.cozy.core.TableInfo;


/**
 * MySQL的ORM实现
 * @author Comdex
 */
public class MySQLOrmImpl implements Ormer{
	private Connection conn = null;
	private OrmManager oManager = null;
		
	public MySQLOrmImpl(Connection conn,OrmManager oManager){
		this.conn = conn;
		this.oManager = oManager;
	}

	/* @author Comdex
	 * @see com.reflectsky.cozy.Ormer#read(java.lang.Object, java.lang.String[])
	 */
	@Override
	public boolean read(Object obj, String... fieldnames)  {
		// TODO 自动生成的方法存根
		OperateSet oSet = generateReadOperateSet(obj, fieldnames);
		PreparedStatement pstmt = null;
		if(oSet == null){
			return false;
		}
		
		try {
			pstmt = conn.prepareStatement(oSet.getStrSql());
			
			ormDebug(oSet);
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		if(pstmt != null){
			Vector<Object> param = oSet.getParam();
			for(int i=0 ; i < param.size() ; i++){
				Object object = param.get(i);
				
				try {
					pstmt.setObject(i+1, object);
				} catch (SQLException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				
			}
			
			try {
				ResultSet rs = pstmt.executeQuery();
				//获取查询结果的值
				Vector<Object> values = new Vector<Object>();
				if(fieldnames.length !=0 ){
					if(rs != null){
						if(rs.next()){
							for(String s : fieldnames){
								values.add(rs.getObject(s));
							}
						}
					}
					//获取fieldname对应的字段名
					Vector<String> fields = new Vector<String>();
					Vector<FieldInfo> fins = oSet.getTbinfo().getAllFieldInfos();
					for(String s : fieldnames){
						for(FieldInfo fin : fins){
							if(fin.getColumnName().equalsIgnoreCase(s)){
								fields.add(fin.getFieldName());
							}
						}
					}
					
					//为对应的field设值
					for(int i=0 ; i<fields.size() ; i++){
						try {
							Field field = obj.getClass().getDeclaredField(fields.get(i));
							field.setAccessible(true);
							field.set(obj, values.get(i));
						} catch (NoSuchFieldException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}
					return true;
				}else {
					//获取该对象所有字段名
					Vector<String> fields = new Vector<String>();
					Vector<FieldInfo> fins = oSet.getTbinfo().getAllFieldInfos();
					if(rs != null){
						if(rs.next()){
							for(FieldInfo fin : fins){
								values.add(rs.getObject(fin.getColumnName()));
							}
						}
					}
					
					
					//为对应的Field设值
					for(int i=0 ; i<fins.size() ; i++){
						Field field = null;
						try {
							field = obj.getClass().getDeclaredField(fins.get(i).getFieldName());
						} catch (NoSuchFieldException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
						field.setAccessible(true);
						try {
							field.set(obj, values.get(i));
							
						} catch (IllegalArgumentException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
					}
					rs.close();
					pstmt.close();
					return true;
				}
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			
		}
		return false;
		
	}
	
	
	/* 返回自增长的id,没有自增长时都是返回-1
	 * @author Comdex
	 * @see com.reflectsky.cozy.Ormer#insert(java.lang.Object)
	 */
	@Override
	public long insert(Object obj)  {
		// TODO 自动生成的方法存根
		OperateSet oSet = generateInsertOperateSet(obj);
		PreparedStatement pstmt = null;
		if(oSet == null){
			
			return -1;
		}
		
		try {
			pstmt = conn.prepareStatement(oSet.getStrSql(),Statement.RETURN_GENERATED_KEYS);
			
			ormDebug(oSet);
			
		
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		if(pstmt != null){
			Vector<Object> param = oSet.getParam();
			for(int i=0 ; i < param.size() ; i++){
				Object object = param.get(i);
				
				try {
					pstmt.setObject(i+1, object);
				} catch (SQLException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				
			}
			
			Class<?> clazz = obj.getClass();
			try {
				Method mtd = clazz.getMethod("beforeInsert");
				mtd.invoke(obj);
			} catch (NoSuchMethodException e1) {
				// TODO 自动生成的 catch 块
				
			} catch (SecurityException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			
			try {
				pstmt.executeUpdate();
			} catch (SQLException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
			
			
			try {
				Method mtd = clazz.getMethod("afterInsert");
				mtd.invoke(obj);
			} catch (NoSuchMethodException e1) {
				// TODO 自动生成的 catch 块
				
			} catch (SecurityException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
			
			

			try {
				if(!oSet.getAutoKeyName().equals("")){
					ResultSet rs = pstmt.getGeneratedKeys();
					if(rs.next()){
						long autoId = rs.getLong(1);
						Field field = null;
						try {
							field = obj.getClass().getDeclaredField(oSet.getAutoKeyName());
						} catch (NoSuchFieldException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
						field.setAccessible(true);
						try {
							if(field.getClass().getName().equals("Integer")){
								field.set(obj, (int)autoId);
							}else if(field.getClass().getName().equals("Short")){
								field.set(obj, (short)autoId);
							}else {
								field.set(obj, (int)autoId);
							}
							
							rs.close();
							pstmt.close();
							
							return (long)autoId;
						} catch (IllegalArgumentException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
					}
					
				}else{
					return -1;
				}
				
				
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			
		}
		
		return -1;
	}
	

	/* @author Comdex
	 * @see com.reflectsky.cozy.Ormer#update(java.lang.Object, java.lang.String[])
	 */
	@Override
	public int update(Object obj, String... fieldnames)  {
		// TODO 自动生成的方法存根
		OperateSet oSet = generateUpdateOperateSet(obj, fieldnames);
		PreparedStatement pstmt = null;
		if(oSet == null){
			return -1;
		}
		
		try {
			pstmt = conn.prepareStatement(oSet.getStrSql());
			
			ormDebug(oSet);
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		if(pstmt != null){
			Vector<Object> param = oSet.getParam();
			for(int i=0 ; i < param.size() ; i++){
				Object object = param.get(i);
				
				try {
					pstmt.setObject(i+1, object);
				} catch (SQLException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				
			}
			
			try {
				pstmt.close();
				return pstmt.executeUpdate();
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		return -1;
		
	}

	/* @author Comdex
	 * @see com.reflectsky.cozy.Ormer#delete(java.lang.Object)
	 */
	@Override
	public int delete(Object obj)  {
		// TODO 自动生成的方法存根
		OperateSet oSet = generateDeleteOperateSet(obj);
		PreparedStatement pstmt = null;
		if(oSet == null){
			return -1;
		}
		
		try {
			pstmt = conn.prepareStatement(oSet.getStrSql());
			
			ormDebug(oSet);
			
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
		if(pstmt != null){
			Vector<Object> param = oSet.getParam();
			for(int i=0 ; i < param.size() ; i++){
				Object object = param.get(i);
				
				try {
					pstmt.setObject(i+1, object);
				} catch (SQLException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				
			}
			
			try {
				pstmt.close();
				return pstmt.executeUpdate();
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		
		return -1;
	}

	/* @author Comdex
	 * @see com.reflectsky.cozy.Ormer#begin()
	 */
	@Override
	public void begin()  {
		// TODO 自动生成的方法存根
		//关闭自动提交，开启事务
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
	}

	/* @author Comdex
	 * @see com.reflectsky.cozy.Ormer#commit()
	 */
	@Override
	public boolean commit()  {
		// TODO 自动生成的方法存根
		try {
			conn.commit();
			conn.setAutoCommit(true);
			return true;
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return false;
		}
	}

	/* @author Comdex
	 * @see com.reflectsky.cozy.Ormer#rollback()
	 */
	@Override
	public void rollback() {
		// TODO 自动生成的方法存根
		try {
			boolean auto = conn.getAutoCommit();
			if(auto){
				conn.setAutoCommit(false);
				conn.rollback();
			}else {
				conn.rollback();
			}
			
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	/* @author Comdex
	 * @see com.reflectsky.cozy.Ormer#close()
	 */
	@Override
	public void close() {
		// TODO 自动生成的方法存根
		if(conn != null){
			oManager.getConnectionPool().freeConnection(conn);
		}
		
	}
	
	private void ormDebug(OperateSet oSet){
		if(oManager.isDebug()){
			String sql = oSet.getStrSql();
			Vector<Object> param = oSet.getParam();
			sql += "  parameter:";
			for(Object object : param){
				if(object != null){
					if(object.getClass().getSimpleName().equals("Date")){
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date date = (Date)object;
						sql += " <" + sdf.format(date) + ">" ;
					}else {
						sql += " <" + object + ">" ;
					}
				}else {
					sql += " <null>";
				}	
			}
			oManager.deBugInfo(sql);
		}
	}
	
	private OperateSet generateDeleteOperateSet(Object obj){
		Class<? extends Object> clazz = obj.getClass();
		//获取tablecache中对应的info
		String typeName = clazz.getName();
		TableInfo tbinfo = null;
		String autoKeyName = ""; 
		
		if(typeName != null){
			tbinfo = oManager.getTableCache().getByTN(typeName);
		}
		
		if(tbinfo == null){
			//提示错误
			System.out.println("[generateDeleteOperateSet error]-Model<"+typeName+"> has not registered!");
			return null;
		}
		
		String strSql = "delete from " + tbinfo.getTableName();
		String strWhere = "";
		Vector<Object> param = new Vector<Object>();
		Vector<FieldInfo> fins = tbinfo.getAllFieldInfos();
		for(FieldInfo fin : fins){
			//以主键为删除条件，如果不是主键的继续,暂时不支持联合主键
			if(!fin.isPrimaryKey()){
				continue;
			}
			autoKeyName = fin.getColumnName();
			strWhere += fin.getColumnName() + "=?";
			
			Field field = null;
			try {
				field = obj.getClass().getDeclaredField(fin.getFieldName());
			} catch (NoSuchFieldException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			field.setAccessible(true);
			
			try {
				param.add(field.get(obj));
			} catch (IllegalArgumentException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		
		if(strWhere.equals("")){
			//提示错误
			System.out.println("[generateDeleteOperateSet error]-Model<"+typeName+"> has not set PrimaryKey to achieve this delete operate!");
			return null;
		}
		
		strSql += " where " + strWhere;
		OperateSet oSet = new OperateSet(strSql, param, tbinfo);
		oSet.setAutoKeyName(autoKeyName);
		return oSet;
	}
	
	//生成查询Sql,以主键为查询条件,如果有指定读入的键那只读该键
	private OperateSet generateReadOperateSet(Object obj , String... fieldnames){
		Class<? extends Object> clazz = obj.getClass();
		//获取tablecache中的对应的info
		String typeName = clazz.getName();
		TableInfo tbinfo = null;
		String autoKeyName = "";
		
		if(typeName != null){
			tbinfo = oManager.getTableCache().getByTN(typeName);
		}
		
		if(tbinfo == null){
			//提示错误
			System.out.println("[generateReadOperateSet error]-Model<"+typeName+"> has not registered!");
			return null;
		}
		String strSql = "select ";
		String strField = "";
		String strWhere = "";
		Vector<Object> param = new Vector<Object>();
		if(fieldnames.length != 0){
			
			if(fieldnames.length == 1){
				if(fieldnames[0].equals("")){
					return null;
				}
			}
			
			for(String s : fieldnames){
				strField += s + ",";
			}
			strField = strField.substring(0, strField.length()-1);
			strSql += strField + " from " + tbinfo.getTableName();
			Vector<FieldInfo> fins = tbinfo.getAllFieldInfos();
			for(FieldInfo fin : fins){
				//以主键为查询条件，如果不是主键的继续,暂时不支持联合主键
				if(!fin.isPrimaryKey()){
					continue;
				}
				autoKeyName = fin.getColumnName();
				strWhere += fin.getColumnName() + "=?";
				Field field = null;
				try {
					field = obj.getClass().getDeclaredField(fin.getFieldName());
				} catch (NoSuchFieldException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				field.setAccessible(true);
				
				try {
					param.add(field.get(obj));
				} catch (IllegalArgumentException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
			
			if(strWhere.equals("")){
				//提示错误
				System.out.println("[generateReadOperateSet error]-Model<"+typeName+"> has not set PrimaryKey to achieve this query!");
				return null;
			}
			
			strSql += " where " + strWhere + " limit 1";
			OperateSet oSet = new OperateSet(strSql, param, tbinfo);
			oSet.setAutoKeyName(autoKeyName);
			return oSet;
		}else {
			strSql += "* from " + tbinfo.getTableName();
			Vector<FieldInfo> fins = tbinfo.getAllFieldInfos();
			for(FieldInfo fin : fins){
				//以主键为查询条件，如果不是主键的继续,暂时不支持联合主键
				if(!fin.isPrimaryKey()){
					continue;
				}
				autoKeyName = fin.getColumnName();
				strWhere += fin.getColumnName() + "=?";
				Field field = null;
				try {
					field = obj.getClass().getDeclaredField(fin.getFieldName());
				} catch (NoSuchFieldException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				field.setAccessible(true);
				
				try {
					param.add(field.get(obj));
				} catch (IllegalArgumentException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
			
			if(strWhere.equals("")){
				//提示错误
				System.out.println("[generateReadOperateSet error]-Model<"+typeName+"> has not set PrimaryKey to achieve this query!");
				return null;
			}
			
			strSql += " where " + strWhere + " limit 1";
			OperateSet oSet = new OperateSet(strSql, param, tbinfo);
			oSet.setAutoKeyName(autoKeyName);
			return oSet;
		}
	}
	
	private OperateSet generateInsertOperateSet(Object obj){
		Class<? extends Object> clazz = obj.getClass();
		//获取tablecache中的对应的info
		String typeName = clazz.getName();
		TableInfo tbinfo = null;
		String autoKeyName = "";
		
		if(typeName != null){
			tbinfo = oManager.getTableCache().getByTN(typeName);
		}
		
		if(tbinfo == null){
			//提示错误
			System.out.println("[generateInsertOperateSet error]-Model<"+typeName+"> has not registered!");
			return null;
		}
		
		String strSql = "insert into " + tbinfo.getTableName();
		String strField = "";
		String strValue = "";
		Vector<Object> param = new Vector<Object>();
		Vector<FieldInfo> fins = tbinfo.getAllFieldInfos();
		for(FieldInfo fin : fins){
			//跳过自动增长的字段
			if(fin.isAutoGenerate()){
				autoKeyName = fin.getColumnName();
				continue;
			}
			strField += fin.getColumnName() + ",";
			strValue += "?,";
			//获取值
			Field fie = null;
			try {
				 fie = clazz.getDeclaredField(fin.getFieldName());
				 fie.setAccessible(true);
			} catch (NoSuchFieldException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			if(fie != null){
				try {
					param.add(fie.get(obj));
				} catch (IllegalArgumentException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
			
		}
		
		//只有自增字段错误退出
		if(strField.equals("")){
			System.out.println("[generateInsertOperateSet error]-Model<"+typeName+"> only have autogenerate field!");
			return null;
		}
		//去除尾部的，号
		strField = strField.substring(0, strField.length()-1);
		strValue = strValue.substring(0,strValue.length()-1);
		strSql += " (" + strField + ") values(" + strValue + ")";
		OperateSet os = new OperateSet(strSql, param, tbinfo);
		os.setAutoKeyName(autoKeyName);
		return os;	
	}
	
	//生成更新Sql,以主键为更新条件,如果有指定更新的键那只更新该键
	private OperateSet generateUpdateOperateSet(Object obj , String... fieldnames){
		Class<? extends Object> clazz = obj.getClass();
		//获取tablecache中的对应的info
		String typeName = clazz.getName();
		TableInfo tbinfo = null;
		String autoKeyName = "";
		
		if(typeName != null){
			tbinfo = oManager.getTableCache().getByTN(typeName);
		}
		
		if(tbinfo == null){
			//提示错误
			System.out.println("[generateUpdateOperateSet error]-Model<"+typeName+"> has not registered!");
			return null;
		}
		String strSql = "update " + tbinfo.getTableName() + " set ";
		String strField = "";
		String strWhere = "";
		Vector<Object> param = new Vector<Object>();
		if(fieldnames.length != 0){
			
			if(fieldnames.length == 1){
				if(fieldnames[0].equals("")){
					return null;
				}
			}
			
			for(String s : fieldnames){
				if(!s.equals("")){
					strField += s + "=?,";
				}
			}
			strField = strField.substring(0, strField.length()-1);
			
			Vector<FieldInfo> fins = tbinfo.getAllFieldInfos();
			for(FieldInfo fin : fins){
				for(String s : fieldnames){
					if(fin.getColumnName().equalsIgnoreCase(s)){
						Field field = null;
						try {
							field = obj.getClass().getDeclaredField(fin.getFieldName());
						} catch (NoSuchFieldException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
						field.setAccessible(true);
						
						try {
							param.add(field.get(obj));
						} catch (IllegalArgumentException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO 自动生成的 catch 块
							e.printStackTrace();
						}
					}
				}
				
				
				//以主键为更新条件，如果不是主键的继续,暂时不支持联合主键
				if(!fin.isPrimaryKey()){
					continue;
				}
				autoKeyName = fin.getColumnName();
				strWhere += fin.getColumnName() + "=?";
			
			}
			
			Field field = null;
			try {
				field = obj.getClass().getDeclaredField(autoKeyName);
			} catch (NoSuchFieldException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			field.setAccessible(true);
			
			try {
				param.add(field.get(obj));
			} catch (IllegalArgumentException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			
			if(strWhere.equals("")){
				//提示错误
				System.out.println("[generateUpdateOperateSet error]-Model<"+typeName+"> has not set PrimaryKey to achieve this update operate!");
				return null;
			}
			
			strSql += strField + " where " + strWhere ;
			OperateSet oSet = new OperateSet(strSql, param, tbinfo);
			oSet.setAutoKeyName(autoKeyName);
			return oSet;
		}else {
			
			Vector<FieldInfo> fins = tbinfo.getAllFieldInfos();
			
			for(FieldInfo fin : fins){
                
				//以主键为查询条件，如果不是主键的继续,暂时不支持联合主键
				if(!fin.isPrimaryKey()){
					strField += fin.getColumnName() + "=?,";
	                
	                Field field = null;
					try {
						field = obj.getClass().getDeclaredField(fin.getFieldName());
					} catch (NoSuchFieldException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
					field.setAccessible(true);
					
					try {
						param.add(field.get(obj));
					} catch (IllegalArgumentException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
					continue;
				}
				autoKeyName = fin.getColumnName();
				strWhere += fin.getColumnName() + "=?";
			}
			
			Field field = null;
			try {
				field = obj.getClass().getDeclaredField(autoKeyName);
			} catch (NoSuchFieldException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			field.setAccessible(true);
			
			try {
				param.add(field.get(obj));
			} catch (IllegalArgumentException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			
            strField = strField.substring(0, strField.length()-1);
			
			if(strWhere.equals("")){
				//提示错误
				System.out.println("[generateUpdateOperateSet error]-Model<"+typeName+"> has not set PrimaryKey to achieve this update operate!");
				return null;
			}
			
			strSql += strField + " where " + strWhere ;
			OperateSet oSet = new OperateSet(strSql, param, tbinfo);
			oSet.setAutoKeyName(autoKeyName);
			return oSet;
		}
	}

	@Override
	public RawSet raw(String sql, Object... params) {
		// TODO 自动生成的方法存根
		if(params.length == 0){
			try {
				Statement stmt = conn.createStatement();
				RawSet rawSet = new RawSetImpl(this.oManager, stmt, sql);
				return rawSet;
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			
		}else {
			try {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				Vector<Object> pas = new Vector<>();
				for(Object p : params){
					pas.add(p);
				}
				RawSet rawSet = new RawSetImpl(this.oManager, pstmt, pas);
				return rawSet;
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public QuerySet queryTable(String tableName){
		return new QuerySetImpl(oManager, conn, tableName);
	}

	/* （非 Javadoc）
	 * @see com.reflectsky.cozy.Ormer#createStatement()
	 */
	@Override
	public Statement createStatement() {
		// TODO 自动生成的方法存根
		try {
			return conn.createStatement();
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return null;
	}

	/* （非 Javadoc）
	 * @see com.reflectsky.cozy.Ormer#createPreparedStatement(java.lang.String)
	 */
	@Override
	public PreparedStatement createPreparedStatement(String sql) {
		// TODO 自动生成的方法存根
		try {
			return conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return null;
	}
	
}
