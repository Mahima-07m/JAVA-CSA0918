import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

class Student {
    private final String regNo;
    private final String name;
    Student(String regNo, String name) { this.regNo = regNo; this.name = name; }
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        return regNo.equals(((Student)o).regNo);
    }
    @Override public int hashCode() { return Objects.hash(regNo); }
    @Override public String toString() { return regNo + " " + name; }
}
class BrokenStudent {
    final String regNo;
    BrokenStudent(String regNo) { this.regNo = regNo; }
    @Override public boolean equals(Object o) {
        return o instanceof BrokenStudent b && regNo.equals(b.regNo);
    }
}
public class Attendance {
    public static void main(String[] args) {
        Set<Student> present = new HashSet<>();
        present.add(new Student("192311001", "Arun"));
        present.add(new Student("192311002", "Divya"));
        present.add(new Student("192311001", "Arun"));
        System.out.println("Unique students: " + present.size());

        Set<BrokenStudent> broken = new HashSet<>();
        broken.add(new BrokenStudent("192311001"));
        broken.add(new BrokenStudent("192311001"));
        System.out.println("Broken class duplicate count (may vary by hash placement): " + broken.size());

        Set<Student> immutableKeyTest = new HashSet<>();
        Student stable = new Student("192311003", "Kavi");
        immutableKeyTest.add(stable);
        System.out.println("Same registration found: " + immutableKeyTest.contains(new Student("192311003", "Different name")));
    }
}