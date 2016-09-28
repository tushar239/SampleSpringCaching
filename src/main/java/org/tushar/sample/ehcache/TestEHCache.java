package org.tushar.sample.ehcache;

import java.util.List;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

public class TestEHCache {
    // This is a test of normal EHCache functionality. Not testing Spring EHCache.
    public static void main(String[] args) throws InterruptedException {
        WeatherDao weatherDao = new DefaultWeatherDao();
        weatherDao.getWeather("98052");

        Ehcache ehcache = CacheManager.getInstance().getCache("weatherCache");

        ehcache.put(new Element("weather", weatherDao.getWeather("98052")));
        checkElements();

        Thread.sleep(5000);
        checkElements();// life of the cached element is 3 seconds. They should be expire by now.
        System.out.println("Number of elements in weatherCache:"+ehcache.getSize());
        ehcache.evictExpiredElements();// deleting all expired elements from cache
        checkElements();

    }

    protected static void checkElements() {
        Ehcache ehcache = CacheManager.getInstance().getCache("weatherCache");

        // List keys = ehcache.getKeysWithExpiryCheck();// does not list expired elements in the ehcache.
        List keys = ehcache.getKeys();

        if (keys == null || keys.size() == 0) {
            System.out.println("expired... load again");
        } else {
            for (Object key : keys) {
                System.out.println(" key: " + key.toString() + " value: " + ehcache.get(key));
            }
        }
    }
}
