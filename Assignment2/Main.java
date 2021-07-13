import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        //ReserveInfo Inner Class의 객체 생성후 Show() 메소드 호출
        ReserveInfo ri = new ReserveInfo();
        ri.Show();
    }

    public static class ReserveInfo {
        private void Show () {
             /*
                main 에서 호출되는 Show() 메소드.
                선택지 출력 후, case에 맞는 숫자 입력할 시 그에 따른 결과 출력하는 메소드
             */

            Scanner sc = new Scanner(System.in);
            int choose;

            // .csv 파일들 BufferedReader 객체 생성
            BufferedReader stu = null, prof = null, resv = null;
            try{
                stu = new BufferedReader(new FileReader("./student.csv"));
                prof = new BufferedReader(new FileReader("./professor.csv"));
                resv = new BufferedReader(new FileReader("./reservation.csv"));
            } catch (FileNotFoundException e1) {
                System.out.println("No such File!!");
                e1.printStackTrace();
                System.exit(0);
            }

            // 교수 정보를 professor.csv에서 읽어와서 ArrayList에 담는 과정.
            ArrayList<Professor> professors = new ArrayList<>();
            try{
                prof.readLine(); // header row 버리기
                String line;

                while((line=prof.readLine()) != null) {
                    StringTokenizer st = new StringTokenizer(line, ",");
                    ArrayList<String> profInfo = new ArrayList<>();
                    while(st.hasMoreTokens()) profInfo.add(st.nextToken());
                    if(profInfo.size() == 4) // profInfo ArrayList에 정보가 4개가 차면, Professor 객체로 생성 후 professors ArrayList에 추가
                        professors.add(new Professor(profInfo.get(0), profInfo.get(1), profInfo.get(2), Integer.parseInt(profInfo.get(3))));
                    else throw new Exception("파일 내용 읽기 실패");
                }
            } catch(IOException e) {
                e.printStackTrace();
                System.exit(0);
            } catch(Exception e) {
                e.printStackTrace();
                System.exit(0);
            }

            //student.csv 에서 정보를 읽어와서 ArrayList에 담는 과정
            ArrayList<Student> students = new ArrayList<>();
            try{
                stu.readLine(); //header row 버리기
                String line;
                while((line=stu.readLine()) != null) {
                    ArrayList<String> stuInfo = new ArrayList<>();
                    StringTokenizer st = new StringTokenizer(line, ",");
                    while(st.hasMoreTokens()) stuInfo.add(st.nextToken());
                    if(stuInfo.size() == 4) // professor 에서와 동일
                        students.add(new Student(stuInfo.get(0), stuInfo.get(1), stuInfo.get(2), stuInfo.get(3)));
                    else throw new Exception("파일 내용 읽기 실패");
                }
            } catch(IOException e) {
                e.printStackTrace();
                System.exit(0);
            } catch(Exception e) {
                e.printStackTrace();
                System.exit(0);
            }

            //reservation.csv 에서 column 기준으로 ArrayList에 담는 과정
            ArrayList<Reservation> reservations = new ArrayList<>();
            try{
                resv.readLine(); // header row 버리기
                String line;

                while((line=resv.readLine()) != null) {
                    StringTokenizer st = new StringTokenizer(line, ",");
                    ArrayList<Integer> reserveInfo = new ArrayList<>();
                    while(st.hasMoreTokens()) {
                        //예약 가능하면 1, 예약 불가하면 0으로 판단.
                        String info = st.nextToken();
                        if(info.equals("impossible")) reserveInfo.add(0);
                        else if(info.equals("possible")) reserveInfo.add(1);
                        else if(info.matches("-?\\d+"))reserveInfo.add(Integer.parseInt(info)); //roomNumber 부분
                        else throw new Exception("파일 내용 읽기 실패");
                    }

                    //한 reservation.csv의 한 row를 담은 ArrayList Collection을 배열로 바꾼뒤 생성자로 전달.
                    //이후 객체 생성하여 reservations ArrayList에 담기.
                    if(reserveInfo.size() == 15) reservations.add(new Reservation(reserveInfo.stream().mapToInt(i->i).toArray()));
                }
            } catch(IOException e) {
                e.printStackTrace();
                System.exit(0);
            } catch(Exception e) {
                e.printStackTrace();
                System.exit(0);
            }

            //stu와 prof와 resv는 정상적으로 닫아준다.
            try {
                stu.close();
                prof.close();
                resv.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            /*
                그 전 프로그램이 종료되었을 경우에 reservationRecord.csv
                에서 기록을 읽어와 다시 Reservation Class의 객체들에 적용하는 과정.
             */
            Reservation.FileRecovery(reservations);
            Reservation.sort(reservations); //Reservation 객체들을 roomNum에 따라 오름차순으로 정렬하는 메소드
            Reservation.fileModify(reservations); //reservationRecord.csv 의 내용을 -> reservation.csv 에 반영하는 메소드


            while (true) {
                // 선택지를 보여줍니당!!
                System.out.println("====강의실 대여 및 인적사항 조회====");
                System.out.println("1. 전체 예약 현황 조회");
                System.out.println("2. 호실 예약 현황 조회");
                System.out.println("3. 예약하기 및 예약 취소하기");
                System.out.println("4. 나의 예약 조회");
                System.out.println("5. 학생 인적사항 변경");
                System.out.println("6. 교수 인적사항 조회");
                System.out.println("7. 종료");

                System.out.print(">>> ");
                choose = sc.nextInt();
                sc.nextLine(); // \n 제거

                switch (choose) {
                    case 1: //1번 case : 전체 예약 현황 조회(예약 가능 날짜). 오름차순으로 정렬한 후 출력
                        System.out.println("[1. 전체 예약 현황 조회]");
                        Reservation.sort(reservations);
                        for (Reservation r : reservations) System.out.print(r);
                        break;

                    case 2: //2번 case : 특정 호실의 예약 가능한 날짜 출력.
                        System.out.println("[2. 호실 예약 현황 조회]");
                        System.out.print(">>> ");
                        int roomNum = sc.nextInt();
                        sc.nextLine();
                        boolean check = false;
                        for (Reservation r : reservations) {
                            if(r.searchRoom(roomNum)) { //검색 성공 시 true, 실패 시 false.
                                check = true; //검색 성공하면 check을 true로 바꾼 후 검색 종료
                                break;
                            }
                        }
                        if(!check) System.out.println("존재하지 않는 호실입니다.");
                        break;

                    case 3:
                        /*
                            3번 case : 예약 및 취소
                            -> 내용은 reservationRecord.csv에 기록 되어야 함.
                            -> reservation.csv에도 기록되어야 함.
                            -> ArrayList의 객체들에도 반영 되어야함
                         */
                        System.out.println("[3. 예약하기 및 취소하기]");
                        System.out.println("1. 예약하기");
                        System.out.println("2. 예약 취소하기");
                        System.out.print(">>> ");
                        int n = sc.nextInt();
                        sc.nextLine();
                        Reservation.reserveOrCancel(n, students, professors, reservations); // n이 1이면 예약 진행, 2면 취소 진행하는 메소드
                        break;

                    case 4:
                        System.out.println("[4. 나의 예약조회]");
                        System.out.println("이름과 아이디(학번 또는 교직원 번호)입력");
                        System.out.print(">>> ");
                        StringTokenizer st = new StringTokenizer(sc.nextLine());
                        if(st.countTokens() != 2) { //단어는 이름과 아이디 2단어이다.
                            System.out.println("없는 예약입니다. 이름과 아이디를 다시 확인해주세요.");
                            break;
                        }
                        Reservation.searchRecord(st.nextToken(), st.nextToken());
                        break;

                    case 5:
                        System.out.println("[5. 학생 인적사항 변경]");
                        System.out.println("1. 인적사항 조회");
                        System.out.println("2. 이름 변경");
                        System.out.println("3. 학생 삭제");
                        System.out.print(">>> ");
                        int sel = sc.nextInt();
                        sc.nextLine();
                        if(sel==1){
                            System.out.println("1. 인적사항 조회");
                            Student.sort(students); //이름을 알파벳 순으로 정렬
                            for(Student student : students) System.out.println(student);
                        } else if(sel==2) {
                            System.out.println("2. 이름 변경");
                            Student.changeStudent(students);
                        } else if(sel==3) {
                            System.out.println("3. 학생 삭제");
                            Student.removeStudent(students);
                        } else System.out.println("선택지의 숫자만 입력하세요.");
                        break;

                    case 6:
                        System.out.println("[6. 교수 인적사항 조회]");
                        Professor.sort(professors); //담당 과목 순으로 정렬
                        for(Professor professor : professors) System.out.println(professor);
                        break;

                    case 7:
                        System.out.println("종료합니다.");
                        System.exit(0);

                    default:
                        System.out.println("선택지의 숫자만 입력하세요.");
                }
            }
        }
    }
}
