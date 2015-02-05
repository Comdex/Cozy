upackage test;

import com.reflectsky.cozy.annoation.Orm;
import com.reflectsky.cozy.annoation.TableName;

public class Student {
	@Orm(pk=true)
	public int sno;
	public String snameString;
	public boolean ok;
	float ha;
	@Orm(digits=3,decimals=3)
	double tea;
}
