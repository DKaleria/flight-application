package dubovikLera.entity;

import java.util.Objects;

public class Airport {
    private String code;
    private String country;
    private String city;


    public Airport(String code, String country, String city) {
        this.code = code;
        this.country = country;
        this.city = city;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "Airport{" +
               "code='" + code + '\'' +
               ", country='" + country + '\'' +
               ", city='" + city + '\'' +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Airport airport = (Airport) o;
        return Objects.equals(code, airport.code) && Objects.equals(country, airport.country) && Objects.equals(city, airport.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, country, city);
    }
}
