package org.tushar.sample.enablecacheannotation;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

/**
 * @author Tushar Chokshi @ 4/12/15.
 */
@Repository
public class SimpleBookRepository implements BookRepository  {

    @Override
    @Cacheable(value="books", key = "#isbn") // This will register the Book instance with key as passed isbn inside cache name 'books'
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
