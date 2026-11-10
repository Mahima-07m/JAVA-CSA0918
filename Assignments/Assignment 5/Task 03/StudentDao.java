import java.sql.*;import java.util.*;
interface Dao<T,K>{void insert(T x)throws SQLException;Optional<T> findById(K id)throws SQLException;List<T> findAll()throws SQLException;void update(T x)throws SQLException;void delete(K id)throws SQLException;}
class Student{final int id;final String name,email;Student(int i,String n,String e){id=i;name=n;email=e;}}
public class StudentDao implements Dao<Student,Integer>{
 private final Connection c;public StudentDao(Connection c){this.c=c;}
 public void insert(Student s)throws SQLException{try(PreparedStatement p=c.prepareStatement("INSERT INTO student(id,name,email) VALUES(?,?,?)")){p.setInt(1,s.id);p.setString(2,s.name);p.setString(3,s.email);p.executeUpdate();}}
 public Optional<Student> findById(Integer id)throws SQLException{try(PreparedStatement p=c.prepareStatement("SELECT id,name,email FROM student WHERE id=?")){p.setInt(1,id);try(ResultSet r=p.executeQuery()){return r.next()?Optional.of(new Student(r.getInt(1),r.getString(2),r.getString(3))):Optional.empty();}}}
 public List<Student> findAll()throws SQLException{List<Student> a=new ArrayList<>();try(Statement s=c.createStatement();ResultSet r=s.executeQuery("SELECT id,name,email FROM student")){while(r.next())a.add(new Student(r.getInt(1),r.getString(2),r.getString(3)));}return a;}
 public void update(Student s)throws SQLException{try(PreparedStatement p=c.prepareStatement("UPDATE student SET name=?,email=? WHERE id=?")){p.setString(1,s.name);p.setString(2,s.email);p.setInt(3,s.id);p.executeUpdate();}}
 public void delete(Integer id)throws SQLException{try(PreparedStatement p=c.prepareStatement("DELETE FROM student WHERE id=?")){p.setInt(1,id);p.executeUpdate();}}
}