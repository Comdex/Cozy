
package com.reflectsky.cozy.apiimpl;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.reflectsky.cozy.RawPreparer;

/**
 * 原生SQL预查询接口实现
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
		// TODO 自动生成的方法存根
		if(pstmt == null){
			return -1;
		}
		for(int i=0;i<objs.length;i++){
			try {
				pstmt.setObject(i+1, objs[i]);
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		try {
			return pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return -1;
	}

	/* @author Comdex
	 * @see com.reflectsky.cozy.RawPreparer#close()
	 */
	@Override
	public boolean close() {
		// TODO 自动生成的方法存根
		if(pstmt == null){
			return false;
		}
		try {
			pstmt.close();
			return true;
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return false;
	}

}
