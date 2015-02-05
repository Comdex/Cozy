package com.reflectsky.cozy.apiimpl;

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
		// TODO �Զ����ɵķ������
		TableInfo tbinfo = new TableInfo();
		String fullName = clazz.getName();
		String simpleName = clazz.getSimpleName();

			//Ĭ������Ϊ����
		tbinfo.setTableName(simpleName);
		tbinfo.setTypeName(fullName);
		//�ж��Ƿ���TableNameע��
		TableName tn = (TableName) clazz.getAnnotation(TableName.class);
		if(tn != null){
			tbinfo.setTableName(tn.value());
		}
		
		//��ע�����ֶ���Ϊid����Ϊshort,int,long���ֶθ���
		int specialNum = 0;
		Field[] fields = clazz.getDeclaredFields();
		for(Field f : fields){
			//��ȡ�ֶ�ע��
			Orm or = f.getAnnotation(Orm.class);
			String fieldType = f.getType().getName();
			FieldInfo fin = null;
			//����ֶ�û��ע��
			if(or == null){
				//���ݿ��ֶ�Ĭ��not null
				//������ֶ���ΪId,id��ID,iD��������Ϊshort,int,long��Ĭ��Ϊ����������
				if(f.getName().equalsIgnoreCase("id")){
					if(fieldType.equalsIgnoreCase("int")||fieldType.equalsIgnoreCase("long")||fieldType.equalsIgnoreCase("short")){
							specialNum++;
					}	
				}
				//�ֶ�����Ϊint
				if(fieldType.equalsIgnoreCase("int")){
					fin = new FieldInfo(f.getName(),f.getName(), false, false, "integer", 0, 0, 0, "", fieldType, null);
				}else if(fieldType.equalsIgnoreCase("java.lang.String")){
					//������ֶ�����ΪStringĬ��fieldinfo��typeΪvarchar(255)
					fin = new FieldInfo(f.getName(),f.getName(), false, false, "varchar", 255, 0, 0, "", fieldType, null);
				}else if(fieldType.equalsIgnoreCase("long")){
					fin = new FieldInfo(f.getName(),f.getName(), false, false, "bigint", 0, 0, 0, "", fieldType, null);
				}else if(fieldType.equalsIgnoreCase("short")){
					fin = new FieldInfo(f.getName(),f.getName(), false, false, "tinyint", 0, 0, 0, "", fieldType, null);
				}else if(fieldType.equalsIgnoreCase("float")||fieldType.equalsIgnoreCase("double")){
					fin = new FieldInfo(f.getName(),f.getName(), false, false, "double", 0, 0, 0, "", fieldType, null);
				}else if(fieldType.equalsIgnoreCase("java.util.Date")){
					//������ֶ�����ΪDateĬ����typeΪdatetime
					fin = new FieldInfo(f.getName(),f.getName(), false, false, "datetime", 0, 0, 0, "", fieldType, null);
				}else if(fieldType.equalsIgnoreCase("boolean")){
					fin = new FieldInfo(f.getName(),f.getName(), false, false, "bool", 0, 0, 0, "", fieldType, null);
				}
					
				
			}else{
				if(or.ignore() == true){
					continue;
				}else{
					fin = new FieldInfo();
					//��ȡ�ֶ���,���ΪĬ��
					if(or.column().equals("")){
						fin.setColumnName(f.getName());
					}else{
						fin.setColumnName(or.column());
					}
					
					fin.setFieldName(f.getName());
					fin.setTypeName(f.getClass().getName());
					
					//�Ƿ�����
					if(or.pk()){
						fin.setPrimaryKey(true);
					}else{
						fin.setPrimaryKey(false);
					}
					
					
					//����other��tag
					if(!or.other().equals("")){
						fin.setOther(or.other());
					}
					
					//�Ƿ�������
					if(or.auto()){
						fin.setAutoGenerate(true);
					}else{
						fin.setAutoGenerate(false);
					}
					
					//�ж��ֶ����ͽ��к���ע��Ĳ���
					if(fieldType.equalsIgnoreCase("int")){
						
						if(!or.type().equals("") && !or.type().equals("integer")){
							throw new Exception("[Register model err]-class<"+clazz.getSimpleName()+">-field<"+f.getName()+">-The int type can just convert to the type integer of database!");
						}else{
							fin.setType("integer");	
						}
						
						if(or.digits()!=0 || or.decimals()!=0){
							throw new Exception("[Register model err]-class<"+clazz.getSimpleName()+">-field<"+f.getName()+">-The int type can not have digits/decimals!");
						}
					}else if(fieldType.equalsIgnoreCase("java.lang.String")){
						//���type������text
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
					
					}else if(fieldType.equalsIgnoreCase("long")){
						
						if(!or.type().equals("") && !or.type().equals("bigint")){
							throw new Exception("[Register model err]-class<"+clazz.getSimpleName()+">-field<"+f.getName()+">-The long type can just convert to the type bigint of database!");
						}else{
							fin.setType("bigint");
						}
						
						if(or.digits()!=0 || or.decimals()!=0){
							throw new Exception("[Register model err]-class<"+clazz.getSimpleName()+">-field<"+f.getName()+">-The long type can not have digits/decimals!");
						}
					}else if(fieldType.equalsIgnoreCase("short")){
						
						if(!or.type().equals("") && !or.type().equals("tinyint")){
							throw new Exception("[Register model err]-class<"+clazz.getSimpleName()+">-field<"+f.getName()+">-The short type can just convert to the type tinyint of database!");
						}else {
							fin.setType("tinyint");
						}
						
						if(or.digits()!=0 || or.decimals()!=0){
							throw new Exception("[Register model err]-class<"+clazz.getSimpleName()+">-field<"+f.getName()+">-The short type can not have digits/decimals!");
						}
					}else if(fieldType.equalsIgnoreCase("float")||fieldType.equalsIgnoreCase("double")){
						
						if(!or.type().equals("") && !or.type().equals("double") && !or.type().equals("numeric")){
							throw new Exception("[Register model err]-class<"+clazz.getSimpleName()+">-field<"+f.getName()+">-The float/double type can just convert to the type double/numeric of database!");
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
					}else if(fieldType.equalsIgnoreCase("boolean")){
						
						if(!or.type().equals("") && !or.type().equals("bool")){
							throw new Exception("[Register model err]-class<"+clazz.getSimpleName()+">-field<"+f.getName()+">-The boolean type can just convert to the type bool of database!");
						}else {
							fin.setType("bool");
						}
						if(fin.isAutoGenerate()==true){
							throw new Exception("[Register model err]-class<"+clazz.getSimpleName()+">-field<"+f.getName()+">-The boolean type can not be autogenerate key!");
						}
						
						if(or.digits()!=0 || or.decimals()!=0){
							throw new Exception("[Register model err]-class<"+clazz.getSimpleName()+">-field<"+f.getName()+">-The boolean type can not have digits/decimals!");
						}
					}	
					
				}
			}
			if(fin != null){
				tbinfo.addFieldInfo(fin);	
			}	
		}
		//�ж��Ƿ����ظ�����������������
		if(tbinfo != null){
			Vector<FieldInfo> fins = tbinfo.getAllFieldInfos();
			if(fins.size() == 0){
				throw new Exception("[Register model error]-Registered entity class <"+tbinfo.getTypeName()+"> does not exist field!");
			}else{
				int pkNumber = 0;//��������
				int autoNumber = 0;//����������
				for(FieldInfo fin : fins){
					if(fin.isPrimaryKey()){
						pkNumber++;
					}
					if(fin.isAutoGenerate()){
						autoNumber++;
					}
				}
				
				//�˴�û�п��ǵ��Ƿ�������ͺ��������ϵĵ���ע���"id"
				if(pkNumber == 0 && autoNumber == 0 && specialNum == 1){
					for(FieldInfo fin : fins){
						if(fin.getTypeName().equals("int")||fin.getTypeName().equals("long")||fin.getTypeName().equals("short")){
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
		// TODO �Զ����ɵķ������
		String strSql = "";
		if(tbinfo != null){
			/*//ɾ�����ٽ���
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

				//����fieldinfo��other����
				if(f.getOther() != null){
					if(!f.getOther().equals("")){
						String[] tags = f.getOther().split(";");
						for(String tag : tags){
							if(oManager.getSupportTag().get(tag) == 1){
								strSql += tag + " ";
							}else if(oManager.getSupportTag().get(tag) == 2){//Ϊindex
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
