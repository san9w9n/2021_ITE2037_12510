import java.util.ArrayList;
import java.util.Collections;

public class Professor implements Comparable<Professor> {
    private String name, department, professorId;
    private int classNum;

    public Professor() {
        this.name = null;
        this.department = null;
        this.professorId = null;
        this.classNum = -1;
    }
    public Professor(String name, String department, String professorId, int classNum) {
        this.name = name;
        this.department = department;
        this.professorId = professorId;
        this.classNum = classNum;
    }
    public int getClassNum() { return classNum; }

    public int compareTo(Professor other) {
        if(other == null) { System.exit(0); }
        else if(getClass() != other.getClass()) { System.exit(0); }

        return Integer.compare(this.classNum, other.getClassNum());
    }
    public static void sort(ArrayList<Professor> professors) { Collections.<Professor>sort(professors); }

    public Boolean compare(String name, String professorId) {
        return ((this.name).equals(name) && (this.professorId).equals(professorId));
    }

    public String toString() {
        return "name: " + name + ", department: " + department +
                ", professorId: " + professorId + ", classNum: " + classNum;
    }


}
