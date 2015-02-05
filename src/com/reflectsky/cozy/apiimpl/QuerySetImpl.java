
package com.reflectsky.cozy.apiimpl;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Vector;

import com.reflectsky.cozy.QuerySet;
import com.reflectsky.cozy.core.FieldInfo;
import com.reflectsky.cozy.core.OrmManager;
import com.reflectsky.cozy.core.TableInfo;

/**
 * ORM查询接口实现
 * @author Comdex
 */
public class QuerySetImpl implements QuerySet {
	private OrmManager oManager = null;
	private Connection conn = null;
	private String tableName = "";
	private String strWhere = "";
	private String strOrderBy = "";
	private Vector<Object> params = new Vector<Object>();
	
	public QuerySetImpl(OrmManager oManager,Connection conn,String tableName){
		this.oManager = oManager;
		this.conn = conn;
		this.tableName = tableName;
	}
	

	/* @author Comdex
	 * @see com.reflectsky.cozy.QuerySet#filter(java.lang.String, java.lang.Object[])
	 */
	@Override
	public QuerySet filter(String expression, Object... ps) {
		// TODO 自动生成的方法存根
		String[] expers = expression.split("__");//split with double underline
		if(expers.length == 2){
			if(expers[1].equalsIgnoreCase("gt")){
				strWhere = strWhere + " and " + expers[0] + " > " + "?";
				params.add(ps[0]);
			}else if(expers[1].equalsIgnoreCase("gte")){
				strWhere = strWhere + " and " + expers[0] + " >= " + "?";
				params.add(ps[0]);
			}else if(expers[1].equalsIgnoreCase("lt")){
				strWhere = strWhere + " and " + expers[0] + " < " + "?";
				params.add(ps[0]);
			}else if(expers[1].equalsIgnoreCase("lte")){
				strWhere = strWhere + " and " + expers[0] + " <= " + "?";
				params.add(ps[0]);
			}else if(expers[1].equalsIgnoreCase("exact")){
				strWhere = strWhere + " and " + expers[0] + " = " + "?";
				params.add(ps[0]);
			}else if(expers[1].equalsIgnoreCase("iexact")){
				strWhere = strWhere + " and " + expers[0] + " like " + "?";
				params.add(ps[0]);
			}else if(expers[1].equalsIgnoreCase("istartswith")){
				strWhere = strWhere + " and " + expers[0] + " like " + "?";
				params.add(ps[0] + "%");
			}else if(expers[1].equalsIgnoreCase("iendswith")){
				strWhere = strWhere + " and " + expers[0] + " like " + "?";
				params.add("%" + ps[0]);
			}else if(expers[1].equalsIgnoreCase("in")){
				strWhere = strWhere + " and " + expers[0] + " in (";
				for(int i=0 ; i<ps.length ; i++){
					strWhere = strWhere + " ? ";
					params.add(ps[i]);
				}
				strWhere = strWhere + ") ";
			}
		}else if(expers.length == 1){
			strWhere = strWhere + " and " + expers[0] + " = " + "?";
			params.add(ps[0]);
		}
		return this;
	}

	/* @author Comdex
	 * @see com.reflectsky.cozy.QuerySet#orderBy(java.lang.String[])
	 */
	@Override
	public QuerySet orderBy(String... expressions) {
		// TODO 自动生成的方法存根
		for(int i=0 ; i<expressions.length ; i++){
			if(expressions[i].startsWith("-")){
				strOrderBy = strOrderBy + " , " + expressions[i].substring(1) + " " + "DESC";
			}else {
				strOrderBy = strOrderBy + " , " + expressions[i] + " " + "ASC";
			}
		}
		return this;
	}

