
package com.reflectsky.cozy.apiimpl;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.reflectsky.cozy.RawPreparer;

/**
 * ԭ��SQLԤ��ѯ�ӿ�ʵ��
 * @author Comdex
 */
public class RawPreparerImpl implements RawPreparer{
	private PreparedStatement pstmt= null;
	
	public RawPreparerImpl(PreparedStatement pstmt){
		this.pstmt = pstmt;
	}
	
	/* @author Comdex
	 * @see com.reflectsky.cozy.RawPreparer#exec(java.lang.Object[])
	 */
	@Override
	public int exec(Object... objs) {
		// TODO �Զ����ɵķ������
		if(pstmt == null){
			return -1;
		}
		for(int i=0;i<objs.length;i++){
			try {
				pstmt.setObject(i+1, objs[i]);
			} catch (SQLException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		}
		try {
			return pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
		return -1;
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
			e.printStackTrace();
		}
		return false;
	}

}
