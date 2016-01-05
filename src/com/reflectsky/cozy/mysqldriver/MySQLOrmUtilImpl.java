package com.reflectsky.cozy.mysqldriver;

import java.lang.reflect.Field;
import java.util.Vector;

import com.reflectsky.cozy.OrmUtil;
import com.reflectsky.cozy.annoation.Orm;
import com.reflectsky.cozy.annoation.TableName;
import com.reflectsky.cozy.core.FieldInfo;
import com.reflectsky.cozy.core.OrmManager;
import com.reflectsky.cozy.core.TableInfo;

public class MySQLOrmUtilImpl implements OrmUtil{
	private OrmManager oManager = null;
	
	public MySQLOrmUtilImpl(OrmManager oManager){
		this.oManager = oManager;
	}

	@Override
	public TableInfo getTableInfo(Class clazz)throws Exception{
		// TODO 自动生成的方法存根
		TableInfo tbinfo = new TableInfo();
		String fullName = clazz.getName();
		String simpleName = clazz.getSimpleName();

			//默认类名为表名
		tbinfo.setTableName(simpleName);
		tbinfo.setTypeName(fullName);
		//判断是否有TableName注解
		TableName tn = (TableName) clazz.getAnnotation(TableName.class);
		if(tn != null){
			tbinfo.setTableName(tn.value());
		}
		
		//无注解且字段名为id类型为short,int,long的字段个数
		int specialNum = 0;
		Field[] fields = clazz.getDeclaredFields();
		for(Field f : fields){
			//获取字段注解
			Orm or = f.getAnnotation(Orm.class);
			String fieldType = f.getType().getName();
			FieldInfo fin = null;
			//如果字段没有注解
			if(or == null){
				//数据库字段默认not null
				//如果该字段名为Id,id或ID,iD且其类型为short,int,long则默认为递增的主键
				if(f.getName().equalsIgnoreCase("id")){
					if(fieldType.equalsIgnoreCase("int")||fieldType.equalsIgnoreCase("long")||fieldType.equalsIgnoreCase("short")
							||fieldType.equalsIgnoreCase("java.lang.Integer")||fieldType.equalsIgnoreCase("java.lang.Long")
							||fieldType.equalsIgnoreCase("java.lang.Short")){
							specialNum++;
					}	
				}
				//字段类型为int或Integer
				if(fieldType.equalsIgnoreCase("int")||fieldType.equalsIgnoreCase("java.lang.Integer")){
					fin = new FieldInfo(f.getName(),f.getName(), false, false, "integer", 0, 0, 0, "", fieldType, null);
				}else if(fieldType.equalsIgnoreCase("java.lang.String")){
					//如果该字段类型为String默认fieldinfo的type为varchar(255)
					fin = new FieldInfo(f.getName(),f.getName(), false, false, "varchar", 255, 0, 0, "", fieldType, null);
				}else if(fieldType.equalsIgnoreCase("long")||fieldType.equalsIgnoreCase("java.lang.Long")){
					fin = new FieldInfo(f.getName(),f.getName(), false, false, "bigint", 0, 0, 0, "", fieldType, null);
				}else if(fieldType.equalsIgnoreCase("short")||fieldType.equalsIgnoreCase("java.lang.Short")){
					fin = new FieldInfo(f.getName(),f.getName(), false, false, "tinyint", 0, 0, 0, "", fieldType, null);
				}else if(fieldType.equalsIgnoreCase("float")||fieldType.equalsIgnoreCase("double")
						||fieldType.equalsIgnoreCase("java.lang.Float")||fieldType.equalsIgnoreCase("java.lang.Double")){
					fin = new FieldInfo(f.getName(),f.getName(), false, false, "double", 0, 0, 0, "", fieldType, null);
				}else if(fieldType.equalsIgnoreCase("java.util.Date")){
					//如果该字段类型为Date默认其type为datetime
					fin = new FieldInfo(f.getName(),f.getName(), false, false, "datetime", 0, 0, 0, "", fieldType, null);
				}else if(fieldType.equalsIgnoreCase("boolean")||fieldType.equalsIgnoreCase("java.lang.Boolean")){
					fin = new FieldInfo(f.getName(),f.getName(), false, false, "bool", 0, 0, 0, "", fieldType, null);
				}
					
				
			}else{
				if(or.ignore() == true){
					continue;
				}else{
					fin = new FieldInfo();
					//获取字段名,如果为默认
					if(or.column().equals("")){
						fin.setColumnName(f.getName());
					}else{
						fin.setColumnName(or.column());
					}
					
					fin.setFieldName(f.getName());
					fin.setTypeName(f.getClass().getName());
					
					//是否主键
					if(or.pk()){
						fin.setPrimaryKey(true);
					}else{
						fin.setPrimaryKey(false);
					}
					
					
					//处理other的tag
					if(!or.other().equals("")){
						fin.setOther(or.other());
					}
					
					//是否自增长
					if(or.auto()){
						fin.setAutoGenerate(true);
					}else{
						fin.setAutoGenerate(false);
					}
					
					//判断字段类型进行后续注解的操作
					if(fieldType.equalsIgnoreCase("int")||fieldType.equalsIgnoreCase("java.lang.Integer")){
						
						if(!or.type().equals("") && !or.type().equals("integer")){
							throw new Exception("[Register model err]-class<"+clazz.getSimpleName()+">-field<"+f.getName()+">-The int/Integer type can just convert to the type integer of database!");
						}else{
							fin.setType("integer");	
						}
						
						if(or.digits()!=0 || or.decimals()!=0){
							throw new Exception("[Register model err]-class<"+clazz.getSimpleName()+">-field<"+f.getName()+">-The int/Integer type can not have digits/decimals!");
						}
					}else if(fieldType.equalsIgnoreCase("java.lang.String")){
						//如果type设置了text
						if(or.type().equals("text")){
							fin.setType("longtext");
						}else if(or.type().equals("varchar")){
							fin.setType("varchar");
							if(or.size() != 0){
								fin.setSize(or.size());
							}else{
								fin.setSize(255);
							}
							
						}else if(or.type() == null || or.type().equals("")){
							fin.setType("varchar");
							if(or.size() != 0){
								fin.setSize(or.size());
							}else{
								fin.setSize(255);
							}
						}else{
							throw new Exception("[Register model err]-class<"+clazz.getSimpleName()+">-field<"+f.getName()+">-Parameters of the type field of String type at present can only be set to varchar or text!");
						}
						
						if(fin.isAutoGenerate()==true){
							throw new Exception("[Register model err]-class<"+clazz.getSimpleName()+">-field<"+f.getName()+">-The String type can not be autogenerate key!");
						}
						if(or.digits()!=0 || or.decimals()!=0){
							throw new Exception("[Register model err]-class<"+clazz.getSimpleName()+">-field<"+f.getName()+">-The String type can not have digits/decimals!");
						}
					
					}else if(fieldType.equalsIgnoreCase("long")||fieldType.equalsIgnoreCase("java.lang.Long")){
						
						if(!or.type().equals("") && !or.type().equals("bigint")){
							throw new Exception("[Register model err]-class<"+clazz.getSimpleName()+">-field<"+f.getName()+">-The long/Long type can just convert to the type bigint of database!");
						}else{
							fin.setType("bigint");
						}
						
						if(or.digits()!=0 || or.decimals()!=0){
							throw new Exception("[Register model err]-class<"+clazz.getSimpleName()+">-field<"+f.getName()+">-The long/Long type can not have digits/decimals!");
						}
					}else if(fieldType.equalsIgnoreCase("short")||fieldType.equalsIgnoreCase("java.lang.Short")){
						
						if(!or.type().equals("") && !or.type().equals("tinyint")){
							throw new Exception("[Register model err]-class<"+clazz.getSimpleName()+">-field<"+f.getName()+">-The short/Short type can just convert to the type tinyint of database!");
						}else {
							fin.setType("tinyint");
						}
						
						if(or.digits()!=0 || or.decimals()!=0){
							throw new Exception("[Register model err]-class<"+clazz.getSimpleName()+">-field<"+f.getName()+">-The short/Short type can not have digits/decimals!");
						}
					}else if(fieldType.equalsIgnoreCase("float")||fieldType.equalsIgnoreCase("double")
							||fieldType.equalsIgnoreCase("java.lang.Short")||fieldType.equalsIgnoreCase("java.lang.Double")){
						
						if(!or.type().equals("") && !or.type().equals("double") && !or.type().equals("numeric")){
							throw new Exception("[Register model err]-class<"+clazz.getSimpleName()+">-field<"+f.getName()+">-The float/double(Float/Double) type can just convert to the type double/numeric of database!");
						}else if(or.type().equals("double")) {
							fin.setType("double");
						}else{
							fin.setType("numeric");
						}
						
						if(or.digits() != 0 ){
					
							fin.setDigits(or.digits());
						}
						if(or.decimals() != 0){
							
							fin.setDecimals(or.decimals());
						}
						
						if(fin.isAutoGenerate()==true){
							throw new Exception("[Register model err]-class<"+clazz.getSimpleName()+">-field<"+f.getName()+">-The float/double type can not be autogenerate key!");
						}
		
					}else if(fieldType.equalsIgnoreCase("Date")){
						if(or.type().equals("date")){
							fin.setType("date");
						}else if(or.type().equals("") || or.type().equals("datetime")){
							fin.setType("datetime");
						}else{
							throw new Exception("[Register model err]-class<"+clazz.getSimpleName()+">-field<"+f.getName()+">-The Date type can just convert to the type date/datetime of database!");
						}
						
						if(fin.isAutoGenerate()==true){
							throw new Exception("[Register model err]-class<"+clazz.getSimpleName()+">-field<"+f.getName()+">-The Date type can not be autogenerate key!");
						}
						
						if(or.digits()!=0 || or.decimals()!=0){
							throw new Exception("[Register model err]-class<"+clazz.getSimpleName()+">-field<"+f.getName()+">-The Date type can not have digits/decimals!");
						}
					}else if(fieldType.equalsIgnoreCase("boolean")||fieldType.equalsIgnoreCase("java.lang.Boolean")){
						
						if(!or.type().equals("") && !or.type().equals("bool")){
							throw new Exception("[Register model err]-class<"+clazz.getSimpleName()+">-field<"+f.getName()+">-The boolean/Boolean type can just convert to the type bool of database!");
						}else {
							fin.setType("bool");
						}
						if(fin.isAutoGenerate()==true){
							throw new Exception("[Register model err]-class<"+clazz.getSimpleName()+">-field<"+f.getName()+">-The boolean/Boolean type can not be autogenerate key!");
						}
						
						if(or.digits()!=0 || or.decimals()!=0){
							throw new Exception("[Register model err]-class<"+clazz.getSimpleName()+">-field<"+f.getName()+">-The boolean/Boolean type can not have digits/decimals!");
						}
					}	
					
				}
			}
			if(fin != null){
				tbinfo.addFieldInfo(fin);	
			}	
		}
		//判断是否有重复的主键或自增长键
		if(tbinfo != null){
			Vector<FieldInfo> fins = tbinfo.getAllFieldInfos();
			if(fins.size() == 0){
				throw new Exception("[Register model error]-Registered entity class <"+tbinfo.getTypeName()+"> does not exist field!");
			}else{
				int pkNumber = 0;//主键个数
				int autoNumber = 0;//自增键个数
				for(FieldInfo fin : fins){
					if(fin.isPrimaryKey()){
						pkNumber++;
					}
					if(fin.isAutoGenerate()){
						autoNumber++;
					}
				}
				
				//此处没有考虑到是否存在类型和名都符合的但有注解的"id"
				if(pkNumber == 0 && autoNumber == 0 && specialNum == 1){
					for(FieldInfo fin : fins){
						String typeName = fin.getTypeName();
						if(typeName.equals("int")||typeName.equals("long")||typeName.equals("short")
								||typeName.equals("java.lang.Integer")||typeName.equals("java.lang.Long")||typeName.equals("java.lang.Short")){
							if(fin.getFieldName().equalsIgnoreCase("id")){
								fin.setPrimaryKey(true);
								fin.setAutoGenerate(true);
							}
						}
					}
				}
				
				if(pkNumber > 1){
					throw new Exception("[Register model error]-class<"+tbinfo.getTypeName()+">-A duplicate primary key is not allowed!");
				}
				if(autoNumber > 1){
					throw new Exception("[Register model error]-class<"+tbinfo.getTypeName()+">-A plurality of self growth field is not allowed!");
				}
			}
		}
		return tbinfo;
	}

