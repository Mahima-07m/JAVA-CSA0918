import java.io.*;import java.nio.file.*;import java.util.*;
public class BusPassStore{
 private final Path file; public BusPassStore(Path file){this.file=file;}
 public void add(String id,String reg,String name,int route,String stop,String validity,double amount)throws IOException{
  if(route<0||amount<0)throw new IllegalArgumentException();
  try(BufferedWriter w=Files.newBufferedWriter(file,StandardOpenOption.CREATE,StandardOpenOption.APPEND)){
   w.write(String.join(",",id,reg,name.replace("\\","\\\\").replace(",","\\,"),""+route,stop,validity,""+amount));w.newLine();}
 }
 public String[] find(String id)throws IOException{
  if(!Files.exists(file))return null;try(BufferedReader r=Files.newBufferedReader(file)){String line;
   while((line=r.readLine())!=null){String[] p=line.split(",(?=(?:[^\\\\]|\\\\.)*$)",-1);if(p.length!=7)continue;
    try{Integer.parseInt(p[3]);Double.parseDouble(p[6]);}catch(NumberFormatException e){continue;}if(p[0].equals(id))return p;}}
  return null;
 }
 public List<String[]> listByRoute(int route)throws IOException{List<String[]> out=new ArrayList<>();if(!Files.exists(file))return out;
  try(BufferedReader r=Files.newBufferedReader(file)){String line;while((line=r.readLine())!=null){String[] p=line.split(",",-1);if(p.length!=7)continue;try{if(Integer.parseInt(p[3])==route)out.add(p);}catch(NumberFormatException ignored){}}}return out;}
 public static void main(String[] a)throws Exception{Path f=Path.of("passes.csv");BusPassStore s=new BusPassStore(f);s.add("P1001","192311001","Arun",12,"Main Gate","2027-05-31",4500);System.out.println(Arrays.toString(s.find("P1001")));System.out.println(s.listByRoute(12).size());}
}