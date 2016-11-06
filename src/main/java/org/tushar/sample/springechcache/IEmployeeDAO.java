package org.tushar.sample.springechcache;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * @author Tushar Chokshi @ 11/6/16.
 */
public interface IEmployeeDAO {
    /*
            Cache element is generated with implicitly created key and value as list of employees
            [key = 0,
             value=[Employee [id=4485, name=Ben, designation=Architect],
                    Employee [id=4486, name=Harley, designation=Programmer],
                    Employee [id=4487, name=Peter, designation=BusinessAnalyst],
                    Employee [id=4488, name=Sasi, designation=Manager],
                    Employee [id=4489, name=Abhi, designation=Designer]],
              version=1,
              hitCount=1,
              CreationTime = 1412366388920,
              LastAccessTime = 1412366388924 ]

              To add the result of the method in multiple caches
              @Cacheable({ "employeeCache", "personsCache" })

              Default key generation can be overridden by overriding KeyGenerator class.
              http://www.codingpedia.org/ama/spring-caching-with-ehcache/
        */
    @Cacheable(value = "employeeCache")
    List<Employee> getEmployees();

    @Cacheable(value = "employeeCache", key = "#name")
    Employee getEmployee(String name, List<Employee> employees);

    @Cacheable(value = "employeeCache", key = "#emp.id", condition="#emp.name.length() < 7")
    //@Cacheable(value="book", key="T(someType).hash(isbn)")
    Employee getEmployee(Employee emp);

    /*
            As opposed to @Cacheable annotation,this annotation does not cause the target method to be skipped - rather it
            always causes the method to be invoked and its result to be placed into the cache.
         */
    @CachePut(value="employeeCache", key="#name")
    Employee addEmployee(String name, List<Employee> employees);

    /*
        Example to evict multiple caches together
        @Caching(evict = {
         @CacheEvict(value="employeeCache", allEntries=true),
         @CacheEvict(value="podcasts", allEntries=true),
         @CacheEvict(value="searchResults", allEntries=true),
         @CacheEvict(value="newestAndRecommendedPodcasts", allEntries=true),
         @CacheEvict(value="randomAndTopRatedPodcasts", allEntries=true)
        */
    @CacheEvict(value="employeeCache", key="#name")
    void evictEmployeeCache(String name);

    @CacheEvict(value="employeeCache", allEntries=true)
    void evictEmployeeCache();
}