	/* @author Comdex
	 * @see com.reflectsky.cozy.QuerySet#count()
	 */
	@Override
	public long count() {
		// TODO 自动生成的方法存根
		String sql = "select count(*) from " + tableName + " ";
		if(strWhere.startsWith(" and ")){
			strWhere = strWhere.substring(5);
			strWhere = " where " + strWhere;
			sql = sql + strWhere + " ;";
		    try {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				for(int i=0 ; i<params.size() ; i++){
					pstmt.setObject(i+1, params.get(i));
				}
				oManager.deBugInfo(sql);
				ResultSet rs = pstmt.executeQuery();
				if(rs.next()){
					return rs.getLong(1);
				}
				
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		oManager.deBugInfo(sql);
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		ResultSet rs = null;
		try {
			rs = pstmt.executeQuery();
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		try {
			if(rs.next()){
				return rs.getLong(1);
			}
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return -1;
	}

	/* @author Comdex
	 * @see com.reflectsky.cozy.QuerySet#exist()
	 */
	@Override
	public boolean exist() {
		// TODO 自动生成的方法存根
		String sql = "select * from " + tableName + " ";
		if(strWhere.startsWith(" and ")){
			strWhere = strWhere.substring(5);
			strWhere = " where " + strWhere;
			sql = sql + strWhere + " ;";
		    try {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				for(int i=0 ; i<params.size() ; i++){
					pstmt.setObject(i+1, params.get(i));
				}
				
				oManager.deBugInfo(sql);
				
				ResultSet rs = pstmt.executeQuery();
				return rs.next();
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		return false;
	}

	/* @author Comdex
	 * @see com.reflectsky.cozy.QuerySet#delete()
	 */
	@Override
	public long delete() {
		// TODO 自动生成的方法存根
		String sql = "delete from " + tableName + " ";
		if(strWhere.startsWith(" and ")){
			strWhere = strWhere.substring(5);
			strWhere = " where " + strWhere;
			sql = sql + strWhere + " ;";
		    try {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				for(int i=0 ; i<params.size() ; i++){
					pstmt.setObject(i+1, params.get(i));
				}
				
				oManager.deBugInfo(sql);
				
				return pstmt.executeUpdate();
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		return 0;
	}

	/* @author Comdex
	 * @see com.reflectsky.cozy.QuerySet#one(java.lang.Object, java.lang.String[])
	 */
	@Override
	public boolean one(Object bean, String... ps) {
		// TODO 自动生成的方法存根
		String sql = "select * from " + tableName + " "; 
		if(strWhere.startsWith(" and ")){
			strWhere = strWhere.substring(5);
			strWhere = " where " + strWhere;
			sql = sql + strWhere;
			if(!strOrderBy.equals("")){
				strOrderBy = strOrderBy.substring(3);
				strOrderBy = "order by " + strOrderBy;
				sql = sql + " " + strOrderBy;
			}
			sql += " ;";
		    try {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				for(int i=0 ; i<params.size() ; i++){
					pstmt.setObject(i+1, params.get(i));
				}
				
				oManager.deBugInfo(sql);
				
				ResultSet rs = pstmt.executeQuery();
				
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
										e.printStackTrace();
									} catch (SecurityException e) {
										// TODO 自动生成的 catch 块
										e.printStackTrace();
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
									e.printStackTrace();
								} catch (SecurityException e) {
									// TODO 自动生成的 catch 块
									e.printStackTrace();
								}
								field.setAccessible(true);
								field.set(bean, rs.getObject(fin.getColumnName()));
							}
						}
					}
					
				return true;
			} catch (SQLException | IllegalArgumentException | IllegalAccessException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		return false;
	}

	/* @author Comdex
	 * @see com.reflectsky.cozy.QuerySet#all(java.lang.Object, java.lang.String[])
	 */
	@Override
	public long all(List list,Class clazz, String... ps) {
		// TODO 自动生成的方法存根
		String sql = "select * from " + tableName + " "; 
		if(strWhere.startsWith(" and ")){
			strWhere = strWhere.substring(5);
			strWhere = " where " + strWhere;
			sql = sql + strWhere;
			if(!strOrderBy.equals("")){
				strOrderBy = strOrderBy.substring(3);
				strOrderBy = "order by " + strOrderBy;
				sql = sql + " " + strOrderBy;
			}
			sql += " ;";
		    try {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				for(int i=0 ; i<params.size() ; i++){
					pstmt.setObject(i+1, params.get(i));
				}
				
				oManager.deBugInfo(sql);
				
				ResultSet rs = pstmt.executeQuery();
				
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
										e.printStackTrace();
									} catch (SecurityException e) {
										// TODO 自动生成的 catch 块
										e.printStackTrace();
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
									e.printStackTrace();
								} catch (SecurityException e) {
									// TODO 自动生成的 catch 块
									e.printStackTrace();
								}
								field.setAccessible(true);
								field.set(bean, rs.getObject(fin.getColumnName()));
							}
					   }
					list.add(bean);
				}
					
					
				return list.size();
			} catch (SQLException | IllegalArgumentException | IllegalAccessException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (InstantiationException e1) {
				// TODO 自动生成的 catch 块
				e1.printStackTrace();
			}
		}
		return -1;
	}

}
