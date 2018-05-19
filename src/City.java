/*
    City class 
    Used as a container for JSON data
 */
public class City {
    private String name;
    private String country; //alpha2 country code
    private double lat;
    private double lng;

    public void Country(String name, String country, double lat, double lng) {
        this.country= country;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @return the lat
     */
    public double getLat() {
        return lat;
    }

    /**
     * @return the lng
     */
    public double getLng() {
        return lng;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    @Override
    public String toString() {
        return "Name: " + name + "\nCountry: " + country + "\nLatitude: " + lat + "\nLongitude: " + lng;
    }
}