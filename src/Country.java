/**
 * Country
 */
public class Country {
    private String name;
    private String alpha2code;
    
    public Country(String name, String alpha2code) {
        this.name = name;
        this.alpha2code = alpha2code;
    }

    /**
     * @param alpha2code the alpha2code to set
     */
    public void setAlpha2code(String alpha2code) {
        this.alpha2code = alpha2code;
    }

    /**
     * @return the alpha2code
     */
    public String getAlpha2code() {
        return alpha2code;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
}