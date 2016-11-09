package org.tushar.sample.spring.aop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

// https://www.mkyong.com/spring3/spring-aop-aspectj-annotation-example/

public class AopService {
    public Boolean execute() throws Exception {

        throw new Exception("error...");
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AopConfiguration.class);
        AopService aopService = applicationContext.getBean(AopService.class);
        aopService.execute();
    }
}
