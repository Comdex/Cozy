
package com.reflectsky.cozy.mysqldriver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.reflectsky.cozy.QuerySet;
import com.reflectsky.cozy.core.FieldInfo;
import com.reflectsky.cozy.core.OrmManager;
import com.reflectsky.cozy.core.TableInfo;

/**
 * ORM查询接口实现
 * @author Comdex
 */
public class MySQLQuerySetImpl implements QuerySet {
	
	private static final long serialVersionUID = -3916590626146857830L;
	private OrmManager oManager = null;
	private Connection conn = null;
	private String tableName = "";
	private String strWhere = "";
	private String strOrderBy = "";
	private String strExclude = "";
	private String strLimit = "";
	private Vector<Object> params = new Vector<Object>();
	
	public MySQLQuerySetImpl(OrmManager oManager,Connection conn,String tableName,String limit){
		this.oManager = oManager;
		this.conn = conn;
		this.tableName = tableName;
		this.strLimit = limit;
	}
	
	public MySQLQuerySetImpl(OrmManager oManager,Connection conn,String tableName,String strWhere,String strExclude,String strOrderBy, 
			String strLimit, Vector<Object> params){
		this.oManager = oManager;
		this.conn = conn;
		this.tableName = tableName;
		this.strWhere = strWhere;
		this.strOrderBy = strOrderBy;
		this.strExclude = strExclude;
		this.strLimit = strLimit;
		this.params = params;
	}
	
	// 深复制参数Vector
	private Vector<Object> deepCloneVector(Vector<Object> params){
		ByteArrayOutputStream bo = new ByteArrayOutputStream();
		try{
			ObjectOutputStream oo = new ObjectOutputStream(bo);
			oo.writeObject(params);//从流里读出来
			ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
			ObjectInputStream oi = new ObjectInputStream(bi);
			return (Vector<Object>) (oi.readObject());
		}catch(Exception e){
			this.oManager.deBugInfo(e.getMessage());
		}
		return null;
	}
	
	private String findFieldName(Vector<FieldInfo> fieldinfos, String fieldName){
		for (FieldInfo fi : fieldinfos) {
			if(fi.getFieldName().equals(fieldName)){
				return fi.getColumnName();
			}
		}
		return null;
	}

