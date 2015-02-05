package test;

import java.util.Date;

public class Teacher {
	private int Id;
	private String tname;
	private Date now;
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
}
