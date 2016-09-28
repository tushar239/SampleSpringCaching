package org.tushar.sample.spring.schedular;

import org.springframework.context.support.ClassPathXmlApplicationContext;

// Example is taken from: http://howtodoinjava.com/2013/04/23/4-ways-to-schedule-tasks-in-spring-3-scheduled-example/
// Spring Scheduler tutorial: http://docs.spring.io/spring/docs/3.0.x/spring-framework-reference/html/scheduling.html
// you can use synchronous(@Scheduled) and asynchronous (@Async) scheduling
public class TestClient {
    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("classpath:spring-schedular.xml");
    }
}