	/* @author Comdex
	 * @see com.reflectsky.cozy.QuerySet#filter(java.lang.String, java.lang.Object[])
	 */
	@Override
	public QuerySet filter(String expression, Object... ps) {
		// TODO 自动生成的方法存根
		String[] expers = expression.split("__");//split with double underline
		String strWhere = this.strWhere;
		Vector<Object> params = deepCloneVector(this.params);
		Vector<FieldInfo> fieldInfos = this.oManager.getTableCache().get(this.tableName).getAllFieldInfos();
		if(expers.length == 2){
			if(ps.length == 0){
				return null;
			}
			if(expers[1].equalsIgnoreCase("gt")){
				String columnName = findFieldName(fieldInfos, expers[0]);
				if(columnName != null){
					strWhere = strWhere + " and " + columnName + " > " + "?";
				}else {
					strWhere = strWhere + " and " + expers[0] + " > " + "?";
				}
				
				params.add(ps[0]);
			}else if(expers[1].equalsIgnoreCase("gte")){
				String columnName = findFieldName(fieldInfos, expers[0]);
				if(columnName != null){
					strWhere = strWhere + " and " + columnName + " >= " + "?";
				}else {
					strWhere = strWhere + " and " + expers[0] + " >= " + "?";
				}
				
				params.add(ps[0]);
			}else if(expers[1].equalsIgnoreCase("lt")){
				String columnName = findFieldName(fieldInfos, expers[0]);
				if(columnName != null){
					strWhere = strWhere + " and " + columnName + " < " + "?";
				}else{
					strWhere = strWhere + " and " + expers[0] + " < " + "?";
				}
				
				params.add(ps[0]);
			}else if(expers[1].equalsIgnoreCase("lte")){
				String columnName = findFieldName(fieldInfos, expers[0]);
				if(columnName != null){
					strWhere = strWhere + " and " + columnName + " <= " + "?";
				}else {
					strWhere = strWhere + " and " + expers[0] + " <= " + "?";
				}
				
				params.add(ps[0]);
			}else if(expers[1].equalsIgnoreCase("exact")){
				String columnName = findFieldName(fieldInfos, expers[0]);
				if(columnName != null){
					strWhere = strWhere + " and " + columnName + " = " + "?";
				}else {
					strWhere = strWhere + " and " + expers[0] + " = " + "?";
				}
				
				params.add(ps[0]);
			}else if(expers[1].equalsIgnoreCase("iexact")){
				String columnName = findFieldName(fieldInfos, expers[0]);
				if(columnName != null){
					strWhere = strWhere + " and " + columnName + " like " + "?";
				}else {
					strWhere = strWhere + " and " + expers[0] + " like " + "?";
					
				}
				
				params.add(ps[0]);
			}else if(expers[1].equalsIgnoreCase("istartswith")){
				String columnName = findFieldName(fieldInfos, expers[0]);
				if(columnName != null){
					strWhere = strWhere + " and " + columnName + " like " + "?";
				}else {
					strWhere = strWhere + " and " + expers[0] + " like " + "?";
				}
				
				params.add(ps[0] + "%");
			}else if(expers[1].equalsIgnoreCase("iendswith")){
				String columnName = findFieldName(fieldInfos, expers[0]);
				if(columnName != null){
					strWhere = strWhere + " and " + columnName + " like " + "?";
				}else {
					strWhere = strWhere + " and " + expers[0] + " like " + "?";
				}
				
				params.add("%" + ps[0]);
			}else if(expers[1].equalsIgnoreCase("in")){
				String columnName = findFieldName(fieldInfos, expers[0]);
				if(columnName != null){
					strWhere = strWhere + " and " + columnName + " in (";
					for(int i=0 ; i<ps.length ; i++){
						strWhere = strWhere + " ? ";
						params.add(ps[i]);
					}
					strWhere = strWhere + ") ";
				}else{
					strWhere = strWhere + " and " + expers[0] + " in (";
					for(int i=0 ; i<ps.length ; i++){
						strWhere = strWhere + " ? ";
						params.add(ps[i]);
					}
					strWhere = strWhere + ") ";
				}
				
			}else if(expers[1].equalsIgnoreCase("icontains")){
				String columnName = findFieldName(fieldInfos, expers[0]);
				if(columnName != null){
					strWhere = strWhere + " and " + columnName + " like " + "?";
				}else {
					strWhere = strWhere + " and " + expers[0] + " like " + "?";
				}
				
				params.add("%" + ps[0] + "%");
			}else if(expers[1].equalsIgnoreCase("contains")){
				String columnName = findFieldName(fieldInfos, expers[0]);
				if(columnName != null){
					strWhere = strWhere + " and " + columnName + " like BINARY " + "?";
				}else{
					strWhere = strWhere + " and " + expers[0] + " like BINARY " + "?";
				}
					
				params.add("%" + ps[0] + "%");
				
			}else if(expers[1].equalsIgnoreCase("isnull")){
				String columnName = findFieldName(fieldInfos, expers[0]);
				if(columnName != null){
					if((boolean)ps[0] == true){
						strWhere = strWhere + " and " + columnName + " IS NULL";
					}else {
						strWhere = strWhere + " and " + columnName + " IS NOT NULL";
					}
					
				}else {
					if((boolean)ps[0] == true){
						strWhere = strWhere + " and " + expers[0] + " IS NULL";
					}else {
						strWhere = strWhere + " and " + expers[0] + " IS NOT NULL";
					}
				}
			}else if (expers[1].equalsIgnoreCase("startswith")) {
				String columnName = findFieldName(fieldInfos, expers[0]);
				if(columnName != null){
					strWhere = strWhere + " and " + columnName + " like BINARY " + "?";
				}else {
					strWhere = strWhere + " and " + expers[0] + " like BINARY " + "?";
				}
				
				params.add(ps[0] + "%");
			}else if (expers[1].equalsIgnoreCase("endswith")) {
				String columnName = findFieldName(fieldInfos, expers[0]);
				if(columnName != null){
					strWhere = strWhere + " and " + columnName + " like BINARY " + "?";
				}else {
					strWhere = strWhere + " and " + expers[0] + " like BINARY " + "?";
				}
				
				params.add("%" + ps[0]);
			}
		}else if(expers.length == 1){
			if(ps.length == 0){
				return null;
			}
			String columnName = findFieldName(fieldInfos, expers[0]);
			if(columnName != null){
				strWhere = strWhere + " and " + columnName + " = " + "?";
			}else {
				strWhere = strWhere + " and " + expers[0] + " = " + "?";
			}
			
			params.add(ps[0]);
		}
		this.qsDebug(strWhere, params);
		return new MySQLQuerySetImpl(this.oManager, this.conn, this.tableName, strWhere, this.strExclude, this.strOrderBy,
				strLimit,params);
	}

