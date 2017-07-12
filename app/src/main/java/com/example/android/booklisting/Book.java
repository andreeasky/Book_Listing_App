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

    // Book picture
    private String bookPicture;

    // Book URL
    private String bookUrl;

    /**
     * Constructs a new Book object.
     *
     * @param title is the title of the book
     * @param author is the author of the book
     * @param description is the description of the book
     * @param picture is the picture of the book
     * @param url is the website URL to find more details about the book
     */
    public Book(String title, String author, String description, String picture, String url) {
        bookTitle = title;
        bookAuthor = author;
        bookDescription = description;
        bookPicture = picture;
        bookUrl = url;
    }

    /**
    * Returns the title of the book.
    */
    public String getTitle(){
        return bookTitle;
    }

    /**
     * Returns the author of the book.
     */
    public String getAuthor(){
        return bookAuthor;
    }

    /**
     * Returns the description of the book.
     */
    public String getDescription(){
        return bookDescription;
    }

    /**
     * Returns the picture of the book.
     */
    public String getPicture(){
        return bookPicture;
    }

    /**
     * Returns the URL of the book.
     */
    public String getUrl(){
        return bookUrl;
    }
}
