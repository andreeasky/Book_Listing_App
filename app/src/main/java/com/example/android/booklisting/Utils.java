package com.example.android.booklisting;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 7/10/2017.
 */

// Helper methods related to requesting and receiving books data from Google Books.
public final class Utils {

    // Tag for the log messages
    //
    private static final String LOG_TAG = Utils.class.getSimpleName();

    // This is the Google API URL
    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?&maxResults=10&q=";

    // Create a private constructor.
    // This class is only meant to hold static variables and methods, which can be accessed
    // directly from the class name Utils (and an object instance of Utils is not needed).
    //
    private Utils() {
    }

    // Make an HTTP request to the given URL and return a String as the response.
    private static String makeHTTPRequest (URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout( 10000 /* milliseconds */ );
            urlConnection.setConnectTimeout( 15000 /* milliseconds */ );
            urlConnection.setRequestMethod( "GET" );
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream( inputStream );
            } else {
                Log.e( LOG_TAG, "Error response code: " + urlConnection.getResponseCode() );
            }
        } catch (IOException e) {
            Log.e( LOG_TAG, "Problem retrieving the book JSON result:", e );
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies that an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    // Return a list of Book objects that has been built up from
    // parsing the given JSON response.
    //
    private static List<Book> extractFeatureFromJson(String bookJSON) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty( bookJSON )) {
            return null;
        }

        // Create an empty ArrayList that we can start adding books to
        List<Book> books = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject( bookJSON );

            // Extract the JSONArray associated with the key called "items",
            // which represents a list of information about the book.
            JSONArray bookArray = baseJsonResponse.getJSONArray( "items" );

            // For each book in the bookArray, create a Book object
            for (int i = 0; i < bookArray.length(); i++) {

                // Get a single book at position i within the list of books
                JSONObject currentBook=bookArray.getJSONObject(i);

                // For a given book, extract the JSONObject associated with the
                // key called "volumeInfo", which represents a list of all information
                // for that book.
                JSONObject volumeInfo=currentBook.getJSONObject("volumeInfo");

                String title = "N/A";
                if (volumeInfo.has("title")) {
                    // Extract the value for the key called "title"
                    title=volumeInfo.getString("title");
                }

                String author = "N/A";
                JSONObject authors = new JSONObject();
                if (volumeInfo.has("author")) {
                    // Extract the value for the key called "author"
                    author=volumeInfo.getJSONObject(authors);
                }

                String description = "N/A";
                if (volumeInfo.has("description")) {
                    // Extract the value for the key called "description"
                    description=volumeInfo.getString("description");
                }

                // Create a new Book object with the title, author, description,
                // image and url from the JSON response.
                Book book=new Book(title, author, description);

                // Add the new Book to the list of books.
                books.add(book);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e( LOG_TAG, "Problem parsing the JSON books list", e );
        }

        // Return the list of books
        return books;
    }

        // Query the Google Books API and return an object with a list of books.
        public static List<Book> fetchBooksData (String searchUrl){

            // Delay the network response by 2 sec, in order to see how the progress bar works
            try {
                Thread.sleep( 2000 );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Create URL object
            URL url = createUrl(BASE_URL + searchUrl );
            Log.i("final url",url.toString());

            //Make an HTTP request to the given URL and return a JSON as the response.
            //
            String jsonResponse = null;
            try {
                jsonResponse = makeHTTPRequest(url);
            } catch (IOException e) {
                Log.e( LOG_TAG, "Error perfoming the HTTP request", e );

            }

            // Return the list of Books.
            return extractFeatureFromJson( jsonResponse );
        }

        // Returns new URL object from the given search query.
        private static URL createUrl (String searchBook){
            URL url = null;
            try {
                url = new URL( searchBook );
            } catch (MalformedURLException e) {
                Log.e( LOG_TAG, "Problem building the URL ", e );
            }
            return url;
        }

        // Convert the InputStream into a String which contains the whole JSON response from the server.
        private static String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName( "UTF-8" ) );
                BufferedReader reader = new BufferedReader( inputStreamReader );
                String line = reader.readLine();
                while (line != null) {
                    output.append( line );
                    line = reader.readLine();
                }
            }
            return output.toString();
        }
}





