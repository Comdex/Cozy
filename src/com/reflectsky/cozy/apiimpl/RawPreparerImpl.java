
package com.reflectsky.cozy.apiimpl;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.reflectsky.cozy.RawPreparer;
import com.reflectsky.cozy.core.OrmManager;

/**
 * ԭ��SQLԤ��ѯ�ӿ�ʵ��
 * @author Comdex
 */
public class RawPreparerImpl implements RawPreparer{
	private PreparedStatement pstmt= null;
	private OrmManager oManager = null;
	
	public RawPreparerImpl(PreparedStatement pstmt,OrmManager ormManager){
		this.pstmt = pstmt;
		this.oManager = ormManager;
	}
	
	/* @author Comdex
	 * @see com.reflectsky.cozy.RawPreparer#exec(java.lang.Object[])
	 */
	@Override
	public int exec(Object... objs) {
		// TODO �Զ����ɵķ������
		int count = 0;
		if(pstmt == null){
			return count;
		}
		for(int i=0;i<objs.length;i++){
			try {
				pstmt.setObject(i+1, objs[i]);
			} catch (SQLException e) {
				// TODO �Զ����ɵ� catch ��
				this.oManager.deBugInfo(e.getMessage());
				
			}
		}
		try {
			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			// TODO �Զ����ɵ� catch ��
			this.oManager.deBugInfo(e.getMessage());
		
		}
		
		return count;
	}

	/* @author Comdex
	 * @see com.reflectsky.cozy.RawPreparer#close()
	 */
	@Override
	public boolean close() {
		// TODO �Զ����ɵķ������
		if(pstmt == null){
			return false;
		}
		try {
			pstmt.close();
			return true;
		} catch (SQLException e) {
			// TODO �Զ����ɵ� catch ��
			this.oManager.deBugInfo(e.getMessage());
			return false;
		}
	}

}
