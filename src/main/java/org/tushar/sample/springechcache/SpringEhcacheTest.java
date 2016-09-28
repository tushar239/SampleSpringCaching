package org.tushar.sample.springechcache;

import java.util.List;

import net.sf.ehcache.Ehcache;

import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringEhcacheTest {

    private static ApplicationContext context;

    public static void main(String[] args) {
        List<Employee> employees;
        Employee employee;
        
        context = new ClassPathXmlApplicationContext("classpath:spring-ehcache.xml");
        EmployeeDAO dao = (EmployeeDAO)context.getBean("employeeDAO");

        CacheManager cacheManager = (CacheManager)context.getBean("cacheManager");
        {
            employees = dao.getEmployees(); // cache element with key=implicitly generated key and value=list of employees will be created by executing getEmployees()
            System.out.println(employees.toString());
            checkElements(cacheManager);
            employees = dao.getEmployees(); // getEmployees() won't be executed, list of employees will be returned from cache
            System.out.println(employees.toString());
            employees = dao.getEmployees();  // getEmployees() won't be executed, list of employees will be returned from cache
            System.out.println(employees.toString());
        }
        System.out.println("---------------------------------------------------");
        {
            employee = dao.getEmployee("Harley", employees); // cache element with key=Harley will be created
            System.out.println(employee.toString());
            checkElements(cacheManager);
            employee = dao.getEmployee("Harley", employees); // getEmployee() won't be executed, element will be fetched from already existing element 
            System.out.println(employee.toString());
            dao.evictEmployeeCache("Harley");// evicting elements with key=Harley from employeeCache
            employee = dao.getEmployee("Harley", employees); // getEmployee() will be executed to create a cache element with key=Harley
            System.out.println(employee.toString());
        }
        System.out.println("---------------------------------------------------");
        {
            // Testing @Cacheable(value = "employeeCache", key = "emp.id")
            Employee emp = new Employee(123, "Tushar", "Software Eng");
            dao.getEmployee(emp);
            
            Employee emp2 = new Employee(456, "Tushar1234567", "Software Eng");
            dao.getEmployee(emp2);// emp2 won't be added to cache because it doesn't match the condition
            checkElements(cacheManager);
            checkElements(cacheManager, 123);
            checkElements(cacheManager, 456);
        }
        System.out.println("---------------------------------------------------");
        {
            // Testing @CachePut --- addEmployee() is always executed to add a new element in cache or overwrite an existing one 
            dao.addEmployee("Ben", employees);
            dao.addEmployee("Ben", employees);
            dao.addEmployee("Ben", employees);
            checkElements(cacheManager);
            checkElements(cacheManager, "Ben");
        }
        System.out.println("---------------------------------------------------");
        {
            dao.evictEmployeeCache();
            checkElements(cacheManager);
        }
        
        
    }
    private static void checkElements(CacheManager cacheManager) {
        
        if (cacheManager instanceof EhCacheCacheManager) {

            Ehcache ehCache = ((EhCacheCacheManager) cacheManager).getCacheManager().getEhcache("employeeCache");
            List keys = ehCache.getKeys();

            if (keys == null || keys.size() == 0) {
                System.out.println("Cache is empty");
            } else {
                for (Object key : keys) {
                    System.out.println("Element: " + ehCache.get(key));
                }
            }
        } 
    }
    
    private static void checkElements(CacheManager cacheManager, Object key) {
        if(cacheManager instanceof SimpleCacheManager) {
            if(key != null) {
                Cache cache = ((SimpleCacheManager)cacheManager).getCache("employeeCache");
                // There is no cache.getKeys(). Very hard to get all cache elements.
                ValueWrapper value = cache.get(key);
                if(value != null) {
                    System.out.println("Element: " + value.get());
                } else {
                    System.out.println("Element with key="+key+" doesn't exist.");
                }
            }
        }
    }
}
