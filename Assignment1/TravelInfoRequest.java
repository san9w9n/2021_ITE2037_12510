import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;

public class TravelInfoRequest {
    public static void main(String[] args){
        //선언부
        FileReader template = null;
        BufferedReader tp = null;
        BufferedReader pp = null;
        FileReader property = null;
        FileWriter output = null;
        BufferedWriter bw = null;

        try {
            //template 파일
            template = new FileReader("template_file.txt");
            tp = new BufferedReader(template);
            //properties 파일
            property = new FileReader("properties.txt");
            pp = new BufferedReader(property);
            //output 파일
            output = new FileWriter("output_file.txt");
            bw = new BufferedWriter(output);

            //properties 파일에서 가져온 KeyValue 객체를 담는 ArrayList
            ArrayList<KeyValue> keyValue = new ArrayList<KeyValue>();
            //key : date
            Calendar cal = Calendar.getInstance();
            keyValue.add(new KeyValue("date", cal.getTime().toString()));

            //key : 나머지 (properties.txt에서 한줄당 한 객체 생성)
            String line = pp.readLine();
            while(line != null) {
                keyValue.add(new KeyValue(line));
                line = pp.readLine();
            }

            //Countries 와 Distance 객체 생성
            Countries start = null, end = null;
            Distance first = null, last = null;
            for(KeyValue k: keyValue) {
                if(k.getKey().contains("start")){
                    start = new Countries(k.getValue());
                    first = new Distance(k.getValue(), start.getLat(), start.getLon());
                }
                else if(k.getKey().contains("depart")){
                    end = new Countries(k.getValue());
                    last = new Distance(k.getValue(), end.getLat(), end.getLon());
                }
            }

            //output_file 작성부분.
            line = tp.readLine();
            while(line != null) {
                //<add info> 부분 나라 정보와 사이 거리
                if(line.contains("<add info>")) {
                    bw.write("Distance of\n");
                    if(first!=null && last !=null) {
                        bw.write(first.writeDistance() + "\n");
                        bw.write(last.writeDistance() + "\n");
                        bw.write("is\n");
                        bw.write(Distance.getDistance(first, last) + "\n");
                        line = tp.readLine();
                    }
                    continue;
                }
                //template에서 { } 이부분을 Value 값으로 변경
                for(KeyValue k: keyValue) {
                    if(line.contains(k.getKey())) {
                        line = line.replace("{"+k.getKey()+"}",  k.getValue());
                    }
                }
                //작성하고 다음 줄 읽음
                bw.write(line+"\n");
                line = tp.readLine();
            }


        } catch (Exception e) { //예외처리
            e.printStackTrace();
        } finally {
            //파일 close() 부분
            try {
                bw.flush();
                bw.close();
                output.close();
                pp.close();
                property.close();
                tp.close();
                template.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
}
