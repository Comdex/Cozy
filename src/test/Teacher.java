package test;

import java.util.Date;

import com.reflectsky.cozy.Ormer;

public class Teacher {
	private int Id;
	private String tname;
	private Date now;
	//long bb;
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getTname() {
		return tname;
	}
	public void setTname(String tname) {
		this.tname = tname;
	}
	public Date getNow() {
		return now;
	}
	public void setNow(Date now) {
		this.now = now;
	}
	private void beforeInsert2(Ormer orm){
		System.out.println(orm);
		System.out.println("执行成功");
	}
}
