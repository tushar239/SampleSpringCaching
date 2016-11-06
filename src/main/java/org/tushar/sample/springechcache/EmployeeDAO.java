package org.tushar.sample.springechcache;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

;

/*
 This is an example of using EhCache using spring xml configuration. 
 Spring context (xml) file (spring-ehcache.xml) contains a bean for EhCacheCacheManager.
 
 If you don't want to use spring xml file and want to use annotations then along with 
 @Configuration, you need to use @EnableCaching. 
 Example is there in "Books->Spring->Spring Caching->@EnableCaching and @Cacheable.pdf"
 
 You can also use SimpleCacheManager instead of EhCacheManager. 
 SimpleCacheManager simply uses java's ConcurrentMap for caching. See spring-ehcache.xml. 
  
 */

/*
How proxying works in case of Caching?
--------------------------------------

InfrastructureAdvisorAutoProxyCreator extends AbstractAutoProxyCreator is a BeanPostProcessor provided by Spring. Which is called for all beans. It creates proxies for all eligible beans.
In postProcessAfterInitialization method, it wraps all eligible beans with proxies. While doing so, it calls DefaultAopProxyFactory that decides whether to use JDK based proxy or CGLIB based proxy.



JdkDynamicAopProxy if target class is interface, otherwise it uses ObjenesisCglibAopProxy
ObjenesisCglibAopProxy extends CglibAopProxy

Spring creates a CglibAopProxy when EmployeeDAO.getEmployees() is called.
It uses DynamicAdvisedInterceptor as a CGLIB proxy's MethodInterceptor which is inside CglibAopProxy.


@EnableCaching uses CachingConfigurationSelector that uses ProxyCachingConfiguration (by default) or AspectJCachingConfiguration.
ProxyCachingConfiguration injects CacheInterceptor.




DynamicAdvisedInterceptor's intercept method will be called for every method call of target class. It detects whether before target class' method is called, some interceptors need to be called.
In case of EmployeeDAO.getEmployees(), it detects that CacheInterceptor needs to be called.

CacheInterceptor extends CacheAspectSupport
its execute method first calls all CacheOperations like CacheEvict, CachePut etc if required and then it calls invoker.invoke() that calls target class method (EmployeeDAO.getEmployees())
CacheOperation object is created based on @Cacheable annotation

This is how Spring uses Proxy for Caching operations. So, required caching operations are called before and after actual class' method (EmployeeDAO.getEmployees())


 */

@Component("employeeDAO")
public class EmployeeDAO /*implements IEmployeeDAO*/ {
    public EmployeeDAO() {
        System.out.println("employeeDAO instance is created");
    }
    
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
    @Cacheable(value = "employeeCache"/*,cacheManager="bCacheManager"*/)
    public List<Employee> getEmployees() {
        Random random = new Random();
        int randomid = random.nextInt(9999);
        System.out.println("*** Creating a list of employees and returning the list ***");
        List<Employee> employees = new ArrayList<Employee>(5);
        employees.add(new Employee(randomid, "Ben", "Architect"));
        employees.add(new Employee(randomid + 1, "Harley", "Programmer"));
        employees.add(new Employee(randomid + 2, "Peter", "BusinessAnalyst"));
        employees.add(new Employee(randomid + 3, "Sasi", "Manager"));
        employees.add(new Employee(randomid + 4, "Abhi", "Designer"));
        return employees;
    }

    /*
        Cache element is generated with key = name passed as a method parameter
        key = Harley, value=Employee [id=4863, name=Harley, designation=Programmer], version=1, hitCount=1, CreationTime = 1412368892570, LastAccessTime = 1412368892570
        
        @Cacheable annotation allows the user to specify how the key is generated through its key attribute. 
        The developer can use SpEL to pick the arguments of interest (or their nested properties), perform operations or even invoke arbitrary methods without having to write any code or implement any interface. 
        This is the recommended approach over the default generator
    */ 

    @Cacheable(value = "employeeCache", key = "#name")
    public Employee getEmployee(String name, List<Employee> employees) {
        System.out.println("*** getEmployee() --- Adding an employee with name : " + name + " ***");
        Employee emp = null;
        for (Employee employee : employees) {
            if (employee.getName().equalsIgnoreCase(name)) {
                emp = employee;
            }
        }
        return emp;
    }

    @Cacheable(value = "employeeCache", key = "#emp.id", condition="#emp.name.length() < 7")
    //@Cacheable(value="book", key="T(someType).hash(isbn)")
    public Employee getEmployee(Employee emp) {
        System.out.println("*** getEmployee() --- Adding an employee with key : " + emp.getId() + "and where name's length < 7 ***");
        return emp;
    }

    
    /*
        As opposed to @Cacheable annotation,this annotation does not cause the target method to be skipped - rather it 
        always causes the method to be invoked and its result to be placed into the cache.
     */
    @CachePut(value="employeeCache", key="#name")
    public Employee addEmployee(String name, List<Employee> employees) {
        System.out.println("*** addEmployee() --- Adding an employee with name : " + name + " ***");
        Employee emp = null;
        for (Employee employee : employees) {
            if (employee.getName().equalsIgnoreCase(name)) {
                emp = employee;
            }
        }
        return emp;
    }

    
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
    public void evictEmployeeCache(String name) {
        System.out.println("*** Evicting cache with key="+name);
    }

    @CacheEvict(value="employeeCache", allEntries=true)
    public void evictEmployeeCache() {
        System.out.println("*** Evicting cache totally");
    }
}