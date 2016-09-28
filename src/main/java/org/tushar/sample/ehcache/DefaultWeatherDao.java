package org.tushar.sample.ehcache;

public class DefaultWeatherDao implements WeatherDao {

    public Weather getWeather(String zipCode) {
        Weather weather = new Weather();
        weather.setZipCode(zipCode);
        return weather;
    }

    // public List<Location> findLocations(String locationSearch) {
    // //Some Code
    // }
}