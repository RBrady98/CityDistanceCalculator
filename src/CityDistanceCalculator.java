import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import com.google.gson.*;

/**
 * CityDistanceCalculator.java
 * Lets a user find the distance between two world cities/large towns
 * 
 * @author Rian Brady
 * @version 1.0 19/5/2018
 */
public class CityDistanceCalculator {
    public static void main(String[] args){
        ArrayList<Country> countries = getCountries();
        ArrayList<City> cities = getCities();

        JOptionPane.showMessageDialog(null, 
                "Find the distance between two world cities");
        //Choose cities
        City city1 = selectCity(countries, cities);
        City city2 = selectCity(countries, cities);

        double distance = greatCircleDistance(city1, city2);
        DecimalFormat df = new DecimalFormat("#.##");
        JOptionPane.showMessageDialog(null, "The distance between " + city1.getName() + " and " 
                + city2.getName() + " is " + df.format(distance) + "km");
                            
    }

    /** 
     * Provides user with option panes to select a country and then a city from that country
     * @param countries ArrayList of Countries to choose from
     * @param cities ArrayList of Cities to choose from
     * @return City Object of city chosen
    */
    public static City selectCity(ArrayList<Country> countries, ArrayList<City> cities) {
        City selectedCity = null;
        String chosenCountry = chooseCountry(getCountryNames(countries));
        if(chosenCountry != null) {
            Country countryChoice = getCountryFromName(chosenCountry, countries);
            //Get cities of selected country
            ArrayList<City> citiesOfChosenCountry = getCitiesOfCountry(cities, countryChoice);
            String cityChoice = chooseCity(getCityNames(citiesOfChosenCountry));
            if(cityChoice != null) {
                selectedCity = getCityFromName(cityChoice, citiesOfChosenCountry);
            }
        }
         
        return selectedCity;
    }

    /**
     * Reads JSON and manipulates the string so that it can be used to instantiate Country Objects
     * which are placed into an ArrayList and then returned
     * @return ArrayList of Country Objects
     */
    public static ArrayList<Country> getCountries() {
        URL url = null;
        InputStreamReader reader = null;
        ArrayList<Country> countries = new ArrayList<Country>();
        JsonObject jObject = null;
        String jData = "";
        try {
            url = new URL("https://raw.githubusercontent.com/annexare/Countries/master/dist/minimal/countries.en.min.json");
            reader = new InputStreamReader(url.openStream());
            Gson gson = new Gson(); 
            jObject = gson.fromJson(reader, JsonObject.class); //Read JSON data into JsonObject
            reader.close();
        } catch (IOException e) {
            System.out.println("URL reading failure");
        }
        /*
            JSON Data Structure as JSON Object
            {"COUNTRY_ALPHA2_CODE" : "COUNTRY NAME"}
         */
        jData = jObject.toString();
        jData = jData.replaceAll("\"", "").replaceAll("\\{|\\}", "");
        String[] jDataArray = jData.split(",");

        for (String data : jDataArray) { 
            //Fill ArrayList of counties with new country objects 
            String[] tempSplit = data.split(":");
            countries.add(new Country(tempSplit[1], tempSplit[0]));
        }
        
        return countries;
    }

    /**
     * Reads the JSON and parses it into City Objects which are then placed into an ArrayList of City
     * Objects.
     * @return ArrayList of City Objects
     */
    public static ArrayList<City> getCities() {
        URL url = null;
        InputStreamReader reader = null;
        try {
            url = new URL("https://raw.githubusercontent.com/lutangar/cities.json/master/cities.json");
            reader = new InputStreamReader(url.openStream());
        } catch (IOException e) {
            System.out.println("URL reading failure");
        }

        Type cityClassType = new TypeToken<ArrayList<City>>(){}.getType();
        ArrayList<City> cities = new Gson().fromJson(reader, new TypeToken<ArrayList<City>>(){}.getType());
        return cities;
    }

    /**
     * Loops through each country in the country array and adds the country name to an
     * ArrayList of strings
     * @param countries The country list to loop through
     * @return ArrayList of Strings containing the names of the countries in the country ArrayList
     */
    public static ArrayList<String> getCountryNames(ArrayList<Country> countries) {
        ArrayList<String> countryNames = new ArrayList<String>();
        for (Country country : countries) {
            countryNames.add(country.getName());
        }
        return countryNames;
    }