	/* @author Comdex
	 * @see com.reflectsky.cozy.QuerySet#orderBy(java.lang.String[])
	 */
	@Override
	public QuerySet orderBy(String... expressions) {
		// TODO 自动生成的方法存根
		String strOrderBy = this.strOrderBy;
		Vector<FieldInfo> fieldInfos = this.oManager.getTableCache().get(this.tableName).getAllFieldInfos();
		for(int i=0 ; i<expressions.length ; i++){
			if(expressions[i].startsWith("-")){
				String columnName = findFieldName(fieldInfos,expressions[i]);
				if(columnName != null){
					strOrderBy = strOrderBy + " , " + columnName.substring(1) + " " + "DESC";
				}else {
					strOrderBy = strOrderBy + " , " + expressions[i].substring(1) + " " + "DESC";
				}
				
			}else {
				String columnName = findFieldName(fieldInfos,expressions[i]);
				if(columnName != null){
					strOrderBy = strOrderBy + " , " + columnName + " " + "ASC";
				}else {
					strOrderBy = strOrderBy + " , " + expressions[i] + " " + "ASC";
				}
				
			}
		}
		this.qsDebug(strOrderBy, null);
		return new MySQLQuerySetImpl(this.oManager, this.conn, this.tableName, this.strWhere,this.strExclude, strOrderBy,
				this.strLimit,this.params);
	}

	/* @author Comdex
	 * @see com.reflectsky.cozy.QuerySet#count()
	 */
	@Override
	public long count() {
		// TODO 自动生成的方法存根
		String sql = "select count(*) from " + tableName + " ";
		long count = -1;
		PreparedStatement pstmt1 = null;
		ResultSet rs = null;
		
		if(strWhere.startsWith(" and ")){
			strWhere = strWhere.substring(5);
			strWhere = " where " + strWhere;
		}
		
		sql = sql + strWhere;
		sql = sql + strExclude;
		if(!strOrderBy.equals("")){
			strOrderBy = strOrderBy.substring(3);
			strOrderBy = "order by " + strOrderBy;
			sql = sql + " " + strOrderBy;
		}
		sql = sql + strLimit;		
		sql = sql + " ;";
	    try {
	    	pstmt1 = conn.prepareStatement(sql);
			for(int i=0 ; i<params.size() ; i++){
				pstmt1.setObject(i+1, params.get(i));
			}
			oManager.deBugInfo(sql);
			rs = pstmt1.executeQuery();
			if(rs.next()){
				count = rs.getLong(1);
				
			}
			
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			this.oManager.deBugInfo(e.getMessage());

		}
		
		this.qsDebug(sql, null);
		
		if(rs != null){
			this.oManager.closeRs(rs);
		}
		if(pstmt1 != null){
			this.oManager.closeStmt(pstmt1);
		}
		
		
		return count;
	}

	/* @author Comdex
	 * @see com.reflectsky.cozy.QuerySet#exist()
	 */
	@Override
	public boolean exist() {
		// TODO 自动生成的方法存根
		String sql = "select * from " + tableName + " ";
		boolean isExist = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		if(strWhere.startsWith(" and ")){
			strWhere = strWhere.substring(5);
			strWhere = " where " + strWhere;
		}
		
		sql = sql + strWhere;
		sql = sql + strExclude;
		if(!strOrderBy.equals("")){
			strOrderBy = strOrderBy.substring(3);
			strOrderBy = "order by " + strOrderBy;
			sql = sql + " " + strOrderBy;
		}
		sql += strLimit;
		sql += " ;";
		
	    try {
	    	pstmt = conn.prepareStatement(sql);
			for(int i=0 ; i<params.size() ; i++){
				pstmt.setObject(i+1, params.get(i));
			}
			
			this.qsDebug(sql, null);
			
			rs = pstmt.executeQuery();
			isExist = rs.next();
			
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			this.oManager.deBugInfo(e.getMessage());
			
		}
		
		if(rs != null){
			this.oManager.closeRs(rs);
		}
		if(pstmt != null){
			this.oManager.closeStmt(pstmt);
		}
		return isExist;
	}

