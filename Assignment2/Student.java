import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Student implements Comparable<Student> {
    private String name,department,studentId,phoneNum;

    public Student() {
        this.name = null;
        this.department = null;
        this.studentId = null;
        this.phoneNum = null;
    }
    public Student(String name, String department, String studentId, String phoneNum) {
        this.name = name;
        this.department = department;
        this.studentId = studentId;
        this.phoneNum = phoneNum;
    }

    public String getName() { return name; }
    public void setName(String name) {
        this.name = name;
    }

    public Boolean compare(String name, String studentId) {
        return ((this.name).equals(name) && (this.studentId).equals(studentId));
    }

    public int compareTo(Student other) {
        if(other == null) { System.exit(0); }
        else if(getClass() != other.getClass()) { System.exit(0); }
        return (this.name).compareToIgnoreCase(other.getName()); //정렬 시 알파벳 순이므로 대소문자 무시.
    }
    public static void sort(ArrayList<Student> students) {
        Collections.<Student>sort(students);
    }

    public static void changeStudent(ArrayList<Student> students) {
        Scanner sc = new Scanner(System.in);
        System.out.print(">>> ");
        String name = sc.nextLine();
        String change = null;
        boolean check = false;
        for(Student student : students) {
            if(student.getName().equals(name)) {
                System.out.println("어떤 이름으로 변경하시겠습니까?");
                System.out.print(">>> ");
                change = sc.nextLine();
                student.setName(change); // 객체 이름 변경.
                check = true;
                break;
            }
        }
        if(!check) System.out.println("없는 이름입니다. 다시 확인해주세요.");
        else { //check이 true면 student.csv에 변경내용 저장.
            studentFileModify(students);
            System.out.println("이름 변경이 완료되었습니다.");
        }
    }

    public static void removeStudent(ArrayList<Student> students) {
        Scanner sc = new Scanner(System.in);
        System.out.print(">>> ");
        String name = sc.nextLine();
        int index=0;
        boolean check = false;
        for(Student student : students) {
            if(student.getName().equals(name)) {
                check = true;
                break;
            }
            index++;
        }
        if(!check) System.out.println("없는 이름입니다. 다시 확인해주세요.");
        else {
            students.remove(index); //ArrayList에서 해당 학생 제거
            studentFileModify(students); // 변경내용 student.csv에 반영
            System.out.println("삭제되었습니다.");
        }
    }

    private static void studentFileModify(ArrayList<Student> students) {
        //ArrayList에 담긴 객체들의 정보를 토대로 student.csv 파일 재작성.
        File file = new File("./student.csv");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write("name,department,studentId,phoneNum");
            bw.write(System.lineSeparator());

            for (Student student : students) { //객체에 저장된 내용을 토대로 파일 다시작성
                bw.write(student.name + "," + student.department + "," +
                        student.studentId + "," + student.phoneNum);
                bw.write(System.lineSeparator());
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String toString() {
        return "name: " + name + ", department: " + department +
                ", studentId: " + studentId + ", phoneNum: " + phoneNum;
    }
}
