package org.tushar.sample.enablecacheannotation;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

/**
 * @author Tushar Chokshi @ 4/12/15.
 */
@Repository
public class SimpleBookRepository implements BookRepository  {

    // if there are multiple cache managers, then you can force CacheResolver to use one of them using 'cacheManager' attribute.
    // Otherwise, CacheResolver will use any CacheManager with which cache name=books is found.
    @Cacheable(value="books", key = "#isbn"/*, cacheManager = "ehCacheCacheManager"*/) // This will register the Book instance with key as passed isbn inside cache name 'books'
    public Book getByIsbn(String isbn) {
        simulateSlowService();
        return new Book(isbn, "Some book");
    }

    // Don't do this at home
    private void simulateSlowService() {
        try {
            long time = 1000L;
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
