package com.example.android.booklisting;

/**
 * Created by User on 7/10/2017.
 */

// A Book object contains information related to a single book.

public class Book {

    // Book title
    private String bookTitle;

    // Book author
    private String bookAuthor;

    // Book description
    private String bookDescription;

    /**
     * Constructs a new Book object.
     *
     * @param title       is the title of the book
     * @param authors      is the author of the book
     * @param description is the description of the book
     */
    public Book(String title, String authors, String description) {
        bookTitle=title;
        bookAuthor=authors;
        bookDescription=description;
    }

    /**
     * Returns the title of the book.
     */
    public String getTitle() {
        return bookTitle;
    }

    /**
     * Returns the author of the book.
     */
    public String getAuthor() {
        return bookAuthor;
    }

    /**
     * Returns the description of the book.
     */
    public String getDescription() {
        return bookDescription;
    }
}
