import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;

public class Countries {
    private String country;
    private double lat, lng;

    //생성자
    public Countries(String country) {
        String line;
        this.country = country;
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader("Countries.csv");
            br = new BufferedReader(fr);

            line = br.readLine();
            while(line != null && !line.contains(this.country)) line = br.readLine();
            StringTokenizer st = new StringTokenizer(line, ",");
            st.nextToken();
            this.lat = Double.parseDouble(st.nextToken().trim());
            this.lng = Double.parseDouble(st.nextToken().trim());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                fr.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public double getLat() { return lat; }
    public double getLon() { return lng; }
}