    /**
     * Loops through each city in the city array and adds the city name to an
     * ArrayList of strings
     * @param cities The country list to loop through
     * @return ArrayList of Strings containing the names of the cities in the cities ArrayList
     */
    public static ArrayList<String> getCityNames(ArrayList<City> cities) {
        ArrayList<String> cityNames = new ArrayList<String>();
        for (City city : cities) {
            cityNames.add(city.getName());
        }
        return cityNames;
    }

    /**
     * Provides the user with a drop down option pane which shows the countries which can be selected
     * @param countryNames Country names to be used in the drop down menu
     * @return String containing the name of the country the user has chosen
     */
    public static String chooseCountry(ArrayList<String> countryNames) {
        String[] choices = countryNames.toArray(new String[0]);
        String choice = (String) JOptionPane.showInputDialog(null, "Choose a country", 
                "Country Choice", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
        return choice;
    }

    /**
     * Loops through the country ArrayList and searches for the country which matches the name provided.
     * @param name Name of the country to search for
     * @param countries ArrayList of Country Objects to search through
     * @return Country Object corresponding to the name provided
     */
    public static Country getCountryFromName(String name, ArrayList<Country> countries) {
        Country chosenCountry = null;
        for (Country c : countries) {
            if(c.getName().equals(name)) {
                chosenCountry = c;
                break;
            }
        }
        return chosenCountry;
    }

    /**
     * Loops through the cities ArrayList and searches for the city which matches the name provided.
     * @param name Name of the city to search for
     * @param cities ArrayList of City Objects to search through
     * @return City Object corresponding to the name provided
     */
    public static City getCityFromName(String name, ArrayList<City> cities) {
        City chosenCity = null;
        for (City city : cities) {
            if(city.getName().equals(name)) {
                chosenCity = city;
                break;
            }
        }
        return chosenCity;
    }

    /**
     * Searches through the cities array list and compares the alpha2 country code of the provided
     * country against the current city. Cities of the provided country are added to an ArrayList
     * @param cities ArrayList of cities to search through
     * @param country Country whose cities you want to get
     * @return ArrayList of City Objects which are from the specific country provided
     */
    public static ArrayList<City> getCitiesOfCountry(ArrayList<City> cities, Country country) {
        ArrayList<City> chosenCityArray = new ArrayList<City>();
        for (City city : cities) {
            if(city.getCountry().equals(country.getAlpha2code())) {
                chosenCityArray.add(city);
            }   
        }
        return chosenCityArray;
    }

    /**
     * Provides the user with a drop down option pane which shows the cities which can be selected
     * @param  cityNames City names to be used in the drop down menu
     * @return String containing the name of the city the user has chosen
     */
    public static String chooseCity(ArrayList<String> cityNames) {
        String[] choices = cityNames.toArray(new String[0]);
        String choice = (String) JOptionPane.showInputDialog(null, "Choose city", 
                "City Choice", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
        return choice;
    }

    /**
     * Calculates the distance between two points using latitude and longitude.
     * Calculation uses the haversine formula for finding the great circle distance
     * @param city1 City Object whose latitude and longitude are used in the calculation
     * @param city2 City Object whose latitude and longitude are used in the calculation
     * @return The distance between the two cities provided
     */
    public static double greatCircleDistance(City city1, City city2) {
        final double EARTH_RADIUS = 6371.01;
        double absLat = degToRad(city2.getLat()) - degToRad(city1.getLat());
        double absLon = degToRad(city2.getLng()) - degToRad(city1.getLng());
        double x = Math.pow(Math.sin(absLat / 2), 2) + Math.cos(degToRad(city1.getLat())) 
                * Math.cos(degToRad(city2.getLat())) * Math.pow(Math.sin(absLon / 2), 2);

        double distance = EARTH_RADIUS * (2 * Math.asin(Math.sqrt(x)));
        return distance;
    }

    /**
     * Converst degrees to radians
     * @param degrees
     * @return Degrees provided in radian form
     */
    public static double degToRad(double degrees) {
        return degrees * (Math.PI / 180.0);
    }
}