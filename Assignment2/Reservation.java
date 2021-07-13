import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Reservation implements Comparable<Reservation>{
    //AM과 PM은 예약 가능상태면 1이고 예약 불가 상태면 0으로
    private int roomNum;
    private int[] reservable = new int[14];

    Reservation() { this.roomNum = -1; }
    Reservation(int... n) {
        if(n.length == 15) {
            this.roomNum = n[0];
            for (int i=1; i<15; i++) reservable[i-1] = n[i];
        } else {
            System.out.println("Wrong Information!!!");
            System.exit(0);
        }
    }
    public int getRoomNum() { return roomNum; }

    public int compareTo(Reservation other) {
        if(other == null) { System.exit(0); }
        else if(getClass() != other.getClass()) { System.exit(0); }
        return Integer.compare(this.roomNum, other.getRoomNum()); //roomNum 에 따라 비교
    }
    public static void FileRecovery(ArrayList<Reservation> reservations) {
        //전 번 프로그램 실행이 종료되었을떄 reservationRecord.csv에 저장된 것이 객체에 반영되게.
        File file = new File("./reservationRecord.csv");
        BufferedReader record = null;
        try{
            file.createNewFile();
            record = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e1) {
            System.out.println("No such File!!");
            System.exit(0);
        } catch (Exception e2) {
            System.exit(0);
        }
        String line1;
        try {
            while ((line1 = record.readLine()) != null) {
                String[] reserveInfo = new String[4];
                StringTokenizer st = new StringTokenizer(line1, ",");
                // reservationRecord.csv에서 읽어온 정보는 총 다섯 단어 이어야한다.
                if(st.countTokens() != 5) throw new Exception("파일 읽기 실패!");
                for(int i=0; i<4; i++) // 객체에 적용하는 정보에는 예약 사유가 필요하지 않다. 그래서 4번만 돌려도된다
                    reserveInfo[i] = st.nextToken();
                for(Reservation reservation : reservations) {
                    if( (reservation.getRoomNum()==Integer.parseInt(reserveInfo[2].substring(0,reserveInfo[2].length() -1))) &&
                            (reservation.canReserve(reserveInfo[3]))) {
                        //ArrayList 안의 Reservation 객체 중 해당 룸넘버의 객체가 예약이 가능한지 확인하고 예약 진행
                        reservation.takeReserve(reserveInfo[3]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
    public static void sort(ArrayList<Reservation> reservations) {
        Collections.<Reservation>sort(reservations);
    } // 정렬

    public static void fileModify(ArrayList<Reservation> reservations) {
        //reservations ArrayList에 있는 객체들의 정보를 토대로 reservation.csv 파일 수정하기
        File file = new File("./reservation.csv");
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            //header row 작성
            bw.write("roomNum,6/1 a.m.,6/1 p.m.,6/2 a.m.,6/2 p.m.,6/3 a.m.,6/3 p.m.,6/4 a.m.,6/4 p.m.,6/5 a.m.,6/5 p.m.,6/6 a.m.,6/6 p.m.,6/7 a.m.,6/7 p.m.");
            bw.write(System.lineSeparator());

            for(Reservation reservation : reservations) {
                StringBuffer sb = new StringBuffer(reservation.roomNum + ",");
                for(int i=0; i<13; i++) {
                    sb.append( (reservation.reservable[i]==1) ? "possible," : "impossible," ); // 1이면 예약 가능한거고 0이면 불가능한 것
                } sb.append( (reservation.reservable[13]==1) ? "possible" : "impossible");
                bw.write(String.valueOf(sb));
                bw.write(System.lineSeparator());
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean searchRoom(int roomNum) { //검색 성공 시 출력후 true return
        if(this.roomNum == roomNum) {
            System.out.print("가능한 시간: ");
            StringBuffer sb = new StringBuffer();
            for(int i=0; i<14; i++) {
                if (reservable[i] == 1)
                    sb.append("6/" + (i / 2 + 1) + ((i % 2 == 0) ? "오전, " : "오후, "));
            }
            System.out.println(sb.substring(0,sb.length()-2));

            File file = new File("./reservationRecord.csv");
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                String[] cantReserve = new String[15]; //index는 날짜이고 , 그 index의 요소에 예약 사유를 담는 배열
                while((line = br.readLine()) != null) {
                    if(line.contains(roomNum + "호")) {
                        String[] splitLine = new String[5];
                        splitLine = line.split(",");
                        String s = splitLine[3].substring(3, 5); // 오전 or 오후
                        int amORpm = ((s.equals("오전") ? 0 : s.equals("오후") ? 1 : -1));
                        cantReserve[2*(Integer.parseInt(splitLine[3].substring(2,3)) -1) + amORpm] = splitLine[4]; //며칠에 어떤 사유로 넣었는지
                    }
                }
                for(int i=0; i<14; i++) { // 사유가 없이 이미 기존에 예약이 되어있던 곳 찾기
                    if (this.reservable[i] == 0 && cantReserve[i]==null) cantReserve[i] = "사유 모름";
                }
                System.out.println("불가능한 시간:");
                for(int i=0; i<14; i++) {
                    if (cantReserve[i]!=null) {
                        System.out.println("\t날짜: " + "6/" + (i/2+ 1)
                                + ((i%2 == 0) ? "오전" : "오후")
                                + ", 사유: " + cantReserve[i]);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
            return true;
        }
        return false; //검색 실패 시 false return
    }

    public static void reserveOrCancel(int n,ArrayList<Student> students, ArrayList<Professor> professors, ArrayList<Reservation> reservations) {
        Scanner sc = new Scanner(System.in);
        boolean check = false;

        // n이 1이면 예약 진행
        if(n==1) {
            System.out.println("이름, 아이디, 호실 번호, 시간, 사유 입력");
            System.out.print(">>> ");
            String line = sc.nextLine();
            String[] reserveInfo = new String[5];
            StringTokenizer st = new StringTokenizer(line);

            if(st.countTokens() != 5) { //정보는 총 5단어 여야 한다.
                System.out.println("예약 실패!");
                return;
            }
            for(int i=0; i<5; i++) reserveInfo[i] = st.nextToken();
            z:for(Reservation reservation : reservations) {
                if( (reservation.getRoomNum()==Integer.parseInt(reserveInfo[2].substring(0,reserveInfo[2].length() -1))) &&
                        (reservation.canReserve(reserveInfo[3]))) {
                    for (Student student : students) { //학생이 예약했다?
                        if (student.compare(reserveInfo[0], reserveInfo[1])) {
                            //객체에 적용
                            reservation.takeReserve(reserveInfo[3]);
                            //reservationRecord.csv에 적용
                            reserveRecord(reserveInfo[0], reserveInfo[1], reserveInfo[2], reserveInfo[3], reserveInfo[4]);
                            //reservation.csv에 적용
                            Reservation.fileModify(reservations);
                            check = true;
                            System.out.println(reserveInfo[2] + " " + reserveInfo[3] + "에 예약되었습니다." );
                            break z; //예약 성공했으면 그냥 전체 for문 빠져 나오기
                        }
                    }
                    for (Professor professor : professors) { //교수님이 예약하셨다?
                        if (professor.compare(reserveInfo[0], reserveInfo[1])) {
                            //객체에 적용
                            reservation.takeReserve(reserveInfo[3]);
                            //reservationRecord.csv에 적용
                            reserveRecord(reserveInfo[0], reserveInfo[1], reserveInfo[2], reserveInfo[3], reserveInfo[4]);
                            //reservation.csv에 적용
                            Reservation.fileModify(reservations);
                            check = true;
                            System.out.println(reserveInfo[2] + " " + reserveInfo[3] + "에 예약되었습니다." );
                            break z; //예약 성공했으면 그냥 전체 for문 빠져 나오기
                        }
                    }
                }
            }
            if(!check) System.out.println("예약 실패!");
        }
        //n이 2일 때 예약 취소
        else if(n==2) {
            System.out.println("이름, 아이디, 호실번호, 시간 입력");
            System.out.print(">>> ");
            String line = sc.nextLine();
            String[] reserveInfo = new String[4];
            StringTokenizer st = new StringTokenizer(line);
            if(st.countTokens() != 4) { //정보는 총 4단어 여야 한다.
                System.out.println("예약 취소 실패!");
                return;
            }
            for(int i=0; i<4; i++) reserveInfo[i] = st.nextToken();
            if(removeRecord(reserveInfo[0], reserveInfo[1], reserveInfo[2], reserveInfo[3])) { //reservationRecord.csv에 해당 내용이 있으면 취소가능
                check = true;
                z:for(Reservation reservation : reservations) {
                    if( (reservation.getRoomNum()==Integer.parseInt(reserveInfo[2].substring(0,reserveInfo[2].length() -1))) &&
                            (reservation.alreadyReserve(reserveInfo[3]))) {
                        for (Student student : students) { //학생이 취소한 경우
                            if (student.compare(reserveInfo[0], reserveInfo[1])) {
                                //객체에 적용
                                reservation.removeReserve(reserveInfo[3]);
                                //reservation.csv에 적용
                                Reservation.fileModify(reservations);
                                break z;
                            }
                        }
                        for (Professor professor : professors) { //교수님이 취소한 경우
                            if (professor.compare(reserveInfo[0], reserveInfo[1])) {
                                //객체에 적용
                                reservation.removeReserve(reserveInfo[3]);
                                //reservation.csv에 적용
                                Reservation.fileModify(reservations);
                                break z;
                            }
                        }
                    }
                }
            }
            if(check) System.out.println("에약이 취소되었습니다.");
            else System.out.println("에약 취소 실패!");
        } else System.out.println("1이나 2만 입력하세요.");
    }

    private void takeReserve(String time) { //예약하기. reservable의 요소가 0이 되면 예약 된 것
        int date = Integer.parseInt(time.substring(2, 3)) - 1;
        String s = time.substring(3, 5);
        int amORpm = ((s.equals("오전") ? 0 : s.equals("오후") ? 1 : -1));
        reservable[2*date + amORpm] = 0;
    }

    private void removeReserve(String time) { //예약취소하기. reservable의 요소가 1이되면 예약 취소 된 것
        int date = Integer.parseInt(time.substring(2, 3)) - 1;
        String s = time.substring(3, 5);
        int amORpm = ((s.equals("오전") ? 0 : s.equals("오후") ? 1 : -1));
        reservable[2*date + amORpm] = 1;
    }

    private boolean canReserve(String time) { //예약이 가능한지 판단 매개변수로 "6/1오전" 형식의 문자열이 들어온다.
        try {
            int date = Integer.parseInt(time.substring(2, 3)) - 1;
            String s = time.substring(3, 5);
            int amORpm = ((s.equals("오전") ? 0 : s.equals("오후") ? 1 : -1));
            if (amORpm == -1) return false;
            return (reservable[2*date + amORpm] == 1);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean alreadyReserve(String time) { //예약이 이미 되어있는지 판단
        try {
            int date = Integer.parseInt(time.substring(2, 3)) - 1;
            String s = time.substring(3, 5);
            int amORpm = ((s.equals("오전") ? 0 : s.equals("오후") ? 1 : -1));
            if (amORpm == -1) return false;
            return (reservable[2*date + amORpm] == 0);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean removeRecord(String name, String ID, String roomNum, String date) {
        //reservationRecord.csv의 예약된 정보가 담긴 row를 삭제하는 메소드
        File file = new File("./reservationRecord.csv");
        boolean check = false;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            StringBuffer sb = new StringBuffer();
            while((line=reader.readLine()) != null) {
                //line이 삭제하려는 예약이면 write하지 않는다.
                if(line.contains(name) && line.contains(ID) && line.contains(roomNum) && line.contains(date)) {
                    check = true;
                    continue;
                }
                sb.append(line + "\n");
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(String.valueOf(sb));
            bw.flush();
            return check;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void reserveRecord(String name, String ID, String roomNum, String date, String reason) {
        // reservationRecord.csv 에 예약정보 기록하는 메소드
        File file = new File("./reservationRecord.csv");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        try {
            BufferedWriter record = new BufferedWriter(new FileWriter(file, true));
            record.write(name + "," + ID + "," + roomNum + "," + date + "," + reason);
            record.write(System.lineSeparator());
            record.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void searchRecord(String name, String ID) {
        //reservationRecord.csv에서 해당 예약 내용있는지 확인후 출력
        File file = new File("./reservationRecord.csv");
        boolean check = false;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while((line=reader.readLine()) != null) {
                String[] lines = line.split(",");
                if(line.contains(name) && line.contains(ID) && lines.length == 5){
                    System.out.println("이름: " + name + ", 아이디: " + ID +
                            ", 호실번호: " + lines[2].substring(0,lines[2].length()-1) +
                            ", 시간: " + lines[3] + ", 예약사유: " + lines[4]);
                    check = true;
                }
            }
            //검색 실패
            if(!check) System.out.println("없는 예약입니다. 이름과 아이디를 다시 확인해주세요.");
        } catch (IOException e) {
            //예외
            System.out.println("없는 예약입니다. 이름과 아이디를 다시 확인해주세요.");
        }
    }

    // 객체 print를 위한 toString() 오버라이딩
    public String toString() {
        StringBuffer am = new StringBuffer("오전 : ");
        StringBuffer pm = new StringBuffer("오후 : ");

        ArrayList<String> _am = new ArrayList<>();
        ArrayList<String> _pm = new ArrayList<>();

        for (int i=0; i<14; i+=2)
            if (reservable[i] == 1) _am.add("6/" + (i / 2 + 1));

        for (int i=1; i<14; i+=2)
            if(reservable[i] == 1) _pm.add("6/" + (i/2+1));

        for (int i=0; i< _am.size()-1; i++) {
            am.append(_am.get(i) + ", ");
        } am.append(_am.get(_am.size()-1) + "\n");

        for (int i=0; i< _pm.size()-1; i++) {
            pm.append(_pm.get(i) + ", ");
        } pm.append(_pm.get(_pm.size()-1) + "\n");

        return roomNum + "\n" + am + pm;
    }
}