	/* @author Comdex
	 * @see com.reflectsky.cozy.QuerySet#delete()
	 */
	@Override
	public long delete() {
		// TODO 自动生成的方法存根
		String sql = "delete from " + tableName + " ";
		long count = 0;
		PreparedStatement pstmt = null;
		
		if(strWhere.startsWith(" and ")){
			strWhere = strWhere.substring(5);
			strWhere = " where " + strWhere;
		}
		
		sql = sql + strWhere;
		sql = sql + strExclude;
		if(!strOrderBy.equals("")){
			strOrderBy = strOrderBy.substring(3);
			strOrderBy = "order by " + strOrderBy;
			sql = sql + " " + strOrderBy;
		}
		sql = sql + strLimit;		
		sql = sql + " ;";
	    try {
			pstmt = conn.prepareStatement(sql);
			for(int i=0 ; i<params.size() ; i++){
				pstmt.setObject(i+1, params.get(i));
			}
			
			this.qsDebug(sql, null);
			count = pstmt.executeUpdate();
		
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			this.oManager.deBugInfo(e.getMessage());
			
		}
		
		if(pstmt != null){
			this.oManager.closeStmt(pstmt);
		}
		return count;
	}

	/* @author Comdex
	 * @see com.reflectsky.cozy.QuerySet#one(java.lang.Object, java.lang.String[])
	 */
	@Override
	public boolean one(Object bean, String... ps) {
		// TODO 自动生成的方法存根
		String sql = "select * from " + tableName + " "; 
		boolean isOk = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		if(strWhere.startsWith(" and ")){
			strWhere = strWhere.substring(5);
			strWhere = " where " + strWhere;
		}
		
		sql = sql + strWhere;
		sql = sql + strExclude;
		if(!strOrderBy.equals("")){
			strOrderBy = strOrderBy.substring(3);
			strOrderBy = "order by " + strOrderBy;
			sql = sql + " " + strOrderBy;
		}
		sql = sql + strLimit;
		sql += " ;";
		
	    try {
			pstmt = conn.prepareStatement(sql);
			for(int i=0 ; i<params.size() ; i++){
				pstmt.setObject(i+1, params.get(i));
			}
			
			this.qsDebug(sql, null);
			
			rs = pstmt.executeQuery();
			
			Class clazz = bean.getClass();
			TableInfo tbinfo = oManager.getTableCache().getByTN(clazz.getName());
			Vector<FieldInfo> fins = tbinfo.getAllFieldInfos();
			
			if(rs.next()){
				if(ps.length != 0){
					for(String s : ps){
						for(FieldInfo fin : fins){
							if(fin.getColumnName().equalsIgnoreCase(s)){
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
								field.set(bean, rs.getObject(s));
							}
						}
					}
				}else {
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
							field.set(bean, rs.getObject(fin.getColumnName()));
						}
					}
				}
			
		} catch (SQLException | IllegalArgumentException | IllegalAccessException e) {
			// TODO 自动生成的 catch 块
			this.oManager.deBugInfo(e.getMessage());
		}
		
