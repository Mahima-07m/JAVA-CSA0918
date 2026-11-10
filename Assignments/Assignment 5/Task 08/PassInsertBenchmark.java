import java.sql.*;
import java.util.*;
public class PassInsertBenchmark {
    private static final String URL="jdbc:mysql://localhost:3306/java_assignment5";
    private static final String USER="root",PASSWORD="password";
    private static List<String[]> generate(int count){
        List<String[]> rows=new ArrayList<>(count);
        for(int i=1;i<=count;i++)rows.add(new String[]{"P"+i,"REG"+i,"Student"+i,String.valueOf(i%20+1),String.valueOf(1000+i)});
        return rows;
    }
    private static void clear(Connection c)throws SQLException{
        try(Statement s=c.createStatement()){s.executeUpdate("DELETE FROM bus_pass");}
    }
    static long statement(Connection c,List<String[]> rows)throws SQLException{
        clear(c);long start=System.nanoTime();
        try(Statement s=c.createStatement()){
            for(String[] p:rows){
                String sql="INSERT INTO bus_pass(pass_id,reg_no,name,route_no,amount) VALUES('"+
                    p[0]+"','"+p[1]+"','"+p[2]+"',"+p[3]+","+p[4]+")";
                s.executeUpdate(sql);
            }
        }return System.nanoTime()-start;
    }
    static long prepared(Connection c,List<String[]> rows)throws SQLException{
        clear(c);long start=System.nanoTime();
        try(PreparedStatement p=c.prepareStatement("INSERT INTO bus_pass(pass_id,reg_no,name,route_no,amount) VALUES(?,?,?,?,?)")){
            for(String[] row:rows){p.setString(1,row[0]);p.setString(2,row[1]);p.setString(3,row[2]);p.setInt(4,Integer.parseInt(row[3]));p.setDouble(5,Double.parseDouble(row[4]));p.executeUpdate();}
        }return System.nanoTime()-start;
    }
    static long batch(Connection c,List<String[]> rows)throws SQLException{
        clear(c);long start=System.nanoTime();boolean old=c.getAutoCommit();c.setAutoCommit(false);
        try(PreparedStatement p=c.prepareStatement("INSERT INTO bus_pass(pass_id,reg_no,name,route_no,amount) VALUES(?,?,?,?,?)")){
            int n=0;for(String[] row:rows){p.setString(1,row[0]);p.setString(2,row[1]);p.setString(3,row[2]);p.setInt(4,Integer.parseInt(row[3]));p.setDouble(5,Double.parseDouble(row[4]));p.addBatch();if(++n%1000==0)p.executeBatch();}
            p.executeBatch();c.commit();
        }catch(SQLException e){c.rollback();throw e;}finally{c.setAutoCommit(old);}
        return System.nanoTime()-start;
    }
    public static void main(String[] args)throws Exception{
        try(Connection c=DriverManager.getConnection(URL,USER,PASSWORD)){
            for(int n:new int[]{10,1000,10000,100000}){
                List<String[]> rows=generate(n);
                System.out.println("Rows: "+n);
                System.out.println("Statement ms: "+statement(c,rows)/1_000_000.0);
                System.out.println("PreparedStatement ms: "+prepared(c,rows)/1_000_000.0);
                System.out.println("Batch transaction ms: "+batch(c,rows)/1_000_000.0);
            }
            DatabaseMetaData m=c.getMetaData();
            System.out.println(m.getDatabaseProductName()+" | "+m.getDriverName()+" "+m.getDriverVersion());
            try(ResultSet r=m.getColumns(null,null,"bus_pass",null)){
                while(r.next())System.out.println(r.getString("COLUMN_NAME")+" : "+r.getString("TYPE_NAME"));
            }
        }
    }
}