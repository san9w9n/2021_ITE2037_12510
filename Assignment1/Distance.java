import java.util.StringTokenizer;

public class Distance {
    private String name;
    private double lat, lng;

    public Distance(String name, double lat, double lng) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    public String writeDistance(){
        return "Country : " + this.name + "\n" +
                "latitude=" + this.lat + "\n" +
                "longitude=" + this.lng + "\n" +
                "--------------------";
    }

    public static String getDistance(Distance a, Distance b) {
        double lng1, lng2, lat1, lat2;
        String n1 = a.writeDistance();
        String n2 = b.writeDistance();
        String line;
        StringTokenizer st = new StringTokenizer(n1,"\n");
        st.nextToken();
        line = st.nextToken();
        lat1 = Double.parseDouble(line.split("=")[1].trim());
        line = st.nextToken();
        lng1 = Double.parseDouble(line.split("=")[1].trim());

        st = new StringTokenizer(n2,"\n");
        st.nextToken();
        line = st.nextToken();
        lat2 = Double.parseDouble(line.split("=")[1].trim());
        line = st.nextToken();
        lng2 = Double.parseDouble(line.split("=")[1].trim());

        return Double.toString(Math.sqrt(Math.pow((lng1 - lng2),2) + Math.pow((lat1-lat2), 2)));
    }
}
