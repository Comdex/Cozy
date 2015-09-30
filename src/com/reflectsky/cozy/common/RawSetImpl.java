package com.reflectsky.cozy.common;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.reflectsky.cozy.RawPreparer;
import com.reflectsky.cozy.RawSet;
import com.reflectsky.cozy.core.FieldInfo;
import com.reflectsky.cozy.core.OrmManager;
import com.reflectsky.cozy.core.TableInfo;

/**
 * 原生SQL查询接口实现
 * @author Comdex
 */
public class RawSetImpl implements RawSet{
	private OrmManager oManager = null;
	private Statement stmt = null;
	private String strSql = "";
	private Vector<Object> params = null;
	
	public RawSetImpl(OrmManager oManager , Statement stmt , String strSql){
		this.oManager = oManager;
		this.stmt = stmt; 
		this.strSql = strSql;
	}
	
	public RawSetImpl(OrmManager oManager , Statement stmt , Vector<Object> params){
		this.oManager = oManager;
		this.stmt = stmt;
		this.params = params;
	}
	
	/**execute sql
	 * @return affected rows
	 */
	@Override
	public int exec() {
		// TODO 自动生成的方法存根
		int count = 0;
		try {
			
			//传入的是preparestatment
			if(stmt != null){
				if(stmt instanceof PreparedStatement){
					PreparedStatement pstmt = (PreparedStatement)stmt;
					for(int i=0 ; i<params.size() ; i++){
						pstmt.setObject(i+1, params.get(i));
					}
					
					count = pstmt.executeUpdate();
				
					return count;
					
				}else {
					if(!strSql.equals("")){
						count = stmt.executeUpdate(strSql);
						
						return count;
					}
				}
			}
				
		}catch(Exception e){
			this.oManager.deBugInfo(e.getMessage());
		}
		
		return count;
	}

	@Override
	public boolean queryRow(Object obj) {
		// TODO 自动生成的方法存根
		Class<? extends Object> clazz = obj.getClass();
		//获取tablecache中对应的info
		String typeName = clazz.getName();
		TableInfo tbinfo = null;
		
		tbinfo = oManager.getTableCache().getByTN(typeName);
		
		if(tbinfo == null){
			//提示错误
			oManager.deBugInfo("[RawSet queryRow error]-Model<"+typeName+"> has not registered!");
			return false;
		}
		
		ResultSet rs = null;
		Vector<FieldInfo> fins = tbinfo.getAllFieldInfos();
		//传入的是preparestatment
		if(stmt != null){
			if(stmt instanceof PreparedStatement){
				PreparedStatement pstmt = (PreparedStatement)stmt;
				for(int i=0 ; i<params.size() ; i++){
					try {
						pstmt.setObject(i+1, params.get(i));
					} catch (SQLException e) {
						// TODO 自动生成的 catch 块
						this.oManager.deBugInfo(e.getMessage());
	
					}
				}
				try {
					rs = pstmt.executeQuery();
					if(rs.next()){
						for(FieldInfo fin : fins){
							Field field = null;
							try {
								field = obj.getClass().getDeclaredField(fin.getFieldName());
							} catch (NoSuchFieldException e) {
								// TODO 自动生成的 catch 块
								this.oManager.deBugInfo(e.getMessage());
								
							} catch (SecurityException e) {
								// TODO 自动生成的 catch 块
								this.oManager.deBugInfo(e.getMessage());
								
							}
							field.setAccessible(true);
							try {
								field.set(obj, rs.getObject(fin.getColumnName()));
							} catch (IllegalArgumentException e) {
								// TODO 自动生成的 catch 块
								this.oManager.deBugInfo(e.getMessage());
								
							} catch (IllegalAccessException e) {
								// TODO 自动生成的 catch 块
								this.oManager.deBugInfo(e.getMessage());
								
							}
						}
					}
					rs.close();
					
					return true;
				} catch (SQLException e) {
					// TODO 自动生成的 catch 块
					this.oManager.deBugInfo(e.getMessage());
					return false;
				}
				
				
			}else {
				if(!strSql.equals("")){
					try {
						rs = stmt.executeQuery(strSql);
						if(rs.next()){
							for(FieldInfo fin : fins){
								Field field = null;
								try {
									field = obj.getClass().getDeclaredField(fin.getFieldName());
								} catch (NoSuchFieldException e) {
									// TODO 自动生成的 catch 块
									this.oManager.deBugInfo(e.getMessage());
								} catch (SecurityException e) {
									// TODO 自动生成的 catch 块
									this.oManager.deBugInfo(e.getMessage());
								}
								field.setAccessible(true);
								try {
									field.set(obj, rs.getObject(fin.getColumnName()));
								} catch (IllegalArgumentException e) {
									// TODO 自动生成的 catch 块
									this.oManager.deBugInfo(e.getMessage());
								} catch (IllegalAccessException e) {
									// TODO 自动生成的 catch 块
									this.oManager.deBugInfo(e.getMessage());
								}
							}
						}
						rs.close();
					
						return true;
					} catch (SQLException e) {
						// TODO 自动生成的 catch 块
						this.oManager.deBugInfo(e.getMessage());
					}
					
				}
			}
		}
		
		return false;
	}
	
