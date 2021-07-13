import java.util.StringTokenizer;

public class KeyValue {
    private String key, value;

    public KeyValue(String str) {
        StringTokenizer parsing = new StringTokenizer(str, "=");
        if(parsing.countTokens() == 2) {
            this.key = parsing.nextToken().trim();
            this.value = parsing.nextToken().trim();
        }
        else {
            System.out.println("Invalid Property String");
            System.exit(0);
        }
    }

    //Calender 로 받아온 String을 parsing 하는 부분
    public KeyValue(String key, String value) {
        this.key = key;
        StringTokenizer parsing = new StringTokenizer(value);
        String year, month = "00", date;
        String[] arr = new String[6];
        int i = 0;
        while(parsing.hasMoreTokens()) {
            arr[i] = parsing.nextToken();
            i++;
        }
        year = arr[5];
        switch (arr[1]) {
            case "Jan" -> month = "01";
            case "Feb" -> month = "02";
            case "Mar" -> month = "03";
            case "Apr" -> month = "04";
            case "May" -> month = "05";
            case "Jun" -> month = "06";
            case "Jul" -> month = "07";
            case "Aug" -> month = "08";
            case "Sep" -> month = "09";
            case "Oct" -> month = "10";
            case "Nov" -> month = "11";
            case "Dec" -> month = "12";
        }
        date = arr[2];

        this.value = year + "-" + month + "-" + date;
    }

    public String getKey() { return key; }
    public String getValue() { return value; }
}
