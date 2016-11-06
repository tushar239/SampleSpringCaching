package org.tushar.sample.enablecacheannotation;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleCacheErrorHandler;
import org.springframework.cache.interceptor.SimpleCacheResolver;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

// Spring doesn't allow to have multiple CachingConfigurers.

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
    // example of multiple CacheManagers
    @Bean
    public CacheManager cacheManager() {
        // configure and return an implementation of Spring's CacheManager SPI
        CompositeCacheManager compositeCacheManager = new CompositeCacheManager();

        List<CacheManager> cacheManagers = new ArrayList<CacheManager>();
        cacheManagers.add(simpleCacheManager());
        cacheManagers.add(ehCacheCacheManager());

        compositeCacheManager.setCacheManagers(cacheManagers);
        return compositeCacheManager;

    }

    @Bean
    public SimpleCacheManager simpleCacheManager() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        simpleCacheManager.setCaches(Arrays.asList(new ConcurrentMapCache("default"), new ConcurrentMapCache("books")));
        return simpleCacheManager;
    }

    @Bean
    public EhCacheCacheManager ehCacheCacheManager() {
        return new EhCacheCacheManager(ehCacheManagerFactoryBean().getObject());
    }

    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        cacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
        cacheManagerFactoryBean.setShared(true);
        return cacheManagerFactoryBean;
    }

    @Bean
    public CacheResolver cacheResolver() {
        return new SimpleCacheResolver(cacheManager());
    }

    @Bean
    public KeyGenerator keyGenerator() {
        // return new MyKeyGenerator();
        return new SimpleKeyGenerator();
    }

    @Bean
    public CacheErrorHandler errorHandler() {
        return new SimpleCacheErrorHandler();
    }

}
