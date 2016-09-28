package org.tushar.sample.enablecacheannotation;

/**
 * @author Tushar Chokshi @ 4/12/15.
 */
public interface BookRepository {
    Book getByIsbn(String isbn);
}
