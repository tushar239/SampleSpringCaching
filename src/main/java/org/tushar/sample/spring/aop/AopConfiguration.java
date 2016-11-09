package org.tushar.sample.spring.aop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author Tushar Chokshi @ 11/9/16.
 */
@Configuration
@EnableAspectJAutoProxy
public class AopConfiguration {
    @Bean
    public AopService aopService() {
        return new AopService();
    }

    @Bean
    public AspectClass aspectClass() {
        return new AspectClass();
    }


}