	@Override
	public String generateTableCreateSql(TableInfo tbinfo) {
		// TODO 自动生成的方法存根
		String strSql = "";
		if(tbinfo != null){
			/*//删除表再建表
			if(Force){
				strSql += "DROP TABLE IF EXISTS " + tbinfo.getTableName() + "; ";
			}*/
			
			strSql += "CREATE TABLE IF NOT EXISTS " + tbinfo.getTableName() + "( ";
			Vector<FieldInfo> fields = tbinfo.getAllFieldInfos();
			String endstr = "";
			for(FieldInfo f : fields){
				if(f.getType().equals("varchar")){
					strSql += f.getColumnName() + " " + f.getType() + "(" + f.getSize() + ") " ;
				}else if(f.getType().equals("double") || f.getType().equals("numeric")){
					if(f.getDigits() != 0 && f.getDecimals() != 0){
						strSql += f.getColumnName()  + " "+f.getType()+"(" + f.getDigits() +"," + f.getDecimals() + ") ";
					}else{
						strSql += f.getColumnName() + " " + f.getType() + " " ;
					}
				}else{
					strSql += f.getColumnName() + " " + f.getType() + " " ;
				}

				//处理fieldinfo的other属性
				if(f.getOther() != null){
					if(!f.getOther().equals("")){
						String[] tags = f.getOther().split(";");
						for(String tag : tags){
							if(oManager.getSupportTag().get(tag) == 1){
								strSql += tag + " ";
							}else if(oManager.getSupportTag().get(tag) == 2){//为index
								endstr += " , " + tag + "(" + f.getColumnName() + ") ";
							}
						}
					}
				}
				
				if(f.isAutoGenerate()){
					strSql += "AUTO_INCREMENT  ";
				}
				if(f.isPrimaryKey()){
					strSql += "PRIMARY KEY  ";
				}
				strSql += ", ";
			}
			if(strSql.endsWith(", ")){
				strSql = strSql.substring(0, strSql.length()-2);
			}
			strSql += endstr + ");  ";
		}
		return strSql;

	}

}
