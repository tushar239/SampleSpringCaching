package org.tushar.sample.enablecacheannotation;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Tushar Chokshi @ 4/12/15.
 */

public class Application {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(CachingConfiguration.class);
        applicationContext.refresh();



        /*String[] beanDefNames = applicationContext.getBeanDefinitionNames();
        for (int i = 0; i < beanDefNames.length; i++) {
            System.out.println(beanDefNames[i]);

        }*/


        BookRepository bookRepository = applicationContext.getBean(BookRepository.class);
        System.out.println("Creating Book instances in cache and fetching it");
        {
            Book book = bookRepository.getByIsbn("isbn-1234");
            System.out.println(book);
        }
        {
            Book book = bookRepository.getByIsbn("isbn-5678");
            System.out.println(book);
        }
        {
            Book book = bookRepository.getByIsbn("isbn-9013");
            System.out.println(book);
        }

        System.out.println("Fetching Book instances from the cache without creating them again");
        {
            Book book = bookRepository.getByIsbn("isbn-1234");
            System.out.println(book);
        }
        {
            Book book = bookRepository.getByIsbn("isbn-5678");
            System.out.println(book);
        }
        {
            Book book = bookRepository.getByIsbn("isbn-9013");
            System.out.println(book);
        }



    }

}