	//要求对象必须要有默认的构造器
	@Override
	public int queryRows(List list,Class clazz) {
		// TODO 自动生成的方法存根
		//获取tablecache中对应的info
		String typeName = clazz.getName();
		TableInfo tbinfo = null;
		
		tbinfo = oManager.getTableCache().getByTN(typeName);
		
		if(tbinfo == null){
			//提示错误
			oManager.deBugInfo("[RawSet queryRows error]-Model<"+typeName+"> has not registered!");
			return -1;
		}
		
		ResultSet rs = null;
		Vector<FieldInfo> fins = tbinfo.getAllFieldInfos();
		//传入的是preparestatment
		if(stmt != null){
			if(stmt instanceof PreparedStatement){
				PreparedStatement pstmt = (PreparedStatement)stmt;
				for(int i=0 ; i<params.size() ; i++){
					try {
						pstmt.setObject(i+1, params.get(i));
					} catch (SQLException e) {
						// TODO 自动生成的 catch 块
						this.oManager.deBugInfo(e.getMessage());
					}
				}
				try {
					rs = pstmt.executeQuery();
					
					while(rs.next()){
						Object obj = null;
						try {
							obj = clazz.newInstance();
						} catch (InstantiationException e1) {
							// TODO 自动生成的 catch 块
							this.oManager.deBugInfo(e1.getMessage());
						} catch (IllegalAccessException e1) {
							// TODO 自动生成的 catch 块
							this.oManager.deBugInfo(e1.getMessage());
						}
						for(FieldInfo fin : fins){
							Field field = null;
							try {
								field = clazz.getDeclaredField(fin.getFieldName());
							} catch (NoSuchFieldException e) {
								// TODO 自动生成的 catch 块
								this.oManager.deBugInfo(e.getMessage());
							} catch (SecurityException e) {
								// TODO 自动生成的 catch 块
								this.oManager.deBugInfo(e.getMessage());
							}
							field.setAccessible(true);
							try {
								field.set(obj, rs.getObject(fin.getColumnName()));
							} catch (IllegalArgumentException e) {
								// TODO 自动生成的 catch 块
								this.oManager.deBugInfo(e.getMessage());
							} catch (IllegalAccessException e) {
								// TODO 自动生成的 catch 块
								this.oManager.deBugInfo(e.getMessage());
							}
						}
						list.add(obj);
					}
					
					rs.close();
			
					return list.size();
				} catch (SQLException e) {
					// TODO 自动生成的 catch 块
					this.oManager.deBugInfo(e.getMessage());
				}
				
				
			}else {
				if(!strSql.equals("")){
					try {
						rs = stmt.executeQuery(strSql);
						
						while(rs.next()){
							Object obj = null;
							try {
								obj = clazz.newInstance();
							} catch (InstantiationException e1) {
								// TODO 自动生成的 catch 块
								this.oManager.deBugInfo(e1.getMessage());
							} catch (IllegalAccessException e1) {
								// TODO 自动生成的 catch 块
								this.oManager.deBugInfo(e1.getMessage());
							}
							for(FieldInfo fin : fins){
								Field field = null;
								try {
									field = clazz.getDeclaredField(fin.getFieldName());
								} catch (NoSuchFieldException e) {
									// TODO 自动生成的 catch 块
									this.oManager.deBugInfo(e.getMessage());
								} catch (SecurityException e) {
									// TODO 自动生成的 catch 块
									this.oManager.deBugInfo(e.getMessage());
								}
								field.setAccessible(true);
								try {
									field.set(obj, rs.getObject(fin.getColumnName()));
								} catch (IllegalArgumentException e) {
									// TODO 自动生成的 catch 块
									this.oManager.deBugInfo(e.getMessage());
								} catch (IllegalAccessException e) {
									// TODO 自动生成的 catch 块
									this.oManager.deBugInfo(e.getMessage());
								}
							}
							list.add(obj);
						}
						
						rs.close();
						
						return list.size();
					} catch (SQLException e) {
						// TODO 自动生成的 catch 块
						this.oManager.deBugInfo(e.getMessage());
					}
					
				}
			}
		}
		
		return -1;
	}

