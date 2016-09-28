package org.tushar.sample.ehcache;

public interface WeatherDao {
    
    public Weather getWeather(String zipCode);
    
    //public List<Location> findLocations(String locationSearch);
}