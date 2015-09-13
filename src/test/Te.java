package test;

import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.reflectsky.cozy.Ormer;
import com.reflectsky.cozy.core.OrmManager;

public class Te {
	public static void main(String[] args) {
		
		Mongo mg = new Mongo("localhost", 27017);
		DB db = mg.getDB("dajia");
		DBCollection collection = db.getCollection("bibi");
		DBObject b = new BasicDBObject();
		b.put("nihao", 123);
		b.put("ta", "baba");
		System.out.println(collection.save(b));
		BasicDBObject dbo = new BasicDBObject();
		
		
		
		
		
		/*OrmManager oman = new OrmManager("com.mysql.jdbc.Driver", "jdbc:mysql://127.0.0.1:3306/ok", "root", "", "mysql");
		oman.RegisterModel(Teacher.class);
		oman.Force(false);
		oman.Debug(true);
		oman.runSyncDB();
		Ormer orm = oman.NewOrm();
		//orm.openCallback(true);
		orm.addCallback(new Object(){
			private void beforeInsert(Student t,Ormer orm){
				System.out.println("before t");
				System.out.println(t.getId()+" id before");
				System.out.println(orm);
			}
			private void afterInsert(Teacher t,Ormer orm){
				System.out.println("after t");
				System.out.println(t.getId()+" id after");
				System.out.println(orm);
			}
		});
	
		class BB{
			public String tname;
			public Date now;
		}
		
		Teacher t = new Teacher();
		t.setTname("»Àf445");
		t.setNow(new Date());
		System.out.println(orm.insert(t));
		System.out.println("kankan:"+t.getId());*/
		
		//Teacher teacher = new Teacher();
		//List<Teacher> list = new ArrayList<Teacher>();
		
		//System.out.println(orm.queryTable("teacher").filter("tname","jbeego34").filter("id__gt", 5).exist());
		
		//System.out.println(list.get(0).getNow());
		//System.out.println(list.get(1).getNow());
		//System.out.println(list.get(2).getNow());
		//HashMap<String, Object> mp = new HashMap<>();
		/*BB b = new BB();
		orm.raw("select tname,now from teacher;").rowsToObject(b, "tname","now");
		System.out.println(b.tname);
		System.out.println(b.now);*/
		//Teacher teacher = new Teacher();
		//teacher.setId(49);
		//teacher.setTname("fe");
		//teacher.setNow(new Date());
		//Teacher t = new Teacher();
		//t.setTname("hahe89");
		//t.setNow(new Date());
		//ArrayList<Teacher> list = new ArrayList<>();
		//System.out.println(orm.raw("select * from teacher").queryRows(list, Teacher.class));
		/*System.out.println(orm.read(teacher));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("gag:"+sdf.format(teacher.getNow()));*/
		//System.out.println(orm.insert(t));
		//System.out.println(t.getId());
		/*for(Teacher te : list){
			System.out.println(te.getTname());
		}*/
		/*Student student = new Student();
		student.snameString = "peteru23";
		student.ok = true;
		student.sno=3;
		System.out.println(orm.insert(student));*/
	}
}