	@Override
	public RawSet setArgs(Object... objects) {
		// TODO 自动生成的方法存根
		params.removeAllElements();
		for(Object object : objects){
			params.add(object);
		}
		return this;
	}

	//此处务必注意只能赋值一条记录
	@Override
	public int rowsToMap(Map<String,Object> map, String... name) {
		// TODO 自动生成的方法存根
		
		ResultSet rs = null;
		//传入的是preparestatment
		if(stmt != null){
			if(stmt instanceof PreparedStatement){
				//提示错误
				oManager.deBugInfo("[RawSet rowsToMap error]-rowToMap can only use sql without params!");
				return -1;
			}else {
				if(!strSql.equals("")){
					try {
						rs = stmt.executeQuery(strSql);
						
						if(rs.next()){
							for(int i=0;i<name.length;i++){
								map.put(name[i], rs.getObject(name[i]));
							}
						}
						
						rs.close();
					
						
						if(map.size() == 0){
							return -1;
						}else {
							return map.size();
						}
					} catch (SQLException e) {
						// TODO 自动生成的 catch 块
						this.oManager.deBugInfo(e.getMessage());
					}
					
				}
			}
		}
		return -1;
	}

	//要求object中的字段名和sql的列名一致
	@Override
	public int rowsToObject(Object obj, String...name) {
		// TODO 自动生成的方法存根
		Class clazz = obj.getClass();
		ResultSet rs = null;
		//传入的是preparestatment
		if(stmt != null){
			if(stmt instanceof PreparedStatement){
				//提示错误
				oManager.deBugInfo("[RawSet rowsToObject error]-rowToObject can only use sql without params!");
				return -1;
			}else {
				if(!strSql.equals("")){
					try {
						rs = stmt.executeQuery(strSql);
						int i = 0;
						if(rs.next()){
							for(i=0;i<name.length;i++){
								Field field;
								try {
									field = clazz.getDeclaredField(name[i]);
									field.setAccessible(true);
									field.set(obj, rs.getObject(name[i]));
								} catch (NoSuchFieldException e) {
									// TODO 自动生成的 catch 块
									this.oManager.deBugInfo(e.getMessage());
								} catch (SecurityException e) {
									// TODO 自动生成的 catch 块
									this.oManager.deBugInfo(e.getMessage());
								} catch (IllegalArgumentException e) {
									// TODO 自动生成的 catch 块
									this.oManager.deBugInfo(e.getMessage());
								} catch (IllegalAccessException e) {
									// TODO 自动生成的 catch 块
									this.oManager.deBugInfo(e.getMessage());
								}
								
							}
						}
						
						rs.close();
				
						return i;
					} catch (SQLException e) {
						// TODO 自动生成的 catch 块
						this.oManager.deBugInfo(e.getMessage());
					}
					
				}
			}
		}
		return -1;
	}

	@Override
	public RawPreparer prepare() {
		// TODO 自动生成的方法存根
		if(stmt == null){
			return null;
		}
		if(stmt instanceof PreparedStatement){
			PreparedStatement pstmt = (PreparedStatement)stmt;
			RawPreparer rap = new RawPreparerImpl(pstmt,this.oManager);
			return rap;
		}
		return null;
	}

}
