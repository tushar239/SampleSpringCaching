package org.tushar.sample.enablecacheannotation;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * @author Tushar Chokshi @ 4/12/15.
 */

/*
@EnableCaching
Documentation - http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/cache/annotation/EnableCaching.html
Example - https://spring.io/guides/gs/caching/

Enables Spring's annotation-driven cache management capability, similar to the support found in Spring's <cache:*> XML namespace.
It replaces <cache:annotation-driven/>.

For those that wish to establish a more direct relationship between @EnableCaching and the exact cache manager bean to be used, the CachingConfigurer callback interface may be implemented.
Notice also the keyGenerator method in the example above. This allows for customizing the strategy for cache key generation, per Spring's KeyGenerator SPI. Normally, @EnableCaching will configure Spring's SimpleKeyGenerator for this purpose, but when implementing CachingConfigurer, a key generator must be provided explicitly. Return null or new SimpleKeyGenerator() from this method if no customization is necessary.
CachingConfigurer offers additional customization options: it is recommended to extend from CachingConfigurerSupport that provides a default implementation for all methods which can be useful if you do not need to customize everything. See CachingConfigurer Javadoc for further details.

To understand how spring takes care of @EnableCaching, read SampleSpringCoreProject's readme.txt - read how @Configuration is processed by spring
 */
@Configuration
@ComponentScan
@EnableCaching
public class CachingConfiguration implements CachingConfigurer // OR extends CachingConfigurerSupport
{

    /*
    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("books");
    }
    */
    @Bean
    public CacheManager cacheManager() {
        // configure and return an implementation of Spring's CacheManager SPI
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache("default"), new ConcurrentMapCache("books")));
        return cacheManager;
    }

    @Override
    public KeyGenerator keyGenerator() {
        // return new MyKeyGenerator();
        return new SimpleKeyGenerator();
    }

}
