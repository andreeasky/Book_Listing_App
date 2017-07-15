package com.example.android.booklisting;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 7/10/2017.
 */

// A Book object contains information related to a single book.

public class Book implements Parcelable{

    // Book title
    private String bookTitle;

    // Book author
    private String bookAuthor;

    // Book description
    private String bookDescription;

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(bookTitle);
        out.writeString(bookAuthor);
        out.writeString(bookDescription);
    }

    private Book(Parcel in) {
        bookTitle = in.readString();
        bookAuthor = in.readString();
        bookDescription = in.readString();
    }

    /**
     * Constructs a new Book object.
     *
     * @param title       is the title of the book
     * @param authors      is the author of the book
     * @param description is the description of the book
     */
    public Book(String title, String authors, String description) {
        // Normal actions performed by class, since this is still a normal object!
        bookTitle=title;
        bookAuthor=authors;
        bookDescription=description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Book> CREATOR
            = new Parcelable.Creator<Book>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

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
