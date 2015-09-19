 package com.reflectsky.cozy.core;

/**
 *����ӳ�����ݿ��ֶ���Ϣ������ 
 * @author Comdex
 */
public class FieldInfo {
	//��Ӧ��������
	private String columnName;
	//��Ӧ���г�Ա��
	private String fieldName;
	private boolean isPrimaryKey;
	private boolean isAutoGenerate;
	//��Ӧ���ݿ�����
	private String type;
	private int size;
	private int digits;
	private int decimals;
	private String other;
	//��Ӧjava����������
	private String typeName;
	private Object value;
	
	public FieldInfo(){
		super();
	}
	
	public FieldInfo(String columnName,String fieldName, boolean isPrimaryKey,
			boolean isAutoGenerate, String type, int size,
			int digits, int decimals, String other, String typeName,
			Object value) {
		super();
		this.columnName = columnName;
		this.fieldName = fieldName;
		this.isPrimaryKey = isPrimaryKey;
		this.isAutoGenerate = isAutoGenerate;
		
		this.type = type;
		this.size = size;
		this.digits = digits;
		this.decimals = decimals;
		this.other = other;
		this.typeName = typeName;
		this.value = value;
	}
	
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}
	public void setPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}
	public boolean isAutoGenerate() {
		return isAutoGenerate;
	}
	public void setAutoGenerate(boolean isAutoGenerate) {
		this.isAutoGenerate = isAutoGenerate;
	}

	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getDigits() {
		return digits;
	}
	public void setDigits(int digits) {
		this.digits = digits;
	}
	public int getDecimals() {
		return decimals;
	}
	public void setDecimals(int decimals) {
		this.decimals = decimals;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
}