		if(rs != null){
			this.oManager.closeRs(rs);
		}
		if(pstmt != null){
			this.oManager.closeStmt(pstmt);
		}
		return isOk ;
	}

	/* @author Comdex
	 * @see com.reflectsky.cozy.QuerySet#all(java.lang.Object, java.lang.String[])
	 */
	@Override
	public long all(List list,Class clazz, String... ps) {
		// TODO 自动生成的方法存根
		long count = 0;
		String sql = "select * from " + tableName + " "; 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		if(strWhere.startsWith(" and ")){
			strWhere = strWhere.substring(5);
			strWhere = " where " + strWhere;
		}
		
		sql = sql + strWhere;
		sql = sql + strExclude;
		if(!strOrderBy.equals("")){
			strOrderBy = strOrderBy.substring(3);
			strOrderBy = "order by " + strOrderBy;
			sql = sql + " " + strOrderBy;
		}
		sql += strLimit;
		sql += " ;";
		
	    try {
			pstmt = conn.prepareStatement(sql);
			for(int i=0 ; i<params.size() ; i++){
				pstmt.setObject(i+1, params.get(i));
			}
			
			this.qsDebug(sql, null);
			
			rs = pstmt.executeQuery();
			
			TableInfo tbinfo = oManager.getTableCache().getByTN(clazz.getName());
			Vector<FieldInfo> fins = tbinfo.getAllFieldInfos();
			
			while(rs.next()){
				Object bean= clazz.newInstance();
				
				if(ps.length != 0){
					for(String s : ps){
						for(FieldInfo fin : fins){
							if(fin.getColumnName().equalsIgnoreCase(s)){
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
								field.set(bean, rs.getObject(s));
							}
						}
					}
				}else {
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
							field.set(bean, rs.getObject(fin.getColumnName()));
						}
				   }
				list.add(bean);
			}
				
			count = list.size();
			
		} catch (SQLException | IllegalArgumentException | IllegalAccessException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			
		} catch (InstantiationException e1) {
			// TODO 自动生成的 catch 块
			this.oManager.deBugInfo(e1.getMessage());
			
		}
		
		if(rs != null){
			this.oManager.closeRs(rs);
		}
		if(pstmt != null){
			this.oManager.closeStmt(pstmt);
		}
		return count;
	}

	/* @author Comdex
	 * @see com.reflectsky.cozy.QuerySet#exclude(java.lang.String, java.lang.Object[])
	 */
	@Override
	public QuerySet exclude(String expression, Object... ps) {
		// TODO 自动生成的方法存根
		String[] expers = expression.split("__");//split with double underline
		String strExclude = this.strExclude;
		Vector<Object> params = deepCloneVector(this.params);
		Vector<FieldInfo> fieldInfos = this.oManager.getTableCache().get(this.tableName).getAllFieldInfos();
		if(expers.length == 2){
			if(ps.length == 0){
				return null;
			}
			if(expers[1].equalsIgnoreCase("gt")){
				String columnName = findFieldName(fieldInfos, expers[0]);
				if(columnName != null){
					strExclude = strExclude + " and not " + columnName + " > " + "?";
				}else {
					strExclude = strExclude + " and not " + expers[0] + " > " + "?";
				}
				
				params.add(ps[0]);
			}else if(expers[1].equalsIgnoreCase("gte")){
				String columnName = findFieldName(fieldInfos, expers[0]);
				if(columnName != null){
					strExclude = strExclude + " and not " + columnName + " >= " + "?";
				}else {
					strExclude = strExclude + " and not " + expers[0] + " >= " + "?";
				}
				
				params.add(ps[0]);
			}else if(expers[1].equalsIgnoreCase("lt")){
				String columnName = findFieldName(fieldInfos, expers[0]);
				if(columnName != null){
					strExclude = strExclude + " and not " + columnName + " < " + "?";
				}else{
					strExclude = strExclude + " and not " + expers[0] + " < " + "?";
				}
				
				params.add(ps[0]);
			}else if(expers[1].equalsIgnoreCase("lte")){
				String columnName = findFieldName(fieldInfos, expers[0]);
				if(columnName != null){
					strExclude = strExclude + " and not " + columnName + " <= " + "?";
				}else {
					strExclude = strExclude + " and not " + expers[0] + " <= " + "?";
				}
				
				params.add(ps[0]);
			}else if(expers[1].equalsIgnoreCase("exact")){
				String columnName = findFieldName(fieldInfos, expers[0]);
				if(columnName != null){
					strExclude = strExclude + " and not " + columnName + " = " + "?";
				}else {
					strExclude = strExclude + " and not " + expers[0] + " = " + "?";
				}
				
				params.add(ps[0]);
			}else if(expers[1].equalsIgnoreCase("iexact")){
				String columnName = findFieldName(fieldInfos, expers[0]);
				if(columnName != null){
					strExclude = strExclude + " and not " + columnName + " like " + "?";
				}else {
					strExclude = strExclude + " and not " + expers[0] + " like " + "?";
					
				}
				
				params.add(ps[0]);
			}else if(expers[1].equalsIgnoreCase("istartswith")){
				String columnName = findFieldName(fieldInfos, expers[0]);
				if(columnName != null){
					strExclude = strExclude + " and not " + columnName + " like " + "?";
				}else {
					strExclude = strExclude + " and not " + expers[0] + " like " + "?";
				}
				
				params.add(ps[0] + "%");
			}else if(expers[1].equalsIgnoreCase("iendswith")){
				String columnName = findFieldName(fieldInfos, expers[0]);
				if(columnName != null){
					strExclude = strExclude + " and not " + columnName + " like " + "?";
				}else {
					strExclude = strExclude + " and not " + expers[0] + " like " + "?";
				}
				
				params.add("%" + ps[0]);
			}else if(expers[1].equalsIgnoreCase("in")){
				String columnName = findFieldName(fieldInfos, expers[0]);
				if(columnName != null){
					strExclude = strExclude + " and not " + columnName + " in (";
					for(int i=0 ; i<ps.length ; i++){
						strExclude = strExclude + " ? ";
						params.add(ps[i]);
					}
					strExclude = strExclude + ") ";
				}else{
					strExclude = strExclude + " and not " + expers[0] + " in (";
					for(int i=0 ; i<ps.length ; i++){
						strExclude = strExclude + " ? ";
						params.add(ps[i]);
					}
					strExclude = strExclude + ") ";
				}
				
			}else if(expers[1].equalsIgnoreCase("icontains")){
				String columnName = findFieldName(fieldInfos, expers[0]);
				if(columnName != null){
					strExclude = strExclude + " and not " + columnName + " like " + "?";
				}else {
					strExclude = strExclude + " and not " + expers[0] + " like " + "?";
				}
				
				params.add("%" + ps[0] + "%");
			}else if(expers[1].equalsIgnoreCase("contains")){
				String columnName = findFieldName(fieldInfos, expers[0]);
				if(columnName != null){
					strExclude = strExclude + " and not " + columnName + " like BINARY " + "?";
				}else{
					strExclude = strExclude + " and not " + expers[0] + " like BINARY " + "?";
				}
					
				params.add("%" + ps[0] + "%");
				
			}else if(expers[1].equalsIgnoreCase("isnull")){
				String columnName = findFieldName(fieldInfos, expers[0]);
				if(columnName != null){
					if((boolean)ps[0] == true){
						strExclude = strExclude + " and not " + columnName + " IS NULL";
					}else {
						strExclude = strExclude + " and not " + columnName + " IS NOT NULL";
					}
					
				}else {
					if((boolean)ps[0] == true){
						strExclude = strExclude + " and not " + expers[0] + " IS NULL";
					}else {
						strExclude = strExclude + " and not " + expers[0] + " IS NOT NULL";
					}
				}
			}else if (expers[1].equalsIgnoreCase("startswith")) {
				String columnName = findFieldName(fieldInfos, expers[0]);
				if(columnName != null){
					strExclude = strExclude + " and not " + columnName + " like BINARY " + "?";
				}else {
					strExclude = strExclude + " and not " + expers[0] + " like BINARY " + "?";
				}
				
				params.add(ps[0] + "%");
			}else if (expers[1].equalsIgnoreCase("endswith")) {
				String columnName = findFieldName(fieldInfos, expers[0]);
				if(columnName != null){
					strExclude = strExclude + " and not " + columnName + " like BINARY " + "?";
				}else {
					strExclude = strExclude + " and not " + expers[0] + " like BINARY " + "?";
				}
				
				params.add("%" + ps[0]);
			}
		}else if(expers.length == 1){
			if(ps.length == 0){
				return null;
			}
			String columnName = findFieldName(fieldInfos, expers[0]);
			if(columnName != null){
				strExclude = strExclude + " and not " + columnName + " = " + "?";
			}else {
				strExclude = strExclude + " and not " + expers[0] + " = " + "?";
			}
			
			params.add(ps[0]);
		}
		this.qsDebug(strExclude, params);
		return new MySQLQuerySetImpl(this.oManager, this.conn, this.tableName, this.strWhere, strExclude, this.strOrderBy,
				this.strLimit,params);
	}

	/* @author Comdex
	 * @see com.reflectsky.cozy.QuerySet#limit(int, long[])
	 */
	@Override
	public QuerySet limit(int count, long... offset) {
		// TODO 自动生成的方法存根
		String strLimit = "";
		if(offset.length == 0){
			int off = this.strLimit.indexOf(",");
			// 如果不存在offset
			if(off == -1){
				strLimit = " LIMIT " + count;
				this.qsDebug(strLimit, null);
				return new MySQLQuerySetImpl(this.oManager, this.conn, this.tableName, this.strWhere, this.strExclude, this.strOrderBy,
						strLimit,this.params);
			}else {
				strLimit = this.strLimit.substring(0, off+1) + " " + count;
				this.qsDebug(strLimit, null);
				return new MySQLQuerySetImpl(this.oManager, this.conn, this.tableName, this.strWhere, this.strExclude, this.strOrderBy,
						strLimit,this.params);
			}
		}else {
			strLimit = " LIMIT " + offset[0] + ", " + count;
			this.qsDebug(strLimit, null);
			return new MySQLQuerySetImpl(this.oManager, this.conn, this.tableName, this.strWhere, this.strExclude, this.strOrderBy,
					strLimit,this.params);
		}
	}

	/* @author Comdex
	 * @see com.reflectsky.cozy.QuerySet#offset(long)
	 */
	@Override
	public QuerySet offset(long offset) {
		// TODO 自动生成的方法存根
		String strLimit = "";
		int off = this.strLimit.indexOf(",");
		if(off == -1){
			String count = this.strLimit.substring(7);
			strLimit = " LIMIT " + offset + ", " + count;
			this.qsDebug(strLimit, null);
			return new MySQLQuerySetImpl(this.oManager, this.conn, this.tableName, this.strWhere, this.strExclude, this.strOrderBy,
					strLimit,this.params);
		}else{
			String count = this.strLimit.substring(off);
			strLimit = " LIMIT " + offset + count;
			this.qsDebug(strLimit, null);
			return new MySQLQuerySetImpl(this.oManager, this.conn, this.tableName, this.strWhere, this.strExclude, this.strOrderBy,
					strLimit,this.params);
		}
	}
	
	/* @author Comdex
	 * @see com.reflectsky.cozy.QuerySet#values
	 */
	public List<Map<String, String>> values(){
		// TODO 自动生成的方法存根
		ArrayList<Map<String,String>> maps = new ArrayList<Map<String,String>>();
		String sql = "select * from " + tableName + " "; 
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		if(strWhere.startsWith(" and ")){
			strWhere = strWhere.substring(5);
			strWhere = " where " + strWhere;
		}
		
		sql = sql + strWhere;
		sql = sql + strExclude;
		if(!strOrderBy.equals("")){
			strOrderBy = strOrderBy.substring(3);
			strOrderBy = "order by " + strOrderBy;
			sql = sql + " " + strOrderBy;
		}
		sql += strLimit;
		sql += " ;";
		
	    try {
			pstmt = conn.prepareStatement(sql);
			for(int i=0 ; i<params.size() ; i++){
				
				pstmt.setObject(i+1, params.get(i));
			}
			
			this.qsDebug(sql, null);
			
			rs = pstmt.executeQuery();
	
			int size = rs.getRow();
			//初始化mps
			for(int i=0; i<size; i++){
				maps.add(new HashMap<String, String>());
			}
			
			int cursor = 0;
			//获取数据库元信息
			ResultSetMetaData metaData = rs.getMetaData();
			//获取列数
			int columnCount = metaData.getColumnCount();
			//获取tableName对应的TableInfo
			TableInfo tbinfo = this.oManager.getTableCache().get(this.tableName);
			//获取对应字段信息
			Vector<FieldInfo> fields = tbinfo.getAllFieldInfos();
			
			while(rs.next()){
				Map<String,String> map = maps.get(cursor);
				for(int i=1; i<=columnCount; i++){
					String fieldName = null;
					for(int j=0; j<fields.size(); j++){
						FieldInfo field = fields.get(j);
						if(field.getColumnName().equals(metaData.getColumnName(i))){
							fieldName = field.getFieldName();
						}
					}
					if(fieldName != null){
						map.put(fieldName, rs.getString(i));
					}
					
				}

			}
					
		} catch (SQLException | IllegalArgumentException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			
		}
		
		if(rs != null){
			this.oManager.closeRs(rs);
		}
		if(pstmt != null){
			this.oManager.closeStmt(pstmt);
		}

		return maps;
	}
	
	private void qsDebug(String sql,Vector<Object> params){
		if(this.oManager.isDebug()){
			if(params != null){
				sql += "  parameter:";
				for(Object object : params){
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
			}
			this.oManager.deBugInfo(sql);	
		}
	}
}
