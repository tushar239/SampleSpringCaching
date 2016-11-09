package org.tushar.sample.spring.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.HashMap;

/**
 * @author Tushar Chokshi @ 11/9/16.
 */
@Aspect
public class AspectClass {

    @Around("execution (* org.tushar.sample.spring.aop.AopService.execute(..))")
    public void aroundAopServiceExecution(final ProceedingJoinPoint joinPoint) {

        System.out.println("inside around advice");
        RetryTemplate retryTemplate = new RetryTemplate();
        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy(3, new HashMap<Class<? extends Throwable>, Boolean>() {{
            put(Exception.class, true);
        }});
        retryTemplate.setRetryPolicy(simpleRetryPolicy);

        try {
            Boolean result = retryTemplate.execute(new RetryCallback<Boolean, Throwable>() {
                public Boolean doWithRetry(RetryContext retryContext) throws Throwable {
                    System.out.println("retry count: " + retryContext.getRetryCount());
                    return (Boolean) joinPoint.proceed(joinPoint.getArgs());

                }
            });
        } catch (Throwable throwable) {
            //throwable.printStackTrace();
            System.out.println("Retry exhausted");
        } finally {

        }

    }

}
